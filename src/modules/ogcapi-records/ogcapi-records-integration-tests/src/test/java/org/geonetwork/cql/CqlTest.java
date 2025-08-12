/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.cql;

import static org.geonetwork.trivial.SimpleRecordsMvcTests.expectedFacets;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.geonetwork.infrastructure.ElasticPgMvcBaseTest;
import org.geonetwork.ogcapi.records.generated.model.*;
import org.geonetwork.trivial.SimpleRecordsMvcTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * This is a combined test of the facet's and CQL. First, we use the expected bucket for several facets (cf
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
@ContextConfiguration(initializers = CqlTest.class)
public class CqlTest extends ElasticPgMvcBaseTest {

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
            SimpleRecordsMvcTests.BucketInfo bucket,
            SimpleRecordsMvcTests.FacetInfo facetValue,
            OgcApiRecordsFacetDto facetConfig)
            throws Exception {
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
    public String makeCql(
            SimpleRecordsMvcTests.BucketInfo bucketInfo,
            SimpleRecordsMvcTests.FacetInfo facetInfo,
            OgcApiRecordsFacetDto facetConfig) {
        if (facetInfo.getType().equals("term")) {
            var termInfo = (OgcApiRecordsFacetTermsDto) facetConfig;
            return termInfo.getProperty() + " = '" + bucketInfo.getValue() + "'";
        } else if (facetInfo.getType().equals("histogram")) {
            var histogramInfo = (OgcApiRecordsFacetHistogramDto) facetConfig;
            if (bucketInfo.getHighestBucket() != null && bucketInfo.getHighestBucket()) {
                return histogramInfo.getProperty() + " >= '" + bucketInfo.getMin() + "' AND "
                        + histogramInfo.getProperty() + " <= '" + bucketInfo.getMax() + "'";
            }
            return histogramInfo.getProperty() + " >= '" + bucketInfo.getMin() + "' AND " + histogramInfo.getProperty()
                    + " < '" + bucketInfo.getMax() + "'";
        } else if (facetInfo.getType().equals("filter")) {
            var filterInfo = (OgcApiRecordsFacetFilterDto) facetConfig;
            return filterInfo.getFilters().get(bucketInfo.getValue());
        }
        throw new RuntimeException(facetInfo.getType() + " is not supported");
    }
}
