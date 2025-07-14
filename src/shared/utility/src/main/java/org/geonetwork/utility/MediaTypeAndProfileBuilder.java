/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.utility;

import java.util.Arrays;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;

@Component
public class MediaTypeAndProfileBuilder {

    final ContentNegotiationManager contentNegotiationManager;

    public MediaTypeAndProfileBuilder(ContentNegotiationManager contentNegotiationManager) {
        this.contentNegotiationManager = contentNegotiationManager;
    }

    /**
     * given a request, find (using the contentNegotiationManager) the resulting mediatype this request will create.
     *
     * @param nativeWebRequest current request
     * @return which media type it will be producing
     */
    @SneakyThrows
    public MediaType getRequestMediaType(NativeWebRequest nativeWebRequest) {
        var types = contentNegotiationManager.resolveMediaTypes(nativeWebRequest);
        if (types.isEmpty()) {
            return MediaType.TEXT_HTML;
        }
        return types.getFirst();
    }

    /**
     * Given a request, find the name ("json") and mediatype ("application/json") that this request will result in. use
     * the contentNegotiationManager to do this work.
     *
     * @param nativeWebRequest from user
     * @return the name and mediatype
     */
    public MediaTypeAndProfile build(NativeWebRequest nativeWebRequest) {
        var mediaType = getRequestMediaType(nativeWebRequest);
        if (mediaType == null) {
            return null;
        }

        var _profile = nativeWebRequest.getParameterMap().get("profile");
        if (_profile == null || _profile.length == 0) {
            return new MediaTypeAndProfile(mediaType, null);
        }
        if (_profile.length == 1) {
            var _profileName = _profile[0].trim();
            if (_profileName.contains(",")) {
                var profiles = Arrays.asList(StringUtils.split(_profileName, ","));
                profiles = profiles.stream().map(x -> x.trim()).toList();
                return new MediaTypeAndProfile(mediaType, profiles);
            }

            return new MediaTypeAndProfile(mediaType, Arrays.asList(_profileName));
        }
        return new MediaTypeAndProfile(mediaType, Arrays.asList(_profile));
    }

    public MediaTypeAndProfile build(MediaType mediaType, String simpleProfile) {

        if (mediaType == null) {
            return null;
        }

        if (simpleProfile == null || simpleProfile.isEmpty()) {
            return new MediaTypeAndProfile(mediaType, null);
        }

        var profiles = Arrays.asList(StringUtils.split(simpleProfile, ","));
        profiles = profiles.stream().map(x -> x.trim()).toList();
        return new MediaTypeAndProfile(mediaType, profiles);
    }

    /**
     * given a mediatype, what is its "name" (i.e. "html", "json") based on the contentNegotiationManager?
     *
     * @param mediaType media type to search for
     * @return name of the media type
     */
    public String getMediaName(MediaType mediaType) {
        var matching = contentNegotiationManager.getMediaTypeMappings().entrySet().stream()
                .filter(e -> e.getValue().equals(mediaType))
                .findFirst();

        if (matching.isEmpty()) {
            return null;
        }
        return matching.get().getKey();
    }
}
