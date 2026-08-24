/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.facets;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetResultBucketDto;
import org.geonetwork.ogcapi.service.configuration.BucketSortingDirection;

/** for sorting buckets (advanced facets) */
public class BucketComparator {

    // how to compare two strings!
    public static Collator stringCollator = Collator.getInstance(Locale.ENGLISH);

    static {
        stringCollator.setStrength(Collator.SECONDARY);
    }

    /**
     * values comparator that's aware of STRING, NUMBER, and DATE types. Null/parsable safe
     *
     * @param sortType data type
     * @param direction sort direction (asc/desc)
     */
    public static Comparator<OgcApiRecordsFacetResultBucketDto> createValueComparator(
            FacetsResponseInjector.SortType sortType, BucketSortingDirection direction) {

        Comparator<OgcApiRecordsFacetResultBucketDto> baseComparator = (a, b) -> {
            try {
                String valA = a.getValue();
                String valB = b.getValue();

                switch (sortType) {
                    case NUMBER:
                        Double d1 = safeDoubleParser(valA);
                        Double d2 = safeDoubleParser(valB);
                        if (d1 == null && d2 == null) {
                            return 0;
                        }
                        if (d1 == null) {
                            return 1;
                        }
                        if (d2 == null) {
                            return -1;
                        }
                        var result = Double.compare(d1, d2);
                        return direction == BucketSortingDirection.ASCENDING ? result : -result;
                    case DATE:
                        // value will be a string that's an epoc time
                        Long l1 = safeLongParser(valA);
                        Long l2 = safeLongParser(valB);
                        if (l1 == null && l2 == null) {
                            return 0;
                        }
                        if (l1 == null) {
                            return 1;
                        }
                        if (l2 == null) {
                            return -1;
                        }
                        result = Long.compare(l1, l2);
                        return direction == BucketSortingDirection.ASCENDING ? result : -result;
                    case STRING:
                        if (valA == null && valB == null) {
                            return 0;
                        }
                        if (valA == null) {
                            return 1;
                        }
                        if (valB == null) {
                            return -1;
                        }
                        result = stringCollator.compare(valA, valB);
                        return direction == BucketSortingDirection.ASCENDING ? result : -result;
                    default:
                        throw new Exception("Unsupported sort type for value comparison: " + sortType);
                }
            } catch (Exception e) {
                // Returning 0 here because nullsLast will handle the nulls created by the 'null-safe' wrapper
                // Actually, for errors to go to the end, we need the comparison to treat 'bad' data as null.
                return 0;
            }
        };

        return Comparator.nullsLast(baseComparator);
    }

    /**
     * parse a double - return null on error
     *
     * @param d string to be parsed
     */
    public static Double safeDoubleParser(String d) {
        try {
            return Double.parseDouble(d);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * parse a long - null if error
     *
     * @param l string value
     */
    public static Long safeLongParser(String l) {
        try {
            return Long.parseLong(l);
        } catch (Exception e) {
            return null;
        }
    }
}
