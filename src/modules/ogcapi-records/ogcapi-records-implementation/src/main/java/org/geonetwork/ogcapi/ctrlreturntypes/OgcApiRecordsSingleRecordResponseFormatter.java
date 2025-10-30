/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.ctrlreturntypes;

import java.io.IOException;
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

@Getter
@Setter
public class OgcApiRecordsSingleRecordResponseFormatter
        implements IControllerResultFormatter<OgcApiRecordsSingleRecordResponse> {

    private String name;
    private String description;

    private MediaType mimeType;
    private String mimeTypeQuickName;
    private Class<OgcApiRecordsSingleRecordResponse> inputType = OgcApiRecordsSingleRecordResponse.class;
    private String defaultProfile;

    private Map<String, FormatterInfo> formatterProfileInfo;

    private FormatterApi formatterApi;

    public OgcApiRecordsSingleRecordResponseFormatter(
            String name,
            String description,
            MediaType mimeType,
            String mimeTypeQuickName,
            String defaultProfile,
            Map<String, FormatterInfo> formatterProfileInfo,
            FormatterApi formatterApi)
            throws Exception {

        this.mimeTypeQuickName = mimeTypeQuickName;
        if (name == null) {
            name = "formatter API for mime type " + mimeType.toString() + " and class " + inputType.getSimpleName();
        }
        this.name = name;

        if (description == null) {
            description = "formatter API for mime type " + mimeType.toString() + " and class "
                    + inputType.getSimpleName() + " with default profile " + defaultProfile;
        }

        this.description = description;
        this.mimeType = mimeType;
        this.defaultProfile = defaultProfile;
        this.formatterProfileInfo = formatterProfileInfo;

        this.formatterApi = formatterApi;
    }

    // ------------
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

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return List.of(mimeType);
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
        return List.of(); // we don't support
    }

    @Override
    public OgcApiRecordsSingleRecordResponse read(
            Class<? extends OgcApiRecordsSingleRecordResponse> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    public void write(OgcApiRecordsSingleRecordResponse obj, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        var tt = 0;

        var object = (OgcApiRecordsSingleRecordResponse) obj;

        var approved = false;

        try {
            // from the mimetype, find out how to call the formatter api
            var collectionId = object.getCatalogId();

            var profileList = object.getUserRequestedProfiles();
            var mediaTypeAndProfile = new MediaTypeAndProfile(contentType, profileList);

            var allFormatters = formatterApi.getRecordFormattersForMetadata(object.getRecordId(), approved);

            var formattersForOurMimeType = allFormatters.stream()
                    .filter(entry -> entry.getContentType().equals(contentType.toString()))
                    .toList();
            var supportedProfileNames = formattersForOurMimeType.stream()
                    .map(x -> x.getOfficialProfileName())
                    .toList();
            var profileName = chooseProfile(defaultProfile, profileList, supportedProfileNames);
            if (profileName == null) {
                throw new Exception("couldnt negotiate profile name");
            }

            var formatEntry = formattersForOurMimeType.stream()
                    .filter(entry -> entry.getOfficialProfileName().equals(profileName))
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

    // --------------------------
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
