/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

@Component
public class OgcApiJsonIndexFormatter extends AbstractOgcApiIndexFormatter {

    public OgcApiJsonIndexFormatter(OgcApiGeoJsonConverter converter, ObjectMapper mapper, BeanFactory factory) {
        super(converter, mapper, factory);
    }

    @Override
    public String getName() {
        return "OgcApiJson";
    }

    @Override
    public String getContentType() {
        return "application/json";
    }
}
