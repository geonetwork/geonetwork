/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.facets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetResultBucketDto;
import org.geonetwork.ogcapi.service.configuration.BucketSortingDirection;
import org.junit.jupiter.api.Test;

/** very simple tests for the bucket sortings - the integration tests are more detailed. */
public class BucketComparatorTest {

    // simple longs (date)  Date bucket will have value as long (epoc time).
    @Test
    public void testCompareSimpleDate() {

        var buckets = createBuckets(
                new ArrayList<>(List.of(1, 2, 3)), // count
                new ArrayList<>(List.of("1743465600000", "1706745600000", "1646092800000")) // vals
                );

        var comparator = BucketComparator.createValueComparator(
                FacetsResponseInjector.SortType.DATE, BucketSortingDirection.ASCENDING);

        buckets.sort(comparator);

        assertEquals(1743465600000L, Long.parseLong(buckets.get(2).getValue()));
        assertEquals(1706745600000L, Long.parseLong(buckets.get(1).getValue()));
        assertEquals(1646092800000L, Long.parseLong(buckets.get(0).getValue()));

        comparator = BucketComparator.createValueComparator(
                FacetsResponseInjector.SortType.DATE, BucketSortingDirection.DESCENDING);

        buckets.sort(comparator);

        assertEquals(1743465600000L, Long.parseLong(buckets.get(0).getValue()));
        assertEquals(1706745600000L, Long.parseLong(buckets.get(1).getValue()));
        assertEquals(1646092800000L, Long.parseLong(buckets.get(2).getValue()));
    }

    // simple doubles
    @Test
    public void testCompareDouble() {

        var buckets = createBuckets(
                new ArrayList<>(List.of(1, 2, 3)), // count
                new ArrayList<>(List.of("11.0", "2.0", "3.0")) // vals
                );

        var comparator = BucketComparator.createValueComparator(
                FacetsResponseInjector.SortType.NUMBER, BucketSortingDirection.ASCENDING);

        buckets.sort(comparator);

        assertEquals(2, Double.parseDouble(buckets.get(0).getValue()));
        assertEquals(3, Double.parseDouble(buckets.get(1).getValue()));
        assertEquals(11, Double.parseDouble(buckets.get(2).getValue()));

        comparator = BucketComparator.createValueComparator(
                FacetsResponseInjector.SortType.NUMBER, BucketSortingDirection.DESCENDING);

        buckets.sort(comparator);
        assertEquals(11, Double.parseDouble(buckets.get(0).getValue()));
        assertEquals(3, Double.parseDouble(buckets.get(1).getValue()));
        assertEquals(2, Double.parseDouble(buckets.get(2).getValue()));
    }

    // simple strings
    @Test
    public void testCompareString() {

        var buckets = createBuckets(
                new ArrayList<>(List.of(1, 2, 3)), // count
                new ArrayList<>(List.of("aa", "ab", "abb")) // vals
                );

        var comparator = BucketComparator.createValueComparator(
                FacetsResponseInjector.SortType.STRING, BucketSortingDirection.ASCENDING);

        buckets.sort(comparator);

        assertEquals("aa", buckets.get(0).getValue());
        assertEquals("ab", buckets.get(1).getValue());
        assertEquals("abb", buckets.get(2).getValue());

        comparator = BucketComparator.createValueComparator(
                FacetsResponseInjector.SortType.STRING, BucketSortingDirection.DESCENDING);

        buckets.sort(comparator);

        assertEquals("abb", buckets.get(0).getValue());
        assertEquals("ab", buckets.get(1).getValue());
        assertEquals("aa", buckets.get(2).getValue());
    }

    // simple doubles with nulls. Nulls should be sorted last regardless of direction.
    @Test
    public void testCompareNumberNulls() {
        var buckets = createBuckets(
                new ArrayList<>(List.of(1, 2, 3)), // count
                new ArrayList<>(Arrays.asList("11.0", null, "3.0")) // vals
                );

        var comparator = BucketComparator.createValueComparator(
                FacetsResponseInjector.SortType.NUMBER, BucketSortingDirection.ASCENDING);

        buckets.sort(comparator);

        assertEquals(3, Double.parseDouble(buckets.get(0).getValue()));
        assertEquals(11, Double.parseDouble(buckets.get(1).getValue()));
        assertNull(buckets.get(2).getValue());

        comparator = BucketComparator.createValueComparator(
                FacetsResponseInjector.SortType.NUMBER, BucketSortingDirection.DESCENDING);

        buckets.sort(comparator);
        assertEquals(11, Double.parseDouble(buckets.get(0).getValue()));
        assertEquals(3, Double.parseDouble(buckets.get(1).getValue()));
        assertNull(buckets.get(2).getValue());
    }

    // simple doubles with errors.
    @Test
    public void testCompareNumberError() {
        var buckets = createBuckets(
                new ArrayList<>(List.of(1, 2, 3)), // count
                new ArrayList<>(Arrays.asList("11.0", "abc", "3.0")) // vals
                );

        var comparator = BucketComparator.createValueComparator(
                FacetsResponseInjector.SortType.NUMBER, BucketSortingDirection.ASCENDING);

        buckets.sort(comparator);

        assertEquals(3, Double.parseDouble(buckets.get(0).getValue()));
        assertEquals(11, Double.parseDouble(buckets.get(1).getValue()));
        assertEquals("abc", buckets.get(2).getValue());

        comparator = BucketComparator.createValueComparator(
                FacetsResponseInjector.SortType.NUMBER, BucketSortingDirection.DESCENDING);

        buckets.sort(comparator);
        assertEquals(11, Double.parseDouble(buckets.get(0).getValue()));
        assertEquals(3, Double.parseDouble(buckets.get(1).getValue()));
        assertEquals("abc", buckets.get(2).getValue());
    }

    public List<OgcApiRecordsFacetResultBucketDto> createBuckets(List<Integer> counts, List<String> values) {
        var result = new ArrayList<OgcApiRecordsFacetResultBucketDto>();
        for (int i = 0; i < counts.size(); i++) {
            var item = new OgcApiRecordsFacetResultBucketDto();
            item.setCount(counts.get(i));
            item.setValue(values.get(i));
            result.add(item);
        }
        return result;
    }
}
