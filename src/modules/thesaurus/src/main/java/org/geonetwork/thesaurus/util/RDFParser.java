/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.thesaurus.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import org.geonetwork.domain.thesaurus.model.ConceptScheme;
import org.geonetwork.domain.thesaurus.model.ConceptSchemeLabel;
import org.geonetwork.domain.thesaurus.model.LabelType;

public class RDFParser {

    public static void main(String[] args) {

        try {
            new RDFParser().parseRDF();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void parseRDF() throws FileNotFoundException {
        Model model = ModelFactory.createDefaultModel();

        InputStream is = RDFParser.class.getClassLoader().getResourceAsStream("codelist_unit_time.rdf");
        model.read(is, null, "RDF/XML");

        Resource conceptScheme =
                model.listResourcesWithProperty(RDF.type, SKOS.ConceptScheme).next();

        ConceptScheme conceptSchemeObject = new ConceptScheme();

        conceptSchemeObject.setUri(conceptScheme.getProperty(DCTerms.identifier).getString());

        conceptScheme.listProperties(DC.title).forEachRemaining(stmt -> {
            Literal l = stmt.getObject().asLiteral();
            ConceptSchemeLabel label = new ConceptSchemeLabel();
            label.setLanguage(l.getLanguage());
            label.setText(l.getString());
            label.setType(LabelType.PREF_LABEL);
        });

        ResIterator concepts = model.listResourcesWithProperty(RDF.type, SKOS.Concept);

        int count = 0;
        while (concepts.hasNext()) {
            count++;
            Resource concept = concepts.next();

            // inScheme filter
            if (!concept.hasProperty(SKOS.inScheme, conceptScheme)) {
                continue;
            }
            String uri = concept.getURI();

            Map<String, String> labels = literalsByLang(concept, SKOS.prefLabel);
            Map<String, String> notes = literalsByLang(concept, SKOS.scopeNote);
        }
    }

    private Map<String, String> literalsByLang(Resource resource, Property property) {
        Map<String, String> map = new HashMap<>();

        resource.listProperties(property).forEachRemaining(stmt -> {
            Literal l = stmt.getObject().asLiteral();
            map.put(l.getLanguage(), l.getString());
        });

        return map;
    }
}
