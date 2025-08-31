/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting.processor.dcatap;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
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

        serializer.init();
    }

    @Test
    void serialize_withCompleteRecord_mapsAllFields() throws JsonProcessingException {
        IndexRecord record = createCompleteIndexRecord();

        String result = serializer.serialize(record);

        assertNotNull(result);
        assertTrue(result.contains("\"@type\" : \"dcat:Dataset\""));
        assertTrue(result.contains("78f93047-74f8-4419-ac3d-fc62e4b0477b"));

        // Verify multilingual title
        assertTrue(result.contains("Physiographic Map"));

        // Verify temporal extent
        assertTrue(result.contains("2000-01-01"));
        assertTrue(result.contains("2008-01-08"));

        // Verify distributions from links
        assertTrue(result.contains("dcat:Distribution"));
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

        String result = serializer.serialize(record);

        assertTrue(result.contains("Default Title"));

        assertNotNull(result);
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

        String result = serializer.serialize(record);

        // INSPIRE themes should be in dcat:theme
        assertTrue(result.contains("\"dcat:theme\""));
        assertTrue(result.contains("inspire.ec.europa.eu/theme"));

        // Keywords with URIs (non-INSPIRE) should be in dct:subject
        assertTrue(result.contains("\"dct:subject\""));

        // Plain text keywords should be in dcat:keyword
        assertTrue(result.contains("\"dcat:keyword\""));
    }

    @Test
    void serialize_distributions_mapsProtocolsToFormats() throws JsonProcessingException {
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

        String result = serializer.serialize(record);

        assertTrue(result.contains("dcat:Distribution"));
        assertTrue(result.contains("dcat:accessURL"));
        assertTrue(result.contains("http://example.org/data.zip"));
        assertTrue(result.contains("https://example.org/wms"));
    }

    @Test
    void serialize_contacts_mapsRolesToDcatProperties() throws JsonProcessingException {
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

        String result = serializer.serialize(record);

        assertTrue(result.contains("\"dcat:contactPoint\""));
        assertTrue(result.contains("\"dct:publisher\""));
        assertTrue(result.contains("\"dct:creator\""));
        assertTrue(result.contains("HAO"));
        assertTrue(result.contains("mailto:john@example.org"));
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

        String result = serializer.serialize(record);

        assertTrue(result.contains("\"dct:spatial\""));
        assertTrue(result.contains("Polygon"));
        assertTrue(result.contains("Eurasia region"));
    }

    @Test
    void serialize_temporalExtent_mapsDateRanges() throws JsonProcessingException {
        IndexRecord record =
                IndexRecord.builder().uuid("test-uuid").publishedToAll(true).build();

        DateRange range = new DateRange();
        range.setGte("2000-01-01T03:29:00.000Z");
        range.setLte("2008-01-08T03:29:00.000Z");
        record.setResourceTemporalExtentDateRange(List.of(range));

        String result = serializer.serialize(record);

        assertTrue(result.contains("\"dct:temporal\""));
        assertTrue(result.contains("\"dcat:startDate\""));
        assertTrue(result.contains("\"dcat:endDate\""));
        assertTrue(result.contains("2000-01-01"));
        assertTrue(result.contains("2008-01-08"));
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

        String result = serializer.serialize(record);

        assertTrue(result.contains("\"dct:issued\""));
        assertTrue(result.contains("1999-01-01")); // creation date
        assertTrue(result.contains("\"dct:modified\""));
        assertTrue(result.contains("2007-11-06")); // revision date
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

        String result = serializer.serialize(record);

        assertTrue(result.contains("\"dct:conformsTo\""));
        assertTrue(result.contains("INSPIRE Data Specification"));
        assertTrue(result.contains("\"dqv:hasQualityMeasurement\""));
    }

    @Test
    void serialize_accessRights_basedOnPublicationStatus() throws JsonProcessingException {
        // Test public access
        IndexRecord publicRecord =
                IndexRecord.builder().uuid("public-uuid").publishedToAll(true).build();

        String publicResult = serializer.serialize(publicRecord);
        assertTrue(publicResult.contains("access-right/PUBLIC"));

        // Test restricted access
        IndexRecord restrictedRecord = IndexRecord.builder()
                .uuid("restricted-uuid")
                .publishedToAll(false)
                .build();

        String restrictedResult = serializer.serialize(restrictedRecord);
        assertTrue(restrictedResult.contains("access-right/RESTRICTED"));
    }

    @Test
    void serialize_withCatalogUri_wrapsInCatalog() throws JsonProcessingException {
        IndexRecord record = IndexRecord.builder()
                .uuid("test-uuid")
                .resourceTitle(Map.of("default", "Test Dataset"))
                .publishedToAll(true)
                .build();

        String catalogUri = "http://catalog.example.org";
        String result = serializer.serialize(record, catalogUri, null);

        assertTrue(result.contains("\"@type\" : \"dcat:Catalog\""));
        assertTrue(result.contains(catalogUri));
        assertTrue(result.contains("\"dcat:dataset\""));
    }

    @Test
    void serialize_languages_mapsToAuthorityUris() throws JsonProcessingException {
        IndexRecord record = IndexRecord.builder()
                .uuid("test-uuid")
                .publishedToAll(true)
                .resourceLanguage(Arrays.asList("eng", "fre", "deu"))
                .build();

        String result = serializer.serialize(record);

        assertTrue(result.contains("\"dct:language\""));
        assertTrue(result.contains("authority/language/ENG"));
        assertTrue(result.contains("authority/language/FRA"));
        assertTrue(result.contains("authority/language/DEU"));
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
