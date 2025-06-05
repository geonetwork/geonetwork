/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.geonetwork.formatting.FormatterApi;
import org.geonetwork.formatting.FormatterInfo;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.stereotype.Component;

@Component
public class FormatterApiMessageWriter implements HttpMessageConverter<OgcApiRecordsRecordGeoJSONDto> {

    private final FormatterApi formatterApi;
    private final List<MediaType> supportedMediaTypes;

    @SneakyThrows
    public FormatterApiMessageWriter(FormatterApi formatterApi) {
        this.formatterApi = formatterApi;

        supportedMediaTypes =
                formatterApi.getAllFormatters().values().stream()
                        .map(x -> x.getMimeType())
                        .collect(Collectors.toSet())
                        .stream()
                        .map(x -> MediaType.valueOf(x))
                        .toList();
    }

    public Map<String, FormatterInfo> getFormatNamesAndMimeTypes() throws Exception {
        return formatterApi.getAllFormatters();
    }

    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        if (!clazz.equals(OgcApiRecordsRecordGeoJSONDto.class)) {
            return false;
        }
        return supportedMediaTypes.contains(mediaType);
    }

    public List<MediaType> getSupportedMediaTypes() {
        return this.supportedMediaTypes;
    }

    public List<MediaType> getSupportedMediaTypes(Class<?> clazz) {
        if (clazz.equals(OgcApiRecordsRecordGeoJSONDto.class)) {
            return supportedMediaTypes;
        }
        return List.of();
    }

    public OgcApiRecordsRecordGeoJSONDto read(
            Class<? extends OgcApiRecordsRecordGeoJSONDto> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        throw new IOException("Not supported");
    }

    @Override
    public void write(
            OgcApiRecordsRecordGeoJSONDto ogcApiRecordsJsonItemDto,
            MediaType contentType,
            HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        var approved = false;

        try {
            var formats = formatterApi.getRecordFormattersForMetadata(ogcApiRecordsJsonItemDto.getId(), approved);
            var formatEntry = formats.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(contentType.toString()))
                    .findFirst();
            if (formatEntry.isEmpty()) {
                throw new Exception("no formatter found for format " + contentType);
            }
            var formatterId = formatEntry.get().getKey();

            formatterApi.getRecordFormattedBy(
                    ogcApiRecordsJsonItemDto.getId(), formatterId, approved, outputMessage.getBody());
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }
}
