/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.ctrlreturntypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetCollections200ResponseDto;
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
public class OgcApiRecordsCollectionsResponseFormatter
        implements HttpMessageConverter<OgcApiRecordsCollectionsResponse> {

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
        return getSupportedMediaTypes().contains(mediaType) && OgcApiRecordsCollectionsResponse.class.equals(clazz);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);
    }

    @Override
    public OgcApiRecordsCollectionsResponse read(
            Class<? extends OgcApiRecordsCollectionsResponse> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void write(
            OgcApiRecordsCollectionsResponse ogcApiRecordsCollectionsResponse,
            MediaType contentType,
            HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        OgcApiRecordsGetCollections200ResponseDto collectionsInfo = null;
        try {
            collectionsInfo = collectionsApi.getCollections(ogcApiRecordsCollectionsResponse);
            if (contentType.equals(MediaType.APPLICATION_XML)) {
                outputMessage.getHeaders().setContentType(MediaType.APPLICATION_XML);
                xmlMapper.writerWithDefaultPrettyPrinter().writeValue(outputMessage.getBody(), collectionsInfo);
            } else if (contentType.equals(MediaType.APPLICATION_JSON)) {
                outputMessage.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputMessage.getBody(), collectionsInfo);
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public List<MediaType> getSupportedMediaTypes(Class<?> clazz) {
        return Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);
    }
}
