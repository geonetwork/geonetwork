/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.ogcapi.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.DynamicPropertiesFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class OgcApiRecordOpenApiConfigProperties implements org.springdoc.core.customizers.OpenApiCustomizer {

    @Autowired
    DynamicPropertiesFacade dynamicPropertiesFacade;

    @Override
    public void customise(OpenAPI openAPI) {
        log.info("OgcApiRecordOpenApiConfigProperties: start customizer");
        var properties = openAPI.getComponents().getSchemas().get("OgcApiRecordsRecordGeoJSONPropertiesDto");
        for (var field : dynamicPropertiesFacade.getAllFields()) {
            if (!field.getConfig().getAddPropertyToOutput()) {
                continue;
            }
            if (field.isList()) {
                var t = field.getOpenAPIType();
                var arrayProperties = new ArraySchema().items(t);
                properties.addProperty(field.getConfig().getOgcProperty(), arrayProperties);

            } else {
                var t = field.getOpenAPIType();
                properties.addProperty(field.getConfig().getOgcProperty(), t);
            }
        }
    }
}
