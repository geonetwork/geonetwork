/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.ctrlreturntypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import org.geonetwork.application.ctrlreturntypes.IControllerResponseObject;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public abstract class AbstractResponseFormatter<T extends IControllerResponseObject>
        implements HttpMessageConverter<T> {

    ObjectMapper objectMapper;
    XmlMapper xmlMapper;
    Class<T> responseClass;

    List<MediaType> supportedMediaTypes = Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);

    @SuppressWarnings("unchecked")
    public AbstractResponseFormatter(ObjectMapper objectMapper, XmlMapper xmlMapper) {
        this.objectMapper = objectMapper;
        this.xmlMapper = xmlMapper;
        this.responseClass =
                ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return getSupportedMediaTypes().contains(mediaType) && responseClass.equals(clazz);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return supportedMediaTypes;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes(Class<?> clazz) {
        return supportedMediaTypes;
    }

    @Override
    public T read(Class<? extends T> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void write(T object, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        try {
            Object fullObject = getFullObject(object);
            if (contentType.equals(MediaType.APPLICATION_XML)) {
                outputMessage.getHeaders().setContentType(MediaType.APPLICATION_XML);
                xmlMapper.writerWithDefaultPrettyPrinter().writeValue(outputMessage.getBody(), fullObject);
            } else if (contentType.equals(MediaType.APPLICATION_JSON)) {
                outputMessage.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputMessage.getBody(), fullObject);
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public abstract Object getFullObject(T object) throws Exception;
}
