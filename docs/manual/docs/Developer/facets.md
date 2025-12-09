# Facets in OGCAPI-Records

Facets are defined in the the [OGCAPI-Records Part 2 - facets specification](https://docs.ogc.org/DRAFTS/25-013.html).

This specification is a work-in-progress and the implementation is based on the written specification as well as question/answers on the OGCAPI-Records GitHub issues.

The defined buckets are converted to Elastic Aggregates - with special handling for Date, Number, and String types (cf. [Dynamic Typing](ogcapi-records-dynamic-properties.md)) and Fixed Width histograms vs Fixed Number-of-Buckets histograms. 

## Overview

The basic Facet process is as follows:

1. The [user configuration](ogcapi-records-dynamic-properties.md) contains a set of dynamic properties (`OgcElasticFieldMapperConfig`).  Each property can defines multiple facets (`OgcFacetConfig`).

2. As in the [OGCAPI-Records Part 2 - facets specification](https://docs.ogc.org/DRAFTS/25-013.html), there are 3 types of histograms (with 2 types of Histogram facets):

    * TERM - for text (keyword) fields in Elastic (ie. the equivalent of java `enum`)
    * FILTER - for arbitrary (defined by CQL) filters
    * HISTOGRAM_FIXED_BUCKET_COUNT - histogram with a fixed number of buckets (for numbers and dates)
    * HISTOGRAM_FIXED_INTERVAL - histogram with a fixed width of the buckets (for numbers and dates)

3. You can also specify other information about the facet:

    * `facetName` - name of the facet (used to name the response facet property name)
    * `facetType` - as defined above
    * `bucketSorting` - either sort by COUNT (i.e. bucket with most records) or sort by VALUE (i.e. date order, numeric order, or string order)
    * `bucketCount` - how many buckets should there be (overrides the default)
    * `minimumDocumentCount` - buckets with less than this number will be removed.  Typically set to 1 (remove empty buckets).
    * `numberBucketInterval` for Number Histograms, the bucket interval (width)
    * `calendarIntervalUnit` for Date Histograms, the bucket interval (width).  Supports  `year`, `month`, `week`, `day`, `hour`, `minute`, `second`, and `quarter` (see [Elastic Documentation](https://www.elastic.co/docs/reference/aggregations/search-aggregations-bucket-datehistogram-aggregation))
    * `field` for information about the field configuration (from user) - this defines where in the data is in the Elastic Index 

4. An Elastic Aggregation query is added to the main (`/items`) query that will retrieve all the results.  These are then converted to the OGCAPI-Records (Part 2) defined format.

## Getting Actual Records from a Bucket

Typically, its fairly easy to query for the Records in a bucket with a CQL expression.

Please see `QueryTest#testBucketCql()` for lots of examples.  Also, the Angular OGCAPI-Records application also does this.

!!! warning "Watch for final bucket"
    In general, to get the contents of a bucket, make a request like:
    
     *property* >= min_value AND *property* < max_value

     However, for the last bucket, your CQL should be (notice the `<=` instead of `<`):

      *property* >= min_value AND *property* <= max_value

     This is marked in the facet responses to easily see which bucket is the last one (`x-highest-bucket: true`).

