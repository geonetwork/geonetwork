/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.ctrlreturntypes;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class OgcApiRecordsMultiRecordResponseJsonFormatter extends OgcApiRecordsMultiRecordResponseGeoJsonFormatter {

    @Override
    public MediaType getMimeType() {
        return MediaType.parseMediaType("application/json");
    }

    @Override
    public String getMimeTypeQuickName() {
        return "json";
    }
}
