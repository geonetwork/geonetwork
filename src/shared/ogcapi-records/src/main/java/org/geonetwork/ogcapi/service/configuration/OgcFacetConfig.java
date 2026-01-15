/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.configuration;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** user config for a facet */
@Getter
@Setter
public class OgcFacetConfig {

    /** name of the facet (shown in the record result) */
    public String facetName;

    /** What type of facet is this (i.e. TERM, HISTOGRAM*, FILTER) */
    public FacetType facetType;

    /** how should buckets be sorted in the ogc api results? */
    public BucketSorting bucketSorting = BucketSorting.COUNT;

    /** needed for FacetType.HISTOGRAM_FIXED_BUCKET_COUNT. For others, this will delete lower-priority buckets */
    public Integer bucketCount;

    /**
     * buckets with <minimumDocumentCount will be removed. Usually 0 (dont remove any buckets) or 1 (remove empty
     * buckets). Typically, you don't want to show empty buckets.
     */
    public int minimumDocumentCount = 1;

    /** For HISTOGRAM_FIXED_INTERVAL with a Number datatype - interval size (number) */
    public Double numberBucketInterval;

    /** For HISTOGRAM_FIXED_INTERVAL with a Date datatype - interval size (date range) */
    public CalendarIntervalUnit calendarIntervalUnit;

    /** only valid for FILTER facets - these are the individual filters. */
    public List<FilterFacetInfo> filters;

    /** To look at the parent object (might be null) */
    public OgcElasticFieldMapperConfig field;
}
