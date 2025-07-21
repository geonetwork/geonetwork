/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.cql;

public class OgcElasticFieldMapper implements IFieldMapper {
    @Override
    public String map(String field) {
        return field;
    }

    @Override
    public String mapSort(String field) {
        throw new RuntimeException("Not implemented");
    }
}
