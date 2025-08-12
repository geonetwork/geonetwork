/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.trivial;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.geonetwork.infrastructure.ElasticPgMvcBaseTest;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetCollections200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * some simple tests to verify that the containers have spun up and there is data available. Also, checks basic
 * functionality WRT facets.
 */
@ContextConfiguration(initializers = SimpleRecordsMvcTests.class)
public class SimpleRecordsMvcTests extends ElasticPgMvcBaseTest {

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
                    "organizations",
                    new FacetInfo(
                            "term",
                            "organizations",
                            List.of(
                                    new BucketInfo(null, null, "Federal Office for Spatial Development", 1, null),
                                    new BucketInfo(
                                            null,
                                            null,
                                            "Coordination, Geo-Information and Services (COGIS)",
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
