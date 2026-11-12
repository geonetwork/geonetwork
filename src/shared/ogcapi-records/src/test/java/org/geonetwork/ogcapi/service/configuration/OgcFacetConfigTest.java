/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class OgcFacetConfigTest {

    @Test
    public void copyKeepsAllProperties() {
        // every value differs from the field defaults, so a property that is not copied fails the test
        var original = new OgcFacetConfig();
        original.setFacetName("year");
        original.setFacetType(FacetType.HISTOGRAM_FIXED_INTERVAL);
        original.setBucketSorting(BucketSorting.VALUE);
        original.setBucketSortingDirection(BucketSortingDirection.ASCENDING);
        original.setBucketCount(7);
        original.setMinimumDocumentCount(0);
        original.setNumberBucketInterval(5.0);
        original.setCalendarIntervalUnit(CalendarIntervalUnit.year);
        original.setFilters(new ArrayList<>(List.of(new FilterFacetInfo("wms", "linkProtocol = 'OGC:WMS'"))));

        var copy = new OgcFacetConfig(original);

        assertEquals("year", copy.getFacetName());
        assertEquals(FacetType.HISTOGRAM_FIXED_INTERVAL, copy.getFacetType());
        assertEquals(BucketSorting.VALUE, copy.getBucketSorting());
        assertEquals(BucketSortingDirection.ASCENDING, copy.getBucketSortingDirection());
        assertEquals(7, copy.getBucketCount());
        assertEquals(0, copy.getMinimumDocumentCount());
        assertEquals(5.0, copy.getNumberBucketInterval());
        assertEquals(CalendarIntervalUnit.year, copy.getCalendarIntervalUnit());

        // filters are deep-copied: same values, new list and new objects
        assertEquals(1, copy.getFilters().size());
        assertNotSame(original.getFilters(), copy.getFilters());
        assertNotSame(original.getFilters().get(0), copy.getFilters().get(0));
        assertEquals("wms", copy.getFilters().get(0).getFilterName());
        assertEquals("linkProtocol = 'OGC:WMS'", copy.getFilters().get(0).getFilterEquationCql());
    }
}
