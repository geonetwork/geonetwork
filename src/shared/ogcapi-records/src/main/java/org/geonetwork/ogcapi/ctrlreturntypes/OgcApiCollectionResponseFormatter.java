/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.ctrlreturntypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.geonetwork.ogcapi.service.ogcapi.OgcApiCollectionsApi;
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
}
