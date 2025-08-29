/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.formatting.processor.dcatap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.index.model.record.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Service for converting IndexRecord to DCAT-AP 3.0 and OGC API Records using Java models. */
@Slf4j
@Service
@RequiredArgsConstructor
public class IndexRecordToDcatModelSerializer {

    private final ObjectMapper objectMapper;

    @Value("${dcat.serializer.base-uri:http://data.europa.eu/}")
    private String defaultBaseUri;

    @Value("${dcat.serializer.language.authority-uri:http://publications.europa.eu/resource/authority/language/}")
    private String languageAuthorityUri;

    @Value(
            "${dcat.serializer.access-rights.public-uri:http://publications.europa.eu/resource/authority/access-right/PUBLIC}")
    private String publicAccessRightUri;

    @Value(
            "${dcat.serializer.access-rights.restricted-uri:http://publications.europa.eu/resource/authority/access-right/RESTRICTED}")
    private String restrictedAccessRightUri;

    @Value("${dcat.serializer.inspire-theme-uri-pattern:inspire.ec.europa.eu/theme}")
    private String inspireThemeUriPattern;

    @Value("${dcat.serializer.media-type-base-uri:https://www.iana.org/assignments/media-types/}")
    private String mediaTypeBaseUri;

    @Value("${dcat.serializer.file-type-authority-uri:http://publications.europa.eu/resource/authority/file-type/}")
    private String fileTypeAuthorityUri;

    @Value("${dcat.serializer.output-format:hybrid}")
    private String outputFormat; // "dcat", "ogc", or "hybrid"

    private Map<String, Object> namespaceContext;
    private Map<String, Object> ogcNamespaceContext;

    @PostConstruct
    public void init() {
        namespaceContext = new ConcurrentHashMap<>();
        // Core namespaces for DCAT-AP 3.0
        namespaceContext.put("dcat", "http://www.w3.org/ns/dcat#");
        namespaceContext.put("dct", "http://purl.org/dc/terms/");
        namespaceContext.put("dctype", "http://purl.org/dc/dcmitype/");
        namespaceContext.put("foaf", "http://xmlns.com/foaf/0.1/");
        namespaceContext.put("vcard", "http://www.w3.org/2006/vcard/ns#");
        namespaceContext.put("adms", "http://www.w3.org/ns/adms#");
        namespaceContext.put("locn", "http://www.w3.org/ns/locn#");
        namespaceContext.put("gsp", "http://www.opengis.net/ont/geosparql#");
        namespaceContext.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        namespaceContext.put("skos", "http://www.w3.org/2004/02/skos/core#");
        namespaceContext.put("spdx", "http://spdx.org/rdfterms/spdx#");
        namespaceContext.put("prov", "http://www.w3.org/ns/prov#");
        namespaceContext.put("owl", "http://www.w3.org/2002/07/owl#");
        namespaceContext.put("xsd", "http://www.w3.org/2001/XMLSchema#");
        namespaceContext.put("dqv", "http://www.w3.org/ns/dqv#");
        namespaceContext.put("time", "http://www.w3.org/2006/time#");
        namespaceContext.put("odrl", "http://www.w3.org/ns/odrl/2/");

        // Additional namespaces for OGC API Records
        ogcNamespaceContext = new ConcurrentHashMap<>(namespaceContext);
        ogcNamespaceContext.put("geo", "http://www.opengis.net/ont/geosparql#");
        ogcNamespaceContext.put("geojson", "https://purl.org/geojson/vocab#");
        ogcNamespaceContext.put("oa", "http://www.w3.org/ns/oa#");
        ogcNamespaceContext.put("time", "http://www.w3.org/2006/time#");
        ogcNamespaceContext.put("schema", "http://schema.org/");
    }

    /** Serialize an IndexRecord to DCAT-AP 3.0 JSON-LD format (backward compatibility). */
    public String serialize(IndexRecord record) throws JsonProcessingException {
        return serialize(record, null, defaultBaseUri);
    }

    /**
     * Serialize with format specification.
     *
     * @param format "dcat" for pure DCAT-AP, "ogc" for OGC API Records, "hybrid" for both
     */
    public String serialize(IndexRecord record, String catalogUri, String baseUri) throws JsonProcessingException {
        return serialize(record, catalogUri, baseUri, "hybrid");
    }

    public String serialize(IndexRecord record, String catalogUri, String baseUri, String format)
            throws JsonProcessingException {
        String effectiveBaseUri = (baseUri != null && !baseUri.isEmpty()) ? baseUri : defaultBaseUri;

        if ("ogc".equals(format) || "hybrid".equals(format)) {
            // Create OGC API Records format (which includes DCAT-AP properties in hybrid mode)
            DcatModel.OGCRecord ogcRecord = mapToOGCRecord(record, effectiveBaseUri, "hybrid".equals(format));

            if (catalogUri != null && !catalogUri.isEmpty()) {
                // Wrap in a FeatureCollection for OGC API Records
                Map<String, Object> featureCollection = new LinkedHashMap<>();
                featureCollection.put("@context", ogcNamespaceContext);
                featureCollection.put("type", "FeatureCollection");
                featureCollection.put("features", List.of(ogcRecord));
                featureCollection.put("numberMatched", 1);
                featureCollection.put("numberReturned", 1);

                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(featureCollection);
            } else {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ogcRecord);
            }
        } else {
            // Original DCAT-AP only format
            DcatModel.Dataset dataset = mapToDataset(record, effectiveBaseUri);

            if (catalogUri != null && !catalogUri.isEmpty()) {
                DcatModel.Catalog catalog = DcatModel.Catalog.builder()
                        .context(namespaceContext)
                        .id(catalogUri)
                        .dataset(List.of(dataset))
                        .build();
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(catalog);
            } else {
                dataset.setContext(namespaceContext);
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataset);
            }
        }
    }

    /** Map IndexRecord to OGC API Records Feature/Record with optional DCAT-AP properties. */
    private DcatModel.OGCRecord mapToOGCRecord(IndexRecord record, String baseUri, boolean includeHybrid) {
        String recordId = record.getUuid();

        DcatModel.OGCRecord.OGCRecordBuilder builder = DcatModel.OGCRecord.builder()
                .context(ogcNamespaceContext)
                .type("Feature")
                .id(recordId)
                .atId(generateUri(baseUri, "dataset", recordId));

        // Add @type for JSON-LD
        List<String> types = new ArrayList<>();
        types.add("geo:Feature");
        if (includeHybrid) {
            types.add("dcat:Dataset");
        }
        builder.atType(types);

        // Map geometry
        builder.geometry(mapToGeoJSON(record));

        // Map temporal extent for OGC time property
        builder.time(mapToOGCTemporalExtent(record));

        // Map OGC API Records properties (simplified metadata)
        DcatModel.OGCRecordProperties properties = mapToOGCRecordProperties(record, baseUri);
        builder.properties(properties);

        // Add OGC API Records links
        builder.links(mapToOGCLinks(record, baseUri));

        // If hybrid mode, add DCAT-AP properties at root level for proper DCAT-AP compliance
        if (includeHybrid) {
            // Core DCAT-AP properties at root
            builder.dctTitle(mapTitle(record));
            builder.dctDescription(mapDescription(record));
            builder.dctIdentifier(mapIdentifiers(record));
            builder.dcatKeyword(mapKeywords(record));
            builder.dcatTheme(mapThemes(record));
            builder.dctIssued(mapIssuedDate(record));
            builder.dctModified(mapModifiedDate(record));
            builder.dctLanguage(mapLanguages(record));
            builder.dctPublisher(mapPublisher(record));
            builder.dcatContactPoint(mapContactPoints(record));
            builder.dcatDistribution(mapDistributions(record, baseUri));
            builder.dctAccessRights(mapAccessRights(record));
            builder.dctSpatial(mapSpatial(record));
            builder.dctTemporal(mapTemporal(record));
        }

        return builder.build();
    }

    /** Map to OGC API Records properties (simplified format for OGC compliance). */
    private DcatModel.OGCRecordProperties mapToOGCRecordProperties(IndexRecord record, String baseUri) {
        DcatModel.OGCRecordProperties.OGCRecordPropertiesBuilder props = DcatModel.OGCRecordProperties.builder();

        // OGC API Records core properties
        props.recordCreated(record.getCreateDate());
        props.recordUpdated(record.getChangeDate());

        // Resource type
        if (record.getResourceType() != null && !record.getResourceType().isEmpty()) {
            props.type(record.getResourceType().get(0));
        }

        // Title and description - simple strings for OGC
        props.title(getDefaultString(record.getResourceTitle()));
        props.description(getDefaultString(record.getResourceAbstract()));

        // Keywords - simple string list for OGC
        props.keywords(mapKeywordsSimpleStrings(record));

        // Language(s)
        if (record.getResourceLanguage() != null
                && !record.getResourceLanguage().isEmpty()) {
            if (record.getResourceLanguage().size() == 1) {
                props.language(record.getResourceLanguage().get(0));
            } else {
                props.languages(record.getResourceLanguage());
            }
            props.resourceLanguages(record.getResourceLanguage());
        }

        // External identifiers
        props.externalIds(mapExternalIds(record));

        // Formats
        props.formats(mapFormats(record));

        // Contacts - simplified for OGC
        props.contacts(mapContactsSimple(record));

        // License and rights - simple strings for OGC
        props.license(mapLicenseSimpleString(record));
        props.rights(mapRightsSimpleString(record));

        // Extent (spatial and temporal combined)
        props.extent(mapToOGCExtent(record));

        // Dates
        props.created(mapIssuedDate(record));
        props.updated(mapModifiedDate(record));

        // Conformance - simple URI list for OGC
        props.conformsTo(mapConformanceSimpleStrings(record));

        // Additional OGC properties
        props.themes(mapThemeUris(record));
        props.spatialResolution(mapSpatialResolution(record));
        props.temporalResolution(mapTemporalResolution(record));

        return props.build();
    }

    /** Convert geometry to GeoJSON format for OGC API Records. */
    private Object mapToGeoJSON(IndexRecord record) {
        if (record.getGeometries() != null && !record.getGeometries().isEmpty()) {
            // Return the first geometry as GeoJSON
            Map geom = record.getGeometries().get(0);
            if (geom != null) {
                // If it's already a proper GeoJSON geometry, return it
                if (geom.containsKey("type") && geom.containsKey("coordinates")) {
                    return geom;
                }
                // Otherwise try to construct one
                try {
                    return geom;
                } catch (Exception e) {
                    log.warn("Failed to convert geometry to GeoJSON: {}", e.getMessage());
                }
            }
        }

        // If no geometry, try to create from bounding box
        // Note: This would need implementation based on available bbox data
        return null;
    }

    /** Map to OGC API Records temporal extent format. */
    private DcatModel.OGCTemporalExtent mapToOGCTemporalExtent(IndexRecord record) {
        if (record.getResourceTemporalExtentDateRange() == null
                || record.getResourceTemporalExtentDateRange().isEmpty()) {
            return null;
        }

        List<List<String>> intervals = new ArrayList<>();
        for (DateRange range : record.getResourceTemporalExtentDateRange()) {
            List<String> interval = new ArrayList<>();
            interval.add(range.getGte() != null ? range.getGte() : "..");
            interval.add(range.getLte() != null ? range.getLte() : "..");
            intervals.add(interval);
        }

        return DcatModel.OGCTemporalExtent.builder()
                .interval(intervals)
                .trs("http://www.w3.org/2006/time#Gregorian")
                .build();
    }

    /** Map to OGC API Records extent (combined spatial and temporal). */
    private DcatModel.OGCExtent mapToOGCExtent(IndexRecord record) {
        DcatModel.OGCExtent.OGCExtentBuilder extent = DcatModel.OGCExtent.builder();

        // Spatial extent
        DcatModel.OGCSpatialExtent spatial = mapToOGCSpatialExtent(record);
        if (spatial != null) {
            extent.spatial(spatial);
        }

        // Temporal extent
        DcatModel.OGCTemporalExtent temporal = mapToOGCTemporalExtent(record);
        if (temporal != null) {
            extent.temporal(temporal);
        }

        return (spatial != null || temporal != null) ? extent.build() : null;
    }

    /** Map to OGC API Records spatial extent format. */
    private DcatModel.OGCSpatialExtent mapToOGCSpatialExtent(IndexRecord record) {
        // Extract bounding box from geometries or extent descriptions
        // This is a simplified implementation - enhance based on your data
        if (record.getGeometries() != null && !record.getGeometries().isEmpty()) {
            // Calculate bbox from geometries if needed
            // For now, return null - implement bbox calculation as needed
        }
        return null;
    }

    /** Map to OGC API Records links format. */
    private List<DcatModel.OGCLink> mapToOGCLinks(IndexRecord record, String baseUri) {
        List<DcatModel.OGCLink> links = new ArrayList<>();

        // Self link
        links.add(DcatModel.OGCLink.builder()
                .href(generateUri(baseUri, "record", record.getUuid()))
                .rel("self")
                .type("application/geo+json")
                .title("This document")
                .build());

        // Alternate representations
        links.add(DcatModel.OGCLink.builder()
                .href(generateUri(baseUri, "record", record.getUuid()) + "?f=xml")
                .rel("alternate")
                .type("application/xml")
                .title("XML representation")
                .build());

        links.add(DcatModel.OGCLink.builder()
                .href(generateUri(baseUri, "record", record.getUuid()) + "?f=html")
                .rel("alternate")
                .type("text/html")
                .title("HTML representation")
                .build());

        // Collection link
        links.add(DcatModel.OGCLink.builder()
                .href(generateUri(baseUri, "collections", "metadata"))
                .rel("collection")
                .type("application/json")
                .title("The collection this record belongs to")
                .build());

        // Add links from record data
        if (record.getLinks() != null) {
            for (Link link : record.getLinks()) {
                String url = extractUrl(link.getUrl());
                if (url != null) {
                    DcatModel.OGCLink.OGCLinkBuilder ogcLink = DcatModel.OGCLink.builder()
                            .href(url)
                            .rel(mapProtocolToRel(link.getProtocol()))
                            .type(link.getMimeType());

                    if (link.getName() != null && !link.getName().isEmpty()) {
                        ogcLink.title(getDefaultString(link.getName()));
                    }

                    links.add(ogcLink.build());
                }
            }
        }

        return links;
    }

    /** Map protocol to OGC link relation type. */
    private String mapProtocolToRel(String protocol) {
        if (protocol == null) return "related";

        if (protocol.contains("DOWNLOAD")) return "enclosure";
        if (protocol.startsWith("OGC:")) return "service";
        if (protocol.contains("INFO")) return "describedby";
        if (protocol.contains("LINK")) return "related";

        return "related";
    }

    // Simplified mapping methods for OGC API Records format

    private String getDefaultString(Map<String, String> multiMap) {
        if (multiMap == null || multiMap.isEmpty()) return null;
        String value = multiMap.get("default");
        if (value == null) value = multiMap.get("lang");
        if (value == null && !multiMap.isEmpty()) {
            value = multiMap.values().iterator().next();
        }
        return value;
    }

    private List<String> mapKeywordsSimpleStrings(IndexRecord record) {
        if (record.getAllKeywords() == null) return null;

        List<String> keywords = new ArrayList<>();
        for (Thesaurus thesaurus : record.getAllKeywords().values()) {
            if (thesaurus.getKeywords() == null) continue;
            for (Keyword keyword : thesaurus.getKeywords()) {
                String value = getDefaultString(keyword.getProperties());
                if (value != null && !value.isEmpty()) {
                    keywords.add(value);
                }
            }
        }
        return keywords.isEmpty() ? null : keywords;
    }

    private List<Object> mapKeywordsSimple(IndexRecord record) {
        List<String> keywords = mapKeywordsSimpleStrings(record);
        return keywords != null ? new ArrayList<>(keywords) : null;
    }

    private List<Object> mapExternalIds(IndexRecord record) {
        if (record.getResourceIdentifier() == null) return null;

        List<Object> ids = new ArrayList<>();
        for (ResourceIdentifier id : record.getResourceIdentifier()) {
            Map<String, String> extId = new HashMap<>();
            extId.put("scheme", id.getCodeSpace());
            extId.put("value", id.getCode());
            ids.add(extId);
        }
        return ids.isEmpty() ? null : ids;
    }

    private List<String> mapFormats(IndexRecord record) {
        if (record.getLinks() == null) return null;

        Set<String> formats = new HashSet<>();
        for (Link link : record.getLinks()) {
            if (link.getMimeType() != null) {
                formats.add(link.getMimeType());
            }
        }
        return formats.isEmpty() ? null : new ArrayList<>(formats);
    }

    private List<Object> mapContactsSimple(IndexRecord record) {
        Map<String, List<Contact>> contactByRole = record.getContactByRole();
        if (contactByRole == null || contactByRole.isEmpty()) return null;

        List<Object> contacts = new ArrayList<>();
        for (Map.Entry<String, List<Contact>> entry : contactByRole.entrySet()) {
            for (Contact contact : entry.getValue()) {
                Map<String, Object> simpleContact = new HashMap<>();

                String name = getDefaultString(contact.getOrganisation());
                if (name == null) name = contact.getIndividual();
                if (name != null) simpleContact.put("name", name);

                if (contact.getEmail() != null) {
                    simpleContact.put("email", contact.getEmail());
                }

                simpleContact.put("role", entry.getKey());

                contacts.add(simpleContact);
            }
        }
        return contacts.isEmpty() ? null : contacts;
    }

    private String mapLicenseSimpleString(IndexRecord record) {
        if (record.getLicenses() == null || record.getLicenses().isEmpty()) return null;

        Map<String, String> license = record.getLicenses().get(0);
        String link = license.get("link");
        if (link != null) return link;

        return getDefaultString(license);
    }

    private Object mapLicenseSimple(IndexRecord record) {
        return mapLicenseSimpleString(record);
    }

    private String mapRightsSimpleString(IndexRecord record) {
        return record.isPublishedToAll() ? "public" : "restricted";
    }

    private Object mapRightsSimple(IndexRecord record) {
        return mapRightsSimpleString(record);
    }

    private List<String> mapConformanceSimpleStrings(IndexRecord record) {
        if (record.getSpecificationConformance() == null) return null;

        List<String> conformance = new ArrayList<>();
        for (SpecificationConformance spec : record.getSpecificationConformance()) {
            if (spec.getLink() != null) {
                conformance.add(spec.getLink());
            }
        }
        return conformance.isEmpty() ? null : conformance;
    }

    private List<Object> mapConformanceSimple(IndexRecord record) {
        List<String> conformance = mapConformanceSimpleStrings(record);
        return conformance != null ? new ArrayList<>(conformance) : null;
    }

    private List<String> mapThemeUris(IndexRecord record) {
        if (record.getAllKeywords() == null) return null;

        Set<String> themeUris = new HashSet<>();
        for (Thesaurus thesaurus : record.getAllKeywords().values()) {
            if (thesaurus.getKeywords() == null) continue;
            for (Keyword keyword : thesaurus.getKeywords()) {
                if (keyword.getProperties() != null) {
                    String link = keyword.getProperties().get("link");
                    if (link != null && link.startsWith("http")) {
                        themeUris.add(link);
                    }
                }
            }
        }
        return themeUris.isEmpty() ? null : new ArrayList<>(themeUris);
    }

    private String extractUrl(Map<String, String> urlMap) {
        if (urlMap == null || urlMap.isEmpty()) return null;
        String url = urlMap.get("default");
        if (url == null && !urlMap.isEmpty()) {
            url = urlMap.values().iterator().next();
        }
        return url;
    }

    private DcatModel.Resource mapHasCurrentVersion(IndexRecord record, String baseUri) {
        // Map to current version if available
        return null; // Placeholder - implement based on your IndexRecord structure
    }

    // ======== Original DCAT-AP mapping methods (kept for backward compatibility) ========

    /** Map IndexRecord to DcatModel.Dataset (DCAT-AP 3.0 compliant). */
    private DcatModel.Dataset mapToDataset(IndexRecord record, String baseUri) {
        return DcatModel.Dataset.builder()
                // Core properties
                .id(generateUri(baseUri, "dataset", record.getUuid()))
                .title(mapTitle(record))
                .description(mapDescription(record))
                .identifier(mapIdentifiers(record))

                // Keywords and themes
                .keyword(mapKeywords(record))
                .theme(mapThemes(record))
                .subject(mapSubjects(record))

                // Spatial and temporal
                .spatial(mapSpatial(record))
                .temporal(mapTemporal(record))
                .spatialResolutionInMeters(mapSpatialResolution(record))
                .temporalResolution(mapTemporalResolution(record))

                // Dates
                .issued(mapIssuedDate(record))
                .modified(mapModifiedDate(record))
                .dateSubmitted(record.getDateStamp())

                // Language and localization
                .language(mapLanguages(record))

                // Actors
                .publisher(mapPublisher(record))
                .creator(mapCreators(record))
                .contactPoint(mapContactPoints(record))

                // Distribution and access
                .distribution(mapDistributions(record, baseUri))
                .accessRights(mapAccessRights(record))
                .landingPage(mapLandingPages(record))

                // Conformance and quality
                .conformsTo(mapConformance(record))
                .license(mapLicense(record))
                .rights(mapRights(record))
                .qualityMeasurement(mapQualityMeasurements(record))

                // Relations
                .relation(mapRelations(record, baseUri))
                .source(mapSources(record, baseUri))
                .isReferencedBy(mapReferences(record, baseUri))

                // Versioning (DCAT-AP 3.0)
                .version(mapVersion(record))
                .versionInfo(mapVersionInfo(record))
                .versionNotes(mapVersionNotes(record))
                .hasVersion(mapHasVersions(record, baseUri))
                .isVersionOf(mapIsVersionOf(record, baseUri))
                .previousVersion(mapPreviousVersion(record, baseUri))

                // Provenance (DCAT-AP 3.0)
                .provenance(mapProvenance(record))
                .qualifiedAttribution(mapQualifiedAttributions(record))
                .wasGeneratedBy(mapGenerationActivity(record))
                .qualifiedRelation(mapQualifiedRelations(record, baseUri))

                // Additional DCAT-AP 3.0 properties
                .datasetType(mapDatasetType(record))
                .accrualPeriodicity(mapAccrualPeriodicity(record))
                .page(mapPages(record))
                .sample(mapSamples(record, baseUri))
                .build();
    }

    // [Include all the original mapping methods from the previous implementation here]
    // I'm including just the essential ones for brevity, but all original methods should be kept

    private DcatModel.MultilingualValue mapTitle(IndexRecord record) {
        if (record.getResourceTitle() == null || record.getResourceTitle().isEmpty()) {
            return null;
        }
        return DcatModel.MultilingualValue.of(record.getResourceTitle());
    }

    private DcatModel.MultilingualValue mapDescription(IndexRecord record) {
        if (record.getResourceAbstract() == null || record.getResourceAbstract().isEmpty()) {
            return null;
        }
        return DcatModel.MultilingualValue.of(record.getResourceAbstract());
    }

    private List<DcatModel.Identifier> mapIdentifiers(IndexRecord record) {
        if (record.getResourceIdentifier() == null
                || record.getResourceIdentifier().isEmpty()) {
            return null;
        }

        List<DcatModel.Identifier> identifiers = new ArrayList<>();
        for (ResourceIdentifier id : record.getResourceIdentifier()) {
            identifiers.add(DcatModel.Identifier.builder()
                    .notation(id.getCode())
                    .schemeAgency(id.getCodeSpace())
                    .build());
        }
        return identifiers;
    }

    // ... [Include all other original mapping methods here - they remain unchanged]
    // For brevity, I'm not including all of them, but they should all be kept from the original

    private String generateUri(String baseUri, String type, String id) {
        if (!baseUri.endsWith("/")) {
            baseUri += "/";
        }
        return baseUri + type + "/" + id;
    }

    // [All other original helper methods remain the same]

    private List<Object> mapKeywords(IndexRecord record) {
        if (record.getAllKeywords() == null) {
            return null;
        }

        List<Object> keywords = new ArrayList<>();

        for (Thesaurus thesaurus : record.getAllKeywords().values()) {
            if (thesaurus.getKeywords() == null) continue;

            for (Keyword keyword : thesaurus.getKeywords()) {
                if (keyword.getProperties() == null) continue;

                String value = keyword.getProperties().get("default");
                if (value == null) {
                    value = keyword.getProperties().get("en");
                }

                if (value != null && !value.isEmpty()) {
                    String link = keyword.getProperties().get("link");
                    if (link == null || !link.startsWith("http")) {
                        keywords.add(value);
                    }
                }
            }
        }

        return keywords.isEmpty() ? null : keywords;
    }

    private List<DcatModel.Concept> mapThemes(IndexRecord record) {
        if (record.getAllKeywords() == null) {
            return null;
        }

        List<DcatModel.Concept> themes = new ArrayList<>();

        for (Thesaurus thesaurus : record.getAllKeywords().values()) {
            if (thesaurus.getKeywords() == null) continue;

            boolean isEuVocabulary = thesaurus.getId() != null
                    && (thesaurus.getId().contains("inspire")
                            || thesaurus.getId().contains("eurovoc")
                            || thesaurus.getId().contains("data-theme"));

            for (Keyword keyword : thesaurus.getKeywords()) {
                if (keyword.getProperties() == null) continue;

                String link = keyword.getProperties().get("link");
                if (link != null && (link.contains(inspireThemeUriPattern) || isEuVocabulary)) {
                    DcatModel.Concept.ConceptBuilder concept = DcatModel.Concept.builder()
                            .id(link)
                            .prefLabel(DcatModel.MultilingualValue.of(keyword.getProperties()));

                    if (thesaurus.getLink() != null) {
                        concept.inScheme(DcatModel.Resource.of(thesaurus.getLink()));
                    }

                    themes.add(concept.build());
                }
            }
        }

        return themes.isEmpty() ? null : themes;
    }

    private List<DcatModel.Concept> mapSubjects(IndexRecord record) {
        if (record.getAllKeywords() == null) {
            return null;
        }

        List<DcatModel.Concept> subjects = new ArrayList<>();

        for (Thesaurus thesaurus : record.getAllKeywords().values()) {
            if (thesaurus.getKeywords() == null) continue;

            for (Keyword keyword : thesaurus.getKeywords()) {
                if (keyword.getProperties() == null) continue;

                String link = keyword.getProperties().get("link");
                if (link != null
                        && link.startsWith("http")
                        && !link.contains(inspireThemeUriPattern)
                        && !link.contains("eurovoc")
                        && !link.contains("data-theme")) {

                    DcatModel.Concept.ConceptBuilder concept = DcatModel.Concept.builder()
                            .id(link)
                            .prefLabel(DcatModel.MultilingualValue.of(keyword.getProperties()));

                    if (thesaurus.getLink() != null) {
                        concept.inScheme(DcatModel.Resource.of(thesaurus.getLink()));
                    }

                    subjects.add(concept.build());
                }
            }
        }

        return subjects.isEmpty() ? null : subjects;
    }

    private List<DcatModel.Location> mapSpatial(IndexRecord record) {
        List<DcatModel.Location> spatial = new ArrayList<>();

        if (record.getExtentDescription() != null) {
            for (Map<String, String> extent : record.getExtentDescription()) {
                spatial.add(DcatModel.Location.builder()
                        .label(DcatModel.MultilingualValue.of(extent))
                        .build());
            }
        }

        if (record.getGeometries() != null) {
            for (Map geom : record.getGeometries()) {
                try {
                    String geoJson = objectMapper.writeValueAsString(geom);
                    spatial.add(DcatModel.Location.builder()
                            .geometry(DcatModel.Geometry.builder()
                                    .asGeoJSON(geoJson)
                                    .build())
                            .build());
                } catch (Exception e) {
                    log.warn("Failed to serialize geometry: {}", e.getMessage());
                }
            }
        }

        return spatial.isEmpty() ? null : spatial;
    }

    private List<DcatModel.PeriodOfTime> mapTemporal(IndexRecord record) {
        if (record.getResourceTemporalExtentDateRange() == null) {
            return null;
        }

        return record.getResourceTemporalExtentDateRange().stream()
                .map(range -> DcatModel.PeriodOfTime.builder()
                        .startDate(range.getGte())
                        .endDate(range.getLte())
                        .build())
                .collect(Collectors.toList());
    }

    private Double mapSpatialResolution(IndexRecord record) {
        return null;
    }

    private String mapTemporalResolution(IndexRecord record) {
        return null;
    }

    private String mapIssuedDate(IndexRecord record) {
        if (record.getResourceDate() == null) {
            return null;
        }

        return record.getResourceDate().stream()
                .filter(date -> "creation".equals(date.getType()))
                .findFirst()
                .map(ResourceDate::getDate)
                .orElse(null);
    }

    private String mapModifiedDate(IndexRecord record) {
        if (record.getResourceDate() == null) {
            return null;
        }

        return record.getResourceDate().stream()
                .filter(date -> "revision".equals(date.getType()) || "publication".equals(date.getType()))
                .map(ResourceDate::getDate)
                .sorted(Comparator.reverseOrder())
                .findFirst()
                .orElse(null);
    }

    private List<DcatModel.LinguisticSystem> mapLanguages(IndexRecord record) {
        if (record.getResourceLanguage() == null || record.getResourceLanguage().isEmpty()) {
            return null;
        }

        List<DcatModel.LinguisticSystem> languages = new ArrayList<>();

        for (String lang : record.getResourceLanguage()) {
            if (lang == null || lang.trim().isEmpty()) {
                continue;
            }

            String langCode = mapLanguageCode(lang);
            languages.add(DcatModel.LinguisticSystem.of(languageAuthorityUri + langCode));
        }

        return languages.isEmpty() ? null : languages;
    }

    private String mapLanguageCode(String lang) {
        String langCode = lang.toUpperCase();

        Map<String, String> iso2ToIso3 = Map.of(
                "EN", "ENG",
                "FR", "FRA",
                "DE", "DEU",
                "IT", "ITA",
                "ES", "SPA",
                "PT", "POR",
                "NL", "NLD");

        Map<String, String> variations = Map.of(
                "FRE", "FRA",
                "GER", "DEU",
                "DUT", "NLD");

        if (langCode.length() == 2 && iso2ToIso3.containsKey(langCode)) {
            return iso2ToIso3.get(langCode);
        } else if (variations.containsKey(langCode)) {
            return variations.get(langCode);
        }

        return langCode;
    }

    private DcatModel.Agent mapPublisher(IndexRecord record) {
        Map<String, List<Contact>> contactByRole = record.getContactByRole();

        List<Contact> publishers = contactByRole.get("publisher");
        if (publishers == null || publishers.isEmpty()) {
            publishers = contactByRole.get("pointOfContact");
        }
        if (publishers == null || publishers.isEmpty()) {
            publishers = contactByRole.get("custodian");
        }

        if (publishers != null && !publishers.isEmpty()) {
            return mapToAgent(publishers.get(0));
        }

        return null;
    }

    private List<DcatModel.Agent> mapCreators(IndexRecord record) {
        Map<String, List<Contact>> contactByRole = record.getContactByRole();

        List<Contact> creators = new ArrayList<>();

        if (contactByRole.get("originator") != null) {
            creators.addAll(contactByRole.get("originator"));
        }
        if (contactByRole.get("author") != null) {
            creators.addAll(contactByRole.get("author"));
        }
        if (contactByRole.get("principalInvestigator") != null) {
            creators.addAll(contactByRole.get("principalInvestigator"));
        }

        if (!creators.isEmpty()) {
            return creators.stream().map(this::mapToAgent).distinct().collect(Collectors.toList());
        }

        return null;
    }

    private DcatModel.Agent mapToAgent(Contact contact) {
        DcatModel.Agent.AgentBuilder agent = DcatModel.Agent.builder();

        if (contact.getOrganisation() != null && !contact.getOrganisation().isEmpty()) {
            agent.name(DcatModel.MultilingualValue.of(contact.getOrganisation()));
        }

        if (contact.getIndividual() != null) {
            agent.givenName(contact.getIndividual());
        }

        if (contact.getEmail() != null) {
            agent.mbox("mailto:" + contact.getEmail());
        }

        if (contact.getWebsite() != null) {
            agent.homepage(DcatModel.Resource.of(contact.getWebsite()));
        }

        if (contact.getIdentifier() != null && !contact.getIdentifier().isEmpty()) {
            contact.getIdentifier().stream()
                    .filter(id -> id.getLink() != null)
                    .findFirst()
                    .ifPresent(id -> agent.id(id.getLink()));
        }

        if (contact.getOrganisation() != null && !contact.getOrganisation().isEmpty()) {
            agent.agentType(DcatModel.Resource.of("http://purl.org/adms/agenttype/PublicOrganisation"));
        }

        return agent.build();
    }

    private List<DcatModel.ContactPoint> mapContactPoints(IndexRecord record) {
        Map<String, List<Contact>> contactByRole = record.getContactByRole();

        List<Contact> contactPoints = new ArrayList<>();

        if (contactByRole.get("pointOfContact") != null) {
            contactPoints.addAll(contactByRole.get("pointOfContact"));
        }

        if (contactPoints.isEmpty() && contactByRole.get("distributor") != null) {
            contactPoints.addAll(contactByRole.get("distributor"));
        }

        if (contactPoints.isEmpty()) {
            return null;
        }

        return contactPoints.stream().map(this::mapToContactPoint).collect(Collectors.toList());
    }

    private DcatModel.ContactPoint mapToContactPoint(Contact contact) {
        DcatModel.ContactPoint.ContactPointBuilder cp = DcatModel.ContactPoint.builder();

        if (contact.getOrganisation() != null && !contact.getOrganisation().isEmpty()) {
            cp.fn(DcatModel.MultilingualValue.of(contact.getOrganisation()));
        } else if (contact.getIndividual() != null) {
            cp.fn(DcatModel.MultilingualValue.builder()
                    .value(contact.getIndividual())
                    .build());
        }

        if (contact.getEmail() != null) {
            cp.hasEmail("mailto:" + contact.getEmail());
        }

        if (contact.getPhone() != null) {
            cp.hasTelephone(DcatModel.Telephone.builder()
                    .hasValue("tel:" + contact.getPhone())
                    .build());
        }

        if (contact.getAddress() != null) {
            cp.hasAddress(DcatModel.Address.builder()
                    .streetAddress(contact.getAddress())
                    .build());
        }

        if (contact.getWebsite() != null) {
            cp.hasURL(contact.getWebsite());
        }

        return cp.build();
    }

    private List<DcatModel.Distribution> mapDistributions(IndexRecord record, String baseUri) {
        if (record.getLinks() == null || record.getLinks().isEmpty()) {
            return null;
        }

        List<DcatModel.Distribution> distributions = new ArrayList<>();
        Map<String, DcatModel.DataService> services = new HashMap<>();
        int index = 0;

        for (Link link : record.getLinks()) {
            String protocol = link.getProtocol();

            if (isServiceProtocol(protocol)) {
                DcatModel.DataService service = createOrGetDataService(link, services, baseUri, record.getUuid());

                DcatModel.Distribution.DistributionBuilder dist = DcatModel.Distribution.builder()
                        .id(generateUri(baseUri, "distribution", record.getUuid() + "-" + index++))
                        .accessService(service)
                        .accessURL(service.getEndpointURL());

                if (link.getName() != null && !link.getName().isEmpty()) {
                    dist.title(DcatModel.MultilingualValue.of(link.getName()));
                }
                if (link.getDescription() != null && !link.getDescription().isEmpty()) {
                    dist.description(DcatModel.MultilingualValue.of(link.getDescription()));
                }

                distributions.add(dist.build());
            } else {
                DcatModel.Distribution dist = createFileDistribution(link, baseUri, record.getUuid(), index++);
                if (dist != null) {
                    distributions.add(dist);
                }
            }
        }

        return distributions.isEmpty() ? null : distributions;
    }

    private boolean isServiceProtocol(String protocol) {
        if (protocol == null) return false;

        return protocol.startsWith("OGC:")
                || protocol.startsWith("ESRI:")
                || protocol.contains("API")
                || protocol.equals("WWW:LINK-1.0-http--link");
    }

    private DcatModel.DataService createOrGetDataService(
            Link link, Map<String, DcatModel.DataService> services, String baseUri, String datasetUuid) {
        String serviceUrl = extractServiceUrl(link);

        if (services.containsKey(serviceUrl)) {
            return services.get(serviceUrl);
        }

        DcatModel.DataService.DataServiceBuilder service = DcatModel.DataService.builder()
                .id(generateUri(baseUri, "service", Integer.toHexString(serviceUrl.hashCode())))
                .endpointURL(DcatModel.Resource.of(serviceUrl));

        if (link.getName() != null && !link.getName().isEmpty()) {
            service.title(DcatModel.MultilingualValue.of(link.getName()));
        }
        if (link.getDescription() != null && !link.getDescription().isEmpty()) {
            service.description(DcatModel.MultilingualValue.of(link.getDescription()));
        }

        String conformsToUri = mapProtocolToStandard(link.getProtocol());
        if (conformsToUri != null) {
            service.conformsTo(
                    List.of(DcatModel.Standard.builder().id(conformsToUri).build()));
        }

        service.servesDataset(List.of(DcatModel.Resource.of(generateUri(baseUri, "dataset", datasetUuid))));

        DcatModel.DataService dataService = service.build();
        services.put(serviceUrl, dataService);
        return dataService;
    }

    private String extractServiceUrl(Link link) {
        if (link.getUrl() != null && !link.getUrl().isEmpty()) {
            String url = link.getUrl().get("default");
            if (url == null && !link.getUrl().isEmpty()) {
                url = link.getUrl().values().iterator().next();
            }

            if (url != null && url.contains("?")) {
                return url.substring(0, url.indexOf("?"));
            }
            return url;
        }
        return "";
    }

    private DcatModel.Distribution createFileDistribution(Link link, String baseUri, String datasetUuid, int index) {
        DcatModel.Distribution.DistributionBuilder dist =
                DcatModel.Distribution.builder().id(generateUri(baseUri, "distribution", datasetUuid + "-" + index));

        if (link.getUrl() != null && !link.getUrl().isEmpty()) {
            String accessUrl = null;
            if (link.getUrl() != null && !link.getUrl().isEmpty()) {
                accessUrl = link.getUrl().get("default");
                if (accessUrl == null && !link.getUrl().isEmpty()) {
                    accessUrl = link.getUrl().values().iterator().next();
                }
            }
            if (accessUrl != null) {
                dist.accessURL(DcatModel.Resource.of(accessUrl));
            } else {
                dist.accessURL(DcatModel.Resource.of(
                        generateUri(baseUri, "distribution", datasetUuid + "-" + index + "/access")));
            }

            if (link.getProtocol() != null && link.getProtocol().contains("DOWNLOAD")) {
                dist.downloadURL(DcatModel.Resource.of(accessUrl));
            }
        }

        if (link.getName() != null && !link.getName().isEmpty()) {
            dist.title(DcatModel.MultilingualValue.of(link.getName()));
        }
        if (link.getDescription() != null && !link.getDescription().isEmpty()) {
            dist.description(DcatModel.MultilingualValue.of(link.getDescription()));
        }

        String mimeType = link.getMimeType();
        if (mimeType == null && link.getProtocol() != null) {
            mimeType = inferMimeType(link.getProtocol(), link.getUrl());
        }

        if (mimeType != null && !mimeType.isEmpty()) {
            dist.mediaType(DcatModel.MediaType.of(mediaTypeBaseUri + mimeType));

            String formatUri = mapMimeTypeToFormat(mimeType);
            if (formatUri != null) {
                dist.format(DcatModel.MediaTypeOrExtent.of(formatUri));
            }
        }

        dist.status(DcatModel.Resource.of("http://publications.europa.eu/resource/authority/dataset-status/COMPLETED"));

        if (link.getProtocol() != null) {
            String conformsToUri = mapProtocolToStandard(link.getProtocol());
            if (conformsToUri != null) {
                dist.conformsTo(
                        List.of(DcatModel.Standard.builder().id(conformsToUri).build()));
            }
        }

        return dist.build();
    }

    private String inferMimeType(String protocol, Map<String, String> urls) {
        if (protocol == null) return null;

        if (protocol.startsWith("OGC:WMS")) {
            return "application/vnd.ogc.wms_xml";
        } else if (protocol.startsWith("OGC:WFS")) {
            return "application/gml+xml";
        } else if (protocol.startsWith("OGC:WCS")) {
            return "application/x-ogc-wcs";
        } else if (protocol.contains("DOWNLOAD")) {
            if (urls != null && !urls.isEmpty()) {
                String url = urls.values().iterator().next();
                if (url != null) {
                    if (url.endsWith(".zip")) return "application/zip";
                    if (url.endsWith(".pdf")) return "application/pdf";
                    if (url.endsWith(".xml")) return "application/xml";
                    if (url.endsWith(".json")) return "application/json";
                    if (url.endsWith(".geojson")) return "application/geo+json";
                    if (url.endsWith(".csv")) return "text/csv";
                    if (url.endsWith(".xlsx"))
                        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    if (url.endsWith(".shp")) return "application/x-shapefile";
                    if (url.endsWith(".gpkg")) return "application/geopackage+sqlite3";
                    if (url.endsWith(".tif") || url.endsWith(".tiff")) return "image/tiff";
                }
            }
            return "application/octet-stream";
        }

        return null;
    }

    private String mapMimeTypeToFormat(String mimeType) {
        Map<String, String> formatMap = Map.of(
                "application/zip", fileTypeAuthorityUri + "ZIP",
                "application/pdf", fileTypeAuthorityUri + "PDF",
                "text/csv", fileTypeAuthorityUri + "CSV",
                "application/json", fileTypeAuthorityUri + "JSON",
                "application/xml", fileTypeAuthorityUri + "XML",
                "application/gml+xml", fileTypeAuthorityUri + "GML",
                "application/geo+json", fileTypeAuthorityUri + "GEOJSON",
                "application/geopackage+sqlite3", fileTypeAuthorityUri + "GPKG");

        return formatMap.getOrDefault(mimeType, fileTypeAuthorityUri + "OCTET");
    }

    private String mapProtocolToStandard(String protocol) {
        if (protocol == null) return null;

        Map<String, String> standardMap = Map.of(
                "OGC:WMS", "http://www.opengis.net/def/serviceType/ogc/wms",
                "OGC:WFS", "http://www.opengis.net/def/serviceType/ogc/wfs",
                "OGC:WCS", "http://www.opengis.net/def/serviceType/ogc/wcs",
                "OGC:WMTS", "http://www.opengis.net/def/serviceType/ogc/wmts",
                "OGC:CSW", "http://www.opengis.net/def/serviceType/ogc/csw",
                "OGC:SOS", "http://www.opengis.net/def/serviceType/ogc/sos",
                "OGC:WPS", "http://www.opengis.net/def/serviceType/ogc/wps");

        for (Map.Entry<String, String> entry : standardMap.entrySet()) {
            if (protocol.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }

    private DcatModel.RightsStatement mapAccessRights(IndexRecord record) {
        String uri = record.isPublishedToAll() ? publicAccessRightUri : restrictedAccessRightUri;
        return DcatModel.RightsStatement.of(uri);
    }

    private List<DcatModel.Resource> mapLandingPages(IndexRecord record) {
        List<DcatModel.Resource> pages = new ArrayList<>();

        if (record.getUuid() != null) {
            pages.add(DcatModel.Resource.of(defaultBaseUri + "catalog/record/" + record.getUuid()));
        }

        return pages.isEmpty() ? null : pages;
    }

    private List<DcatModel.Standard> mapConformance(IndexRecord record) {
        if (record.getSpecificationConformance() == null
                || record.getSpecificationConformance().isEmpty()) {
            return null;
        }

        return record.getSpecificationConformance().stream()
                .map(spec -> DcatModel.Standard.builder()
                        .id(spec.getLink())
                        .title(
                                spec.getTitle() != null
                                        ? DcatModel.MultilingualValue.builder()
                                                .value(spec.getTitle())
                                                .build()
                                        : null)
                        .build())
                .collect(Collectors.toList());
    }

    private Object mapLicense(IndexRecord record) {
        if (record.getLicenses() == null || record.getLicenses().isEmpty()) {
            return null;
        }

        List<DcatModel.Standard> licenses = record.getLicenses().stream()
                .map(license -> {
                    String link = license.get("link");
                    Map<String, String> licenseText = new HashMap<>(license);
                    licenseText.remove("link");

                    return DcatModel.Standard.builder()
                            .type("dct:LicenseDocument")
                            .id(
                                    link != null
                                            ? link
                                            : "http://publications.europa.eu/resource/authority/licence/UNKNOWN")
                            .title(!licenseText.isEmpty() ? DcatModel.MultilingualValue.of(licenseText) : null)
                            .build();
                })
                .collect(Collectors.toList());

        return licenses.size() == 1 ? licenses.get(0) : licenses;
    }

    private List<DcatModel.RightsStatement> mapRights(IndexRecord record) {
        return null;
    }

    private List<DcatModel.Resource> mapRelations(IndexRecord record, String baseUri) {
        List<DcatModel.Resource> relations = new ArrayList<>();

        if (record.getParentUuid() != null) {
            for (String parentUuid : record.getParentUuid()) {
                relations.add(DcatModel.Resource.of(generateUri(baseUri, "dataset", parentUuid)));
            }
        }

        if (record.getAssociatedRecords() != null) {
            for (RecordLink link : record.getAssociatedRecords()) {
                if (!isSpecialRelationType(link.getType())) {
                    relations.add(DcatModel.Resource.of(generateUri(baseUri, "dataset", link.getTo())));
                }
            }
        }

        return relations.isEmpty() ? null : relations;
    }

    private List<DcatModel.Resource> mapSources(IndexRecord record, String baseUri) {
        if (record.getAssociatedRecords() == null) {
            return null;
        }

        List<DcatModel.Resource> sources = record.getAssociatedRecords().stream()
                .filter(link -> "source".equals(link.getType()) || "lineage".equals(link.getType()))
                .map(link -> DcatModel.Resource.of(generateUri(baseUri, "dataset", link.getTo())))
                .collect(Collectors.toList());

        return sources.isEmpty() ? null : sources;
    }

    private List<DcatModel.Resource> mapReferences(IndexRecord record, String baseUri) {
        if (record.getAssociatedRecords() == null) {
            return null;
        }

        List<DcatModel.Resource> references = record.getAssociatedRecords().stream()
                .filter(link -> "references".equals(link.getType()) || "isReferencedBy".equals(link.getType()))
                .map(link -> DcatModel.Resource.of(generateUri(baseUri, "dataset", link.getTo())))
                .collect(Collectors.toList());

        return references.isEmpty() ? null : references;
    }

    private boolean isSpecialRelationType(String type) {
        return "source".equals(type)
                || "lineage".equals(type)
                || "references".equals(type)
                || "isReferencedBy".equals(type)
                || "revisionOf".equals(type)
                || "version".equals(type);
    }

    private List<DcatModel.QualityMeasurement> mapQualityMeasurements(IndexRecord record) {
        if (record.getSpecificationConformance() == null) {
            return null;
        }

        List<DcatModel.QualityMeasurement> measurements = new ArrayList<>();

        for (SpecificationConformance spec : record.getSpecificationConformance()) {
            if (spec.getPass() != null) {
                measurements.add(DcatModel.QualityMeasurement.builder()
                        .isMeasurementOf(DcatModel.Resource.of(spec.getLink()))
                        .value(Boolean.parseBoolean(spec.getPass()))
                        .build());
            }
        }

        return measurements.isEmpty() ? null : measurements;
    }

    private String mapVersion(IndexRecord record) {
        return null;
    }

    private String mapVersionInfo(IndexRecord record) {
        return null;
    }

    private DcatModel.MultilingualValue mapVersionNotes(IndexRecord record) {
        return null;
    }

    private List<DcatModel.Resource> mapHasVersions(IndexRecord record, String baseUri) {
        if (record.getAssociatedRecords() == null) {
            return null;
        }

        List<DcatModel.Resource> versions = record.getAssociatedRecords().stream()
                .filter(link -> "revisionOf".equals(link.getType()) || "version".equals(link.getType()))
                .map(link -> DcatModel.Resource.of(generateUri(baseUri, "dataset", link.getTo())))
                .collect(Collectors.toList());

        return versions.isEmpty() ? null : versions;
    }

    private DcatModel.Resource mapIsVersionOf(IndexRecord record, String baseUri) {
        return null;
    }

    private DcatModel.Resource mapPreviousVersion(IndexRecord record, String baseUri) {
        return null;
    }

    private List<DcatModel.Resource> mapProvenance(IndexRecord record) {
        return null;
    }

    private List<DcatModel.Attribution> mapQualifiedAttributions(IndexRecord record) {
        Map<String, List<Contact>> contactByRole = record.getContactByRole();
        List<DcatModel.Attribution> attributions = new ArrayList<>();

        for (Map.Entry<String, List<Contact>> entry : contactByRole.entrySet()) {
            String role = entry.getKey();
            List<Contact> contacts = entry.getValue();

            if (shouldMapAsAttribution(role)) {
                String roleUri = mapRoleToUri(role);
                for (Contact contact : contacts) {
                    attributions.add(DcatModel.Attribution.builder()
                            .agent(mapToAgent(contact))
                            .hadRole(DcatModel.Resource.of(roleUri))
                            .build());
                }
            }
        }

        return attributions.isEmpty() ? null : attributions;
    }

    private boolean shouldMapAsAttribution(String role) {
        return !"publisher".equals(role)
                && !"pointOfContact".equals(role)
                && !"originator".equals(role)
                && !"author".equals(role);
    }

    private String mapRoleToUri(String role) {
        Map<String, String> roleMap = Map.of(
                "owner", "http://publications.europa.eu/resource/authority/role/OWN",
                "custodian", "http://publications.europa.eu/resource/authority/role/CUS",
                "distributor", "http://publications.europa.eu/resource/authority/role/DIS",
                "processor", "http://publications.europa.eu/resource/authority/role/PRO",
                "user", "http://publications.europa.eu/resource/authority/role/USE");

        return roleMap.getOrDefault(role, "http://publications.europa.eu/resource/authority/role/OTH");
    }

    private DcatModel.Activity mapGenerationActivity(IndexRecord record) {
        return null;
    }

    private List<DcatModel.Relationship> mapQualifiedRelations(IndexRecord record, String baseUri) {
        return null;
    }

    private DcatModel.Resource mapDatasetType(IndexRecord record) {
        if (record.getResourceType() != null && !record.getResourceType().isEmpty()) {
            String type = record.getResourceType().get(0);
            return DcatModel.Resource.of("http://inspire.ec.europa.eu/metadata-codelist/ResourceType/" + type);
        }
        return null;
    }

    private DcatModel.Resource mapAccrualPeriodicity(IndexRecord record) {
        return null;
    }

    private List<DcatModel.Resource> mapPages(IndexRecord record) {
        return null;
    }

    private List<DcatModel.Resource> mapSamples(IndexRecord record, String baseUri) {
        return null;
    }
}
