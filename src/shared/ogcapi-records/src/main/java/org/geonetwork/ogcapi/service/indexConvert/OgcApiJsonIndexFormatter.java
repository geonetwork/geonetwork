/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
