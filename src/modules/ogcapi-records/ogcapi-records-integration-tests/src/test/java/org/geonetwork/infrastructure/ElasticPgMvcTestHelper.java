/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
import org.geonetwork.GeonetworkGenericApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.ExecConfig;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.MountableFile;

@SpringBootTest(classes = GeonetworkGenericApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test", "integration-test"})
@ContextConfiguration(initializers = ElasticPgMvcBaseTest.class)
public class ElasticPgMvcBaseTest implements ApplicationContextInitializer<ConfigurableApplicationContext> {



    static void beforeAll(PostgreSQLContainer postgreSQLContainer, ElasticsearchContainer elasticsearchContainer) throws IOException, InterruptedException {
        postgreSQLContainer.start();
        elasticsearchContainer.start();

        var client = RestClient.builder(HttpHost.create("http://" + elasticsearchContainer.getHttpHostAddress()))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    return httpClientBuilder;
                })
                .build();

        decompressElasticSnapshotTarball();
        createElasticRepository(client);
        importElasticSnapshot(client);
    }

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

    protected static void decompressElasticSnapshotTarball() throws IOException, InterruptedException {
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

    @AfterAll
    static void afterAll() throws IOException {
        postgreSQLContainer.stop();
        elasticsearchContainer.stop();
    }

    // ===========================================================================

    public final int MVC_PORT = 8888;
    public final String BASE_URL = "http://localhost:" + MVC_PORT + "/";

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        TestPropertyValues.of(
                        //        "geonetwork.url=" + getGeoNetworkCoreUrl(),
                        "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                        "spring.datasource.username=postgres",
                        "spring.datasource.password=postgres",
                        "spring.datasource.driverClassName=org.postgresql.Driver",
                        "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect",
                        "server.port=" + MVC_PORT,
                        "geonetwork.index.url=http://" + elasticsearchContainer.getHttpHostAddress())
                .applyTo(ctx.getEnvironment());
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    public String retrieveUrlJson(String endpoint) throws Exception {
        var jsonResult = mockMvc.perform(get(BASE_URL + endpoint).accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        return jsonResult;
    }

    public <T> T retrieveUrlJson(String endpoint, Class<T> clazz) throws Exception {
        var json = retrieveUrlJson(endpoint);
        return objectMapper.readValue(json, clazz);
    }

    public Authentication createAuthentication(String userName) {
        var user = new org.springframework.security.core.userdetails.User(
                userName, userName, new ArrayList<GrantedAuthority>());

        // sets authenticated=true
        var result = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<GrantedAuthority>());
        return result;
    }

    public String retrieveUrlJson(String endpoint, String username) throws Exception {
        var jsonResult = mockMvc.perform(get(BASE_URL + endpoint)
                        .with(authentication(createAuthentication(username)))
                        //        .apply(springSecurity())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return jsonResult;
    }

    public <T> T retrieveUrlJson(String endpoint, Class<T> clazz, String username) throws Exception {
        var json = retrieveUrlJson(endpoint, username);
        return objectMapper.readValue(json, clazz);
    }
}
