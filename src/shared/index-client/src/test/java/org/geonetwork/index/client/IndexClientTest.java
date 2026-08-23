/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.index.client;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class IndexClientTest {

    static class StubIndexClient extends IndexClient {
        boolean setupCalled = false;
        boolean missingOrEmpty = true;

        StubIndexClient(boolean createIfEmpty, boolean missingOrEmpty) {
            super("http://localhost:9200", null, null, "gn-", "gn-records", createIfEmpty, 50000, 10000L, 45000);
            this.missingOrEmpty = missingOrEmpty;
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

    @Test
    void shouldNotInitializeIndexWhenIndexAlreadyContainsRecords() {
        StubIndexClient client = new StubIndexClient(true, false);
        client.afterPropertiesSet();
        assertFalse(client.setupCalled, "setupIndex must not be called when index already contains records");
    }

    @Test
    void shouldInitializeIndexWhenCreateIfEmptyAndIndexMissingOrEmpty() {
        StubIndexClient client = new StubIndexClient(true, true);
        client.afterPropertiesSet();
        assertTrue(
                client.setupCalled,
                "setupIndex must be called when index is missing or empty and createIfEmpty is true");
    }

    @Test
    void shouldNotInitializeIndexWhenCreateIfEmptyIsFalse() {
        StubIndexClient client = new StubIndexClient(false, true);
        client.afterPropertiesSet();
        assertFalse(client.setupCalled, "setupIndex must not be called when createIfEmpty is false");
    }
}
