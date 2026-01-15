/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.configuration;

public enum FacetType {
    TERM,
    HISTOGRAM_FIXED_BUCKET_COUNT,
    HISTOGRAM_FIXED_INTERVAL,
    FILTER;

    public static String ogcString(FacetType type) {
        if (type == FacetType.TERM) {
            return "term";
        } else if (type == FacetType.HISTOGRAM_FIXED_BUCKET_COUNT) {
            return "histogram";
        } else if (type == FacetType.HISTOGRAM_FIXED_INTERVAL) {
            return "histogram";
        } else if (type == FacetType.FILTER) {
            return "filter";
        } else {
            throw new RuntimeException("Unknown FacetType: " + type);
        }
    }
}
