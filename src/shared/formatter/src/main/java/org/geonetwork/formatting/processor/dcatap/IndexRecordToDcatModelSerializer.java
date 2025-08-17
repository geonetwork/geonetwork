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

/** Service for converting IndexRecord to DCAT-AP 3.0 using Java models. */
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

    private Map<String, Object> namespaceContext;

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
    }

    /** Serialize an IndexRecord to DCAT-AP 3.0 JSON-LD format. */
    public String serialize(IndexRecord record) throws JsonProcessingException {
        return serialize(record, null, defaultBaseUri);
    }

    public String serialize(IndexRecord record, String catalogUri, String baseUri) throws JsonProcessingException {
        String effectiveBaseUri = (baseUri != null && !baseUri.isEmpty()) ? baseUri : defaultBaseUri;

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
                    // .issued(id.getCreatedDate()) // Not available in current model
                    .build());
        }
        return identifiers;
    }

    /**
     * Map keywords to string literals only. According to DCAT-AP 3.0, dcat:keyword should contain only literal strings.
     * Concepts with URIs go to dcat:theme (for INSPIRE/EU vocabularies) or dct:subject (for others).
     */
    private List<Object> mapKeywords(IndexRecord record) {
        if (record.getAllKeywords() == null) {
            return null;
        }

        List<Object> keywords = new ArrayList<>();

        for (Thesaurus thesaurus : record.getAllKeywords().values()) {
            if (thesaurus.getKeywords() == null) continue;

            for (Keyword keyword : thesaurus.getKeywords()) {
                if (keyword.getProperties() == null) continue;

                // Extract the label value
                String value = keyword.getProperties().get("default");
                if (value == null) {
                    value = keyword.getProperties().get("en");
                }

                // Only add non-empty string values to keywords
                // Concepts with URIs are handled in mapThemes() and mapSubjects()
                if (value != null && !value.isEmpty()) {
                    String link = keyword.getProperties().get("link");
                    if (link == null || !link.startsWith("http")) {
                        // Only add as keyword if it doesn't have a URI
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

            // Check if this is an EU vocabulary (INSPIRE, EUROVOC, etc.)
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

                    // Add scheme reference if available
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
                // Add concepts with URIs that are not EU controlled vocabularies to subjects
                if (link != null
                        && link.startsWith("http")
                        && !link.contains(inspireThemeUriPattern)
                        && !link.contains("eurovoc")
                        && !link.contains("data-theme")) {

                    DcatModel.Concept.ConceptBuilder concept = DcatModel.Concept.builder()
                            .id(link)
                            .prefLabel(DcatModel.MultilingualValue.of(keyword.getProperties()));

                    // Add scheme reference if available
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

        // Add named locations
        if (record.getExtentDescription() != null) {
            for (Map<String, String> extent : record.getExtentDescription()) {
                spatial.add(DcatModel.Location.builder()
                        .label(DcatModel.MultilingualValue.of(extent))
                        .build());
            }
        }

        // Add geometries
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

        // Add bounding box if available
        // Note: getGeoBoundingBox() is not available in current IndexRecord model
        // This would need to be implemented when the model is extended
        /*
        if (record.getGeoBoundingBox() != null && !record.getGeoBoundingBox().isEmpty()) {
            // Convert bounding box to WKT or GeoJSON if needed
            for (String bbox : record.getGeoBoundingBox()) {
                spatial.add(DcatModel.Location.builder()
                        .bbox(bbox)
                        .build());
            }
        }
        */

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
        // Map spatial resolution if available in the record
        // This would need to be extracted from the metadata
        return null; // Placeholder - implement based on your IndexRecord structure
    }

    private String mapTemporalResolution(IndexRecord record) {
        // Map temporal resolution as xsd:duration if available
        // Format: P[n]Y[n]M[n]DT[n]H[n]M[n]S
        return null; // Placeholder - implement based on your IndexRecord structure
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
                .sorted(Comparator.reverseOrder()) // Get the most recent
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

        // Map common 2-letter codes to 3-letter ISO 639-2 codes
        Map<String, String> iso2ToIso3 = Map.of(
                "EN", "ENG",
                "FR", "FRA",
                "DE", "DEU",
                "IT", "ITA",
                "ES", "SPA",
                "PT", "POR",
                "NL", "NLD");

        // Map common variations
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

        // Collect all creator-like roles
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

        // Set organization name
        if (contact.getOrganisation() != null && !contact.getOrganisation().isEmpty()) {
            agent.name(DcatModel.MultilingualValue.of(contact.getOrganisation()));
        }

        // Set individual name
        if (contact.getIndividual() != null) {
            agent.givenName(contact.getIndividual());
        }

        // Set email
        if (contact.getEmail() != null) {
            agent.mbox("mailto:" + contact.getEmail());
        }

        // Set website
        if (contact.getWebsite() != null) {
            agent.homepage(DcatModel.Resource.of(contact.getWebsite()));
        }

        // Set identifier from contact identifiers
        if (contact.getIdentifier() != null && !contact.getIdentifier().isEmpty()) {
            contact.getIdentifier().stream()
                    .filter(id -> id.getLink() != null)
                    .findFirst()
                    .ifPresent(id -> agent.id(id.getLink()));
        }

        // Set agent type if available (organization vs person)
        if (contact.getOrganisation() != null && !contact.getOrganisation().isEmpty()) {
            agent.agentType(DcatModel.Resource.of("http://purl.org/adms/agenttype/PublicOrganisation"));
        }

        return agent.build();
    }

    private List<DcatModel.ContactPoint> mapContactPoints(IndexRecord record) {
        Map<String, List<Contact>> contactByRole = record.getContactByRole();

        List<Contact> contactPoints = new ArrayList<>();

        // Primary contact points
        if (contactByRole.get("pointOfContact") != null) {
            contactPoints.addAll(contactByRole.get("pointOfContact"));
        }

        // Also include distributor as contact point if no point of contact
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

            // Check if this is a service endpoint (OGC services, APIs, etc.)
            if (isServiceProtocol(protocol)) {
                // Create or get the data service
                DcatModel.DataService service = createOrGetDataService(link, services, baseUri, record.getUuid());

                // Create a distribution that references this service
                DcatModel.Distribution.DistributionBuilder dist = DcatModel.Distribution.builder()
                        .id(generateUri(baseUri, "distribution", record.getUuid() + "-" + index++))
                        .accessService(service)
                        .accessURL(service.getEndpointURL());

                // Add title and description if available
                if (link.getName() != null && !link.getName().isEmpty()) {
                    dist.title(DcatModel.MultilingualValue.of(link.getName()));
                }
                if (link.getDescription() != null && !link.getDescription().isEmpty()) {
                    dist.description(DcatModel.MultilingualValue.of(link.getDescription()));
                }

                distributions.add(dist.build());
            } else {
                // Regular file distribution
                DcatModel.Distribution dist = createFileDistribution(link, baseUri, record.getUuid(), index++);
                if (dist != null) { // Only add non-null distributions
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

        // Add title and description
        if (link.getName() != null && !link.getName().isEmpty()) {
            service.title(DcatModel.MultilingualValue.of(link.getName()));
        }
        if (link.getDescription() != null && !link.getDescription().isEmpty()) {
            service.description(DcatModel.MultilingualValue.of(link.getDescription()));
        }

        // Map protocol to conformsTo
        String conformsToUri = mapProtocolToStandard(link.getProtocol());
        if (conformsToUri != null) {
            service.conformsTo(
                    List.of(DcatModel.Standard.builder().id(conformsToUri).build()));
        }

        // Link back to the dataset
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

            // For OGC services, extract base URL without parameters
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

        // Set access URL
        if (link.getUrl() != null && !link.getUrl().isEmpty()) {
            String accessUrl = null;
            if (link.getUrl() != null && !link.getUrl().isEmpty()) {
                accessUrl = link.getUrl().get("default");
                if (accessUrl == null && !link.getUrl().isEmpty()) {
                    accessUrl = link.getUrl().values().iterator().next();
                }
            }
            // Always set accessURL - use a placeholder if necessary
            if (accessUrl != null) {
                dist.accessURL(DcatModel.Resource.of(accessUrl));
            } else {
                // Use a placeholder
                dist.accessURL(DcatModel.Resource.of(
                        generateUri(baseUri, "distribution", datasetUuid + "-" + index + "/access")));
                // ... or skip this distribution entirely
                // return null;
            }

            // If it's a direct download, also set downloadURL
            if (link.getProtocol() != null && link.getProtocol().contains("DOWNLOAD")) {
                dist.downloadURL(DcatModel.Resource.of(accessUrl));
            }
        }

        // Set title and description
        if (link.getName() != null && !link.getName().isEmpty()) {
            dist.title(DcatModel.MultilingualValue.of(link.getName()));
        }
        if (link.getDescription() != null && !link.getDescription().isEmpty()) {
            dist.description(DcatModel.MultilingualValue.of(link.getDescription()));
        }

        // Determine and set media type and format
        String mimeType = link.getMimeType();
        if (mimeType == null && link.getProtocol() != null) {
            mimeType = inferMimeType(link.getProtocol(), link.getUrl());
        }

        if (mimeType != null && !mimeType.isEmpty()) {
            // Set media type
            dist.mediaType(DcatModel.MediaType.of(mediaTypeBaseUri + mimeType));

            // Also set format based on mime type
            String formatUri = mapMimeTypeToFormat(mimeType);
            if (formatUri != null) {
                dist.format(DcatModel.MediaTypeOrExtent.of(formatUri));
            }
        }

        // Set byte size if available
        // Note: getSize() is not available in current Link model
        /*
        if (link.getSize() != null) {
            try {
                dist.byteSize(Long.parseLong(link.getSize()));
            } catch (NumberFormatException e) {
                // Ignore if not a valid number
            }
        }
        */

        // Set license if available
        // Note: getLicense() is not available in current Link model
        /*
        if (link.getLicense() != null) {
            dist.license(DcatModel.Resource.of(link.getLicense()));
        }
        */

        // Set rights if available
        // Note: getRights() is not available in current Link model
        /*
        if (link.getRights() != null) {
            dist.rights(DcatModel.RightsStatement.builder()
                    .label(DcatModel.MultilingualValue.builder()
                            .value(link.getRights())
                            .build())
                    .build());
        }
        */

        // Set status (DCAT-AP 3.0)
        dist.status(DcatModel.Resource.of("http://publications.europa.eu/resource/authority/dataset-status/COMPLETED"));

        // Set conformsTo for file standards if applicable
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

        // Check protocol
        if (protocol.startsWith("OGC:WMS")) {
            return "application/vnd.ogc.wms_xml";
        } else if (protocol.startsWith("OGC:WFS")) {
            return "application/gml+xml";
        } else if (protocol.startsWith("OGC:WCS")) {
            return "application/x-ogc-wcs";
        } else if (protocol.contains("DOWNLOAD")) {
            // Check file extension from URL
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
        // Map landing pages if available
        List<DcatModel.Resource> pages = new ArrayList<>();

        // Add the catalog page as landing page
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
        // Map additional rights statements if available
        return null; // Placeholder - implement based on your IndexRecord structure
    }

    private List<DcatModel.Resource> mapRelations(IndexRecord record, String baseUri) {
        List<DcatModel.Resource> relations = new ArrayList<>();

        // Add parent datasets
        if (record.getParentUuid() != null) {
            for (String parentUuid : record.getParentUuid()) {
                relations.add(DcatModel.Resource.of(generateUri(baseUri, "dataset", parentUuid)));
            }
        }

        // Add associated records
        if (record.getAssociatedRecords() != null) {
            for (RecordLink link : record.getAssociatedRecords()) {
                // Filter out specific relation types that map to other properties
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

    // DCAT-AP 3.0 specific versioning methods
    private String mapVersion(IndexRecord record) {
        // Extract version from metadata if available
        return null; // Placeholder - implement based on your IndexRecord structure
    }

    private String mapVersionInfo(IndexRecord record) {
        // Map version info if available
        return null; // Placeholder - implement based on your IndexRecord structure
    }

    private DcatModel.MultilingualValue mapVersionNotes(IndexRecord record) {
        // Map version notes if available
        return null; // Placeholder - implement based on your IndexRecord structure
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
        // Map to original dataset if this is a version
        return null; // Placeholder - implement based on your IndexRecord structure
    }

    private DcatModel.Resource mapPreviousVersion(IndexRecord record, String baseUri) {
        // Map to previous version if available
        return null; // Placeholder - implement based on your IndexRecord structure
    }

    // DCAT-AP 3.0 specific provenance methods
    private List<DcatModel.Resource> mapProvenance(IndexRecord record) {
        // Map provenance statements if available
        return null; // Placeholder - implement based on your IndexRecord structure
    }

    private List<DcatModel.Attribution> mapQualifiedAttributions(IndexRecord record) {
        // Map qualified attributions for different roles
        Map<String, List<Contact>> contactByRole = record.getContactByRole();
        List<DcatModel.Attribution> attributions = new ArrayList<>();

        // Map specific roles to attributions
        for (Map.Entry<String, List<Contact>> entry : contactByRole.entrySet()) {
            String role = entry.getKey();
            List<Contact> contacts = entry.getValue();

            // Only include roles that should be attributions (not already mapped elsewhere)
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
        // Exclude roles already mapped to specific properties
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
        // Map generation activity if provenance information is available
        return null; // Placeholder - implement based on your IndexRecord structure
    }

    private List<DcatModel.Relationship> mapQualifiedRelations(IndexRecord record, String baseUri) {
        // Map qualified relationships with roles
        return null; // Placeholder - implement based on your IndexRecord structure
    }

    // Additional DCAT-AP 3.0 properties
    private DcatModel.Resource mapDatasetType(IndexRecord record) {
        // Map dataset type (e.g., from resource type)
        if (record.getResourceType() != null && !record.getResourceType().isEmpty()) {
            String type = record.getResourceType().get(0);
            return DcatModel.Resource.of("http://inspire.ec.europa.eu/metadata-codelist/ResourceType/" + type);
        }
        return null;
    }

    private DcatModel.Resource mapAccrualPeriodicity(IndexRecord record) {
        // Map update frequency if available
        return null; // Placeholder - implement based on your IndexRecord structure
    }

    private List<DcatModel.Resource> mapPages(IndexRecord record) {
        // Map documentation pages if available
        return null; // Placeholder - implement based on your IndexRecord structure
    }

    private List<DcatModel.Resource> mapSamples(IndexRecord record, String baseUri) {
        // Map sample distributions if available
        return null; // Placeholder - implement based on your IndexRecord structure
    }

    private String generateUri(String baseUri, String type, String id) {
        if (!baseUri.endsWith("/")) {
            baseUri += "/";
        }
        return baseUri + type + "/" + id;
    }
}
