/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.testing;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
            .withExposedPorts(9211);

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
