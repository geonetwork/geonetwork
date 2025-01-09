/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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

    public void setFormats(List<String> newFormats) {
        //        formats = newFormats;
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
