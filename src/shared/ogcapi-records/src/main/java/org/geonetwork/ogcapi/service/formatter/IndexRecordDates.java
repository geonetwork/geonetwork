/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.formatter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/** Date parsing for the index record dates (changeDate, createDate, ...). */
final class IndexRecordDates {

    private IndexRecordDates() {}

    /** first date that can be parsed, null otherwise. */
    static Instant firstParseable(String... candidates) {
        if (candidates == null) {
            return null;
        }
        for (var candidate : candidates) {
            var parsed = parseInstant(candidate);
            if (parsed != null) {
                return parsed;
            }
        }
        return null;
    }

    static Instant parseInstant(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Instant.parse(value);
        } catch (Exception e) {
            // Continue with other supported date shapes.
        }
        try {
            return OffsetDateTime.parse(value).toInstant();
        } catch (Exception e) {
            // Continue with other supported date shapes.
        }
        try {
            return ZonedDateTime.parse(value).toInstant();
        } catch (Exception e) {
            // Continue with other supported date shapes.
        }
        try {
            return LocalDateTime.parse(value).toInstant(ZoneOffset.UTC);
        } catch (Exception e) {
            // Continue with other supported date shapes.
        }
        try {
            return LocalDate.parse(value).atStartOfDay().toInstant(ZoneOffset.UTC);
        } catch (Exception e) {
            return null;
        }
    }
}
