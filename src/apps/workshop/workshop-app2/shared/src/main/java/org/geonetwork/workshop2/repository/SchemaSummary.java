/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.workshop2.repository;

import lombok.AllArgsConstructor;

/**
 * POJO to return the schemaId/count aggregate.
 *
 * <p>SELECT schemaId, count(*) FROM metadata GROUP BY schemaId;
 */
@AllArgsConstructor
public class SchemaSummary {
    public String schemaId;
    public Long count;
}
