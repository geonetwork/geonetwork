/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.application.ctrlreturntypes;

import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import org.geonetwork.application.formatters.MessageWriterUtil;
import org.geonetwork.application.profile.ProfileDefaultsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;

@Component
public class RequestMediaTypeAndProfileBuilder {

    @Autowired
    MessageWriterUtil messageWriterUtil;

    @Autowired
    ContentNegotiationManager contentNegotiationManager;

    @Autowired
    ProfileDefaultsConfiguration profileDefaultsConfiguration;

    @Autowired
    MimeAndProfilesForResponseType mimeAndProfilesForResponseType;

    public RequestMediaTypeAndProfile build(MediaType mediaType, List<String> requestedProfiles, Class<?> clazz)
            throws Exception {
        // no profiles in request, use default for the type
        if (requestedProfiles == null || requestedProfiles.isEmpty()) {
            return new RequestMediaTypeAndProfile(
                    mediaType,
                    requestedProfiles,
                    profileDefaultsConfiguration.getDefaultProfile(mediaType, clazz),
                    clazz);
        }
        // otherwise, negotiate!
        var responseInfos = mimeAndProfilesForResponseType.getResponseTypeInfos(clazz).stream()
                .filter(x -> x.getMimeType().equals(mediaType))
                .toList();
        for (var requestProfile : requestedProfiles) {
            var match = responseInfos.stream()
                    .filter(x -> x.getProfiles().contains(requestProfile))
                    .findFirst();
            if (match.isPresent()) {
                return new RequestMediaTypeAndProfile(mediaType, requestedProfiles, requestProfile, clazz);
            }
        }

        return null;
    }

    public RequestMediaTypeAndProfile build(NativeWebRequest nativeWebRequest, Class<?> clazz) throws Exception {
        var mediaType = getRequestMediaType(nativeWebRequest);
        if (mediaType == null) {
            return null;
        }

        var requestedProfiles = getRequestedProfiles(nativeWebRequest);

        // no profiles in request, use default for the type
        if (requestedProfiles == null || requestedProfiles.isEmpty()) {
            return new RequestMediaTypeAndProfile(
                    mediaType,
                    requestedProfiles,
                    profileDefaultsConfiguration.getDefaultProfile(mediaType, clazz),
                    clazz);
        }

        // otherwise, negotiate!
        var responseInfos = mimeAndProfilesForResponseType.getResponseTypeInfos(clazz).stream()
                .filter(x -> x.getMimeType().equals(mediaType))
                .toList();
        for (var requestProfile : requestedProfiles) {
            var match = responseInfos.stream()
                    .filter(x -> x.getProfiles().contains(requestProfile))
                    .findFirst();
            if (match.isPresent()) {
                return new RequestMediaTypeAndProfile(mediaType, requestedProfiles, requestProfile, clazz);
            }
        }

        return null;
    }

    private List<String> getRequestedProfiles(NativeWebRequest nativeWebRequest) {
        var profile = nativeWebRequest.getParameterMap().get("profile");
        if (profile == null || profile.length == 0) {
            return null;
        }

        var profileName = profile[0].trim();
        if (profileName.contains(",")) {
            var profiles = Arrays.asList(StringUtils.split(profileName, ","));
            return profiles.stream().map(String::trim).toList();
        }
        return List.of(profileName);
    }

    /**
     * very simple profile negotiation. <br>
     * 1. user requested no profile -> use default profile<br>
     * 2. go through (in order) the profiles the user requested versus the ones supported<br>
     * -> First one we find, return it!<br>
     *
     * @param defaultProfile default profile for this mime type
     * @param requestedProfiles what the user wanted
     * @param possibleProfiles supported profile
     * @return best matching profile
     * @throws Exception config problem?
     */
    public String chooseProfile(String defaultProfile, List<String> requestedProfiles, List<String> possibleProfiles)
            throws Exception {
        // didn't request any profile - use default
        if (requestedProfiles == null || requestedProfiles.isEmpty()) {
            return defaultProfile;
        }

        // see one of the requested profiles is available
        for (String profile : requestedProfiles) {
            if (possibleProfiles.contains(profile)) {
                return profile;
            }
        }

        // no requested profile is available
        throw new Exception("couldnt negotiate a profile");
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
}
