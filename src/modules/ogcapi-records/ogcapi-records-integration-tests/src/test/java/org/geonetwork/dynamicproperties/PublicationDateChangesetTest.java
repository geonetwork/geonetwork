/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.dynamicproperties;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
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
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonSchemaDto;
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
 * Tests the 00035 changeset that adds "issued" (the resource publication date) to the property mapping in the DB
 * (#246).
 *
 * <p>The test DB is seeded from application-integration-test.yml, which doesn't have the property, and all changesets
 * are marked as already run. This re-runs 00035 against that DB, like an upgrade of an existing instance.
 */
@SpringBootTest(classes = GeonetworkGenericApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test", "integration-test"})
@ContextConfiguration(initializers = PublicationDateChangesetTest.class)
public class PublicationDateChangesetTest implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public final String MAIN_COLLECTION_ID = "3bef299d-cf82-4033-871b-875f6936b2e2";
    private static final String CHANGESET_ID = "00035-ogcapi-field-mapping-publication-date";
    private static final String PROPERTY = "issued";

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
        assertEquals(0, countFieldRows(), "the YAML seed shouldn't have " + PROPERTY);
        runChangeset();
        changesetApplied = true;
    }

    @Test
    public void testChangesetInsertsFieldOnce() throws Exception {
        assertEquals(1, countFieldRows());

        // running it again doesn't add a second row
        runChangeset();
        assertEquals(1, countFieldRows());
    }

    @Test
    public void testItemOutput() throws Exception {
        // resource publication date - not the metadata record's createDate
        var props = getItemAdditionalProperties("00487222-9fc0-490c-8f28-07dda6df08fc");
        assertEquals(List.of("2018-06-01"), props.get(PROPERTY));

        // all publication dates are returned
        props = getItemAdditionalProperties("02a63353-b1ad-487b-a343-b4971eaf4a98");
        assertEquals(List.of("2020-07-06", "2020-10-08"), props.get(PROPERTY));

        // no publication date -> property is left out
        props = getItemAdditionalProperties("8698bf0b-fceb-4f0f-989b-111e7c4af0a4");
        assertFalse(props.containsKey(PROPERTY));
    }

    @Test
    public void testSortableAndQueryable() throws Exception {
        var sortables = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/sortables",
                OgcApiRecordsJsonSchemaDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);
        assertEquals("date", sortables.getProperties().get(PROPERTY).getFormat());

        // /queryables (and ?property=value filters) use a list built once at startup (QueryablesService), so they
        // don't see a field added while the app is running - a CQL filter resolves it from the reloaded mapping
        var filtered = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?filter=" + PROPERTY + "='2018-06-01'",
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);
        assertEquals(1, filtered.getNumberMatched());
        assertEquals(
                "00487222-9fc0-490c-8f28-07dda6df08fc",
                filtered.getFeatures().get(0).getId());

        var items = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?sortby=-" + PROPERTY + "&limit=1",
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);
        assertEquals(
                List.of("2024-05-31T11:00:00+00:00"),
                items.getFeatures()
                        .get(0)
                        .getProperties()
                        .getAdditionalProperties()
                        .get(PROPERTY));
    }

    /** Marks 00035 as not run, runs the changelog (only 00035 is pending), then reloads the property mapping. */
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

    private int countFieldRows() throws Exception {
        try (Connection conn = dataSource.getConnection();
                var statement = conn.prepareStatement(
                        "SELECT count(*) FROM ogcapi_field_mapping WHERE config_id = 'property-mapping' AND ogc_property = ?")) {
            statement.setString(1, PROPERTY);
            try (var rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private Map<String, Object> getItemAdditionalProperties(String uuid) throws Exception {
        var items = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?id=" + uuid,
                OgcApiRecordsGetRecords200ResponseDto.class,
                BASE_URL,
                mockMvc,
                objectMapper);
        assertEquals(1, items.getNumberMatched());
        return items.getFeatures().get(0).getProperties().getAdditionalProperties();
    }
}
