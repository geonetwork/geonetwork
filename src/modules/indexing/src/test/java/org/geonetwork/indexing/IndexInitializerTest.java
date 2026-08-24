/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.indexing;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import org.geonetwork.index.client.IndexClient;
import org.junit.jupiter.api.Test;

class IndexInitializerTest {

    static class TestIndexClient extends IndexClient {
        boolean setupCalled = false;
        boolean missingOrEmpty = true;

        TestIndexClient() {
            super("http://localhost:9200", null, null, "gn-", "gn5test-records", false, 50000, 10000L, 45000);
        }

        @Override
        public boolean isIndexMissingOrEmpty() {
            return missingOrEmpty;
        }

        @Override
        public void setupIndex(boolean dropIfExists) {
            setupCalled = true;
        }
    }

    static class TestIndexingService extends IndexingService {
        boolean indexCalled = false;

        TestIndexingService() {
            super(false, 500, 1, "45s", null, null, null, null);
        }

        @Override
        public List<Future<?>> index(List<String> uuids) {
            indexCalled = true;
            return Collections.emptyList();
        }
    }

    @Test
    void shouldInitializeIndexAndReindexWhenMissingOrEmpty() {
        TestIndexClient client = new TestIndexClient();
        client.missingOrEmpty = true;
        TestIndexingService service = new TestIndexingService();

        IndexInitializer initializer = new IndexInitializer(client, service);
        initializer.run();

        assertTrue(client.setupCalled);
        assertTrue(service.indexCalled);
    }

    @Test
    void shouldSkipInitializationWhenIndexAlreadyContainsRecords() {
        TestIndexClient client = new TestIndexClient();
        client.missingOrEmpty = false;
        TestIndexingService service = new TestIndexingService();

        IndexInitializer initializer = new IndexInitializer(client, service);
        initializer.run();

        assertFalse(client.setupCalled);
        assertFalse(service.indexCalled);
    }
}
