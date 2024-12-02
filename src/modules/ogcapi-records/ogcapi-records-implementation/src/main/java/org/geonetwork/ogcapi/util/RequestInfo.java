/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.HandlerMapping;

public class RequestInfo {

    NativeWebRequest request;
    Map<String, String[]> parameterMap;
    HttpServletRequest servletRequest;
    HttpServletResponse servletResponse;
    String language;
    List<MediaType> produceableMediaTypes;

    public RequestInfo(NativeWebRequest request) {
        this.request = request;
        servletRequest = request.getNativeRequest(HttpServletRequest.class);
        servletResponse = request.getNativeResponse(HttpServletResponse.class);

        Locale locale = LocaleContextHolder.getLocale();
        language = locale.getISO3Language();

        parameterMap = request.getParameterMap();

        var producables = (Set<MediaType>) servletRequest.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
        produceableMediaTypes = new ArrayList<>(producables);
    }
}
