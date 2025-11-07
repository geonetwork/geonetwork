/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.configuration;

import io.swagger.v3.oas.models.media.*;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonPropertyDto;

public enum SimpleType {
    DOUBLE,
    INTEGER,
    STRING,
    DATE,
    BOOLEAN;

    public static String getOgcTypeFormat(SimpleType type) {
        if (type == DATE) {
            return "date";
        }
        return null;
    }

    public static String getOgcTypeName(SimpleType type) {
        if (type == SimpleType.INTEGER || type == SimpleType.DOUBLE) {
            return "number";
        } else if (type == SimpleType.STRING) {
            return "string";
        } else if (type == SimpleType.DATE) {
            return "string";
        } else if (type == SimpleType.BOOLEAN) {
            return "boolean";
        } else {
            throw new RuntimeException("don't know type - " + type);
        }
    }

    public static void fillInOgc(SimpleType type, OgcApiRecordsJsonPropertyDto p) {
        if (type == SimpleType.INTEGER || type == SimpleType.DOUBLE) {
            p.setType("number");
        } else if (type == SimpleType.STRING) {
            p.setType("string");
        } else if (type == SimpleType.DATE) {
            p.setType("string");
            p.setFormat("date");
        } else if (type == SimpleType.BOOLEAN) {
            p.setType("boolean");
        } else {
            throw new RuntimeException("don't know type - " + type);
        }
    }
}
