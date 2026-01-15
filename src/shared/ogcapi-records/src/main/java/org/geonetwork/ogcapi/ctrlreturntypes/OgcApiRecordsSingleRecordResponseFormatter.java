/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.ctrlreturntypes;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.geonetwork.application.ctrlreturntypes.IControllerResultFormatter;
import org.geonetwork.formatting.FormatterApi;
import org.geonetwork.formatting.FormatterInfo;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.geonetwork.utility.MediaTypeAndProfile;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

/**
 * Provides the GN formatterApi formats to a single metadata record.
 *
 * <p>This is a profile aware formatter for a single mimetype. For example, application/xml+rdf will contain all the
 * DCAT formatters...
 *
 * <p>Typically, there will be several of these - one for each mime type that is supported in the formatterApi
 *
 * <p>OgcApiRecordsSingleRecordResponse - simple object with the catalogId and recordId
 */
@Getter
@Setter
public class OgcApiRecordsSingleRecordResponseFormatter
        implements IControllerResultFormatter<OgcApiRecordsSingleRecordResponse> {

    private MediaType mimeType;
    // shortcut name for the mimeType i.e. json->application/json  typically its the same as the mime type
    private String mimeTypeQuickName;
    private Class<OgcApiRecordsSingleRecordResponse> inputType = OgcApiRecordsSingleRecordResponse.class;
    private String defaultProfile;

    private Map<String, FormatterInfo> formatterProfileInfo;

    private FormatterApi formatterApi;

    public OgcApiRecordsSingleRecordResponseFormatter(
            MediaType mimeType,
            String mimeTypeQuickName,
            String defaultProfile,
            Map<String, FormatterInfo> formatterProfileInfo,
            FormatterApi formatterApi)
            throws Exception {

        this.mimeTypeQuickName = mimeTypeQuickName;
        this.mimeType = mimeType;
        this.defaultProfile = defaultProfile;
        this.formatterProfileInfo = formatterProfileInfo;

        this.formatterApi = formatterApi;
    }

    // ------------------------------------------------------------------------------------

    // this is write only
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    /**
     * Can write if the class is OgcApiRecordsRecordGeoJSONDto and we support the media type
     *
     * @param clazz What object are we outputting
     * @param mediaType what mime type do we want
     * @return if we can write this object with this mediatype
     */
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        if (!clazz.equals(this.inputType)) {
            return false;
        }
        return mediaType.equals(mimeType);
    }

    /**
     * we only support the one mime type.
     *
     * @return list containing our mimetype
     */
    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(mimeType);
    }

    /**
     * for an object type (must be OgcApiRecordsRecordGeoJSONDto), what mimetype do we support?
     *
     * @param clazz object type (should be OgcApiRecordsRecordGeoJSONDto)
     * @return what formats do we support for that object
     */
    @Override
    public @NotNull List<MediaType> getSupportedMediaTypes(Class<?> clazz) {
        if (clazz.equals(OgcApiRecordsRecordGeoJSONDto.class)) {
            return List.of(mimeType);
        }
        return Arrays.asList(); // we don't support
    }

    // read is not supported
    @Override
    public OgcApiRecordsSingleRecordResponse read(
            Class<? extends OgcApiRecordsSingleRecordResponse> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return null;
    }

    /**
     * Write the OgcApiRecordsSingleRecordResponse using the formatterApi. We do some simple profile negotiation and
     * choose the best formatter given what the user has given us.
     *
     * <p>note- OgcApiRecordsSingleRecordResponse contains a list of profile official names that the user has requested.
     *
     * @param object the object to write to the output message. The type of this object must have previously been passed
     *     to the {@link #canWrite canWrite} method of this interface, which must have returned {@code true}.
     * @param contentType the content type to use when writing. May be {@code null} to indicate that the default content
     *     type of the converter must be used. If not {@code null}, this media type must have previously been passed to
     *     the {@link #canWrite canWrite} method of this interface, which must have returned {@code true}.
     * @param outputMessage the message to write to
     * @throws IOException config problem?
     * @throws HttpMessageNotWritableException config problem?
     */
    @Override
    public void write(OgcApiRecordsSingleRecordResponse object, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        var approved = false;

        try {
            // from the mimetype, find out how to call the formatter api
            var collectionId = object.getCatalogId();

            var resolvedProfileName = object.getRequestMediaTypeAndProfile().getRequestResolvedProfile();

            var allFormatters = formatterApi.getRecordFormattersForMetadata(object.getRecordId(), approved);

            var formattersForOurMimeType = allFormatters.stream()
                    .filter(entry -> entry.getContentType().equals(contentType.toString()))
                    .filter(x -> x.getOfficialProfileName().equals(resolvedProfileName))
                    .toList();

            var mediaTypeAndProfile = new MediaTypeAndProfile(contentType, List.of(resolvedProfileName));

            var formatEntry = formattersForOurMimeType.stream()
                    .filter(entry -> entry.getOfficialProfileName().equals(resolvedProfileName))
                    .findFirst();

            if (formatEntry.isEmpty()) {
                throw new Exception("no formatter found for format " + contentType);
            }
            var formatterId = formatEntry.get().getName();
            var officialProfileName = formatEntry.get().getOfficialProfileName();

            outputMessage.getHeaders().setContentType(contentType);
            if (officialProfileName != null) {
                outputMessage.getHeaders().set("Link", "<" + officialProfileName + ">; rel=\"profile\"");
            }

            var formatterConfig = new HashMap<String, Object>();
            formatterConfig.put("mediaTypeAndProfile", mediaTypeAndProfile);
            formatterConfig.put("collectionId", collectionId);

            // do the actual formatting and output it
            formatterApi.getRecordFormattedBy(
                    object.recordId,
                    formatterId,
                    officialProfileName,
                    approved,
                    outputMessage.getBody(),
                    formatterConfig);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    // ----------------------------------------------------

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

    @Override
    public String getMimeTypeQuickName() {
        return this.mimeTypeQuickName == null ? this.mimeType.toString() : this.mimeTypeQuickName;
    }

    @Override
    public List<String> getProvidedProfileNames() {
        return formatterProfileInfo.values().stream()
                .map(x -> x.getProfile().getOfficialProfileName())
                .toList();
    }

    @Override
    public String toString() {
        var result = "Formatter for object type: " + this.inputType.getSimpleName() + "\n";
        result += "     + mime type: " + mimeType + "\n";
        result += "     + profiles: " + String.join(", ", getProvidedProfileNames());

        return result;
    }
}
