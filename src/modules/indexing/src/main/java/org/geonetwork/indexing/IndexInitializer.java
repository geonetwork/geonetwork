/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.indexing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.index.client.IndexClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Initializes the Elasticsearch index on application startup if it is missing or empty, and triggers a full reindex of
 * database records into the index.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
@Order(10)
@ConditionalOnProperty(name = "geonetwork.index.createIfEmpty", havingValue = "true", matchIfMissing = true)
public class IndexInitializer implements CommandLineRunner {

    private final IndexClient indexClient;
    private final IndexingService indexingService;

    @Override
    public void run(String... args) {
        String indexName = indexClient.getIndexRecordName();
        log.atInfo().log("Checking if index '{}' needs to be initialized on startup...", indexName);

        if (indexClient.isIndexMissingOrEmpty()) {
            log.atInfo().log("Index '{}' is missing or empty. Creating index mappings and analyzers...", indexName);
            indexClient.setupIndex(true);
            log.atInfo().log("Index '{}' successfully created.", indexName);

            log.atInfo().log("Triggering full reindex of database records into '{}'...", indexName);
            indexingService.index(null);
            log.atInfo().log("Startup reindexing task submitted.");
        } else {
            log.atInfo().log("Index '{}' already exists and is not empty. Skipping startup index creation.", indexName);
        }
    }
}
