/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.ctrlreturntypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.util.Arrays;
import java.util.List;
import org.geonetwork.ogcapi.service.ogcapi.OgcApiCollectionsApi;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class OgcApiCollectionResponseFormatter extends AbstractResponseFormatter<OgcApiCollectionResponse> {

    private final OgcApiCollectionsApi collectionsApi;

    public OgcApiCollectionResponseFormatter(
            OgcApiCollectionsApi collectionsApi, ObjectMapper objectMapper, XmlMapper xmlMapper) {
        super(objectMapper, xmlMapper);
        this.collectionsApi = collectionsApi;
    }

    @Override
    public Object getFullObject(OgcApiCollectionResponse object) throws Exception {
        var result =
                collectionsApi.describeCollection(object.getCollectionId(), object.getRequestMediaTypeAndProfile());
        return result;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes(Class<?> clazz) {
        return getSupportedMediaTypes();
    }
}
