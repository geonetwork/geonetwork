/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.dynamicproperties;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.base.Ascii;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.SneakyThrows;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.geonetwork.GeonetworkGenericApplication;
import org.geonetwork.infrastructure.ElasticPgMvcTestHelper;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonSchemaDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.geonetwork.ogcapi.service.configuration.SimpleType;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.ElasticTypeInfo;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.ElasticTypingSystem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.MountableFile;

@SpringBootTest(classes = GeonetworkGenericApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test", "integration-test"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = DynamicPropertiesTest.class)
@SuppressWarnings("unchecked")
public class DynamicPropertiesTest implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public final String MAIN_COLLECTION_ID = "3bef299d-cf82-4033-871b-875f6936b2e2";
    public final String METAWAL_COLLECTION_ID = "cec997ba-1fa4-48d9-8be0-890da8cc65cf";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    ElasticTypingSystem elasticTypingSystem;

    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("geonetwork")
            .withUsername("postgres")
            .withPassword("postgres")
            .withCopyToContainer(
                    MountableFile.forClasspathResource("dump.gn.sql"), "/docker-entrypoint-initdb.d/dump.gn.sql");

    static ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer(
                    "docker.elastic.co/elasticsearch/elasticsearch:8.14.0")
            .withEnv("path.repo", "/tmp")
            .withEnv("ES_JAVA_OPTS", "-Xms750m -Xmx2g")
            .withEnv("discovery.type", "single-node")
            .withEnv("xpack.security.enabled", "false")
            .withEnv("xpack.security.enrollment.enabled", "false")
            .withCopyToContainer(MountableFile.forClasspathResource("es_backups.tar.gz"), "/tmp/es_backups.tar.gz");

    public final int MVC_PORT = 8888;
    public final String BASE_URL = "http://localhost:" + MVC_PORT + "/";

    @SneakyThrows
    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        ElasticPgMvcTestHelper.initialize(ctx, postgreSQLContainer, elasticsearchContainer, MVC_PORT);
    }

    @BeforeAll
    static void beforeAll() throws Exception {
        ElasticPgMvcTestHelper.beforeAll(postgreSQLContainer, elasticsearchContainer);
    }

    @AfterAll
    static void afterAll() throws Exception {
        ElasticPgMvcTestHelper.afterAll(postgreSQLContainer, elasticsearchContainer);
    }

    // -----------------------

    @Test
    public void testOpenAPI() throws Exception {
        var json = ElasticPgMvcTestHelper.retrieveUrlJson("v3/api-docs?f=json", BASE_URL, mockMvc);

        // there's compatibility issues when including this and everthing stops working!!!
        //          OpenAPI openAPI = new OpenAPIV3Parser().readContents(json).getOpenAPI();

        var tree = new ObjectMapper().readTree(json);

        // CHECK CONTENT MIME TYPES AND PROFILES
        // ==========================================================================================

        // -- multiple items  "/ogcapi-records/collections/{catalogId}/items
        // -----------------------------------------------------------------------------------------

        // should be 4 response types
        var itemsNode = tree.get("paths")
                .get("/ogcapi-records/collections/{catalogId}/items")
                .get("get")
                .get("responses")
                .get("200")
                .get("content");
        assertEquals(4, itemsNode.size());
        assertNotNull(itemsNode.get("application/geo+json"));
        assertNotNull(itemsNode.get("text/html"));
        assertNotNull(itemsNode.get("application/json"));
        assertNotNull(itemsNode.get("application/xml"));

        assertProvider(
                itemsNode.get("application/geo+json"), List.of("OgcApiRecordsMultiRecordResponseGeoJsonFormatter"));
        // none for the generic HTML
        // assertProvider(itemsNode.get("text/html"), List.of("OgcApiRecordsMultiRecordResponseGeoJsonFormatter"));
        assertProvider(itemsNode.get("application/json"), List.of("OgcApiRecordsMultiRecordResponseJsonFormatter"));
        assertProvider(itemsNode.get("application/xml"), List.of("CswCollectionMessageWriter"));

        // -- single item "/ogcapi-records/collections/{catalogId}/items/{recordId}"
        // -----------------------------------------------------------------------------------------

        // should be 5 response types
        var itemNode = tree.get("paths")
                .get("/ogcapi-records/collections/{catalogId}/items/{recordId}")
                .get("get")
                .get("responses")
                .get("200")
                .get("content");
        assertEquals(5, itemNode.size());

        // check single record mine type
        assertNotNull(itemNode.get("application/geo+json"));
        assertNotNull(itemNode.get("text/html"));
        assertNotNull(itemNode.get("application/json"));
        assertNotNull(itemNode.get("application/xml"));
        assertNotNull(itemNode.get("application/rdf+xml"));

        assertProfiles(
                itemNode.get("application/geo+json"), List.of("http://www.opengis.net/def/profile/OGC/0/ogc-record"));
        assertProvider(itemNode.get("application/geo+json"), List.of("OgcApiRecordsSingleRecordResponseFormatter"));

        // text/html is generic

        assertProfiles(
                itemNode.get("application/json"),
                List.of(
                        "http://www.opengis.net/def/profile/OGC/0/ogc-record",
                        "http://geonetwork.net/def/profile/elastic-json-index"));
        assertProvider(itemNode.get("application/json"), List.of("OgcApiRecordsSingleRecordResponseFormatter"));

        assertProfiles(
                itemNode.get("application/xml"),
                List.of("http://geonetwork.net/def/profile/raw-xml", "http://geonetwork.net/def/profile/datacite"));
        assertProvider(itemNode.get("application/json"), List.of("OgcApiRecordsSingleRecordResponseFormatter"));

        assertProfiles(
                itemNode.get("application/rdf+xml"),
                List.of(
                        "http://geonetwork.net/def/profile/eu-dcat-ap-hvd",
                        "http://geonetwork.net/def/profile/eu-geodcat-ap",
                        "http://geonetwork.net/def/profile/dcat",
                        "http://geonetwork.net/def/profile/eu-dcat-ap"));
        assertProvider(itemNode.get("application/json"), List.of("OgcApiRecordsSingleRecordResponseFormatter"));

        // CHECK SCHEMAS
        // ==========================================================================================
        var componentSchemas = tree.get("components").get("schemas");

        var itemsSchema =
                componentSchemas.get("OgcApiRecordsGetRecords200ResponseDto").get("properties");

        // features should be a link to the same object (OgcApiRecordsRecordGeoJSONDto)
        var itemsSchemaRef = itemsSchema.get("features").get("items").get("$ref");
        assertEquals("#/components/schemas/OgcApiRecordsRecordGeoJSONDto", itemsSchemaRef.asText());

        var itemSchema =
                componentSchemas.get("OgcApiRecordsRecordGeoJSONPropertiesDto").get("properties");

        for (var configuredProperty : this.elasticTypingSystem.getAllFieldInfos()) {
            var ogcPropertyName = configuredProperty.getConfig().getOgcProperty();
            var openapiInfo = itemSchema.get(ogcPropertyName);
            System.out.println("checking consistency of dynamic property (ogc name): " + ogcPropertyName);
            if (!configuredProperty.getConfig().getAddPropertyToOutput()) {
                assertNull(openapiInfo);
                continue; // not in output, so not in openapi
            }
            assertConsistent(openapiInfo, configuredProperty);
        }
    }

    // above checked that the typing is consistent, need to check that the values are being put in correctly
    // we're just going to check one features, but if its working for one, should be working for all
    @Test
    public void testValues() throws Exception {

        //      var item =  ElasticPgMvcTestHelper.retrieveUrlJson(
        //        "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?id=8698bf0b-fceb-4f0f-989b-111e7c4af0a4",
        //        OgcApiRecordsGetRecords200ResponseDto.class,
        //        BASE_URL,
        //        mockMvc,
        //        objectMapper);

        var items = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?limit=100",
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        var feature = items.getFeatures().stream()
                .filter(x -> x.getId().equals("8698bf0b-fceb-4f0f-989b-111e7c4af0a4"))
                .findFirst()
                .get();

        // all the feature values are there
        for (var configuredProperty : this.elasticTypingSystem.getAllFieldInfos()) {
            if (!configuredProperty.getConfig().getAddPropertyToOutput()) {
                continue;
            }
            var featureValue = feature.getProperties()
                    .getAdditionalProperties()
                    .get(configuredProperty.getConfig().getOgcProperty());
            assertNotNull(featureValue);
        }

        assertValue(feature, "creationYearForResource", List.of(1999));
        assertValue(feature, "spatialRepresentationType", List.of("vector"));
        assertValue(feature, "resourceTitleObject", "Alpenkonvention");
        //    assertValue(feature, "format", List.of()); // set as not in output
        assertValue(feature, "resolutionScaleDenominator", List.of(25000));
        assertValue(feature, "updateFrequency", "asNeeded");
        assertValue(
                feature, "orgForResource", List.of("Bundesamt für Raumentwicklung", "Bundesamt für Raumentwicklung"));
        assertValue(feature, "resourceType", List.of("dataset"));
        //        assertValue(feature, "tags", List.of());// set as not in output
        assertValue(
                feature,
                "linkProtocol",
                List.of(
                        "OGC:WMTS",
                        "CHTOPO:specialised-geoportal",
                        "ESRI:REST",
                        "WWW:LINK",
                        "WWW:DOWNLOAD-URL",
                        "MAP:Preview",
                        "OGC:WMS",
                        "OPENDATA:SWISS"));
        assertValue(feature, "createDate", List.of("2023-05-24T14:40:04.056Z"));
    }

    @Test
    public void testQueryables() throws Exception {
        var queryables = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/queryables",
                OgcApiRecordsJsonSchemaDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        // user defined queryables should present
        for (var configuredProperty : this.elasticTypingSystem.getAllFieldInfos()) {
            if (!configuredProperty.getConfig().getIsQueryable()) {
                continue;
            }
            // this property should be in the queryables
            assertTrue(queryables
                    .getProperties()
                    .containsKey(configuredProperty.getConfig().getOgcProperty()));
        }

        //        var dynamicQueryables = this.elasticTypingSystem.getAllFieldInfos().stream()
        //                .filter(x -> x.getConfig().getIsQueryable())
        //                .toList();

        // lets execute some queryables (on dynamic fields) and check to see if they work...
        //        var allItems = ElasticPgMvcTestHelper.retrieveUrlJson(
        //                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?limit=100",
        //                OgcApiRecordsGetRecords200ResponseDto.class,
        //                BASE_URL,
        //                mockMvc,
        //                objectMapper);

        // exact title match
        var queryItems = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?resourceTitleObject="
                        + URLEncoder.encode(
                                "Parcellaire agricole anonyme (situation 2020) - Service de visualisation WMS",
                                StandardCharsets.UTF_8),
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        assertEquals(1, queryItems.getFeatures().size());
        assertEquals(
                "01ec6ec7-6454-4504-ac95-befb16bacb0e",
                queryItems.getFeatures().getFirst().getId());

        // partial title match
        queryItems = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?resourceTitleObject="
                        + URLEncoder.encode("Parcellaire agricole anonyme", StandardCharsets.UTF_8),
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        assertEquals(3, queryItems.getFeatures().size());
        var ids = queryItems.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(ids.contains("01ec6ec7-6454-4504-ac95-befb16bacb0e"));
        assertTrue(ids.contains("01ec6ec7-6454-4504-ac95-befb16bacb0e"));
        assertTrue(ids.contains("02366c9c-3b4d-4c92-803b-f4d2d8b434a5"));

        // integer
        // allItems.getFeatures().stream().map(x->x.getProperties().getAdditionalProperties().get("resolutionScaleDenominator")).toList()
        queryItems = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?resolutionScaleDenominator=5000",
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);
        assertEquals(2, queryItems.getFeatures().size());
        ids = queryItems.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(ids.contains("ee965118-2416-4d48-b07e-bbc696f002c2"));
        assertTrue(ids.contains("fr-120066022-jdd-f20f8125-877e-46dc-8cf8-2a8a372045eb"));

        // date exact
        // allItems.getFeatures().stream().map(x->x.getProperties().getAdditionalProperties().get("createDate")).toList()
        queryItems = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?createDate=2022-03-11T08:52:03.365Z",
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);
        assertEquals(1, queryItems.getFeatures().size());
        ids = queryItems.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(ids.contains("02366c9c-3b4d-4c92-803b-f4d2d8b434a5"));

        // date range
        // allItems.getFeatures().stream().map(x->x.getProperties().getAdditionalProperties().get("createDate")).toList()
        // allItems.getFeatures().stream().map(x->x.getProperties().getAdditionalProperties().get("createDate")).toList().stream().map(x->(List)x).flatMap(t -> t.stream()).toList()

        queryItems = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?createDate=2022-03-11T08:52:03.365Z/2022-12-11T08:52:03.365Z",
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);
        assertEquals(6, queryItems.getFeatures().size());
        ids = queryItems.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(ids.contains("004571b9-4649-42b3-9c28-a8cdc2bf53c7"));
        assertTrue(ids.contains("00916a35-786b-4569-9da6-71ca64ca54b1"));
        assertTrue(ids.contains("01ec6ec7-6454-4504-ac95-befb16bacb0e"));
        assertTrue(ids.contains("020c04f8-cc3d-4cd7-8af7-336b0a1a58f5"));
        assertTrue(ids.contains("02366c9c-3b4d-4c92-803b-f4d2d8b434a5"));
        assertTrue(ids.contains("02a63353-b1ad-487b-a343-b4971eaf4a98"));
    }

    @Test
    public void testSortables() throws Exception {
        var sortables = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/sortables",
                OgcApiRecordsJsonSchemaDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        // user defined queryables should present
        for (var configuredProperty : this.elasticTypingSystem.getAllFieldInfos()) {
            if (!configuredProperty.getConfig().getIsSortable()) {
                continue;
            }
            // this property should be in the queryables
            assertTrue(sortables
                    .getProperties()
                    .containsKey(configuredProperty.getConfig().getOgcProperty()));
        }

        //        var dynamicSortables = this.elasticTypingSystem.getAllFieldInfos().stream()
        //                .filter(x -> x.getConfig().getIsSortable())
        //                .toList();

        // lets execute some queryables (on dynamic fields) and check to see if they work...
        //        var allItems = ElasticPgMvcTestHelper.retrieveUrlJson(
        //                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?limit=100",
        //                OgcApiRecordsGetRecords200ResponseDto.class,
        //                BASE_URL,
        //                mockMvc,
        //                objectMapper);

        // creationYearForResource (int)
        var sortedItems = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?limit=100&sortby=creationYearForResource",
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        var creationYearForResources = sortedItems.getFeatures().stream()
                .map(x -> x.getProperties().getAdditionalProperties().get("creationYearForResource"))
                .filter(x -> x != null)
                .toList()
                .stream()
                .map(x -> (List) x)
                .flatMap(t -> t.stream())
                .toList();
        assertTrue(Ordering.natural().isOrdered(creationYearForResources));

        // resourceTitleObject (string - text)
        sortedItems = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?limit=100&sortby=resourceTitleObject",
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        // typically, GN4 index is configured to be sorted in lowercase and asciifolding (é -> e).
        //    "normalizer": {
        //      "sorting": {
        //        "filter": [
        //        "lowercase",
        //          "asciifolding"
        //              ],
        //        "type": "custom"
        //      }
        //    },

        var resourceTitleObjects = new ArrayList<String>(sortedItems.getFeatures().stream()
                .map(x -> x.getProperties().getAdditionalProperties().get("resourceTitleObject"))
                .filter(x -> x != null)
                .map(x -> (String) x)
                .toList()); // .stream().map(x->(List)x).flatMap(t -> t.stream()).toList();
        var resourceTitleObjects_as_per_elastic = resourceTitleObjects.stream()
                .map(x -> Ascii.toLowerCase(foldToAscii(x)))
                .toList();

        assertTrue(Ordering.natural().isOrdered(resourceTitleObjects_as_per_elastic));

        // createDate (date)
        sortedItems = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?limit=100&sortby=createDate",
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        // this is ok because dates sort alphabetically if they are in the same TZ
        var createDates = sortedItems.getFeatures().stream()
                .map(x -> x.getProperties().getAdditionalProperties().get("createDate"))
                .filter(x -> x != null)
                .toList()
                .stream()
                .map(x -> (List) x)
                .flatMap(t -> t.stream())
                .toList();
        assertTrue(Ordering.natural().isOrdered(createDates));

        // resourceType (string - keyword)
        sortedItems = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?limit=100&sortby=resourceType",
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        // this is ok because dates sort alphabetically if they are in the same TZ
        var resourceTypes = sortedItems.getFeatures().stream()
                .map(x -> x.getProperties().getAdditionalProperties().get("resourceType"))
                .filter(x -> x != null)
                .toList()
                .stream()
                .map(x -> (List) x)
                .flatMap(t -> t.stream())
                .toList();
        assertTrue(Ordering.natural().isOrdered(resourceTypes));

        // verify with the "+"
        sortedItems = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?limit=100&sortby=+resourceType",
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        // this is ok because dates sort alphabetically if they are in the same TZ
        resourceTypes = sortedItems.getFeatures().stream()
                .map(x -> x.getProperties().getAdditionalProperties().get("resourceType"))
                .filter(x -> x != null)
                .toList()
                .stream()
                .map(x -> (List) x)
                .flatMap(t -> t.stream())
                .toList();
        assertTrue(Ordering.natural().isOrdered(resourceTypes));

        // verify with the "-"
        sortedItems = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?limit=100&sortby=-resourceType",
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        // this is ok because dates sort alphabetically if they are in the same TZ
        resourceTypes = sortedItems.getFeatures().stream()
                .map(x -> x.getProperties().getAdditionalProperties().get("resourceType"))
                .filter(x -> x != null)
                .toList()
                .stream()
                .map(x -> (List) x)
                .flatMap(t -> t.stream())
                .toList();
        var resourceTypes2 = new ArrayList<String>(resourceTypes);
        Collections.reverse(resourceTypes2);
        assertTrue(Ordering.natural().isOrdered(resourceTypes2));
    }

    private void assertValue(OgcApiRecordsRecordGeoJSONDto feature, String ogcpname, Object expectedvalue) {
        var actualValue = feature.getProperties().getAdditionalProperties().get(ogcpname);
        if (expectedvalue instanceof List expectedList) {
            var actualList = (List) actualValue;
            assertTrue(Iterables.elementsEqual(expectedList, actualList));
        } else {
            assertEquals(expectedvalue, actualValue);
        }
    }

    public void assertConsistent(JsonNode openapiInfo, ElasticTypeInfo elasticTypeInfo) {
        // lists?
        if (elasticTypeInfo.isList() || openapiInfo.get("type").asText().equals("array")) {
            assertEquals("array", openapiInfo.get("type").asText());
            assertTrue(elasticTypeInfo.isList());
            assertConsistent(openapiInfo.get("items"), elasticTypeInfo.getType());
            return;
        }
        assertConsistent(openapiInfo, elasticTypeInfo.getType());
    }

    public void assertConsistent(JsonNode openapiInfo, SimpleType type) {
        var expectedType = SimpleType.getOpenApiType(type);
        assertEquals(openapiInfo.get("type").asText(), expectedType);
    }

    public void assertProvider(JsonNode node, List<String> expectedProviders) {
        var xFormatProviders = ArrayNode2List((ArrayNode) node.get("x-format-providers"));
        assertTrue(Iterables.elementsEqual(xFormatProviders, expectedProviders));
    }

    public void assertProfiles(JsonNode node, List<String> expectedProfiles) {
        var xProfiles = ArrayNode2List((ArrayNode) node.get("x-profiles"));
        assertTrue(Iterables.elementsEqual(xProfiles, expectedProfiles));
    }

    public List ArrayNode2List(ArrayNode arrayNode) {
        return StreamSupport.stream(arrayNode.spliterator(), false)
                .map(o -> (TextNode) o)
                .map(o -> o.asText())
                .toList();
    }

    // https://stackoverflow.com/questions/59723144/using-lucene-analyzer-without-indexing-is-my-approach-reasonable
    public static String foldToAscii(String input) {
        var output = new char[input.length() * 4];
        var len = ASCIIFoldingFilter.foldToASCII(input.toCharArray(), 0, output, 0, input.length());
        return new String(output, 0, len);
    }
}
