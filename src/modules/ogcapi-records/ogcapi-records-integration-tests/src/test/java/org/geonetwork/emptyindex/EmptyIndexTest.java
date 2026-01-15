/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.emptyindex;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.geonetwork.GeonetworkGenericApplication;
import org.geonetwork.infrastructure.ElasticPgMvcTestHelper;
import org.geonetwork.ogcapi.records.generated.model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.MountableFile;

/**
 * empty elastic -> no records.
 *
 * <p>We are testing that there isn't a crash when we request the main items
 */
@SpringBootTest(classes = GeonetworkGenericApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test", "integration-test"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = EmptyIndexTest.class)
public class EmptyIndexTest implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public final String MAIN_COLLECTION_ID = "3bef299d-cf82-4033-871b-875f6936b2e2";
    public final String METAWAL_COLLECTION_ID = "cec997ba-1fa4-48d9-8be0-890da8cc65cf";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("geonetwork")
            .withUsername("postgres")
            .withPassword("postgres")
            .withCopyToContainer(
                    MountableFile.forClasspathResource("dump.gn.sql"), "/docker-entrypoint-initdb.d/dump.gn.sql");

    static ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer(
                    "docker.elastic.co/elasticsearch/elasticsearch:8.14.0")
            .withEnv("path.repo", "/tmp")
            .withEnv("ES_JAVA_OPTS", "-Xms750m -Xmx2g")
            .withEnv("discovery.type", "single-node")
            .withEnv("xpack.security.enabled", "false")
            .withEnv("xpack.security.enrollment.enabled", "false")
            .withCopyToContainer(MountableFile.forClasspathResource("es_empty_index.tar.gz"), "/tmp/es_backups.tar.gz");

    public final int MVC_PORT = 8888;
    public final String BASE_URL = "http://localhost:" + MVC_PORT + "/";

    public EmptyIndexTest() throws Exception {}

    @SneakyThrows
    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        ElasticPgMvcTestHelper.initialize(ctx, postgreSQLContainer, elasticsearchContainer, MVC_PORT);
    }

    @BeforeAll
    static void beforeAll() throws Exception {
        ElasticPgMvcTestHelper.beforeAll(postgreSQLContainer, elasticsearchContainer);
    }

    @AfterAll
    static void afterAll() throws Exception {
        ElasticPgMvcTestHelper.afterAll(postgreSQLContainer, elasticsearchContainer);
    }

    @Test
    public void testDoesntCrash_landingpage() throws Exception {
        var landingPage = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records", OgcApiRecordsLandingPageDto.class, BASE_URL, mockMvc, objectMapper);

        assertNotNull(landingPage);
        assertTrue(landingPage.getLinks().size() > 5); // there will be several
    }

    @Test
    public void testDoesntCrash_collections() throws Exception {
        var collections = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections",
                OgcApiRecordsGetCollections200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        assertNotNull(collections);
        assertTrue(collections.getLinks().size() > 2);
        assertEquals(2, collections.getCollections().size());

        assertTrue(collections.getCollections().get(0).getId().equals(MAIN_COLLECTION_ID)
                || collections.getCollections().get(0).getId().equals(METAWAL_COLLECTION_ID));
        assertTrue(collections.getCollections().get(1).getId().equals(MAIN_COLLECTION_ID)
                || collections.getCollections().get(1).getId().equals(METAWAL_COLLECTION_ID));
    }

    @Test
    public void testDoesntCrash_collection() throws Exception {
        var collection = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID,
                OgcApiRecordsCatalogDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        assertNotNull(collection);
        assertTrue(collection.getLinks().size() > 10);
        assertEquals(MAIN_COLLECTION_ID, collection.getId());
    }

    @Test
    public void testDoesntCrash_items() throws Exception {
        var items = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items",
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        // nothing in index, so wouldn't expect much here
        assertNotNull(items);
        assertEquals(0, items.getFeatures().size());

        // there ought to be more, but some removed because of the missing values
        assertEquals(8, items.getFacets().size());
    }
}
