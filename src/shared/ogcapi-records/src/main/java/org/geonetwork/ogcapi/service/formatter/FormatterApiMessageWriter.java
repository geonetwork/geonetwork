/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.formatter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.SneakyThrows;
import org.geonetwork.formatting.FormatterApi;
import org.geonetwork.formatting.FormatterInfo;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.geonetwork.utility.MediaTypeAndProfile;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

/**
 * This is the output writer for the FormatterApi.
 *
 * <p>1. We get all the (unique) mime types that the formatter supports. These are the supportedFormats
 *
 * <p>2. We do not support reading
 *
 * <p>3. For writing, we canWrite iff <br>
 * + mimetype is what we support (supportedFormats) <br>
 * + the object type is OgcApiRecordsRecordGeoJSONDto
 *
 * <p>4. For writing, <br>
 * + we get the metadata uuid from the OgcApiRecordsRecordGeoJSONDto <br>
 * + we get the mimetype from the request <br>
 * + we do profile negotiation <br>
 * + get the correct formatter <br>
 * + convert the request mimetype to the formatter id (i.e. how to run the formatter)
 *
 * <p>5. We set the response mimetype
 *
 * <p>6. We set the Header `Link:`
 */
@Component
public class FormatterApiMessageWriter implements HttpMessageConverter<OgcApiRecordsRecordGeoJSONDto> {

    private static final String PROFILE_HEADER_NAME = "GN5.OGCAPI-RECORDS.REQUEST-PROFILES";
    private static final String COLLECTION_ID_HEADER_NAME = "GN5.OGCAPI-RECORDS.REQUEST-COLLECTIONID";
    private static final String RECORD_ID_HEADER_NAME = "GN5.OGCAPI-RECORDS.REQUEST-RECORDID";

    /** actually does the formatting work and knows what formatters are available */
    private final FormatterApi formatterApi;

    private final ProfileSelector profileSelector;

    /** what media types we can output - from the FormatterApi -- GETTER -- what media types do we support */
    @Getter
    private final List<MediaType> supportedMediaTypes;

    @SneakyThrows
    public FormatterApiMessageWriter(FormatterApi formatterApi, ProfileSelector profileSelector) {
        this.formatterApi = formatterApi;
        this.profileSelector = profileSelector;

        supportedMediaTypes = formatterApi.getAllFormatters().keySet().stream()
                .map(x -> MediaType.valueOf(x))
                .toList();
    }

    /**
     * wrapper around FormatterApi#getAllFormatters()
     *
     * @return metadata about all the formatterApi formats
     */
    public Map<String, Map<String, FormatterInfo>> getFormatNamesAndMimeTypes() throws Exception {
        return formatterApi.getAllFormatters();
    }

    /** we do not support read - always false */
    @Override
    public boolean canRead(@NotNull Class<?> clazz, MediaType mediaType) {
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
        if (!clazz.equals(OgcApiRecordsRecordGeoJSONDto.class)) {
            return false;
        }
        return supportedMediaTypes.contains(mediaType);
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
            return supportedMediaTypes;
        }
        return List.of(); // we don't support
    }

    /** we don't support reading */
    @Override
    public @NotNull OgcApiRecordsRecordGeoJSONDto read(
            @NotNull Class<? extends OgcApiRecordsRecordGeoJSONDto> clazz, @NotNull HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        throw new IOException("Not supported");
    }

    /**
     * Write the (formatted) output to the stream.
     *
     * <p>ASSUMPTIONS:
     *
     * <p>1.There is a header named `COLLECTION_ID_HEADER_NAME` whose value is the collectionId.
     *
     * <p>2. There is a header named `PROFILE_HEADER_NAME` whose value is list of profiles in the user's request
     *
     * <p>3. There is a header named `RECORD_ID_HEADER_NAME` whose value is the recordID NOTE: this isn't actually used
     * since it's available in the ogcApiRecordsJsonItemDto object
     *
     * <p>NOTE: these headers should be attached to the outputMessage by the Controller.
     *
     * <p>We do content negotiation (`ProfileSelector`) to determine which formatter to use.
     *
     * <p>Basic process is:
     *
     * <p>1. Find the correct formatter (compatible with this record's schema, correct mimetype, determine which
     * profile) <br>
     * 2. call the formatter, and set these in the config: <br>
     * + CollectionID (this is to setup links) <br>
     * + which mimetype/profile we are creating (this is so links can differentiate between `self` and `alternate`
     *
     * @param ogcApiRecordsJsonItemDto object to write
     * @param contentType what mimetype are we writing
     * @param outputMessage where to output the formatted text. Also contains the headers (see above).
     */
    @Override
    public void write(
            @NotNull OgcApiRecordsRecordGeoJSONDto ogcApiRecordsJsonItemDto,
            MediaType contentType,
            @NotNull HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        var approved = false;

        try {
            // from the mimetype, find out how to call the formatter api
            var _collectionId = outputMessage.getHeaders().get(COLLECTION_ID_HEADER_NAME);
            String collectionId = null;
            if (_collectionId != null && !_collectionId.isEmpty()) {

                collectionId = _collectionId.getFirst();
            }
            var profileList = outputMessage.getHeaders().get(PROFILE_HEADER_NAME);
            var mediaTypeAndProfile = new MediaTypeAndProfile(contentType, profileList);
            outputMessage.getHeaders().remove(PROFILE_HEADER_NAME);
            outputMessage.getHeaders().remove(COLLECTION_ID_HEADER_NAME);
            outputMessage.getHeaders().remove(RECORD_ID_HEADER_NAME);

            var allFormatters = formatterApi.getRecordFormattersForMetadata(ogcApiRecordsJsonItemDto.getId(), approved);
            var formatters = allFormatters.stream()
                    .filter(entry -> entry.getContentType().equals(contentType.toString()))
                    .toList();
            var profileName = profileSelector.chooseProfile(contentType, profileList, formatters);
            var formatEntry = formatters.stream()
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
                    ogcApiRecordsJsonItemDto.getId(),
                    formatterId,
                    officialProfileName,
                    approved,
                    outputMessage.getBody(),
                    formatterConfig);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }
}
