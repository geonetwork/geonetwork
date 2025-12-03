/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
