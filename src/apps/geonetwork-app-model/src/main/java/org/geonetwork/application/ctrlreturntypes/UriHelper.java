/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.application.ctrlreturntypes;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.web.util.UriUtils;

public class UriHelper {

    public static URI createUri(String baseUrl, String endpoint, Map<String, String> params) {
        String url = baseUrl + UriUtils.encodePath(endpoint, StandardCharsets.UTF_8);
        if (params != null && !params.isEmpty()) {
            url += "?";
            for (Map.Entry<String, String> param : params.entrySet()) {
                url += param.getKey() + "=" + UriUtils.encodePath(param.getValue(), StandardCharsets.UTF_8) + "&";
            }
        }
        url = url.substring(0, url.length() - 1);
        return URI.create(url);
    }
}
