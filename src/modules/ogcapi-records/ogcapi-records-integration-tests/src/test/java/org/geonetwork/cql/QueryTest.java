/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.cql;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.collect.Ordering;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.geonetwork.infrastructure.ElasticPgMvcBaseTest;
import org.geonetwork.ogcapi.records.generated.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * In order to efficiently do the integration tests, I've combined several together. This save about 1 minute/class in
 * container + Spring-boot + GN5 startup time!
 *
 * <p>-------------
 *
 * <p>This is a combined test of the facet's and CQL. First, we use the expected bucket for several facets (cf
 * SimpleRecordsMvcTests.expectedFacets).
 *
 * <p>For each of the buckets, we create a simple CQL expression
 *
 * <p>NOTE: we use the "getHighestBucket" to construct the histogram queries. basically, if we see a min/max in bucket
 * we will do a cql like;
 *
 * <p>PROPERTY >= MIN AND PROPERTY < MAX
 *
 * <p>however, for the last (highest value bucket), we do this (notice the MAX is <= instead of <):
 *
 * <p>PROPERTY >= MIN AND PROPERTY <= MAX
 */
@ContextConfiguration(initializers = QueryTest.class)
public class QueryTest extends ElasticPgMvcBaseTest {

    OgcApiRecordsFacetsDto facetsConfig;

    /**
     * Gets the facet configuration.
     *
     * @throws Exception http issue (unlikely)
     */
    @BeforeEach
    public void beforeEach() throws Exception {
        facetsConfig = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/facets", OgcApiRecordsFacetsDto.class);
    }

    /**
     * Given some information about the facets (expectedFacets), we make a CQL expression that will execute to get the
     * records in each bucket. We then check that the expected # of records in each bucket matches the actual number
     * returned.
     *
     * @throws Exception - test case failure
     */
    @Test
    public void testBucketCql() throws Exception {
        for (var facetInfo : expectedFacets.entrySet()) {
            var facetName = facetInfo.getKey();
            var facetValue = facetInfo.getValue();
            var facetConfig = facetsConfig.getFacets().get(facetName);
            for (var bucketIndx = 0; bucketIndx < facetValue.getBuckets().size(); bucketIndx++) {
                var bucket = facetValue.getBuckets().get(bucketIndx);
                var items = query(bucket, facetValue, facetConfig);
                if (!Objects.equals(items.getNumberMatched(), bucket.getCount())) {
                    throw new Exception(
                            "for facet " + facetName + ", bucketValue=" + bucket.getValue() + ", it has bucketCount="
                                    + bucket.getCount() + ", while actual number =" + items.getNumberMatched());
                } else {
                    System.out.println("facet " + facetName + ", bucketValue=" + bucket.getValue()
                            + ", it has expected bucketCount=" + bucket.getCount() + ", and actual number returned ="
                            + items.getNumberMatched() + " -- GOOD");
                }
            }
        }
    }

    /**
     * executes a query to retrieve the items in a bucket.
     *
     * @param bucket info about the bucket (i.e. value or min/max)
     * @param facetValue info about the expected facet (i.e. to find the property name)
     * @param facetConfig info about the facet config (for extracting the pre-defined filter queries)
     * @return the records in the bucket (just the 1st 10, but includes the actual # of full results)
     * @throws Exception issue querying
     */
    private OgcApiRecordsGetRecords200ResponseDto query(
            BucketInfo bucket, FacetInfo facetValue, OgcApiRecordsFacetDto facetConfig) throws Exception {
        var cql = makeCql(bucket, facetValue, facetConfig);
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?filter="
                        + URLEncoder.encode(cql, StandardCharsets.UTF_8),
                OgcApiRecordsGetRecords200ResponseDto.class);

        return items;
    }

    /**
     * given a bucket and metainfo about the facet, create a CQL expression. For histograms, this is aware of the
     * handling of the "Highest value bucket".
     *
     * @param bucketInfo info about the bucket (i.e. value or min/max)
     * @param facetInfo info about the expected facet (i.e. to find the property name)
     * @param facetConfig info about the facet config (for extracting the pre-defined filter queries)
     * @return cql expression for the bucket
     */
    public String makeCql(BucketInfo bucketInfo, FacetInfo facetInfo, OgcApiRecordsFacetDto facetConfig) {
        if (facetInfo.getType().equals("term")) {
            var termInfo = (OgcApiRecordsFacetTermsDto) facetConfig;
            return termInfo.getProperty() + " = '" + escapeCql(bucketInfo.getValue()) + "'";
        } else if (facetInfo.getType().equals("histogram")) {
            var histogramInfo = (OgcApiRecordsFacetHistogramDto) facetConfig;
            if (bucketInfo.getHighestBucket() != null && bucketInfo.getHighestBucket()) {
                return histogramInfo.getProperty() + " >= '" + escapeCql(bucketInfo.getMin()) + "' AND "
                        + histogramInfo.getProperty() + " <= '" + escapeCql(bucketInfo.getMax()) + "'";
            }
            return histogramInfo.getProperty() + " >= '" + escapeCql(bucketInfo.getMin()) + "' AND "
                    + histogramInfo.getProperty() + " < '" + escapeCql(bucketInfo.getMax()) + "'";
        } else if (facetInfo.getType().equals("filter")) {
            var filterInfo = (OgcApiRecordsFacetFilterDto) facetConfig;
            return filterInfo.getFilters().get(bucketInfo.getValue());
        }
        throw new RuntimeException(facetInfo.getType() + " is not supported");
    }

    public String escapeCql(String cql) {
        return cql.replace("'", "''");
    }

    // ----------------------------------------------------------------------------------------------------------
    // OGCAPI-Records (and -Features) standard QUERY parameters

    @Test
    public void testQueryBBOX() throws Exception {

        var query = "bbox=7.908176232935124,43.301952247017084,17.541462920453505,46.83473548422572";
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?" + query,
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(5, items.getNumberMatched());
        var returnedIds = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(returnedIds.contains("a3774ef6-809d-4dd1-984f-9254f49cbd0a"));
        assertTrue(returnedIds.contains("a8b5e6c0-c21d-4c32-b8f9-10830215890a"));
        assertTrue(returnedIds.contains("9e1ea778-d0ce-4b49-90b7-37bc0e448300"));
        assertTrue(returnedIds.contains("8698bf0b-fceb-4f0f-989b-111e7c4af0a4"));
        assertTrue(returnedIds.contains("7eb795c2-d612-4b5e-b15e-d985b0f4e697"));
    }

    @Test
    public void testQueryDateTime() throws Exception {
        var query = "datetime=2022-01-01T00:00:00Z/2023-08-01T00:00:00Z";
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?" + query,
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(9, items.getNumberMatched());

        var returnedIds = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(returnedIds.contains("ee965118-2416-4d48-b07e-bbc696f002c2"));
        assertTrue(returnedIds.contains("04bcec79-5b25-4b16-b635-73115f7456e4"));
        assertTrue(returnedIds.contains("9e1ea778-d0ce-4b49-90b7-37bc0e448300"));
        assertTrue(returnedIds.contains("01491630-78ce-49f3-b479-4b30dabc4c69"));
        assertTrue(returnedIds.contains("fr-120066022-jdd-f20f8125-877e-46dc-8cf8-2a8a372045eb"));
        assertTrue(returnedIds.contains("00916a35-786b-4569-9da6-71ca64ca54b1"));
        assertTrue(returnedIds.contains("020c04f8-cc3d-4cd7-8af7-336b0a1a58f5"));
        assertTrue(returnedIds.contains("01ec6ec7-6454-4504-ac95-befb16bacb0e"));
        assertTrue(returnedIds.contains("004571b9-4649-42b3-9c28-a8cdc2bf53c7"));
    }

    @Test
    public void testOffsetLimit() throws Exception {
        // retrieve the first 10 items (sorted)
        var allItems = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?sortby=id",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(10, allItems.getNumberReturned());
        assertEquals(10, allItems.getFeatures().size());

        // get the first 2 records
        var first2Items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?sortby=id&offset=0&limit=2",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(2, first2Items.getNumberReturned());
        assertEquals(2, first2Items.getFeatures().size());
        assertEquals(
                allItems.getFeatures().get(0).getId(),
                first2Items.getFeatures().get(0).getId());
        assertEquals(
                allItems.getFeatures().get(1).getId(),
                first2Items.getFeatures().get(1).getId());

        // get records 3-5
        var items3tot5 = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?sortby=id&offset=2&limit=3",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(3, items3tot5.getNumberReturned());
        assertEquals(3, items3tot5.getFeatures().size());
        assertEquals(
                allItems.getFeatures().get(2).getId(),
                items3tot5.getFeatures().get(0).getId());
        assertEquals(
                allItems.getFeatures().get(3).getId(),
                items3tot5.getFeatures().get(1).getId());
        assertEquals(
                allItems.getFeatures().get(4).getId(),
                items3tot5.getFeatures().get(2).getId());
    }

    @Test
    public void testType() throws Exception {
        var datasetItems = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?type=dataset",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(12, datasetItems.getNumberMatched());

        var serviceItems = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?type=service",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(10, serviceItems.getNumberMatched());

        var datasetServiceItems = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?type=service,dataset",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(22, datasetServiceItems.getNumberMatched());
    }

    /**
     * its unclear what ExternalIds means in the context of a gn elastic index record.
     *
     * @throws Exception problem occurred
     */
    @Test
    public void testExternalIds() throws Exception {
        var oneItem = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?externalIds=accroche_velos",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(1, oneItem.getNumberMatched());
        assertEquals("accroche_velos", oneItem.getFeatures().get(0).getId());

        var twoItems = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?externalIds=accroche_velos,00487222-9fc0-490c-8f28-07dda6df08fc",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(2, twoItems.getNumberMatched());

        var returnedIds = twoItems.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(returnedIds.contains("accroche_velos"));
        assertTrue(returnedIds.contains("00487222-9fc0-490c-8f28-07dda6df08fc"));
    }

    @Test
    public void testIds() throws Exception {
        var oneItem = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?ids=accroche_velos",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(1, oneItem.getNumberMatched());
        assertEquals("accroche_velos", oneItem.getFeatures().get(0).getId());

        var twoItems = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?ids=accroche_velos,00487222-9fc0-490c-8f28-07dda6df08fc",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(2, twoItems.getNumberMatched());

        var returnedIds = twoItems.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(returnedIds.contains("accroche_velos"));
        assertTrue(returnedIds.contains("00487222-9fc0-490c-8f28-07dda6df08fc"));
    }

    @Test
    public void testQ() throws Exception {
        var oneItem = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?q=alpine",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(1, oneItem.getNumberMatched());
        assertEquals(
                "8698bf0b-fceb-4f0f-989b-111e7c4af0a4",
                oneItem.getFeatures().get(0).getId());
    }

    @Test
    public void testQueryableString() throws Exception {
        // everything search for "mel"
        var twoItem = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?q=mel",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(2, twoItem.getNumberMatched());
        var returnedIds = twoItem.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(returnedIds.contains("accroche_velos"));
        assertTrue(returnedIds.contains("ed34db28-5dd4-480f-bf29-dc08f0086131"));

        var oneItem = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?title=mel",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(1, oneItem.getNumberMatched());
        returnedIds = oneItem.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(returnedIds.contains("accroche_velos"));
    }

    @Test
    public void testQueryableDate() throws Exception {

        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?time=2022-01-01T00:00:00Z/2023-08-01T00:00:00Z",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(9, items.getNumberMatched());

        var returnedIds = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(returnedIds.contains("ee965118-2416-4d48-b07e-bbc696f002c2"));
        assertTrue(returnedIds.contains("04bcec79-5b25-4b16-b635-73115f7456e4"));
        assertTrue(returnedIds.contains("9e1ea778-d0ce-4b49-90b7-37bc0e448300"));
        assertTrue(returnedIds.contains("01491630-78ce-49f3-b479-4b30dabc4c69"));
        assertTrue(returnedIds.contains("fr-120066022-jdd-f20f8125-877e-46dc-8cf8-2a8a372045eb"));
        assertTrue(returnedIds.contains("00916a35-786b-4569-9da6-71ca64ca54b1"));
        assertTrue(returnedIds.contains("020c04f8-cc3d-4cd7-8af7-336b0a1a58f5"));
        assertTrue(returnedIds.contains("01ec6ec7-6454-4504-ac95-befb16bacb0e"));
        assertTrue(returnedIds.contains("004571b9-4649-42b3-9c28-a8cdc2bf53c7"));
    }

    @Test
    public void testQueryableGeom() throws Exception {
        var query = "geometry=7.908176232935124,43.301952247017084,17.541462920453505,46.83473548422572";
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?" + query,
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(5, items.getNumberMatched());
        var returnedIds = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(returnedIds.contains("a3774ef6-809d-4dd1-984f-9254f49cbd0a"));
        assertTrue(returnedIds.contains("a8b5e6c0-c21d-4c32-b8f9-10830215890a"));
        assertTrue(returnedIds.contains("9e1ea778-d0ce-4b49-90b7-37bc0e448300"));
        assertTrue(returnedIds.contains("8698bf0b-fceb-4f0f-989b-111e7c4af0a4"));
        assertTrue(returnedIds.contains("7eb795c2-d612-4b5e-b15e-d985b0f4e697"));
    }

    @Test
    public void testQueryableNested() throws Exception {
        var query = "contacts=Métropole";
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?" + query,
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(2, items.getNumberMatched());
        var returnedIds = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(returnedIds.contains("accroche_velos"));
        assertTrue(returnedIds.contains("ed34db28-5dd4-480f-bf29-dc08f0086131"));
    }

    /**
     * this tests how matches are made. In this test, we search for `"Métropole Européenne de Lille"`. The results
     * should have ALL these words in it.
     *
     * <p>TEST: previously, this would match contacts which had ANY of the search words in it. This would match any
     * contact with " de " in the text. This was too many results.
     *
     * @throws Exception request problem
     */
    @Test
    public void testQueryableNestedMultiplePhrases() throws Exception {
        var query = "contacts=Métropole Européenne de Lille";
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?" + query,
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(2, items.getNumberMatched());
        var returnedIds = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(returnedIds.contains("accroche_velos"));
        assertTrue(returnedIds.contains("ed34db28-5dd4-480f-bf29-dc08f0086131"));
    }

    // ----------------------------------------------------------------------------------------------------------

    /**
     * Record id 50aa3037-ac17-4c0d-ad15-b43a5a0929ae is not accessible by the anonymous user. It shouldn't be returned
     * in any queries. We query both MAIN_COLLECTION_ID and METAWAL_COLLECTION_ID in three ways - (a) get the record
     * directly (b) get all records (c) search by a title keyword (TOPO)
     */
    @Test
    public void testSecuritySimple() throws Exception {
        // single record query
        var query = "id=50aa3037-ac17-4c0d-ad15-b43a5a0929ae";
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?" + query,
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(0, items.getNumberMatched());
        assertEquals(0, items.getFeatures().size());

        // single record query
        items = retrieveUrlJson(
                "ogcapi-records/collections/" + METAWAL_COLLECTION_ID + "/items?" + query,
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(0, items.getNumberMatched());
        assertEquals(0, items.getFeatures().size());

        // all records query
        items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?limit=100",
                OgcApiRecordsGetRecords200ResponseDto.class);
        var returnedIds = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertFalse(returnedIds.contains("50aa3037-ac17-4c0d-ad15-b43a5a0929ae"));

        // all records query
        items = retrieveUrlJson(
                "ogcapi-records/collections/" + METAWAL_COLLECTION_ID + "/items?limit=100",
                OgcApiRecordsGetRecords200ResponseDto.class);
        returnedIds = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertFalse(returnedIds.contains("50aa3037-ac17-4c0d-ad15-b43a5a0929ae"));

        // all records query
        items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?q=TOPO",
                OgcApiRecordsGetRecords200ResponseDto.class);
        returnedIds = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertFalse(returnedIds.contains("50aa3037-ac17-4c0d-ad15-b43a5a0929ae"));

        // all records query
        items = retrieveUrlJson(
                "ogcapi-records/collections/" + METAWAL_COLLECTION_ID + "/items?q=TOPO",
                OgcApiRecordsGetRecords200ResponseDto.class);
        returnedIds = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertFalse(returnedIds.contains("50aa3037-ac17-4c0d-ad15-b43a5a0929ae"));

        // user johndoe is part of "sample group" which has access to this record
        items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?" + query,
                OgcApiRecordsGetRecords200ResponseDto.class,
                "johndoe");

        assertEquals(1, items.getNumberMatched());
        assertEquals(1, items.getFeatures().size());

        returnedIds = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(returnedIds.contains("50aa3037-ac17-4c0d-ad15-b43a5a0929ae"));
    }

    // ----------------------------------------------------------------------------------------------------------

    @Test
    public void testTypeConversion() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?id=urn:isogeo:metadata:uuid:1355be63-7f12-41f7-bcc8-724146679eb3",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getNumberMatched());

        var feature = items.getFeatures().get(0);
        var additionalProps = feature.getProperties().getAdditionalProperties();

        assertTrue(additionalProps.containsKey("resolutionScaleDenominator"));
        assertTrue(additionalProps.get("resolutionScaleDenominator") instanceof List);
        assertEquals(1, ((List) additionalProps.get("resolutionScaleDenominator")).size());
        assertEquals(
                Integer.class,
                ((List) additionalProps.get("resolutionScaleDenominator"))
                        .get(0)
                        .getClass());
        assertEquals(100000, ((List) additionalProps.get("resolutionScaleDenominator")).get(0));

        assertTrue(additionalProps.containsKey("resourceTitleObject"));
        assertTrue(additionalProps.get("resourceTitleObject") instanceof String);
        assertEquals(
                "Concentrations annuelles de polluants dans l'air ambiant issues du réseau permanent de mesures en région Hauts-de-France",
                additionalProps.get("resourceTitleObject"));

        assertTrue(additionalProps.containsKey("updateFrequency"));
        assertTrue(additionalProps.get("updateFrequency") instanceof String);
        assertEquals("annually", additionalProps.get("updateFrequency"));
    }

    @Test
    public void testTypeConversion2() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?id=8698bf0b-fceb-4f0f-989b-111e7c4af0a4",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getNumberMatched());

        var feature = items.getFeatures().get(0);
        var additionalProps = feature.getProperties().getAdditionalProperties();

        // tests upgrading to list and string->int
        assertTrue(additionalProps.containsKey("creationYearForResource"));
        assertTrue(additionalProps.get("creationYearForResource") instanceof List);
        assertEquals(1, ((List) additionalProps.get("creationYearForResource")).size());
        assertEquals(
                Integer.class,
                ((List) additionalProps.get("creationYearForResource")).get(0).getClass());
        assertEquals(1999, ((List) additionalProps.get("creationYearForResource")).get(0));

        assertEquals(25000, ((List) additionalProps.get("resolutionScaleDenominator")).get(0));

        assertEquals("2023-05-24T14:40:04.056Z", ((List) additionalProps.get("createDate")).get(0));
    }

    // ----------------------------------------------------------------------------------------------------------

    /** tests that the dynamic properties are in the swagger document */
    @Test
    public void testSwagger() throws Exception {
        var json = retrieveUrlJson("v3/api-docs?f=json");
        // there's compatibility issues when including this and everthing stops working!!!
        //      OpenAPI openAPI = new OpenAPIV3Parser().readContents(json).getOpenAPI();
        var tree = new ObjectMapper().readTree(json);
        var properties = tree.get("components")
                .get("schemas")
                .get("OgcApiRecordsRecordGeoJSONPropertiesDto")
                .get("properties");

        assertEquals(
                "array",
                ((TextNode) properties.get("resolutionScaleDenominator").get("type")).asText());
        assertEquals(
                "integer",
                ((TextNode) properties
                                .get("resolutionScaleDenominator")
                                .get("items")
                                .get("type"))
                        .asText());

        assertEquals("array", ((TextNode) properties.get("createDate").get("type")).asText());
        assertEquals(
                "string", ((TextNode) properties.get("createDate").get("items").get("type")).asText());
        assertEquals(
                "date", ((TextNode) properties.get("createDate").get("items").get("format")).asText());

        assertEquals("string", ((TextNode) properties.get("updateFrequency").get("type")).asText());
    }

    // ----------------------------------------------------------------------------------------------------------

    @Test
    public void testQueryables() throws Exception {
        var queryables = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/queryables", OgcApiRecordsJsonSchemaDto.class);

        assertEquals(18, queryables.getProperties().size());
        assertEquals("string", queryables.getProperties().get("createDate").getType());
        assertEquals("date", queryables.getProperties().get("createDate").getFormat());

        assertEquals("string", queryables.getProperties().get("updateFrequency").getType());
        assertEquals(
                "number",
                queryables.getProperties().get("resolutionScaleDenominator").getType());
    }

    @Test
    public void testDynamicQueryablesNumber() throws Exception {
        //
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?resolutionScaleDenominator=10000",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getNumberMatched());
        assertEquals(
                "a8b5e6c0-c21d-4c32-b8f9-10830215890a",
                items.getFeatures().get(0).getId());
    }

    @Test
    public void testDynamicQueryablesString() throws Exception {
        //
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?resourceTitleObject=Orthophotos",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getNumberMatched());
        assertEquals(
                "004571b9-4649-42b3-9c28-a8cdc2bf53c7",
                items.getFeatures().get(0).getId());

        items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?resourceTitleObject=Orthophotos 2021 - Service de visualisation REST",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getNumberMatched());
        assertEquals(
                "004571b9-4649-42b3-9c28-a8cdc2bf53c7",
                items.getFeatures().get(0).getId());

        items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?resourceTitleObject=2021",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(3, items.getNumberMatched());
        var returnedIds = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(returnedIds.contains("004571b9-4649-42b3-9c28-a8cdc2bf53c7"));
        assertTrue(returnedIds.contains("00916a35-786b-4569-9da6-71ca64ca54b1"));
        assertTrue(returnedIds.contains("6d0bfdf4-4e94-48c6-9740-3f9facfd453c"));
    }

    @Test
    public void testDynamicQueryablesDate() throws Exception {
        // date =
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?createDate=2021-06-08T14:08:00.968Z",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getNumberMatched());
        assertEquals(
                "02b1ef9c-d53c-400f-81bf-38dec751ce76",
                items.getFeatures().get(0).getId());

        // date >=
        items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?createDate=2021-06-07T14:00:00Z/..",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(23, items.getNumberMatched());

        // date >  AND date <
        items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?createDate=2021-06-07T14:00:00Z/2021-06-09T15:00:00Z",
                OgcApiRecordsGetRecords200ResponseDto.class);
        assertEquals(1, items.getNumberMatched());
        assertEquals(
                "02b1ef9c-d53c-400f-81bf-38dec751ce76",
                items.getFeatures().get(0).getId());
    }

    // ----------------------------------------------------------------------------------------------------------

    @Test
    public void testSortDefault() throws Exception {
        var result = retrieveUrlJson("ogcapi-records/collections", OgcApiRecordsGetCollections200ResponseDto.class);
        assertNotNull(result.getCollections().get(0).getDefaultSortOrder());
        assertFalse(result.getCollections().get(0).getDefaultSortOrder().isEmpty());

        assertNotNull(
                result.getCollections().get(0).getDefaultSortOrder().get(0).getDirection());
        assertNotNull(
                result.getCollections().get(0).getDefaultSortOrder().get(0).getField());

        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items",
                OgcApiRecordsGetRecords200ResponseDto.class);

        // should be ordered by id (uuid)
        var ids = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(Ordering.<String>natural().isOrdered(ids));
    }

    @Test
    public void testSort() throws Exception {
        // just id
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?sortby=id",
                OgcApiRecordsGetRecords200ResponseDto.class);

        var ids = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(Ordering.<String>natural().isOrdered(ids));

        // +id
        items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?sortby=+id",
                OgcApiRecordsGetRecords200ResponseDto.class);

        ids = items.getFeatures().stream().map(x -> x.getId()).toList();
        assertTrue(Ordering.<String>natural().isOrdered(ids));

        // -id
        items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?sortby=-id",
                OgcApiRecordsGetRecords200ResponseDto.class);

        ids = items.getFeatures().stream().map(x -> x.getId()).toList();
        ids = ids.reversed();
        assertTrue(Ordering.<String>natural().isOrdered(ids));
    }

    @Test
    public void testSortables() throws Exception {
        var sortables = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/sortables", OgcApiRecordsJsonSchemaDto.class);

        assertEquals(7, sortables.getProperties().size());
        assertEquals("string", sortables.getProperties().get("id").getType());
        assertEquals("string", sortables.getProperties().get("updateFrequency").getType());

        assertEquals("string", sortables.getProperties().get("createDate").getType());
        assertEquals("date", sortables.getProperties().get("createDate").getFormat());

        assertEquals(
                "number",
                sortables.getProperties().get("creationYearForResource").getType());
        assertEquals(
                "number",
                sortables.getProperties().get("resolutionScaleDenominator").getType());

        assertEquals(
                "string", sortables.getProperties().get("resourceTitleObject").getType());
        assertEquals("string", sortables.getProperties().get("resourceType").getType());
    }

    /** Strange means that its sorting on a value that has multiple values in a record (i.e. most of them). */
    @Test
    public void testSortNonStandard() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?sortby=updateFrequency",
                OgcApiRecordsGetRecords200ResponseDto.class);

        var updateFrequencies = items.getFeatures().stream()
                .map(x -> x.getProperties()
                        .getAdditionalProperties()
                        .get("updateFrequency")
                        .toString())
                .toList();
        assertTrue(Ordering.<String>natural().isOrdered(updateFrequencies));

        items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?sortby=-updateFrequency",
                OgcApiRecordsGetRecords200ResponseDto.class);

        updateFrequencies = items.getFeatures().stream()
                .map(x -> x.getProperties()
                        .getAdditionalProperties()
                        .get("updateFrequency")
                        .toString())
                .toList();
        updateFrequencies = updateFrequencies.reversed();
        assertTrue(Ordering.<String>natural().isOrdered(updateFrequencies));

        // multi-value
        items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?sortby=updateFrequency,id",
                OgcApiRecordsGetRecords200ResponseDto.class);
        var grouped = items.getFeatures().stream().collect(Collectors.groupingBy(o -> o.getProperties()
                .getAdditionalProperties()
                .get("updateFrequency")
                .toString()));

        for (var ids : grouped.values()) {
            assertTrue(Ordering.<String>natural()
                    .isOrdered(ids.stream().map(x -> x.getId()).toList()));
        }

        items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?sortby=-updateFrequency,-id",
                OgcApiRecordsGetRecords200ResponseDto.class);
        grouped = items.getFeatures().stream().collect(Collectors.groupingBy(o -> o.getProperties()
                .getAdditionalProperties()
                .get("updateFrequency")
                .toString()));

        for (var ids : grouped.values()) {
            var vals = ids.stream().map(x -> x.getId()).toList();
            vals = vals.reversed();
            assertTrue(Ordering.<String>natural().isOrdered(vals));
        }
    }

    // ----------------------------------------------------------------------------------------------------------

    /**
     * checks that there are 2 named (title) collections. This will fail if Postgresql didn't come up properly!
     *
     * @throws Exception bad response (usually unparseable or bad MVC or bad PGSQL)
     */
    @Test
    void test_simple_collections_test() throws Exception {
        var result = retrieveUrlJson("ogcapi-records/collections", OgcApiRecordsGetCollections200ResponseDto.class);

        assertEquals(2, result.getCollections().size());
        var collectionAll = result.getCollections().stream()
                .filter(x -> x.getTitle().equals("Test GeoNetwork-UI instance"))
                .findFirst()
                .get();
        var collectionMetawal = result.getCollections().stream()
                .filter(x -> x.getTitle().equals("Metawal"))
                .findFirst()
                .get();

        assertNotNull(collectionAll);
        assertNotNull(collectionMetawal);

        assertEquals(MAIN_COLLECTION_ID, collectionAll.getId());
        assertEquals(METAWAL_COLLECTION_ID, collectionMetawal.getId());
    }

    /**
     * simple test to get some records from the MAIN collection. This will fail if elastic isn't correctly "up".
     *
     * @throws Exception elastic down?
     */
    @Test
    void test_query() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertNotNull(items);
        assertEquals(10, items.getFeatures().size());
        assertEquals(28, items.getNumberMatched());
    }

    /**
     * This executes a blank (all records) query and checks to see if the facets (elastic aggregates) are correct.
     *
     * @throws Exception elastic down?
     */
    @Test
    public void test_simple_facets() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertNotNull(items);
        assertNotNull(items.getFeatures());
        assertNotNull(items.getFacets());

        var facets = items.getFacets();

        // failure here?  Likely a change in definition of the default facets.
        // probably ok to delete that part of the test - but likely need to add another one.

        for (var facetInfo : expectedFacets.entrySet()) {
            var facetName = facetInfo.getKey();
            var facetValue = facetInfo.getValue();
            var fromApp = facets.get(facetName);
            assertNotNull(fromApp);
            assertEquals(facetValue.getType(), fromApp.getType());
            assertEquals(facetValue.getProperty(), fromApp.getProperty());
            assertEquals(facetValue.getBuckets().size(), fromApp.getBuckets().size());
            for (var bucketIndx = 0; bucketIndx < facetValue.getBuckets().size(); bucketIndx++) {
                assertEquals(
                        facetValue.getBuckets().get(bucketIndx).getValue(),
                        fromApp.getBuckets().get(bucketIndx).getValue());
                assertEquals(
                        facetValue.getBuckets().get(bucketIndx).getCount(),
                        fromApp.getBuckets().get(bucketIndx).getCount());
                assertEquals(
                        facetValue.getBuckets().get(bucketIndx).getMin(),
                        fromApp.getBuckets().get(bucketIndx).getMin());
                assertEquals(
                        facetValue.getBuckets().get(bucketIndx).getMax(),
                        fromApp.getBuckets().get(bucketIndx).getMax());
                assertEquals(
                        facetValue.getBuckets().get(bucketIndx).getHighestBucket(),
                        fromApp.getBuckets().get(bucketIndx).getxHighestBucket());
            }
        }
    }

    public static Map<String, FacetInfo> expectedFacets = Map.ofEntries(
            //      entry("creationYearForResource2",
            //        new FacetInfo("histogram","creationYearForResource",
            //            List.of(new BucketInfo("2023.0","2025.0","2023.0", 3))))
            // ------------------------------
            // this is a simple TERM facet
            entry(
                    "orgForResource",
                    new FacetInfo(
                            "term",
                            "orgForResource",
                            List.of(
                                    new BucketInfo(null, null, "Service public de Wallonie (SPW)", 12, null),
                                    new BucketInfo(
                                            null,
                                            null,
                                            "Helpdesk carto du SPW (SPW - Secrétariat général - SPW Digital - Département de la Géomatique - Direction de l'Intégration des géodonnées)",
                                            12,
                                            null),
                                    new BucketInfo(
                                            null,
                                            null,
                                            "Direction de l'Intégration des géodonnées (SPW - Secrétariat général - SPW Digital - Département de la Géomatique - Direction de l'Intégration des géodonnées)",
                                            10,
                                            null),
                                    new BucketInfo(null, null, "Métropole Européenne de Lille", 2, null),
                                    new BucketInfo(null, null, "atmo Hauts-de-France", 1, null),
                                    new BucketInfo(null, null, "Société Publique de Gestion de l'Eau (SPGE)", 1, null),
                                    new BucketInfo(null, null, "Réseau Ongulés sauvages OFB-FNC-FDC", 1, null),
                                    new BucketInfo(null, null, "Région Hauts-de-France", 1, null),
                                    new BucketInfo(null, null, "Office France de la Biodiversité", 1, null),
                                    new BucketInfo(null, null, "Moi même", 1, null),
                                    new BucketInfo(null, null, "Géo2France", 1, null),
                                    new BucketInfo(null, null, "Fédération Nationale de la Chasse", 1, null),
                                    new BucketInfo(null, null, "Fédération Départementale de la Chasse", 1, null),
                                    new BucketInfo(
                                            null,
                                            null,
                                            "Direction de l'Action sociale (SPW - Intérieur et Action sociale - Département de l'Action sociale - Direction de l'Action sociale)",
                                            1,
                                            null),
                                    new BucketInfo(
                                            null,
                                            null,
                                            "DREAL HdF (Direction Régionale de l'Environnement de l'Aménagement et du Logement des Hauts de France)",
                                            1,
                                            null),
                                    new BucketInfo(null, null, "DREAL", 1, null),
                                    new BucketInfo(
                                            null,
                                            null,
                                            "Coordination, Services et Informations Géographiques (COSIG), swisstopo",
                                            1,
                                            null),
                                    new BucketInfo(
                                            null,
                                            null,
                                            "Cellule informatique et géomatique (SPW - Intérieur et Action sociale - Direction fonctionnelle et d’appui)",
                                            1,
                                            null),
                                    new BucketInfo(
                                            null,
                                            null,
                                            "Canton du Valais - Service de l'environnement (SEN) - Protection des sols",
                                            1,
                                            null),
                                    new BucketInfo(null, null, "Bundesamt für Raumentwicklung", 1, null),
                                    new BucketInfo(null, null, "Barbie Inc.", 1, null),
                                    new BucketInfo(
                                            null,
                                            null,
                                            "Agence wallonne du Patrimoine (SPW - Territoire, Logement, Patrimoine, Énergie - Agence wallonne du Patrimoine)",
                                            1,
                                            null)))),
            // ------------------------------
            // this is a histogram facet (fixed interval)
            entry(
                    "creationYearForResource",
                    new FacetInfo(
                            "histogram",
                            "creationYearForResource",
                            List.of(
                                    new BucketInfo("2025.0", "2030.0", "2025.0", 1, null),
                                    new BucketInfo("2020.0", "2025.0", "2020.0", 8, null),
                                    new BucketInfo("2015.0", "2020.0", "2015.0", 1, null),
                                    new BucketInfo("2010.0", "2015.0", "2010.0", 1, null),
                                    new BucketInfo("2005.0", "2010.0", "2005.0", 1, null),
                                    new BucketInfo("1995.0", "2000.0", "1995.0", 1, null)))),
            // ------------------------------
            // this is a FILTER facet
            entry(
                    "availableInServices",
                    new FacetInfo(
                            "filter",
                            null,
                            List.of(
                                    new BucketInfo(null, null, "availableInDownloadService", 5, null),
                                    new BucketInfo(null, null, "availableInViewService", 13, null)))),
            // --------------------------------
            // this is a fixed-number-of-buckets facet (numbers)
            entry(
                    "creationYearForResource2",
                    new FacetInfo(
                            "histogram",
                            "creationYearForResource",
                            List.of(
                                    new BucketInfo("2023.0", "2025.0", "2023.0", 3, true),
                                    new BucketInfo("2019.0", "2023.0", "2019.0", 7, null),
                                    new BucketInfo("2013.0", "2019.0", "2013.0", 1, null),
                                    new BucketInfo("2005.0", "2013.0", "2005.0", 1, null),
                                    new BucketInfo("1999.0", "2005.0", "1999.0", 1, null)))),
            // ------------------------------
            // this is a histogram facet (fixed interval) (date)
            entry(
                    "createDate",
                    new FacetInfo(
                            "histogram",
                            "createDate",
                            List.of(
                                    new BucketInfo(
                                            "2025-04-01T00:00:00Z", "2025-05-01T00:00:00Z", "1743465600000", 1, null),
                                    new BucketInfo(
                                            "2025-01-01T00:00:00Z", "2025-02-01T00:00:00Z", "1735689600000", 1, null),
                                    new BucketInfo(
                                            "2024-05-01T00:00:00Z", "2024-06-01T00:00:00Z", "1714521600000", 1, null),
                                    new BucketInfo(
                                            "2024-02-01T00:00:00Z", "2024-03-01T00:00:00Z", "1706745600000", 2, null),
                                    new BucketInfo(
                                            "2023-05-01T00:00:00Z", "2023-06-01T00:00:00Z", "1682899200000", 10, null),
                                    new BucketInfo(
                                            "2023-03-01T00:00:00Z", "2023-04-01T00:00:00Z", "1677628800000", 1, null),
                                    new BucketInfo(
                                            "2022-10-01T00:00:00Z", "2022-11-01T00:00:00Z", "1664582400000", 1, null),
                                    new BucketInfo(
                                            "2022-07-01T00:00:00Z", "2022-08-01T00:00:00Z", "1656633600000", 1, null),
                                    new BucketInfo(
                                            "2022-06-01T00:00:00Z", "2022-07-01T00:00:00Z", "1654041600000", 1, null),
                                    new BucketInfo(
                                            "2022-03-01T00:00:00Z", "2022-04-01T00:00:00Z", "1646092800000", 3, null),
                                    new BucketInfo(
                                            "2021-06-01T00:00:00Z", "2021-07-01T00:00:00Z", "1622505600000", 1, null),
                                    new BucketInfo(
                                            "2020-06-01T00:00:00Z", "2020-07-01T00:00:00Z", "1590969600000", 1, null),
                                    new BucketInfo(
                                            "2019-10-01T00:00:00Z", "2019-11-01T00:00:00Z", "1569888000000", 2, null),
                                    new BucketInfo(
                                            "2019-05-01T00:00:00Z",
                                            "2019-06-01T00:00:00Z",
                                            "1556668800000",
                                            2,
                                            null)))),
            // --------------------------------
            // this is a fixed-number-of-buckets facet (date)
            entry(
                    "createDate2",
                    new FacetInfo(
                            "histogram",
                            "createDate",
                            List.of(
                                    new BucketInfo(
                                            "2024-01-01T00:00:00Z", "2029-01-01T00:00:00Z", "1704067200000", 5, true),
                                    new BucketInfo(
                                            "2019-01-01T00:00:00Z",
                                            "2024-01-01T00:00:00Z",
                                            "1546300800000",
                                            23,
                                            null)))));

    @Getter
    @Setter
    @AllArgsConstructor
    public static class FacetInfo {
        String type;
        String property;
        List<BucketInfo> buckets;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class BucketInfo {
        String min;
        String max;
        String value;
        Integer count;
        Boolean highestBucket;
    }
}
