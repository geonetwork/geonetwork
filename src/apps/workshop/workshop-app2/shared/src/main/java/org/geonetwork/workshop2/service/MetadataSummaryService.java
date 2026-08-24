/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.workshop2.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geonetwork.schemas.SchemaManager;
import org.geonetwork.workshop2.model.Workshop2MetadataSummarySchemaDto;
import org.geonetwork.workshop2.repository.SummarizingMetadataRepository;
import org.springframework.stereotype.Component;

/** Service class that summarizes the schema usage in the `metadata` db table. */
@Component
public class MetadataSummaryService {

    private final SummarizingMetadataRepository summarizingMetadataRepository;
    private final SchemaManager schemaManager;

    public MetadataSummaryService(
            SummarizingMetadataRepository summarizingMetadataRepository, SchemaManager schemaManager) {
        this.summarizingMetadataRepository = summarizingMetadataRepository;
        this.schemaManager = schemaManager;
    }

    /**
     * actually does the work to compute the summary - hands off to `schemaManager` and the DB (via the repository.
     *
     * @return summary of the metadata schema counts
     */
    public Workshop2MetadataSummarySchemaDto createSummary() {

        var result = new Workshop2MetadataSummarySchemaDto();
        // use the schema manager to get the set of supported schemas
        List<String> allSchemaIds = this.schemaManager.getSchemas().stream().toList();

        // calculate the number of records in each schema from the DB `metadata` table
        Map<String, Integer> schemaCounts = calculateSchemaCounts();

        // sum the counts inside schemaCounts
        // Alternatively, we could have counted the number of records in the `metadata` table
        int nRecords = schemaCounts.values().stream().mapToInt(x -> x).sum();

        result.setRecordsPerSchema(schemaCounts);
        result.setAllSchemaIds(allSchemaIds);
        result.setTotalNumberOfRecords(nRecords);

        return result;
    }

    /**
     * converts the DB summary results to the Map<String, Integer> required.
     *
     * @return DB summary results
     */
    private Map<String, Integer> calculateSchemaCounts() {
        var metadataCountSummary = summarizingMetadataRepository.countBySchemaId();
        var result = new HashMap<String, Integer>();
        metadataCountSummary.forEach(metadata -> {
            result.put(metadata.schemaId, metadata.count.intValue());
        });
        return result;
    }
}
