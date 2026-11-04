/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.permissions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.geonetwork.GeonetworkGenericApplication;
import org.geonetwork.index.client.IndexClient;
import org.geonetwork.infrastructure.ElasticPgMvcTestHelper;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.MountableFile;

/**
 * Tests that items and single records only return what the user is allowed to see. Uses a record of the sample data
 * only visible to group 2 (johndoe is a member, barbie and user are not) and a public record with a draft added like
 * GN4 does.
 */
@SpringBootTest(classes = GeonetworkGenericApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test", "integration-test"})
@ContextConfiguration(initializers = RecordPermissionTest.class)
public class RecordPermissionTest implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final String MAIN_COLLECTION_ID = "3bef299d-cf82-4033-871b-875f6936b2e2";
    public static final String ITEMS = "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items";

    static final String PRIVATE_GROUP = "2";
    static final String DRAFT_TITLE = "PermTest working copy title";

    static String privateUuid;
    static String publicUuid;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    IndexClient indexClient;

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
    void prepareRecords() throws Exception {
        if (publicUuid != null) {
            return;
        }
        var privateRecord = findRecord(Query.of(
                q -> q.bool(b -> b.filter(term("op0", PRIVATE_GROUP), term("isTemplate", "n"), term("draft", "n"))
                        .mustNot(term("op0", "1")))));
        privateUuid = privateRecord.get("uuid").asText();

        var publicRecord = findRecord(
                Query.of(q -> q.bool(b -> b.filter(term("op0", "1"), term("isTemplate", "n"), term("draft", "n")))));
        publicUuid = publicRecord.get("uuid").asText();

        // GN4 indexes the draft as <uuid>-draft with draft=y, and the approved record with draft=e
        publicRecord.put("draft", "e");
        index(publicUuid, publicRecord);
        var workingCopy = publicRecord.deepCopy();
        workingCopy.put("draft", "y");
        workingCopy.putObject("resourceTitleObject").put("default", DRAFT_TITLE);
        index(publicUuid + "-draft", workingCopy);
    }

    @Test
    public void publicRecordIsVisibleToAnonymous() throws Exception {
        assertEquals(200, getRecord(publicUuid, null, "json").getStatus());
        assertEquals(200, getRecord(publicUuid, null, "html").getStatus());
    }

    @Test
    public void privateRecordIsNotFoundForAnonymous() throws Exception {
        assertEquals(404, getRecord(privateUuid, null, "html").getStatus());
        assertEquals(404, getRecord(privateUuid, null, "json").getStatus());
    }

    @Test
    public void privateRecordIsNotFoundForUsersOutsideTheGroup() throws Exception {
        assertEquals(404, getRecord(privateUuid, "barbie", "html").getStatus());
        assertEquals(404, getRecord(privateUuid, "user", "html").getStatus());
        assertEquals(404, getRecord(privateUuid, "barbie", "json").getStatus());
    }

    @Test
    public void privateRecordIsVisibleToGroupMember() throws Exception {
        assertEquals(200, getRecord(privateUuid, "johndoe", "html").getStatus());
        assertEquals(200, getRecord(privateUuid, "johndoe", "json").getStatus());
    }

    @Test
    public void privateRecordIsVisibleToAdministrator() throws Exception {
        assertEquals(200, getRecord(privateUuid, "admin", "html").getStatus());
        assertEquals(200, getRecord(privateUuid, "admin", "json").getStatus());
    }

    @Test
    public void recordIdIsNotParsedAsAQuery() throws Exception {
        assertEquals(
                404, getRecord("nomatch OR _id:" + privateUuid, null, "html").getStatus());
        assertEquals(404, getRecord("nomatch OR uuid:*", null, "html").getStatus());
        assertEquals(404, getRecord("*", null, "html").getStatus());
    }

    @Test
    public void workingCopyIsNotReturnedAsSingleRecord() throws Exception {
        assertEquals(404, getRecord(publicUuid + "-draft", null, "html").getStatus());
        var html = getRecord(publicUuid, null, "html").getContentAsString();
        assertFalse(html.contains(DRAFT_TITLE), "the approved version is returned, not the working copy");
    }

    @Test
    public void searchDoesNotReturnWorkingCopies() throws Exception {
        var result = search("&ids=" + publicUuid, null);
        assertEquals(1, result.getNumberMatched());
        assertFalse(objectMapper.writeValueAsString(result).contains(DRAFT_TITLE));
    }

    @Test
    public void searchFiltersPrivateRecords() throws Exception {
        assertEquals(0, search("&ids=" + privateUuid, null).getNumberMatched());
        assertEquals(0, search("&ids=" + privateUuid, "barbie").getNumberMatched());
        assertEquals(1, search("&ids=" + privateUuid, "johndoe").getNumberMatched());
        assertEquals(1, search("&ids=" + privateUuid, "admin").getNumberMatched());
    }

    private MockHttpServletResponse getRecord(String recordId, String username, String format) throws Exception {
        var request = MockMvcRequestBuilders.get(BASE_URL + ITEMS + "/{recordId}?f={f}", recordId, format);
        if (username != null) {
            request.with(authentication(ElasticPgMvcTestHelper.createAuthentication(username)));
        }
        return mockMvc.perform(request).andReturn().getResponse();
    }

    private OgcApiRecordsGetRecords200ResponseDto search(String params, String username) throws Exception {
        var request = MockMvcRequestBuilders.get(BASE_URL + ITEMS + "?f=json" + params)
                .accept(MediaType.APPLICATION_JSON);
        if (username != null) {
            request.with(authentication(ElasticPgMvcTestHelper.createAuthentication(username)));
        }
        var response = mockMvc.perform(request).andReturn().getResponse();
        assertEquals(200, response.getStatus(), response.getContentAsString());
        return objectMapper.readValue(response.getContentAsString(), OgcApiRecordsGetRecords200ResponseDto.class);
    }

    private ObjectNode findRecord(Query query) throws Exception {
        var response = indexClient
                .getEsClient()
                .search(s -> s.index(indexClient.getIndexRecordName()).size(1).query(query), ObjectNode.class);
        assertFalse(response.hits().hits().isEmpty(), "sample data has no record for " + query);
        return response.hits().hits().getFirst().source();
    }

    private static Query term(String field, String value) {
        return Query.of(q -> q.term(t -> t.field(field).value(value)));
    }

    private void index(String id, ObjectNode document) throws Exception {
        indexClient.getEsClient().index(i -> i.index(indexClient.getIndexRecordName())
                .id(id)
                .document(document)
                .refresh(Refresh.True));
    }
}
