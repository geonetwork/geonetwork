/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.util;

import org.springframework.web.context.request.NativeWebRequest;

public class RequestUtil {

    public static RequestInfo getInfo(NativeWebRequest nativeWebRequest) {
        return new RequestInfo(nativeWebRequest);
    }
}
