/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.links;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.geonetwork.utility.MediaTypeAndProfile;
import org.geonetwork.utility.MediaTypeAndProfileBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * basic ops for links.
 *
 * <p>1. This document links Links should point to the various representations of this document ("alternative") and the
 * exact link to this document ("self").
 */
@Component
public class BasicLinks {

    @Autowired
    ContentNegotiationManager contentNegotiationManager;

    @Autowired
    MediaTypeAndProfileBuilder mediaTypeAndProfileBuilder;

    public void addStandardLinks(
            NativeWebRequest nativeWebRequest,
            String baseUrl,
            String endpointLoc,
            Object page,
            List<String> formatNames,
            String selfName,
            String altName) {
        var mediaTypeAndName = mediaTypeAndProfileBuilder.build(nativeWebRequest);
        var formats = formatNames.stream()
                .map(x -> new MediaTypeAndProfile(
                        contentNegotiationManager.getMediaTypeMappings().get(x), null))
                .toList();
        addStandardLinks(mediaTypeAndName, baseUrl, endpointLoc, page, formats, selfName, altName);
    }
    /**
     * this adds the "standard" links - which is to "self" and "alternative" versions of the landing page.
     *
     * @param mediaTypeAndProfile request's media type and profile info
     * @param baseUrl baseURL for the system
     * @param endpointLoc which endpoint is this for
     * @param page where to attach the links?
     * @param formatNames what formats should we use
     */
    public void addStandardLinks(
            MediaTypeAndProfile mediaTypeAndProfile,
            String baseUrl,
            String endpointLoc,
            Object page,
            List<MediaTypeAndProfile> formatNames,
            String selfName,
            String altName) {
        var links =
                this.createSelfRelativeLinks(mediaTypeAndProfile, baseUrl, endpointLoc, formatNames, selfName, altName);

        // the objects are auto-generated from yaml, so we have to use reflection
        addLinksReflect(links, page);
    }

    public void addLinksReflect(List<OgcApiRecordsLinkDto> links, Object page) {
        // the objects are auto-generated from yaml, so we have to use reflection
        try {
            var method = page.getClass().getMethod("addLinksItem", OgcApiRecordsLinkDto.class);
            links.forEach(x -> {
                try {
                    method.invoke(page, x);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public List<OgcApiRecordsLinkDto> createSelfRelativeLinks(
            MediaTypeAndProfile mediaTypeAndProfile,
            String baseUrl,
            String endpointLocation,
            List<MediaTypeAndProfile> mediaNames,
            String selfName,
            String altName) {
        var url = baseUrl + endpointLocation;
        if (!url.contains("?")) {
            url += "?";
        } else {
            url += "&";
        }

        var result = new ArrayList<OgcApiRecordsLinkDto>();

        var requestProfile = (mediaTypeAndProfile.getProfile() == null || mediaTypeAndProfile.getProfile().isEmpty())
                ? null : mediaTypeAndProfile.getProfile().getFirst();
        var requestMediaType = mediaTypeAndProfile.getMediaType();
        String finalUrl = url;

        for (var _mediaTypeAndProfile : mediaNames) {
            var profiles = _mediaTypeAndProfile.getProfile();
            if (profiles == null || profiles.isEmpty()) {
                // null profiles -> keep going
                profiles = new ArrayList<>();
                profiles.add(null);
            }
            for (var _profile : profiles) {
                try {
                    if (_mediaTypeAndProfile.getMediaType().equals(requestMediaType) && _profile != null && _profile.equals(requestProfile)) {
                        var link = new OgcApiRecordsLinkDto();
                        var href =new URI(finalUrl + "f=" + URLEncoder.encode(_mediaTypeAndProfile.getMediaType().toString(), StandardCharsets.UTF_8));
                        if (_profile != null) {
                            href =new URI(href.toString() + "&profile=" + URLEncoder.encode(_profile, StandardCharsets.UTF_8));
                        }
                        link.rel(selfName)
                                .type(_mediaTypeAndProfile.getMediaType().toString())
                                .hreflang("eng")
                                .href(href);
                        if (_profile != null) {
                             link.profile(List.of(_profile));
                        }
                        result.add(link);
                    } else {
                        var link = new OgcApiRecordsLinkDto();
                        var href =new URI(finalUrl + "f=" + URLEncoder.encode(_mediaTypeAndProfile.getMediaType().toString(), StandardCharsets.UTF_8));
                        if (_profile != null) {
                            href =new URI(href.toString() + "&profile=" + URLEncoder.encode(_profile, StandardCharsets.UTF_8));
                        }
                        link.rel(altName)
                                .type(_mediaTypeAndProfile.getMediaType().toString())
                                .hreflang("eng")
                                .href(href);
                        if (_profile != null) {
                            link.profile(List.of(_profile));
                        }
                        result.add(link);
                    }
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }

        return result;

//        var links = mediaNames.stream()
//                .map(x -> {
//
//                    if (x.getMediaType().equals(mediaTypeAndProfile.getMediaType())) {
//                        // self
//                        var _links = new ArrayList<OgcApiRecordsLinkDto>();
//                        var link = new OgcApiRecordsLinkDto();
//                        try {
//                            link.rel(selfName)
//                                    .type(mediaTypeAndProfile.getMediaType().toString())
//                                    .hreflang("eng")
//                                    .profile(x.getProfile())
//                                    .href(new URI(
//                                            finalUrl + "f=" + x.getMediaType().toString()));
//                            _links.add(link);
//
//                        } catch (URISyntaxException e) {
//                            throw new RuntimeException(e);
//                        }
//                        return _links;
//                    } else {
//                        // alternative
//                        var link = new OgcApiRecordsLinkDto();
//                        try {
//                            link.rel(altName)
//                                    .type(x.getMediaType().toString())
//                                    .hreflang("eng")
//                                    .profile(x.getProfile())
//                                    .href(new URI(
//                                            finalUrl + "f=" + x.getMediaType().toString()));
//                            return link;
//                        } catch (URISyntaxException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                })
//                .toList();
//
//        return links;
    }
}
