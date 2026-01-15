/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.workshop2.repository;

import java.util.List;
import org.geonetwork.domain.repository.MetadataRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * We add one more @Query method to the standard MetadataRepository that does our GROUP BY so we can summarize the count
 * records for each schema.
 *
 * <p>SELECT schemaId, count(*) FROM metadata GROUP BY schemaId;
 */
public interface SummarizingMetadataRepository extends MetadataRepository {

    @Query(
            "SELECT new org.geonetwork.workshop2.repository.SchemaSummary(md.schemaid, count(*)) FROM Metadata md GROUP BY md.schemaid")
    List<SchemaSummary> countBySchemaId();
}
