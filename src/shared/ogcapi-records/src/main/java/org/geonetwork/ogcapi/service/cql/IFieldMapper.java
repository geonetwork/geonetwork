/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.cql;

public interface IFieldMapper {
    String map(String field) ;

    String mapSort(String field);
}
