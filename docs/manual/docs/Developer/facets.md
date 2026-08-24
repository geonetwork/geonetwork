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


## Advanced Facets

GeoNetwork also supports "Advanced Facets" from the [OGCAPI-Records Part 2 - facets specification](https://docs.ogc.org/DRAFTS/25-013.html).

### Which Facets In Response

"Advanced Facets" allow you to turn on/off facets in response.

| URL | Meaning |
| ------- | ------- |
| don't mention `&facets=` | Normal operation - show all pre-defined facets |
| `&facets=" | Don't include any facets |
| `@facets=facetName1,facetName2` | Just include `facetName1` and `facetName2` in the response |

### Controlling Facet Configuration

You have some partial control of the facet definition.  Instead of just mentioning `&facets=facetName`, you have two options:

`&facets=facetName:<number of buckets>:<sort>`

| Item | Meaning |
| ------- | ------- |
| `facetName` | name of the facet |
| `<number of buckets>` | integer for controlling the number of buckets.  Defaults to pre-configured (default) number |
| `<sort>` | on of `value_desc`, `value_asc`, `count_desc`, `count_asc` to control the order of the buckets.  Defaults to the to pre-configured (default) sort |

Examples:

| Item | Meaning |
| ------- | ------- |
| `facetName`  | show "facetName" with the pre-configured (default) number of buckets and sort |
| `facetName:12` |  show "facetName" with 12 buckets and pre-configured (default) sort |
| `facetName::value_desc` | show facetName" with the pre-configured (default) number of buckets and sort by value (descending) |
| `facetName:12:value_desc `| show "facetName" with 12 buckets and sort by value (descending) |

Bucket sorting:

| Item | Meaning |
| ------- | ------- |
| `value_desc` | sort the buckets by value (highest value first), then by count (number of documents in the buckets) (highest value first)  |
| `value_asc` | sort the buckets by value (lowest value first), then by count (lowest value first)  |
| `count_desc` | sort the buckets by count (highest value first), then by value (highest value first)  |
| `count_asc` |sort the buckets by count (lowest value first), then by value (lowest value first)  |

"Value" is the minimum value for the bucket.

## Implementation Notes

### Term Facets

These are well-supported.  Common use case is to sort `count_desc`, which will give you the most popular "terms" for that facet.

### Filter Facets

Filter Facets are a pre-defined set of queries (one bucket for each query).  Because of this, it doesn't usually make a lot of sense to change the number of buckets or sorting for these type of facets.  

### Histogram: Fixed Interval

Fixed Interval Histograms are defined by a known interval:

* For Numbers, this would typically be something like "each bucket is 5 units wide".  For example, buckets "0-5", "5-10", "10-15", ...
* For Dates, this would typically be something like "each bucket is one month wide".  For example, buckets "January 2026", "February 2026", etc...   There are only some intervals defined in elastic - see [Elastic Calendar Intervals](https://www.elastic.co/docs/reference/aggregations/search-aggregations-bucket-datehistogram-aggregation)

Changing the sort & number of buckets for these type of histograms will just limit the buckets returned - which might not be what you expect.

 Requesting less buckets will just removed buckets from the "pre-defined facet definition" (default behavior) since all the buckets have a pre-defined interval (and each record will always be in the same bucket no matter what the advanced facet's number-of-buckets or sort is defined to be).  

An example of this would be a "each bucket is one month wide" - each record will be in a particular "month bucket".  Using the number-of-bucket/sort just controls which of those "month buckets" are returned in the response.  Using sort `count_desc` will give you the months with the most number records in the bucket.  Using `value_asc` will give you the first buckets (i.e "January 2026", "February 2026", ...).

### Histogram: Fixed Bucket Count

In Elastic, when you use the "Fixed Bucket Count" facet (implemented as Elastic's "`auto_date_histogram`") Elastic will do some statistical analysis of the data and choose an appropriate interval - and each bucket will have the same "width."  The chosen "appropriate" interval may not be quite what you expect.

If you change the requested number of buckets, Elastic will choose a different appropriate interval.  This, again, might not be what you expect.

This takes some experimentation.