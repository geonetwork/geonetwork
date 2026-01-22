/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeType;
import org.springframework.web.accept.ContentNegotiationManager;

/** parent class for the endpoint links configuration. */
public class BasicLinksConfiguration {

    @Autowired
    ContentNegotiationManager contentNegotiationManager;

    private Map<String, MimeType> mimeFormats = new HashMap<>();

    public Map<String, MimeType> getMimeFormats() {
        return mimeFormats == null ? null : new HashMap<>(mimeFormats);
    }

    /**
     * used by application.yaml to populate the formats (format names) for the links configuration.
     *
     * @param newFormats list of format names
     */
    public void setFormats(List<String> newFormats) {
        mimeFormats = new HashMap<>();
        if (newFormats != null && !newFormats.isEmpty()) {
            for (String format : newFormats) {
                var mediaType = contentNegotiationManager.getMediaTypeMappings().get(format);
                if (mediaType != null) {
                    mimeFormats.put(format, mediaType);
                }
            }
        }
    }
}
