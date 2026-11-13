/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.dynamicproperties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.MountableFile;

/**
 * Tests the 00036 changeset that maps "time" to the temporal extent only.
 *
 * <p>The test DB is seeded from application-integration-test.yml, which has no "time" row, so each test inserts the row
 * as 00029 created it, then re-runs 00036 like an upgrade of an existing instance.
 */
@SpringBootTest(classes = GeonetworkGenericApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test", "integration-test"})
@ContextConfiguration(initializers = TimeTemporalExtentChangesetTest.class)
public class TimeTemporalExtentChangesetTest implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String CHANGESET_ID = "00036-ogcapi-field-mapping-time-temporal-extent";

    @Autowired
    DataSource dataSource;

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
    void resetTimeRow() throws Exception {
        execute("DELETE FROM ogcapi_field_mapping WHERE config_id = 'property-mapping' AND ogc_property = 'time'");
    }

    @Test
    public void testChangesetMapsTimeToTemporalExtent() throws Exception {
        insertTimeRow("resourceTemporalDateRange");
        runChangeset();
        assertEquals("resourceTemporalExtentDateRange", getTimeElasticProperty());
    }

    @Test
    public void testChangesetKeepsCustomMapping() throws Exception {
        insertTimeRow("myCustomDateRange");
        runChangeset();
        assertEquals("myCustomDateRange", getTimeElasticProperty());
    }

    /** Marks 00036 as not run and runs the changelog (only 00036 is pending). */
    private void runChangeset() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            try (var statement = conn.prepareStatement("DELETE FROM databasechangelog WHERE id = ?")) {
                statement.setString(1, CHANGESET_ID);
                statement.executeUpdate();
            }
            var database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            try (Liquibase liquibase = new Liquibase("db/changelog.xml", new ClassLoaderResourceAccessor(), database)) {
                liquibase.update(new Contexts("prod"), new LabelExpression());
            }
        }
    }

    private void insertTimeRow(String elasticProperty) throws Exception {
        try (Connection conn = dataSource.getConnection();
                var statement = conn.prepareStatement(
                        "INSERT INTO ogcapi_field_mapping (config_id, sort_order, ogc_property, elastic_property,"
                                + " is_sortable, is_queryable, add_property_to_output)"
                                + " VALUES ('property-mapping', 110, 'time', ?, false, false, true)")) {
            statement.setString(1, elasticProperty);
            statement.executeUpdate();
        }
    }

    private String getTimeElasticProperty() throws Exception {
        try (Connection conn = dataSource.getConnection();
                var statement = conn.prepareStatement("SELECT elastic_property FROM ogcapi_field_mapping"
                        + " WHERE config_id = 'property-mapping' AND ogc_property = 'time'")) {
            try (var rs = statement.executeQuery()) {
                rs.next();
                return rs.getString(1);
            }
        }
    }

    private void execute(String sql) throws Exception {
        try (Connection conn = dataSource.getConnection();
                var statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }
}
