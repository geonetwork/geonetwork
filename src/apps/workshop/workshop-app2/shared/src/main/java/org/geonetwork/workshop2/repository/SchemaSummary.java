/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.workshop2.repository;

/**
 * POJO to return the schemaId/count aggregate.
 *
 * <p>SELECT schemaId, count(*) FROM metadata GROUP BY schemaId;
 */
public class SchemaSummary {

    public String schemaId;
    public Long count;

    public SchemaSummary(String schemaId, Long count) {
        this.schemaId = schemaId;
        this.count = count;
    }
}
