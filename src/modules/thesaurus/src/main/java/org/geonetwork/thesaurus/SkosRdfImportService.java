/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.thesaurus;

import java.io.InputStream;
import java.util.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SkosRdfImportService {

    private final JdbcTemplate jdbc;

    public SkosRdfImportService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // --- Common vocab
    private static final String SKOS_NS = "http://www.w3.org/2004/02/skos/core#";
    private static final Property SKOS_IN_SCHEME = ResourceFactory.createProperty(SKOS_NS, "inScheme");
    private static final Property SKOS_PREF_LABEL = ResourceFactory.createProperty(SKOS_NS, "prefLabel");
    private static final Property SKOS_ALT_LABEL = ResourceFactory.createProperty(SKOS_NS, "altLabel");
    private static final Property SKOS_HIDDEN_LABEL = ResourceFactory.createProperty(SKOS_NS, "hiddenLabel");
    private static final Property SKOS_SCOPE_NOTE = ResourceFactory.createProperty(SKOS_NS, "scopeNote");
    private static final Property SKOS_BROADER = ResourceFactory.createProperty(SKOS_NS, "broader");
    private static final Property SKOS_NARROWER = ResourceFactory.createProperty(SKOS_NS, "narrower");
    private static final Property SKOS_RELATED = ResourceFactory.createProperty(SKOS_NS, "related");
    private static final Property SKOS_TOP_CONCEPT_OF = ResourceFactory.createProperty(SKOS_NS, "topConceptOf");

    private static final Resource SKOS_CONCEPT = ResourceFactory.createResource(SKOS_NS + "Concept");
    private static final Resource SKOS_CONCEPT_SCHEME = ResourceFactory.createResource(SKOS_NS + "ConceptScheme");

    // dc:title, dcterms:type, dcterms:identifier from  RDF
    private static final String DC_NS = "http://purl.org/dc/elements/1.1/";
    private static final Property DC_TITLE = ResourceFactory.createProperty(DC_NS, "title");

    private static final String DCTERMS_NS = "http://purl.org/dc/terms/";
    private static final Property DCTERMS_TYPE = ResourceFactory.createProperty(DCTERMS_NS, "type");
    private static final Property DCTERMS_IDENTIFIER = ResourceFactory.createProperty(DCTERMS_NS, "identifier");

    @Transactional
    public void importRdf(InputStream rdfXmlStream) {
        Model model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, rdfXmlStream, org.apache.jena.riot.Lang.RDFXML);

        // 1) find all ConceptSchemes in this RDF
        List<Resource> schemes =
                model.listResourcesWithProperty(RDF.type, SKOS_CONCEPT_SCHEME).toList();
        if (schemes.isEmpty()) {
            throw new IllegalArgumentException("No skos:ConceptScheme found in RDF.");
        }

        // We import each scheme separately
        for (Resource schemeRes : schemes) {
            if (!schemeRes.isURIResource()) continue;

            String schemeUri = schemeRes.getURI();
            String internalIdentifier = getFirstStringLiteralOrNull(schemeRes, DCTERMS_IDENTIFIER);
            String thesaurusType = getFirstStringLiteralOrNull(schemeRes, DCTERMS_TYPE);

            // What should be the default ?
            String originType = "external";

            // Insert/Upsert ConceptScheme
            long schemeId = upsertConceptScheme(schemeUri, internalIdentifier, thesaurusType, originType);

            // Insert scheme titles as concept_scheme_label with type prefLabel
            long prefLabelTypeId = getLabelTypeId("prefLabel");
            StmtIterator titleIt = schemeRes.listProperties(DC_TITLE);
            while (titleIt.hasNext()) {
                Statement st = titleIt.nextStatement();
                if (!st.getObject().isLiteral()) continue;

                Literal lit = st.getObject().asLiteral();
                String lang = normalizeLang(lit.getLanguage());
                String text = lit.getString();

                upsertConceptSchemeLabel(schemeId, prefLabelTypeId, lang, text);
            }

            // 2) import Concepts belonging to that scheme
            //    We consider concept belongs if it has skos:inScheme == schemeUri
            Map<String, Long> conceptIdByUri = new HashMap<>();

            ResIterator conceptResIt = model.listResourcesWithProperty(RDF.type, SKOS_CONCEPT);
            while (conceptResIt.hasNext()) {
                Resource conceptRes = conceptResIt.nextResource();
                if (!conceptRes.isURIResource()) continue;

                // must be inScheme this scheme
                if (!hasInScheme(conceptRes, schemeUri)) continue;

                String conceptUri = conceptRes.getURI();
                long conceptId = upsertConcept(schemeId, conceptUri);

                conceptIdByUri.put(conceptUri, conceptId);

                // labels
                upsertConceptLabels(conceptId, conceptRes);

                // notes (scopeNote -> concept_note.type = 'scopeNote')
                upsertConceptScopeNotes(conceptId, conceptRes);
            }

            // 3) relations between concepts (broader/narrower/related/topConceptOf)
            //    Only add relations if both ends are present in this scheme import.
            upsertRelations(model, conceptIdByUri);
        }
    }

    // ---------------------------
    // Scheme helpers
    // ---------------------------

    private long upsertConceptScheme(String uri, String internalIdentifier, String thesaurusType, String originType) {
        // We keep title/description null here; titles go into concept_scheme_label (per your model).
        jdbc.update(
                """
            INSERT INTO concept_scheme (title, description, uri, namespace_uri, internal_identifier, thesaurus_type, origin_type)
            VALUES (NULL, NULL, ?, NULL, ?, ?, ?)
            ON CONFLICT (uri)
            DO UPDATE SET
              internal_identifier = COALESCE(EXCLUDED.internal_identifier, concept_scheme.internal_identifier),
              thesaurus_type      = COALESCE(EXCLUDED.thesaurus_type, concept_scheme.thesaurus_type),
              origin_type         = COALESCE(EXCLUDED.origin_type, concept_scheme.origin_type)
            """,
                uri,
                internalIdentifier,
                thesaurusType,
                originType);

        return jdbc.queryForObject("SELECT id FROM concept_scheme WHERE uri = ?", Long.class, uri);
    }

    private void upsertConceptSchemeLabel(long schemeId, long typeId, String lang, String text) {
        jdbc.update(
                """
            INSERT INTO concept_scheme_label (concept_scheme_id, type_id, language, text)
            VALUES (?, ?, ?, ?)
            ON CONFLICT (concept_scheme_id, type_id, language)
            DO UPDATE SET text = EXCLUDED.text
            """,
                schemeId,
                typeId,
                lang,
                text);
    }

    // ---------------------------
    // Concept helpers
    // ---------------------------

    private long upsertConcept(long schemeId, String conceptUri) {
        jdbc.update(
                """
            INSERT INTO concept (concept_scheme_id, uri, status_id, position, icon_url)
            VALUES (?, ?, 1, NULL, NULL)
            ON CONFLICT (uri)
            DO UPDATE SET concept_scheme_id = EXCLUDED.concept_scheme_id
            """,
                schemeId,
                conceptUri);

        return jdbc.queryForObject("SELECT id FROM concept WHERE uri = ?", Long.class, conceptUri);
    }

    private void upsertConceptLabels(long conceptId, Resource conceptRes) {
        upsertLabelType(conceptId, conceptRes, SKOS_PREF_LABEL, "prefLabel");
        upsertLabelType(conceptId, conceptRes, SKOS_ALT_LABEL, "altLabel");
        upsertLabelType(conceptId, conceptRes, SKOS_HIDDEN_LABEL, "hiddenLabel");
    }

    private void upsertLabelType(long conceptId, Resource conceptRes, Property prop, String labelTypeName) {
        long typeId = getLabelTypeId(labelTypeName);

        StmtIterator it = conceptRes.listProperties(prop);
        while (it.hasNext()) {
            Statement st = it.nextStatement();
            if (!st.getObject().isLiteral()) continue;

            Literal lit = st.getObject().asLiteral();
            String lang = normalizeLang(lit.getLanguage());
            String text = lit.getString();

            jdbc.update(
                    """
                INSERT INTO concept_label (concept_id, type_id, language, text)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (concept_id, type_id, language)
                DO UPDATE SET text = EXCLUDED.text
                """,
                    conceptId,
                    typeId,
                    lang,
                    text);
        }
    }

    private void upsertConceptScopeNotes(long conceptId, Resource conceptRes) {
        StmtIterator it = conceptRes.listProperties(SKOS_SCOPE_NOTE);
        while (it.hasNext()) {
            Statement st = it.nextStatement();
            if (!st.getObject().isLiteral()) continue;

            Literal lit = st.getObject().asLiteral();
            String lang = normalizeLang(lit.getLanguage());
            String text = lit.getString();

            // Your concept_note table has no uniqueness constraint.
            // We can avoid duplicates by inserting only if not exists.
            jdbc.update(
                    """
                INSERT INTO concept_note (concept_id, type, text, language)
                SELECT ?, 'scopeNote', ?, ?
                WHERE NOT EXISTS (
                    SELECT 1 FROM concept_note
                    WHERE concept_id = ?
                      AND type = 'scopeNote'
                      AND language = ?
                      AND text = ?
                )
                """,
                    conceptId,
                    text,
                    lang,
                    conceptId,
                    lang,
                    text);
        }
    }

    // ---------------------------
    // Relations
    // ---------------------------

    private void upsertRelations(Model model, Map<String, Long> conceptIdByUri) {
        // Map SKOS property -> relation_type.name in your DB
        Map<Property, String> relMap = Map.of(
                SKOS_BROADER, "broader",
                SKOS_NARROWER, "narrower",
                SKOS_RELATED, "related",
                SKOS_TOP_CONCEPT_OF, "topConceptOf");

        // For each concept URI we imported, scan outgoing relations
        for (String fromUri : conceptIdByUri.keySet()) {
            Resource fromRes = model.getResource(fromUri);
            if (fromRes == null) continue;

            long fromId = conceptIdByUri.get(fromUri);

            for (Map.Entry<Property, String> e : relMap.entrySet()) {
                Property p = e.getKey();
                long typeId = getRelationTypeId(e.getValue());

                StmtIterator it = fromRes.listProperties(p);
                while (it.hasNext()) {
                    Statement st = it.nextStatement();
                    RDFNode obj = st.getObject();
                    if (!obj.isResource()) continue;

                    Resource toRes = obj.asResource();
                    if (!toRes.isURIResource()) continue;

                    Long toId = conceptIdByUri.get(toRes.getURI());
                    if (toId == null) continue; // only relate within imported concepts

                    jdbc.update(
                            """
                        INSERT INTO relation (from_concept_id, to_concept_id, type_id)
                        VALUES (?, ?, ?)
                        ON CONFLICT (from_concept_id, to_concept_id, type_id)
                        DO NOTHING
                        """,
                            fromId,
                            toId,
                            typeId);
                }
            }
        }
    }

    // ---------------------------
    // Lookups
    // ---------------------------

    private long getLabelTypeId(String name) {
        return jdbc.queryForObject("SELECT id FROM label_type WHERE name = ?", Long.class, name);
    }

    private long getRelationTypeId(String name) {
        return jdbc.queryForObject("SELECT id FROM relation_type WHERE name = ?", Long.class, name);
    }

    // ---------------------------
    // RDF helpers
    // ---------------------------

    private boolean hasInScheme(Resource conceptRes, String schemeUri) {
        StmtIterator it = conceptRes.listProperties(SKOS_IN_SCHEME);
        while (it.hasNext()) {
            Statement st = it.nextStatement();
            RDFNode obj = st.getObject();
            if (!obj.isResource()) continue;
            Resource r = obj.asResource();
            if (r.isURIResource() && schemeUri.equals(r.getURI())) return true;
        }
        return false;
    }

    private String getFirstStringLiteralOrNull(Resource r, Property p) {
        Statement st = r.getProperty(p);
        if (st == null) return null;
        if (!st.getObject().isLiteral()) return null;
        return st.getObject().asLiteral().getString();
    }

    private String normalizeLang(String lang) {
        if (lang == null || lang.isBlank()) return "en";
        // if a transformation is needed ?
        return lang.trim();
    }
}
