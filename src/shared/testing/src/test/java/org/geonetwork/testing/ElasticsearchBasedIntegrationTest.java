/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.testing;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/** Integration test running with Elasticsearch */
@Testcontainers
public class ElasticsearchBasedIntegrationTest {
    @Container
    protected static ElasticsearchContainer elasticsearchContainer = new ElasticsearchTestContainer()
            .withEnv("xpack.security.enabled", "false")
            .withExposedPorts(9211)
            .withStartupTimeout(Duration.ofSeconds(90));

    @BeforeAll
    static void setUp() {
        elasticsearchContainer.start();
    }

    @BeforeEach
    void testIsContainerRunning() {
        assertTrue(elasticsearchContainer.isRunning());
    }

    @AfterAll
    static void destroy() {
        elasticsearchContainer.stop();
    }
}
