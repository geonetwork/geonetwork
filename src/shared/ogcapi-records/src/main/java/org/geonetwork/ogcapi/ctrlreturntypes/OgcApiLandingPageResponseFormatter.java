/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.ctrlreturntypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.geonetwork.ogcapi.service.ogcapi.OgcApiCollectionsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

@Component
public class OgcApiLandingPageResponseFormatter implements HttpMessageConverter<OgcApiLandingPageResponse> {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    XmlMapper xmlMapper;

    @Autowired
    OgcApiCollectionsApi collectionsApi;

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return getSupportedMediaTypes().contains(mediaType) && clazz.equals(OgcApiLandingPageResponse.class);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);
    }

    @Override
    public OgcApiLandingPageResponse read(
            Class<? extends OgcApiLandingPageResponse> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void write(
            OgcApiLandingPageResponse ogcApiLandingPageResponse, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        try {
            var object = computeObject(ogcApiLandingPageResponse);
            if (contentType.equals(MediaType.APPLICATION_XML)) {
                outputMessage.getHeaders().setContentType(MediaType.APPLICATION_XML);
                xmlMapper.writerWithDefaultPrettyPrinter().writeValue(outputMessage.getBody(), object);
            } else if (contentType.equals(MediaType.APPLICATION_JSON)) {
                outputMessage.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputMessage.getBody(), object);
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private Object computeObject(OgcApiLandingPageResponse ogcApiLandingPageResponse) throws Exception {
        var result = collectionsApi.getLandingPage(ogcApiLandingPageResponse.getRequestMediaTypeAndProfile());
        return result;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes(Class<?> clazz) {
        return List.of(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);
    }
}
