/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.cql;

import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldsMapperConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The CQL filter puts "/" in place of "." - we convert a property name with "/" with a ".".
 *
 * <p>`cl_spatialRepresentationType.key` -> `cl_spatialRepresentationType/key`
 *
 * <p>we convert to `cl_spatialRepresentationType.key`
 */
@Component
public class OgcElasticFieldMapper implements IFieldMapper {

    @Autowired
    OgcElasticFieldsMapperConfig ogcElasticFieldsMapperConfig;

    @Override
    public String map(String field) {
        field = field.replaceAll("/", ".");
        var fieldInfo = ogcElasticFieldsMapperConfig.findByOgc(field);
        if (fieldInfo != null) {
            return fieldInfo.getElasticProperty();
        }
        throw new RuntimeException("Don't know what field '" + field + "' maps to in elastic!");
    }

    @Override
    public String mapSort(String field) {
        throw new RuntimeException("Not implemented");
    }
}
