/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.infrastructure;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.ExecConfig;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
//
// @SpringBootTest(classes = GeonetworkGenericApplication.class)
// @AutoConfigureMockMvc
// @ActiveProfiles(value = {"test", "integration-test"})
// @ContextConfiguration(initializers = ElasticPgMvcTestHelper.class)

public class ElasticPgMvcTestHelper {

    public static void beforeAll(PostgreSQLContainer postgreSQLContainer, ElasticsearchContainer elasticsearchContainer)
            throws IOException, InterruptedException {}

    protected static void importElasticSnapshot(RestClient restClient) throws IOException {
        var request = new Request("POST", "/_snapshot/my_backup/snapshot_1/_restore");
        request.setJsonEntity("{\n" + "  \"indices\": \"*\",\n"
                + "  \"ignore_unavailable\": true,\n"
                + "  \"include_global_state\": false\n"
                + "}");
        Response response = restClient.performRequest(request);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IOException(response.getStatusLine().toString());
        }
    }

    protected static void decompressElasticSnapshotTarball(ElasticsearchContainer elasticsearchContainer)
            throws IOException, InterruptedException {
        var exec = ExecConfig.builder()
                //        .user("root")
                .workDir("/tmp")
                .command(new String[] {"tar", "-xvzf", "es_backups.tar.gz"})
                .build();
        elasticsearchContainer.execInContainer(exec);
    }

    public static void createElasticRepository(RestClient restClient) throws IOException {
        var request = new Request("PUT", "/_snapshot/my_backup");
        request.setJsonEntity("{\n" + "\"type\": \"fs\",\n"
                + "\"settings\": {\n"
                + "\"location\": \"/tmp/es_backups\",\n"
                + "\"compress\": true\n"
                + "}\n"
                + "}");
        Response response = restClient.performRequest(request);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IOException(response.getStatusLine().toString());
        }
    }

    public static void afterAll(PostgreSQLContainer postgreSQLContainer, ElasticsearchContainer elasticsearchContainer)
            throws IOException {
        postgreSQLContainer.stop();
        elasticsearchContainer.stop();
    }

    // ===========================================================================

    public static void initialize(
            ConfigurableApplicationContext ctx,
            PostgreSQLContainer postgreSQLContainer,
            ElasticsearchContainer elasticsearchContainer,
            int mvcPort)
            throws Exception {

        postgreSQLContainer.start();
        elasticsearchContainer.start();

        var client = RestClient.builder(HttpHost.create("http://" + elasticsearchContainer.getHttpHostAddress()))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    return httpClientBuilder;
                })
                .build();

        decompressElasticSnapshotTarball(elasticsearchContainer);
        createElasticRepository(client);
        importElasticSnapshot(client);

        TestPropertyValues.of(
                        //        "geonetwork.url=" + getGeoNetworkCoreUrl(),
                        "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                        "spring.datasource.username=postgres",
                        "spring.datasource.password=postgres",
                        "spring.datasource.driverClassName=org.postgresql.Driver",
                        "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect",
                        "server.port=" + mvcPort,
                        "geonetwork.index.url=http://" + elasticsearchContainer.getHttpHostAddress())
                .applyTo(ctx.getEnvironment());
    }

    public static String retrieveUrlJson(String endpoint, String BASE_URL, MockMvc mockMvc) throws Exception {
        var jsonResult = mockMvc.perform(get(BASE_URL + endpoint).accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        return jsonResult;
    }

    public static <T> T retrieveUrlJson(
            String endpoint, Class<T> clazz, String BASE_URL, MockMvc mockMvc, ObjectMapper objectMapper)
            throws Exception {
        var json = retrieveUrlJson(endpoint, BASE_URL, mockMvc);
        return objectMapper.readValue(json, clazz);
    }

    public static Authentication createAuthentication(String userName) {
        var user = new org.springframework.security.core.userdetails.User(
                userName, userName, new ArrayList<GrantedAuthority>());

        // sets authenticated=true
        var result = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<GrantedAuthority>());
        return result;
    }

    public static String retrieveUrlJson(String endpoint, String username, String BASE_URL, MockMvc mockMvc)
            throws Exception {
        var jsonResult = mockMvc.perform(get(BASE_URL + endpoint)
                        .with(authentication(createAuthentication(username)))
                        //        .apply(springSecurity())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return jsonResult;
    }

    public static <T> T retrieveUrlJson(
            String endpoint,
            Class<T> clazz,
            String username,
            String BASE_URL,
            MockMvc mockMvc,
            ObjectMapper objectMapper)
            throws Exception {
        var json = retrieveUrlJson(endpoint, username, BASE_URL, mockMvc);
        return objectMapper.readValue(json, clazz);
    }
}
