package org.geonetwork.ogcapi.util;

import org.springframework.web.context.request.NativeWebRequest;

public class RequestUtil {

    public static RequestInfo getInfo(NativeWebRequest nativeWebRequest) {
        return new RequestInfo(nativeWebRequest);
    }
}
