/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** user config for a facet */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OgcFacetConfig {

    /** name of the facet (shown in the record result) */
    public String facetName;

    /** What type of facet is this (i.e. TERM, HISTOGRAM*, FILTER) */
    public FacetType facetType;

    /** how should buckets be sorted in the ogc api results? */
    public BucketSorting bucketSorting = BucketSorting.COUNT;

    public BucketSortingDirection bucketSortingDirection = BucketSortingDirection.DESCENDING;

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

    /**
     * copy constructor
     *
     * @param other copy from here
     */
    public OgcFacetConfig(OgcFacetConfig other) {
        if (other == null) {
            return;
        }

        this.facetName = other.facetName;
        this.facetType = other.facetType;
        this.bucketSorting = other.bucketSorting;
        this.bucketCount = other.bucketCount;
        this.minimumDocumentCount = other.minimumDocumentCount;
        this.numberBucketInterval = other.numberBucketInterval;
        this.calendarIntervalUnit = other.calendarIntervalUnit;

        // Deep copy the List (assuming FilterFacetInfo also has a copy constructor)
        if (other.filters != null) {
            this.filters = other.filters.stream()
                    .map(f -> f == null ? null : new FilterFacetInfo(f))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }
}
