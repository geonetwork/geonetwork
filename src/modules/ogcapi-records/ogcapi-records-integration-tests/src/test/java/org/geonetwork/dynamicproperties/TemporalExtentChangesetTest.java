/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.dynamicproperties;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.SneakyThrows;
import org.geonetwork.GeonetworkGenericApplication;
import org.geonetwork.infrastructure.ElasticPgMvcTestHelper;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.geonetwork.ogcapi.service.configuration.OgcApiPropertyMappingService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
 * Tests the 00036 changesets that add the "temporalExtent" facet to the property mapping in the DB (#260).
 *
 * <p>The test DB is seeded from application-integration-test.yml, which already has the facet, and all changesets are
 * marked as already run. This removes the facet from the DB and re-runs 00036, like an upgrade of an existing instance.
 */
@SpringBootTest(classes = GeonetworkGenericApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test", "integration-test"})
@ContextConfiguration(initializers = TemporalExtentChangesetTest.class)
public class TemporalExtentChangesetTest implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public final String MAIN_COLLECTION_ID = "3bef299d-cf82-4033-871b-875f6936b2e2";
    private static final List<String> CHANGESET_IDS =
            List.of("00036-ogcapi-field-mapping-temporal-extent", "00036-ogcapi-facet-config-temporal-extent");
    private static final String PROPERTY = "temporalExtent";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    DataSource dataSource;

    @Autowired
    OgcApiPropertyMappingService propertyMappingService;

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
            .withCopyToContainer(MountableFile.forClasspathResource("es_backups.tar.gz"), "/tmp/es_backups.tar.gz");

    public final int MVC_PORT = 8888;
    public final String BASE_URL = "http://localhost:" + MVC_PORT + "/";

    private static boolean changesetApplied = false;

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

    @BeforeEach
    void applyChangeset() throws Exception {
        if (changesetApplied) {
            return;
        }
        // remove the facet the YAML seed added, so the DB looks like an instance from before 00036
        var config = propertyMappingService.getConfig();
        config.setFields(config.getFields().stream()
                .filter(f -> !PROPERTY.equals(f.getOgcProperty()))
                .toList());
        propertyMappingService.updateConfig(config);
        assertEquals(0, countFieldRows());

        runChangeset();
        changesetApplied = true;
    }

    @Test
    public void testChangesetInsertsFacetOnce() throws Exception {
        assertEquals(1, countFieldRows());
        assertEquals(
                "0|1900|now/y",
                queryString("SELECT fc.minimum_document_count || '|' || fc.hard_bounds_min || '|' || fc.hard_bounds_max"
                        + " FROM ogcapi_facet_config fc"
                        + " JOIN ogcapi_field_mapping fm ON fm.id = fc.field_mapping_id"
                        + " WHERE fm.config_id = 'property-mapping' AND fm.ogc_property = 'temporalExtent'"));

        // running it again doesn't add a second row
        runChangeset();
        assertEquals(1, countFieldRows());
    }

    @Test
    public void testFacet() throws Exception {
        var items = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items",
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);

        var buckets = items.getFacets().get(PROPERTY).getBuckets();
        assertEquals(
                List.of("2021=1", "2022=1", "2023=1", "2024=2"),
                buckets.stream()
                        .map(b -> b.getMin().substring(0, 4) + "=" + b.getCount())
                        .toList());
    }

    /** Marks the 00036 data changesets as not run, runs the changelog, then reloads the property mapping. */
    private void runChangeset() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            for (var id : CHANGESET_IDS) {
                try (var statement = conn.prepareStatement("DELETE FROM databasechangelog WHERE id = ?")) {
                    statement.setString(1, id);
                    statement.executeUpdate();
                }
            }
            var database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            try (Liquibase liquibase = new Liquibase("db/changelog.xml", new ClassLoaderResourceAccessor(), database)) {
                liquibase.update(new Contexts("prod"), new LabelExpression());
            }
        }
        // the app doesn't watch the DB - re-save the config so the in-memory mapping reloads
        propertyMappingService.updateConfig(propertyMappingService.getConfig());
    }

    private int countFieldRows() throws Exception {
        return Integer.parseInt(queryString("SELECT count(*) FROM ogcapi_field_mapping"
                + " WHERE config_id = 'property-mapping' AND ogc_property = '" + PROPERTY + "'"));
    }

    private String queryString(String sql) throws Exception {
        try (Connection conn = dataSource.getConnection();
                var statement = conn.prepareStatement(sql);
                var rs = statement.executeQuery()) {
            rs.next();
            return rs.getString(1);
        }
    }
}
