/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting.processor.dcatap;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.*;
import org.geonetwork.index.model.record.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class IndexRecordToDcatModelSerializerTest {

    private IndexRecordToDcatModelSerializer serializer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        serializer = new IndexRecordToDcatModelSerializer(objectMapper);

        ReflectionTestUtils.setField(serializer, "defaultBaseUri", "http://data.europa.eu/");
        ReflectionTestUtils.setField(
                serializer, "languageAuthorityUri", "http://publications.europa.eu/resource/authority/language/");
        ReflectionTestUtils.setField(
                serializer,
                "publicAccessRightUri",
                "http://publications.europa.eu/resource/authority/access-right/PUBLIC");
        ReflectionTestUtils.setField(
                serializer,
                "restrictedAccessRightUri",
                "http://publications.europa.eu/resource/authority/access-right/RESTRICTED");
        ReflectionTestUtils.setField(serializer, "inspireThemeUriPattern", "inspire.ec.europa.eu/theme");
        ReflectionTestUtils.setField(serializer, "mediaTypeBaseUri", "https://www.iana.org/assignments/media-types/");
        ReflectionTestUtils.setField(
                serializer, "fileTypeAuthorityUri", "http://publications.europa.eu/resource/authority/file-type/");

        serializer.init();
    }

    @Test
    void serialize_withCompleteRecord_mapsAllFields_dcatFormat() throws JsonProcessingException {
        IndexRecord record = createCompleteIndexRecord();

        String result = serializer.serialize(record, null, null, "dcat");

        assertNotNull(result);
        assertTrue(result.contains("\"@type\" : \"dcat:Dataset\""));
        assertTrue(result.contains("78f93047-74f8-4419-ac3d-fc62e4b0477b"));

        // Verify multilingual title at root level for DCAT
        assertTrue(result.contains("\"dct:title\""));
        assertTrue(result.contains("Physiographic Map"));

        // Verify temporal extent
        assertTrue(result.contains("2000-01-01"));
        assertTrue(result.contains("2008-01-08"));

        // Verify distributions from links
        assertTrue(result.contains("dcat:Distribution"));

        // Should NOT have Feature type in pure DCAT mode
        assertFalse(result.contains("\"type\" : \"Feature\""));
    }

    @Test
    void serialize_withCompleteRecord_ogcFormat() throws JsonProcessingException {
        IndexRecord record = createCompleteIndexRecord();

        String result = serializer.serialize(record, null, null, "ogc");

        assertNotNull(result);

        // OGC API Records specific assertions
        assertTrue(result.contains("\"type\" : \"Feature\""));
        assertTrue(result.contains("\"@type\" : [ \"geo:Feature\" ]"));
        assertTrue(result.contains("\"properties\""));

        // Properties should contain simple strings for OGC
        assertTrue(result.contains("\"recordCreated\""));
        assertTrue(result.contains("\"recordUpdated\""));

        // Title should be in properties as simple string
        assertTrue(result.contains("\"title\" : \"Physiographic Map"));
        assertTrue(result.contains("\"description\" : \"Physiographic maps"));

        // Should have links array
        assertTrue(result.contains("\"links\""));

        // Should NOT have DCAT properties at root in pure OGC mode
        assertFalse(result.contains("\"dct:title\""));
        assertFalse(result.contains("\"dcat:distribution\""));
    }

    @Test
    void serialize_withCompleteRecord_hybridFormat() throws JsonProcessingException {
        IndexRecord record = createCompleteIndexRecord();

        String result = serializer.serialize(record, null, null, "hybrid");

        assertNotNull(result);

        // Should have both Feature and Dataset types
        assertTrue(result.contains("\"@type\" : [ \"geo:Feature\", \"dcat:Dataset\" ]"));
        assertTrue(result.contains("\"type\" : \"Feature\""));

        // DCAT-AP properties should be at ROOT level for proper compliance
        assertTrue(result.contains("\"dct:title\" : {"));
        assertTrue(result.contains("\"dct:description\" : {"));
        assertTrue(result.contains("\"dcat:distribution\""));

        // OGC properties should be in properties object with simple values
        assertTrue(result.contains("\"properties\" : {"));

        // Verify properties contains simple OGC values
        JsonNode jsonNode = objectMapper.readTree(result);
        JsonNode properties = jsonNode.get("properties");
        assertNotNull(properties, "properties object should not be null");
        assertTrue(properties.has("recordCreated"));
        assertTrue(properties.has("recordUpdated"));
        assertTrue(properties.has("title"));
        assertTrue(properties.has("description"));

        // Properties should have simple string values, not objects
        assertTrue(
                properties.get("title").isTextual(),
                "title in properties should be a simple string, but was: " + properties.get("title"));
        assertTrue(
                properties.get("description").isTextual(),
                "description in properties should be a simple string, but was: " + properties.get("description"));

        // DCAT properties at root should be objects/arrays
        assertTrue(jsonNode.has("dct:title"));
        assertTrue(jsonNode.get("dct:title").isObject());

        // Should have geometry and time
        assertTrue(result.contains("\"geometry\""));
        assertTrue(result.contains("\"time\""));

        // Should have links
        assertTrue(result.contains("\"links\""));
    }

    @Test
    void serialize_multilingualFields_correctlyMapsLanguages() throws JsonProcessingException {
        IndexRecord record = IndexRecord.builder()
                .uuid("test-uuid")
                .resourceTitle(Map.of(
                        "default", "Default Title",
                        "lang", "Language Title",
                        "langfre", "Titre en français"))
                .resourceAbstract(Map.of(
                        "default", "Default Abstract",
                        "lang", "Language Abstract"))
                .publishedToAll(true)
                .build();

        // Test DCAT format
        String dcatResult = serializer.serialize(record, null, null, "dcat");
        assertTrue(dcatResult.contains("Default Title"));
        assertTrue(dcatResult.contains("\"dct:title\""));

        // Test hybrid format - should have multilingual at root
        String hybridResult = serializer.serialize(record, null, null, "hybrid");
        JsonNode hybridJson = objectMapper.readTree(hybridResult);

        // DCAT multilingual at root
        assertTrue(hybridJson.has("dct:title"), "Missing dct:title at root");
        JsonNode title = hybridJson.get("dct:title");
        assertTrue(
                title.has("@value") || title.has("fr"), "Title should have @value or fr language: " + title.toString());

        // OGC simple string in properties
        assertTrue(hybridJson.get("properties").has("title"));
        assertEquals("Default Title", hybridJson.get("properties").get("title").asText());
    }

    @Test
    void serialize_keywords_separatesInspireThemesFromGeneralSubjects() throws JsonProcessingException {
        IndexRecord record =
                IndexRecord.builder().uuid("test-uuid").publishedToAll(false).build();

        // Add INSPIRE theme keyword
        Thesaurus inspireThesaurus = new Thesaurus();
        inspireThesaurus.setId("inspire-themes");
        Keyword inspireKeyword = new Keyword();
        inspireKeyword.setProperties(Map.of(
                "default", "Land cover",
                "lang", "Land cover",
                "link", "http://inspire.ec.europa.eu/theme/lc"));
        inspireThesaurus.setKeywords(new ArrayList<>(List.of(inspireKeyword)));

        // Add regular keywords
        Thesaurus regularThesaurus = new Thesaurus();
        regularThesaurus.setId("general");
        Keyword regularKeyword1 = new Keyword();
        regularKeyword1.setProperties(Map.of(
                "default", "physiography, soil",
                "lang", "physiography, soil"));
        Keyword regularKeyword2 = new Keyword();
        regularKeyword2.setProperties(Map.of(
                "default", "Eurasia",
                "lang", "Eurasia",
                "link", "http://example.org/concept/eurasia"));
        regularThesaurus.setKeywords(new ArrayList<>(Arrays.asList(regularKeyword1, regularKeyword2)));

        record.getAllKeywords().put("th_inspire", inspireThesaurus);
        record.getAllKeywords().put("th_otherKeywords", regularThesaurus);

        String hybridResult = serializer.serialize(record, null, null, "hybrid");
        JsonNode jsonNode = objectMapper.readTree(hybridResult);

        // In hybrid mode, DCAT themes should be at root
        assertTrue(jsonNode.has("dcat:theme"), "Missing dcat:theme");
        assertTrue(hybridResult.contains("inspire.ec.europa.eu/theme"));

        // Plain text keywords should be in dcat:keyword at root
        assertTrue(jsonNode.has("dcat:keyword"), "Missing dcat:keyword");

        // OGC keywords should be simple array in properties
        JsonNode properties = jsonNode.get("properties");
        assertTrue(properties.has("keywords"));
        assertTrue(properties.get("keywords").isArray());
    }

    @Test
    void serialize_distributions_mapsProtocolsToFormats_hybridMode() throws JsonProcessingException {
        IndexRecord record =
                IndexRecord.builder().uuid("test-uuid").publishedToAll(true).build();

        List<Link> links = new ArrayList<>();

        // Download link
        Link downloadLink = new Link();
        downloadLink.setProtocol("WWW:DOWNLOAD-1.0-http--download");
        downloadLink.setUrl(Map.of(
                "default", "http://example.org/data.zip",
                "lang", "http://example.org/data.zip"));
        downloadLink.setName(Map.of(
                "default", "phy.zip",
                "lang", "phy.zip"));
        downloadLink.setDescription(Map.of(
                "default", "Download the dataset",
                "lang", "Download the dataset"));
        links.add(downloadLink);

        // WMS link
        Link wmsLink = new Link();
        wmsLink.setProtocol("OGC:WMS");
        wmsLink.setUrl(Map.of(
                "default", "https://example.org/wms",
                "lang", "https://example.org/wms"));
        wmsLink.setName(Map.of(
                "default", "geonetwork:layer",
                "lang", "geonetwork:layer"));
        links.add(wmsLink);

        record.setLinks(links);

        String result = serializer.serialize(record, null, null, "hybrid");
        JsonNode jsonNode = objectMapper.readTree(result);

        // DCAT distributions should be at root level in hybrid mode
        assertTrue(jsonNode.has("dcat:distribution"));
        assertTrue(result.contains("dcat:Distribution"));
        assertTrue(result.contains("dcat:accessURL"));
        assertTrue(result.contains("http://example.org/data.zip"));
        assertTrue(result.contains("https://example.org/wms"));

        // OGC links should be in links array
        assertTrue(jsonNode.has("links"));
        assertTrue(jsonNode.get("links").isArray());
    }

    @Test
    void serialize_contacts_mapsRolesToDcatProperties_hybridMode() throws JsonProcessingException {
        IndexRecord record =
                IndexRecord.builder().uuid("test-uuid").publishedToAll(true).build();

        // Point of contact (should map to contactPoint)
        Contact pointOfContact = Contact.builder()
                .role("pointOfContact")
                .individual("Anna Cerullo")
                .email("cerullo@example.org")
                .phone("+1234567890")
                .address("Via Monfalcone, Naples, Italy")
                .organisation(Map.of(
                        "default", "HAO - Ice and Fire Division",
                        "lang", "HAO - Ice and Fire Division"))
                .build();

        // Publisher
        Contact publisher = Contact.builder()
                .role("publisher")
                .organisation(Map.of(
                        "default", "Publishing Organization",
                        "lang", "Publishing Organization"))
                .website("http://publisher.org")
                .build();

        // Author (should map to creator)
        Contact author = Contact.builder()
                .role("author")
                .individual("John Doe")
                .email("john@example.org")
                .build();

        record.getContactByRole().put("pointOfContact", List.of(pointOfContact));
        record.getContactByRole().put("publisher", List.of(publisher));
        record.getContactByRole().put("author", List.of(author));

        String result = serializer.serialize(record, null, null, "hybrid");
        JsonNode jsonNode = objectMapper.readTree(result);

        // DCAT contacts should be at root level in hybrid mode
        assertTrue(jsonNode.has("dcat:contactPoint"), "Missing dcat:contactPoint");
        assertTrue(jsonNode.has("dct:publisher"), "Missing dct:publisher");
        assertTrue(jsonNode.has("dct:creator"), "Missing dct:creator");

        // Content verification
        assertTrue(result.contains("HAO"));
        assertTrue(result.contains("mailto:john@example.org"));

        // OGC contacts should be in properties as simple array
        JsonNode properties = jsonNode.get("properties");
        assertTrue(properties.has("contacts"));
        assertTrue(properties.get("contacts").isArray());
    }

    @Test
    void serialize_spatialExtent_handlesGeometryAndDescription() throws JsonProcessingException {
        IndexRecord record =
                IndexRecord.builder().uuid("test-uuid").publishedToAll(true).build();

        // Add geometry as JSON string (as shown in the example)
        Map<String, Object> geometry = new LinkedHashMap<>();
        geometry.put("type", "Polygon");
        geometry.put(
                "coordinates",
                Arrays.asList(Arrays.asList(
                        Arrays.asList(37.0, -3.0),
                        Arrays.asList(156.0, -3.0),
                        Arrays.asList(156.0, 83.0),
                        Arrays.asList(37.0, 83.0),
                        Arrays.asList(37.0, -3.0))));
        record.setGeometries(List.of(geometry));

        // Add extent description
        record.setExtentDescription(List.of(Map.of(
                "default", "Eurasia region",
                "lang", "Eurasia region")));

        String result = serializer.serialize(record, null, null, "hybrid");
        JsonNode jsonNode = objectMapper.readTree(result);

        // In hybrid mode, DCAT spatial should be at root
        assertTrue(jsonNode.has("dct:spatial"));
        assertTrue(result.contains("Polygon"));
        assertTrue(result.contains("Eurasia region"));

        // OGC geometry should be at root level as GeoJSON
        assertTrue(jsonNode.has("geometry"));
        JsonNode geometryNode = jsonNode.get("geometry");
        assertEquals("Polygon", geometryNode.get("type").asText());
    }

    @Test
    void serialize_temporalExtent_mapsDateRanges() throws JsonProcessingException {
        IndexRecord record =
                IndexRecord.builder().uuid("test-uuid").publishedToAll(true).build();

        DateRange range = new DateRange();
        range.setGte("2000-01-01T03:29:00.000Z");
        range.setLte("2008-01-08T03:29:00.000Z");
        record.setResourceTemporalExtentDateRange(List.of(range));

        String result = serializer.serialize(record, null, null, "hybrid");
        JsonNode jsonNode = objectMapper.readTree(result);

        // DCAT temporal should be at root in hybrid mode
        assertTrue(jsonNode.has("dct:temporal"));
        assertTrue(result.contains("\"dcat:startDate\""));
        assertTrue(result.contains("\"dcat:endDate\""));
        assertTrue(result.contains("2000-01-01"));
        assertTrue(result.contains("2008-01-08"));

        // OGC time should be at root level
        assertTrue(jsonNode.has("time"));
        JsonNode timeNode = jsonNode.get("time");
        assertTrue(timeNode.has("interval"));
    }

    @Test
    void serialize_resourceDates_mapsToCorrectDcatProperties() throws JsonProcessingException {
        IndexRecord record =
                IndexRecord.builder().uuid("test-uuid").publishedToAll(true).build();

        List<ResourceDate> dates = Arrays.asList(
                new ResourceDate("publication", "1999-09-30T22:00:00.000Z"),
                new ResourceDate("creation", "1999-01-01T00:00:00.000Z"),
                new ResourceDate("revision", "2007-11-06T11:10:47.000Z"));
        record.setResourceDate(dates);

        String result = serializer.serialize(record, null, null, "hybrid");
        JsonNode jsonNode = objectMapper.readTree(result);

        // DCAT dates at root level
        assertTrue(jsonNode.has("dct:issued"));
        assertTrue(result.contains("1999-01-01")); // creation date
        assertTrue(jsonNode.has("dct:modified"));
        assertTrue(result.contains("2007-11-06")); // revision date

        // OGC dates in properties
        JsonNode properties = jsonNode.get("properties");
        assertTrue(properties.has("created"));
        assertTrue(properties.has("updated"));
    }

    @Test
    void serialize_conformance_mapsSpecifications() throws JsonProcessingException {
        IndexRecord record = IndexRecord.builder()
                .uuid("test-uuid")
                .publishedToAll(true)
                .specificationConformance(SpecificationConformance.builder()
                        .title("INSPIRE Data Specification")
                        .link("http://inspire.ec.europa.eu/spec/123")
                        .pass("true")
                        .date("2020-01-01")
                        .build())
                .build();

        String result = serializer.serialize(record, null, null, "hybrid");

        // Pure DCAT format should have dct:conformsTo
        String dcatResult = serializer.serialize(record, null, null, "dcat");
        assertTrue(dcatResult.contains("\"dct:conformsTo\""));
        assertTrue(dcatResult.contains("INSPIRE Data Specification"));
        assertTrue(dcatResult.contains("\"dqv:hasQualityMeasurement\""));

        // OGC format should have simple conformsTo in properties
        String ogcResult = serializer.serialize(record, null, null, "ogc");
        JsonNode ogcJson = objectMapper.readTree(ogcResult);
        assertTrue(ogcJson.get("properties").has("conformsTo"));
        assertTrue(ogcJson.get("properties").get("conformsTo").isArray());
    }

    @Test
    void serialize_accessRights_basedOnPublicationStatus() throws JsonProcessingException {
        // Test public access
        IndexRecord publicRecord =
                IndexRecord.builder().uuid("public-uuid").publishedToAll(true).build();

        String publicResult = serializer.serialize(publicRecord, null, null, "hybrid");
        assertTrue(publicResult.contains("access-right/PUBLIC"));

        JsonNode publicJson = objectMapper.readTree(publicResult);
        assertEquals("public", publicJson.get("properties").get("rights").asText());

        // Test restricted access
        IndexRecord restrictedRecord = IndexRecord.builder()
                .uuid("restricted-uuid")
                .publishedToAll(false)
                .build();

        String restrictedResult = serializer.serialize(restrictedRecord, null, null, "hybrid");
        assertTrue(restrictedResult.contains("access-right/RESTRICTED"));

        JsonNode restrictedJson = objectMapper.readTree(restrictedResult);
        assertEquals(
                "restricted", restrictedJson.get("properties").get("rights").asText());
    }

    @Test
    void serialize_withCatalogUri_wrapsInCatalog_dcatFormat() throws JsonProcessingException {
        IndexRecord record = IndexRecord.builder()
                .uuid("test-uuid")
                .resourceTitle(Map.of("default", "Test Dataset"))
                .publishedToAll(true)
                .build();

        String catalogUri = "http://catalog.example.org";
        String result = serializer.serialize(record, catalogUri, null, "dcat");

        assertTrue(result.contains("\"@type\" : \"dcat:Catalog\""));
        assertTrue(result.contains(catalogUri));
        assertTrue(result.contains("\"dcat:dataset\""));
    }

    @Test
    void serialize_withCatalogUri_wrapsInFeatureCollection_ogcFormat() throws JsonProcessingException {
        IndexRecord record = IndexRecord.builder()
                .uuid("test-uuid")
                .resourceTitle(Map.of("default", "Test Dataset"))
                .publishedToAll(true)
                .build();

        String catalogUri = "http://catalog.example.org";
        String result = serializer.serialize(record, catalogUri, null, "ogc");

        assertTrue(result.contains("\"type\" : \"FeatureCollection\""));
        assertTrue(result.contains("\"features\""));
        assertTrue(result.contains("\"numberMatched\""));
        assertTrue(result.contains("\"numberReturned\""));
    }

    @Test
    void serialize_languages_mapsToAuthorityUris() throws JsonProcessingException {
        IndexRecord record = IndexRecord.builder()
                .uuid("test-uuid")
                .publishedToAll(true)
                .resourceLanguage(Arrays.asList("eng", "fre", "deu"))
                .build();

        String result = serializer.serialize(record, null, null, "hybrid");
        JsonNode jsonNode = objectMapper.readTree(result);

        // DCAT languages at root
        assertTrue(jsonNode.has("dct:language"));
        assertTrue(result.contains("authority/language/ENG"));
        assertTrue(result.contains("authority/language/FRA"));
        assertTrue(result.contains("authority/language/DEU"));

        // OGC languages in properties
        JsonNode properties = jsonNode.get("properties");
        assertTrue(properties.has("languages") || properties.has("language"));
    }

    @Test
    void serialize_backwardCompatibility_defaultsToHybrid() throws JsonProcessingException {
        IndexRecord record = IndexRecord.builder()
                .uuid("test-uuid")
                .resourceTitle(Map.of("default", "Test"))
                .publishedToAll(true)
                .build();

        // Test backward compatibility - old method signature should default to hybrid
        String result = serializer.serialize(record);

        assertNotNull(result);
        // Default should be hybrid, so should have both Feature and Dataset
        assertTrue(result.contains("\"@type\" : [ \"geo:Feature\", \"dcat:Dataset\" ]"));
        assertTrue(result.contains("\"type\" : \"Feature\""));
    }

    @Test
    void serialize_ogcLinksGeneration() throws JsonProcessingException {
        IndexRecord record =
                IndexRecord.builder().uuid("test-uuid").publishedToAll(true).build();

        String result = serializer.serialize(record, null, "http://example.org/", "ogc");
        JsonNode jsonNode = objectMapper.readTree(result);

        assertTrue(jsonNode.has("links"));
        JsonNode links = jsonNode.get("links");
        assertTrue(links.isArray());

        // Should have self link
        boolean hasSelfLink = false;
        for (JsonNode link : links) {
            if ("self".equals(link.get("rel").asText())) {
                hasSelfLink = true;
                assertEquals("application/geo+json", link.get("type").asText());
            }
        }
        assertTrue(hasSelfLink);
    }

    @Test
    void serialize_verifyDcatPropertiesNotInPropertiesObject() throws JsonProcessingException {
        IndexRecord record = createCompleteIndexRecord();

        String result = serializer.serialize(record, null, null, "hybrid");
        JsonNode jsonNode = objectMapper.readTree(result);

        // DCAT properties should NOT be in properties object
        JsonNode properties = jsonNode.get("properties");
        assertFalse(properties.has("dct:title"));
        assertFalse(properties.has("dct:description"));
        assertFalse(properties.has("dcat:distribution"));
        assertFalse(properties.has("dcat:theme"));

        // DCAT properties SHOULD be at root
        assertTrue(jsonNode.has("dct:title"));
        assertTrue(jsonNode.has("dct:description"));
        assertTrue(jsonNode.has("dcat:distribution"));
    }

    private IndexRecord createCompleteIndexRecord() {
        IndexRecord record = IndexRecord.builder()
                .uuid("78f93047-74f8-4419-ac3d-fc62e4b0477b")
                .metadataIdentifier("78f93047-74f8-4419-ac3d-fc62e4b0477b")
                .resourceTitle(Map.of(
                        "default", "Physiographic Map of North and Central Eurasia (Sample record, please remove!)",
                        "lang", "Physiographic Map of North and Central Eurasia (Sample record, please remove!)"))
                .resourceAbstract(Map.of(
                        "default", "Physiographic maps for the CIS and Baltic States",
                        "lang", "Physiographic maps for the CIS and Baltic States"))
                .publishedToAll(true)
                .dateStamp("2007-11-06T11:10:47.000Z")
                .changeDate("2007-11-06T11:10:47Z")
                .createDate("2007-07-25T10:05:45Z")
                .build();

        // Add temporal extent
        DateRange dateRange = new DateRange();
        dateRange.setGte("2000-01-01T03:29:00.000Z");
        dateRange.setLte("2008-01-08T03:29:00.000Z");
        record.setResourceTemporalExtentDateRange(List.of(dateRange));

        // Add resource dates
        record.setResourceDate(Arrays.asList(
                new ResourceDate("publication", "1999-09-30T22:00:00.000Z"),
                new ResourceDate("revision", "2007-11-06T11:10:47.000Z")));

        // Add links
        Link wmsLink = new Link();
        wmsLink.setProtocol("OGC:WMS");
        wmsLink.setUrl(Map.of("default", "https://data.apps.fao.org/wms"));
        wmsLink.setName(Map.of("default", "geonetwork:phy_landf"));
        record.setLinks(List.of(wmsLink));

        return record;
    }
}
