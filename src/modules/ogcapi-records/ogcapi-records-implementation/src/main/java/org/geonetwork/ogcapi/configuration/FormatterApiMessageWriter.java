/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.SneakyThrows;
import org.geonetwork.formatting.FormatterApi;
import org.geonetwork.formatting.FormatterInfo;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
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
 * <p>1. We get all the (unique) mime types that the formatter supports. These are the supportedFormats 2. We do not
 * support reading 3. For writing, we canWrite iff + mimetype is what we support (supportedFormats) + the object type is
 * OgcApiRecordsRecordGeoJSONDto 4. For writing, + we get the metadata uuid from the OgcApiRecordsRecordGeoJSONDto + we
 * get the mimetype from the request + convert the request mimetype to the formatter id (i.e. how to run the formatter)
 * 5. We set the response mimetype (to the, long, unique, mime type).
 */
@Component
public class FormatterApiMessageWriter implements HttpMessageConverter<OgcApiRecordsRecordGeoJSONDto> {

    private static final String PROFILE_HEADER_NAME = "GN5.OGCAPI-RECORDS.REQUEST-PROFILES";

    /** actually does the formatting work and knows what formatters are available */
    private final FormatterApi formatterApi;

    /**
     * what media types we can output - from the FormatterApi -- GETTER -- what media types do we support
     *
     * @return what media types do we support
     */
    @Getter
    private final List<MediaType> supportedMediaTypes;

    @SneakyThrows
    public FormatterApiMessageWriter(FormatterApi formatterApi) {
        this.formatterApi = formatterApi;

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
     * write the (formatted) output to the stream.
     *
     * @param ogcApiRecordsJsonItemDto object to write
     * @param contentType what mimetype are we writing
     * @param outputMessage where to output the formatted text
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
            var profileList = outputMessage.getHeaders().get(PROFILE_HEADER_NAME);
            var _profile = "";
            if (profileList != null && !profileList.isEmpty()) {
                _profile = profileList.get(0);
            }
            var profile = _profile;
            outputMessage.getHeaders().remove(PROFILE_HEADER_NAME);

            var formatters = formatterApi.getRecordFormattersForMetadata(ogcApiRecordsJsonItemDto.getId(), approved);
            var formatEntry = formatters.stream()
                    .filter(entry -> entry.getContentType().equals(contentType.toString()))
                    .filter(entry -> entry.getOfficialProfileName().equals(profile))
                    .findFirst();
            if (formatEntry.isEmpty()) {
                throw new Exception("no formatter found for format " + contentType);
            }
            var formatterId = formatEntry.get().getName();
            var officialProfileName = formatEntry.get().getOfficialProfileName();

            outputMessage.getHeaders().setContentType(contentType);

            // do the actual formatting and output it
            formatterApi.getRecordFormattedBy(
                    ogcApiRecordsJsonItemDto.getId(),
                    formatterId,
                    officialProfileName,
                    approved,
                    outputMessage.getBody());
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }
}
