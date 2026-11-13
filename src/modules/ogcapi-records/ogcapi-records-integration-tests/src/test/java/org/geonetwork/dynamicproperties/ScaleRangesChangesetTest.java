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
 * Tests the 00037 changeset that adds the resolutionScaleDenominatorRanges FILTER facet (#259).
 *
 * <p>The test DB is seeded from application-integration-test.yml, which doesn't have the facet, and all changesets are
 * marked as already run. This re-runs 00037 against that DB, like an upgrade of an existing instance.
 */
@SpringBootTest(classes = GeonetworkGenericApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test", "integration-test"})
@ContextConfiguration(initializers = ScaleRangesChangesetTest.class)
public class ScaleRangesChangesetTest implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public final String MAIN_COLLECTION_ID = "3bef299d-cf82-4033-871b-875f6936b2e2";
    private static final String CHANGESET_ID = "00037-ogcapi-facet-config-scale-ranges";
    private static final String FACET = "resolutionScaleDenominatorRanges";

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
        assertEquals(0, count("SELECT count(*) FROM ogcapi_facet_config WHERE facet_name = '" + FACET + "'"));
        runChangeset();
        changesetApplied = true;
    }

    @Test
    public void testChangesetInsertsFacetOnce() throws Exception {
        assertEquals(1, count("SELECT count(*) FROM ogcapi_facet_config WHERE facet_name = '" + FACET + "'"));
        assertEquals(4, countFilters());

        // running it again doesn't add a second facet
        runChangeset();
        assertEquals(1, count("SELECT count(*) FROM ogcapi_facet_config WHERE facet_name = '" + FACET + "'"));
        assertEquals(4, countFilters());
    }

    @Test
    public void testFacetCounts() throws Exception {
        // test data scales: 1000, 5000, 10000 and 25000 (4 records), and exactly 100000 (2 records), which falls in the
        // second range. Ranges come back in order; empty ranges are left out.
        var expected = List.of("1_upTo100000=4", "2_100000to1000000=2");

        // requested by name, and returned with all the facets when none is requested
        assertEquals(expected, buckets("items?limit=1&facets=" + FACET));
        assertEquals(expected, buckets("items?limit=1"));
    }

    private List<String> buckets(String path) throws Exception {
        return get(path).getFacets().get(FACET).getBuckets().stream()
                .map(b -> b.getValue() + "=" + b.getCount())
                .toList();
    }

    /** Marks 00037 as not run, runs the changelog (only 00037 is pending), then reloads the property mapping. */
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
        // the app doesn't watch the DB - re-save the config so the in-memory mapping reloads
        propertyMappingService.updateConfig(propertyMappingService.getConfig());
    }

    private int countFilters() throws Exception {
        return count(
                "SELECT count(*) FROM ogcapi_filter_facet ff JOIN ogcapi_facet_config fc ON fc.id = ff.facet_config_id"
                        + " WHERE fc.facet_name = '" + FACET + "'");
    }

    private int count(String sql) throws Exception {
        try (Connection conn = dataSource.getConnection();
                var statement = conn.prepareStatement(sql);
                var rs = statement.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    private OgcApiRecordsGetRecords200ResponseDto get(String path) throws Exception {
        return ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/" + path,
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);
    }
}
