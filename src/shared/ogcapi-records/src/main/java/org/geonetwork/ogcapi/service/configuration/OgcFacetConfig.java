/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.configuration;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OgcFacetConfig {

    public String facetName;
    public FacetType facetType;
    public BucketSorting bucketSorting = BucketSorting.COUNT;

    /** needed for FacetType.HISTOGRAM_FIXED_BUCKET_COUNT. For others, this will delete lower-priority buckets */
    public Integer bucketCount;

    /** buckets with <minimumDocumentCount will be removed. Usually 0 (dont remove) or 1 (remove empty buckets) */
    public int minimumDocumentCount = 1;

    /** For HISTOGRAM_FIXED_INTERVAL with a Number datatype */
    public Double numberBucketInterval;

    /** For HISTOGRAM_FIXED_INTERVAL with a Date datatype */
    public CalendarIntervalUnit calendarIntervalUnit;

    /** only valid for FILTER facets */
    public List<FilterFacetInfo> filters;

    /** often null - you might have to look at the parent */
    public OgcElasticFieldMapperConfig field;
}
