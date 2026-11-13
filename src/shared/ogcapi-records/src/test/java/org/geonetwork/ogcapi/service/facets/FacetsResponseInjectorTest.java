/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.facets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetResultBucketDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetSummaryDto;
import org.geonetwork.ogcapi.service.configuration.BucketSorting;
import org.geonetwork.ogcapi.service.configuration.BucketSortingDirection;
import org.junit.jupiter.api.Test;

public class FacetsResponseInjectorTest {

    @Test
    public void minimumDocumentCountZeroKeepsEmptyBuckets() {
        assertEquals(List.of("2000=1", "2001=0", "2002=2"), handleBuckets(0));
    }

    @Test
    public void minimumDocumentCountOneDropsEmptyBuckets() {
        assertEquals(List.of("2000=1", "2002=2"), handleBuckets(1));
    }

    private List<String> handleBuckets(int minOccurs) {
        var result = new OgcApiRecordsFacetSummaryDto();
        result.setBuckets(new ArrayList<>(List.of(bucket("2000", 1), bucket("2001", 0), bucket("2002", 2))));

        new FacetsResponseInjector()
                .handleBuckets(
                        result,
                        20,
                        minOccurs,
                        BucketSorting.VALUE,
                        FacetsResponseInjector.SortType.NUMBER,
                        false,
                        null,
                        BucketSortingDirection.ASCENDING);

        return result.getBuckets().stream()
                .map(b -> b.getValue() + "=" + b.getCount())
                .toList();
    }

    private static OgcApiRecordsFacetResultBucketDto bucket(String value, int count) {
        var b = new OgcApiRecordsFacetResultBucketDto();
        b.setValue(value);
        b.setCount(count);
        return b;
    }
}
