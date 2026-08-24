/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.facets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import org.geonetwork.cql.QueryTest;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;

/**
 * This class exists just to make the QueryTest be "smallers".
 *
 * <p>Because of how the TestContainers work, you cannot really share a connection to the container between test
 * classes. Also, the containers take about 30s to spin up. I am breaking the advanced facets to here just for
 * readability.
 *
 * <p>First section - simple tests <br>
 *
 * <p>2nd section (HISTOGRAM_FIXED_INTERVAL number) - explicit tests on creationYearForResource facet, which is a
 * HISTOGRAM_FIXED_INTERVAL facet with numberBucketInterval=5 and bucketCount=25. This means that it should return
 * buckets of 5 year intervals and should return up to 25 buckets. In practice, our test dataset only has 6 buckets. It
 * doesn't make much sense to request more than 6 buckets (there aren't any since there are only 6 in the data).
 *
 * <p>3rd section (FILTER) - tests on the availableInServices facet, which is a FILTER facet with 2 filters:
 * availableInDownloadService and availableinViewService.
 *
 * <p>4th section (HISTOGRAM_FIXED_BUCKET_COUNT - number) - tests on creationYearForResource2 facet, which is a
 * HISTOGRAM_FIXED_BUCKET_COUNT. NOTE: elastic seems to be "forgetting" about the final document at the bucket bounds.
 *
 * <p>5th section (TERM) - test on orgForResource, which is a TERM facet.
 *
 * <p>6th section - create date (for full date). This is a fixed interval (doesn't make too much sense to request fewer
 * buckets).
 *
 * <p>7th section - create date (for full date). This is a variable interval. Elastic will try to find a reasonable
 * calendar interval. It will often not return some of the records (the ones in the last bucket).
 *
 * <p>This isn't unit test best practices, but it should be fine and make life easier for everyone.
 */
public class AdvancedFacetTests {

    public String MAIN_COLLECTION_ID;
    QueryTest parent;

    public AdvancedFacetTests(QueryTest parent) {
        this.MAIN_COLLECTION_ID = parent.MAIN_COLLECTION_ID;
        this.parent = parent;
    }

    private <T> T retrieveUrlJson(String s, Class<T> clazz) throws Exception {
        return parent.retrieveUrlJson(s, clazz);
    }

    /** run all the @AdvancedFacetTest methods in this class */
    public void runTests() throws Exception {
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(AdvancedFacetTest.class)) {
                System.out.println("Running test: " + method.getName());
                method.invoke(this); // invoke the test method
            }
        }
    }

    // ---------------------- advanced facets

    /**
     * when &facets= NOT in request, return all facets
     *
     * @throws Exception elastic down?
     */
    @AdvancedFacetTest
    public void testAdvancedFacets_return_all() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertTrue(items.getFacets().size() > 5); // should all be there
    }

    /**
     * "&facets=" means to return NONE
     *
     * @throws Exception elastic down?
     */
    @AdvancedFacetTest
    public void testAdvancedFacets_return_none() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(0, items.getFacets().size()); // should be none there
    }

    /**
     * "&facets=creationYearForResource" means to return only that one facet
     *
     * @throws Exception elastic down?
     */
    @AdvancedFacetTest
    public void testAdvancedFacets_return_1() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=creationYearForResource",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());
        assertEquals(
                "creationYearForResource", items.getFacets().keySet().iterator().next());
    }

    /**
     * "&facets=creationYearForResource:2" means to return only that one facet and should have 2 buckets
     *
     * @throws Exception elastic down?
     */
    @AdvancedFacetTest
    public void testAdvancedFacets_return_1_number_of_buckets() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=creationYearForResource:2",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());
        assertEquals(
                2, items.getFacets().get("creationYearForResource").getBuckets().size());
    }

    /**
     * "&facets=creationYearForResource:2:count_asc" means to return only that one facet and should have 2 buckets and
     * should be ordered by ascending count
     *
     * @throws Exception elastic down?
     */
    @AdvancedFacetTest
    public void testAdvancedFacets_creationYearForResource_sort_count_asc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?facets=creationYearForResource:2:count_asc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("creationYearForResource").getBuckets();

        assertEquals(2, buckets.size());

        assertEquals("1995.0", buckets.get(0).getValue());
        assertEquals(1, buckets.get(0).getCount());

        assertEquals("2005.0", buckets.get(1).getValue());
        assertEquals(1, buckets.get(1).getCount());
    }

    // ========================================================

    /**
     * "&facets=creationYearForResource:2:count_desc" means to return only that one facet and should have 2 buckets and
     * should be ordered by ascending count
     *
     * <p>facetsConfig: - facetName: creationYearForResource facetType: HISTOGRAM_FIXED_INTERVAL numberBucketInterval: 5
     * bucketCount: 25 bucketSorting: VALUE
     *
     * <p>Data range is 1999-2025. There are 14 records that are not "null".
     *
     * <p>curl -s "localhost:58956/gn-records/_search?q=*:*&size=5555&_source=creationYearForResource" | jq -r
     * '.hits.hits[]._source.field'
     *
     * @throws Exception elastic down?
     */
    @AdvancedFacetTest
    public void testAdvancedFacets_creationYearForResource_sort_count_desc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?facets=creationYearForResource:2:count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("creationYearForResource").getBuckets();
        assertEquals(2, buckets.size());

        assertEquals("2020.0", buckets.get(0).getValue());
        assertEquals(8, buckets.get(0).getCount());

        assertEquals("2025.0", buckets.get(1).getValue());
        assertEquals(1, buckets.get(1).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_creationYearForResource_sort_count_desc_lots_of_buckets() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?facets=creationYearForResource:33:count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("creationYearForResource").getBuckets();
        assertEquals(6, buckets.size());

        assertEquals("2020.0", buckets.get(0).getValue());
        assertEquals(8, buckets.get(0).getCount());

        assertEquals("1995.0", buckets.get(5).getValue());
        assertEquals(1, buckets.get(1).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_creationYearForResource_sort_count_asc_lots_of_buckets() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?facets=creationYearForResource:33:count_asc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("creationYearForResource").getBuckets();
        assertEquals(6, buckets.size());

        assertEquals("1995.0", buckets.get(0).getValue());
        assertEquals(1, buckets.get(0).getCount());

        assertEquals("2020.0", buckets.get(5).getValue());
        assertEquals(8, buckets.get(5).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_creationYearForResource_sort_value_desc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?facets=creationYearForResource:2:value_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("creationYearForResource").getBuckets();
        assertEquals(2, buckets.size());

        assertEquals("2025.0", buckets.get(0).getValue());
        assertEquals(1, buckets.get(0).getCount());

        assertEquals("2020.0", buckets.get(1).getValue());
        assertEquals(8, buckets.get(1).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_creationYearForResource_sort_value_asc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?facets=creationYearForResource:2:value_asc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("creationYearForResource").getBuckets();
        assertEquals(2, buckets.size());

        assertEquals("1995.0", buckets.get(0).getValue());
        assertEquals(1, buckets.get(0).getCount());

        assertEquals("2005.0", buckets.get(1).getValue());
        assertEquals(1, buckets.get(1).getCount());
    }

    // =========================================================
    // These are tests for the FILTER facet
    //
    //  ogcProperty: linkProtocol
    //  elasticProperty: linkProtocol
    //  indexRecordProperty: linkProtocols
    //  facetsConfig:
    //    - facetName: availableInServices
    //      facetType: FILTER
    //      filters:
    //       - filterName: availableInDownloadService
    //         filterEquationCql: linkProtocol = 'OGC:WFS'
    //      - filterName: availableInViewService
    //        filterEquationCql: linkProtocol = 'OGC:WMS'
    //
    //
    // doesn't really make sense to limit buckets on this
    @AdvancedFacetTest
    public void testAdvancedFacets_availableInServices_sort_value_asc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=availableInServices:2:value_asc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("availableInServices").getBuckets();
        assertEquals(2, buckets.size());

        assertEquals("availableInDownloadService", buckets.get(0).getValue());
        assertEquals(5, buckets.get(0).getCount());

        assertEquals("availableInViewService", buckets.get(1).getValue());
        assertEquals(13, buckets.get(1).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_availableInServices_sort_value_desc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=availableInServices:2:value_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("availableInServices").getBuckets();
        assertEquals(2, buckets.size());

        assertEquals("availableInViewService", buckets.get(0).getValue());
        assertEquals(13, buckets.get(0).getCount());

        assertEquals("availableInDownloadService", buckets.get(1).getValue());
        assertEquals(5, buckets.get(1).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_availableInServices_sort_count_desc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=availableInServices:2:count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("availableInServices").getBuckets();
        assertEquals(2, buckets.size());

        assertEquals("availableInViewService", buckets.get(0).getValue());
        assertEquals(13, buckets.get(0).getCount());

        assertEquals("availableInDownloadService", buckets.get(1).getValue());
        assertEquals(5, buckets.get(1).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_availableInServices_sort_count_asc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=availableInServices:2:count_asc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("availableInServices").getBuckets();
        assertEquals(2, buckets.size());

        assertEquals("availableInDownloadService", buckets.get(0).getValue());
        assertEquals(5, buckets.get(0).getCount());

        assertEquals("availableInViewService", buckets.get(1).getValue());
        assertEquals(13, buckets.get(1).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_availableInServices_buckets1() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=availableInServices:1:count_asc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("availableInServices").getBuckets();
        assertEquals(1, buckets.size());

        assertEquals("availableInDownloadService", buckets.get(0).getValue());
        assertEquals(5, buckets.get(0).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_availableInServices_buckets2() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=availableInServices:1:count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("availableInServices").getBuckets();
        assertEquals(1, buckets.size());

        assertEquals("availableInViewService", buckets.get(0).getValue());
        assertEquals(13, buckets.get(0).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_availableInServices_bucket3() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=availableInServices::count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("availableInServices").getBuckets();
        assertEquals(2, buckets.size());

        assertEquals("availableInViewService", buckets.get(0).getValue());
        assertEquals(13, buckets.get(0).getCount());

        assertEquals("availableInDownloadService", buckets.get(1).getValue());
        assertEquals(5, buckets.get(1).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_availableInServices_bucket4() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=availableInServices:50:count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("availableInServices").getBuckets();
        assertEquals(2, buckets.size());

        assertEquals("availableInViewService", buckets.get(0).getValue());
        assertEquals(13, buckets.get(0).getCount());

        assertEquals("availableInDownloadService", buckets.get(1).getValue());
        assertEquals(5, buckets.get(1).getCount());
    }

    // =========================================================
    // creationYearForResource2 - is fixed # of buckets (HISTOGRAM_FIXED_BUCKET_COUNT)

    @AdvancedFacetTest
    public void testAdvancedFacets_creationYearForResource2_sort_value_desc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?facets=creationYearForResource2::value_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("creationYearForResource2").getBuckets();
        assertEquals(5, buckets.size());

        assertEquals("2023.0", buckets.get(0).getValue());
        assertEquals(3, buckets.get(0).getCount());

        assertEquals("2019.0", buckets.get(1).getValue());
        assertEquals(7, buckets.get(1).getCount());

        // ...

        assertEquals("1999.0", buckets.get(4).getValue());
        assertEquals(1, buckets.get(4).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_creationYearForResource2_sort_value_asc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?facets=creationYearForResource2::value_asc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("creationYearForResource2").getBuckets();
        assertEquals(5, buckets.size());

        assertEquals("1999.0", buckets.get(0).getValue());
        assertEquals(1, buckets.get(0).getCount());

        assertEquals("2005.0", buckets.get(1).getValue());
        assertEquals(1, buckets.get(1).getCount());

        // ...

        assertEquals("2023.0", buckets.get(4).getValue());
        assertEquals(3, buckets.get(4).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_creationYearForResource2_sort_count_desc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?facets=creationYearForResource2::count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("creationYearForResource2").getBuckets();
        assertEquals(5, buckets.size());

        assertEquals("2019.0", buckets.get(0).getValue());
        assertEquals(7, buckets.get(0).getCount());

        assertEquals("2023.0", buckets.get(1).getValue());
        assertEquals(3, buckets.get(1).getCount());

        // ...

        assertEquals("1999.0", buckets.get(4).getValue());
        assertEquals(1, buckets.get(4).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_creationYearForResource2_sort_count_asc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?facets=creationYearForResource2::count_asc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("creationYearForResource2").getBuckets();
        assertEquals(5, buckets.size());

        assertEquals("1999.0", buckets.get(0).getValue());
        assertEquals(1, buckets.get(0).getCount());

        assertEquals("2005.0", buckets.get(1).getValue());
        assertEquals(1, buckets.get(1).getCount());

        // ...

        assertEquals("2023.0", buckets.get(3).getValue());
        assertEquals(3, buckets.get(3).getCount());

        assertEquals("2019.0", buckets.get(4).getValue());
        assertEquals(7, buckets.get(4).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_creationYearForResource2_buckets1() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?facets=creationYearForResource2:1:count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("creationYearForResource2").getBuckets();
        assertEquals(1, buckets.size());

        assertEquals("1999.0", buckets.get(0).getValue());
        assertEquals(13, buckets.get(0).getCount());

        // NB: we would expect to have 14 here.  However, the last one (2025.0) isn't counted as in that bucket.
        //    this is an elastic issue.
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_creationYearForResource2_buckets2() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                        + "/items?facets=creationYearForResource2:2:count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("creationYearForResource2").getBuckets();
        assertEquals(2, buckets.size());

        assertEquals("2013.0", buckets.get(0).getValue());
        assertEquals(11, buckets.get(0).getCount());

        assertEquals("1999.0", buckets.get(1).getValue());
        assertEquals(2, buckets.get(1).getCount());

        // NB: we would expect to have 14 here.  However, the last one (2025.0) isn't counted as in that bucket.
        //    this is an elastic issue.
    }

    // =========================================================
    // orgForResource facet is a TERMS facet on the orgForResource field, which is an array of strings.

    @AdvancedFacetTest
    public void testAdvancedFacets_orgForResource_sort_value_desc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=orgForResource::value_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("orgForResource").getBuckets();
        assertEquals(22, buckets.size());

        assertEquals(
                "Société Publique de Gestion de l'Eau (SPGE)", buckets.get(0).getValue());
        assertEquals(1, buckets.get(0).getCount());

        assertEquals("Service public de Wallonie (SPW)", buckets.get(1).getValue());
        assertEquals(12, buckets.get(1).getCount());

        // ...

        assertEquals(
                "Agence wallonne du Patrimoine (SPW - Territoire, Logement, Patrimoine, Énergie - Agence wallonne du Patrimoine)",
                buckets.get(21).getValue());
        assertEquals(1, buckets.get(21).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_orgForResource_sort_value_asc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=orgForResource::value_asc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("orgForResource").getBuckets();
        assertEquals(22, buckets.size());

        assertEquals(
                "Agence wallonne du Patrimoine (SPW - Territoire, Logement, Patrimoine, Énergie - Agence wallonne du Patrimoine)",
                buckets.get(0).getValue());
        assertEquals(1, buckets.get(0).getCount());
        // ...

        assertEquals("Service public de Wallonie (SPW)", buckets.get(20).getValue());
        assertEquals(12, buckets.get(20).getCount());

        assertEquals(
                "Société Publique de Gestion de l'Eau (SPGE)", buckets.get(21).getValue());
        assertEquals(1, buckets.get(21).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_orgForResource_sort_count_desc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=orgForResource::count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("orgForResource").getBuckets();
        assertEquals(22, buckets.size());

        assertEquals("Service public de Wallonie (SPW)", buckets.get(0).getValue());
        assertEquals(12, buckets.get(0).getCount());

        assertEquals(
                "Helpdesk carto du SPW (SPW - Secrétariat général - SPW Digital - Département de la Géomatique - Direction de l'Intégration des géodonnées)",
                buckets.get(1).getValue());
        assertEquals(12, buckets.get(1).getCount());

        assertEquals(
                "Direction de l'Intégration des géodonnées (SPW - Secrétariat général - SPW Digital - Département de la Géomatique - Direction de l'Intégration des géodonnées)",
                buckets.get(2).getValue());
        assertEquals(10, buckets.get(2).getCount());

        // ...

        assertEquals(
                "Agence wallonne du Patrimoine (SPW - Territoire, Logement, Patrimoine, Énergie - Agence wallonne du Patrimoine)",
                buckets.get(21).getValue());
        assertEquals(1, buckets.get(21).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_orgForResource_sort_count_asc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=orgForResource::count_asc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("orgForResource").getBuckets();
        assertEquals(22, buckets.size());

        assertEquals("Service public de Wallonie (SPW)", buckets.get(21).getValue());
        assertEquals(12, buckets.get(21).getCount());

        assertEquals(
                "Helpdesk carto du SPW (SPW - Secrétariat général - SPW Digital - Département de la Géomatique - Direction de l'Intégration des géodonnées)",
                buckets.get(20).getValue());
        assertEquals(12, buckets.get(20).getCount());

        assertEquals(
                "Direction de l'Intégration des géodonnées (SPW - Secrétariat général - SPW Digital - Département de la Géomatique - Direction de l'Intégration des géodonnées)",
                buckets.get(19).getValue());
        assertEquals(10, buckets.get(19).getCount());

        // ...

        assertEquals(
                "Agence wallonne du Patrimoine (SPW - Territoire, Logement, Patrimoine, Énergie - Agence wallonne du Patrimoine)",
                buckets.get(0).getValue());
        assertEquals(1, buckets.get(0).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_orgForResource_buckets1() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=orgForResource:1:count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("orgForResource").getBuckets();
        assertEquals(1, buckets.size());

        assertEquals("Service public de Wallonie (SPW)", buckets.get(0).getValue());
        assertEquals(12, buckets.get(0).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_orgForResource_buckets2() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=orgForResource:2:count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("orgForResource").getBuckets();
        assertEquals(2, buckets.size());

        assertEquals("Service public de Wallonie (SPW)", buckets.get(0).getValue());
        assertEquals(12, buckets.get(0).getCount());

        assertEquals(
                "Helpdesk carto du SPW (SPW - Secrétariat général - SPW Digital - Département de la Géomatique - Direction de l'Intégration des géodonnées)",
                buckets.get(1).getValue());
        assertEquals(12, buckets.get(1).getCount());
    }

    // =========================================================
    // createDate -
    //
    //  facetsConfig:
    //    - facetName: createDate
    //      facetType: HISTOGRAM_FIXED_INTERVAL
    //      calendarIntervalUnit: MONTH
    //      bucketCount: 25
    //      bucketSorting: VALUE

    @AdvancedFacetTest
    public void testAdvancedFacets_createDate_sort_value_desc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=createDate::value_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("createDate").getBuckets();
        assertEquals(14, buckets.size());

        assertEquals("1743465600000", buckets.get(0).getValue());
        assertEquals("2025-04-01T00:00:00Z", buckets.get(0).getMin());
        assertEquals(1, buckets.get(0).getCount());

        // ...

        assertEquals("1556668800000", buckets.get(13).getValue());
        assertEquals("2019-05-01T00:00:00Z", buckets.get(13).getMin());
        assertEquals(2, buckets.get(13).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_createDate_sort_value_asc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=createDate::value_asc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("createDate").getBuckets();
        assertEquals(14, buckets.size());

        assertEquals("1556668800000", buckets.get(0).getValue());
        assertEquals("2019-05-01T00:00:00Z", buckets.get(0).getMin());
        assertEquals(2, buckets.get(0).getCount());

        assertEquals("1569888000000", buckets.get(1).getValue());
        assertEquals("2019-10-01T00:00:00Z", buckets.get(1).getMin());
        assertEquals(2, buckets.get(1).getCount());

        // ...

        assertEquals("1743465600000", buckets.get(13).getValue());
        assertEquals("2025-04-01T00:00:00Z", buckets.get(13).getMin());
        assertEquals(1, buckets.get(13).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_createDate_sort_count_desc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=createDate::count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("createDate").getBuckets();
        assertEquals(14, buckets.size());

        assertEquals("1682899200000", buckets.get(0).getValue());
        assertEquals("2023-05-01T00:00:00Z", buckets.get(0).getMin());
        assertEquals(10, buckets.get(0).getCount());

        assertEquals("1646092800000", buckets.get(1).getValue());
        assertEquals("2022-03-01T00:00:00Z", buckets.get(1).getMin());
        assertEquals(3, buckets.get(1).getCount());

        // ...

        assertEquals("1622505600000", buckets.get(12).getValue());
        assertEquals("2021-06-01T00:00:00Z", buckets.get(12).getMin());
        assertEquals(1, buckets.get(12).getCount());

        assertEquals("1590969600000", buckets.get(13).getValue());
        assertEquals("2020-06-01T00:00:00Z", buckets.get(13).getMin());
        assertEquals(1, buckets.get(13).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_createDate_buckets1() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=createDate:1:count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("createDate").getBuckets();
        assertEquals(1, buckets.size());

        assertEquals("1682899200000", buckets.get(0).getValue());
        assertEquals("2023-05-01T00:00:00Z", buckets.get(0).getMin());
        assertEquals(10, buckets.get(0).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_createDate_buckets2() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=createDate:2:count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("createDate").getBuckets();
        assertEquals(2, buckets.size());

        assertEquals("1682899200000", buckets.get(0).getValue());
        assertEquals("2023-05-01T00:00:00Z", buckets.get(0).getMin());
        assertEquals(10, buckets.get(0).getCount());

        assertEquals("1646092800000", buckets.get(1).getValue());
        assertEquals("2022-03-01T00:00:00Z", buckets.get(1).getMin());
        assertEquals(3, buckets.get(1).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_createDate_sort_count_asc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=createDate::count_asc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("createDate").getBuckets();
        assertEquals(14, buckets.size());

        assertEquals("1590969600000", buckets.get(0).getValue());
        assertEquals("2020-06-01T00:00:00Z", buckets.get(0).getMin());
        assertEquals(1, buckets.get(0).getCount());

        assertEquals("1622505600000", buckets.get(1).getValue());
        assertEquals("2021-06-01T00:00:00Z", buckets.get(1).getMin());
        assertEquals(1, buckets.get(1).getCount());

        // ...

        assertEquals("1646092800000", buckets.get(12).getValue());
        assertEquals("2022-03-01T00:00:00Z", buckets.get(12).getMin());
        assertEquals(3, buckets.get(12).getCount());

        assertEquals("1682899200000", buckets.get(13).getValue());
        assertEquals("2023-05-01T00:00:00Z", buckets.get(13).getMin());
        assertEquals(10, buckets.get(13).getCount());
    }

    // =========================================================
    // createDate2 -
    //
    //  facetsConfig:
    //      - facetName: createDate2
    //              facetType: HISTOGRAM_FIXED_BUCKET_COUNT
    //              bucketCount: 5
    //              bucketSorting: VALUE
    //
    // NOTE: somewhat broken in elastic (at least it doesn't do what you would expect).

    @AdvancedFacetTest
    public void testAdvancedFacets_createDate2_sort_value_desc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=createDate2::value_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("createDate2").getBuckets();
        assertEquals(2, buckets.size());

        // NB: elastic chooses 5y as interval (!!)

        assertEquals("1704067200000", buckets.get(0).getValue());
        assertEquals("2024-01-01T00:00:00Z", buckets.get(0).getMin());
        assertEquals(5, buckets.get(0).getCount());

        assertEquals("1546300800000", buckets.get(1).getValue());
        assertEquals(23, buckets.get(1).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_createDate2_sort_value_asc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=createDate2::value_asc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("createDate2").getBuckets();
        assertEquals(2, buckets.size());

        // NB: elastic chooses 5y as interval (!!)

        assertEquals("1546300800000", buckets.get(0).getValue());
        assertEquals(23, buckets.get(0).getCount());

        assertEquals("1704067200000", buckets.get(1).getValue());
        assertEquals(5, buckets.get(1).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_createDate2_sort_count_desc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=createDate2::count_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("createDate2").getBuckets();
        assertEquals(2, buckets.size());

        // NB: elastic chooses 5y as interval (!!)

        assertEquals("1546300800000", buckets.get(0).getValue());
        assertEquals(23, buckets.get(0).getCount());

        assertEquals("1704067200000", buckets.get(1).getValue());
        assertEquals(5, buckets.get(1).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_createDate2_sort_count_asc() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=createDate2::count_asc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("createDate2").getBuckets();
        assertEquals(2, buckets.size());

        // NB: elastic chooses 5y as interval (!!)

        assertEquals("1704067200000", buckets.get(0).getValue());
        assertEquals(5, buckets.get(0).getCount());

        assertEquals("1546300800000", buckets.get(1).getValue());
        assertEquals(23, buckets.get(1).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_createDate2_buckets1() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=createDate2:1:value_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("createDate2").getBuckets();
        assertEquals(1, buckets.size());

        // NB: elastic chooses 5y as interval (!!)

        assertEquals("1546300800000", buckets.get(0).getValue());
        assertEquals("2019-01-01T00:00:00Z", buckets.get(0).getMin());
        assertEquals(28, buckets.get(0).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_createDate2_buckets2() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=createDate2:2:value_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("createDate2").getBuckets();
        assertEquals(2, buckets.size());

        // NB: elastic chooses 5y as interval (!!)

        assertEquals("1704067200000", buckets.get(0).getValue());
        assertEquals(5, buckets.get(0).getCount());

        assertEquals("1546300800000", buckets.get(1).getValue());
        assertEquals(23, buckets.get(1).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_createDate2_buckets3() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=createDate2:20:value_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("createDate2").getBuckets();
        assertEquals(7, buckets.size());

        // NB: elastic chooses 5y as interval (!!)

        assertEquals("1735689600000", buckets.get(0).getValue());
        assertEquals(2, buckets.get(0).getCount());

        assertEquals("1704067200000", buckets.get(1).getValue());
        assertEquals(3, buckets.get(1).getCount());

        assertEquals("1546300800000", buckets.get(6).getValue());
        assertEquals(4, buckets.get(6).getCount());
    }

    @AdvancedFacetTest
    public void testAdvancedFacets_createDate2_buckets4() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?facets=createDate2:50:value_desc",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertEquals(1, items.getFacets().size());

        var buckets = items.getFacets().get("createDate2").getBuckets();
        assertEquals(13, buckets.size());

        // NB: elastic chooses 5y as interval (!!)

        assertEquals("1738368000000", buckets.get(0).getValue());
        assertEquals(1, buckets.get(0).getCount());

        assertEquals("1730419200000", buckets.get(1).getValue());
        assertEquals(1, buckets.get(1).getCount());

        assertEquals("1556668800000", buckets.get(12).getValue());
        assertEquals(2, buckets.get(12).getCount());
    }

    @Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AdvancedFacetTest {}
}
