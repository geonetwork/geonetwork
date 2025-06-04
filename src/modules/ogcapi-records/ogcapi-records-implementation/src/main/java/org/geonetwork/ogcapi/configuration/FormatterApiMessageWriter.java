/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.configuration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.geonetwork.formatting.FormatterApi;
import org.geonetwork.formatting.FormatterInfo;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class FormatterApiMessageWriter<OgcApiRecordsJsonItemDto>
        extends AbstractGenericHttpMessageConverter<OgcApiRecordsJsonItemDto> {

    private final FormatterApi formatterApi;

    @SneakyThrows
    public FormatterApiMessageWriter(FormatterApi formatterApi) {
        super(computeSupportedMimes(formatterApi));
        this.formatterApi = formatterApi;
    }

    @SneakyThrows
    private static MediaType[] computeSupportedMimes(FormatterApi formatterApi) {
        var supportedMimeTypes =
                formatterApi.getAllFormatters().values().stream()
                        .map(x -> x.getMimeType())
                        .collect(Collectors.toSet())
                        .stream()
                        .map(x -> MediaType.valueOf(x))
                        .toArray(MediaType[]::new);
        return supportedMimeTypes;
    }

    public Map<String, FormatterInfo> getFormatNamesAndMimeTypes() throws Exception {
        return formatterApi.getAllFormatters();
    }

    @Override
    protected void writeInternal(Object o, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        var t = 0;
    }

    @Override
    protected Object readInternal(Class clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        throw new IOException("Not supported");
    }

    @Override
    public Object read(Type type, Class contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        throw new IOException("Not supported");
    }

    @Override
    protected boolean canRead(@Nullable MediaType mediaType) {
        return false;
    }
}
