/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.sitemap;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.SneakyThrows;
import org.geonetwork.GeonetworkGenericApplication;
import org.geonetwork.infrastructure.ElasticPgMvcTestHelper;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.geonetwork.ogcapi.service.ogcapi.OgcApiItemsApi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.MountableFile;
import org.w3c.dom.Element;

/** Tests for /collections/{catalogId}/items?f=sitemap */
@SpringBootTest(classes = GeonetworkGenericApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test", "integration-test"})
@ContextConfiguration(initializers = SitemapTest.class)
public class SitemapTest implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public final String MAIN_COLLECTION_ID = "3bef299d-cf82-4033-871b-875f6936b2e2";
    private static final String SITEMAP_NAMESPACE = "http://www.sitemaps.org/schemas/sitemap/0.9";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    OgcApiItemsApi itemsApi;

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

    @Test
    public void sitemapDefaultsToOnePage() throws Exception {
        var response = perform("ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?f=sitemap", null);

        assertEquals(200, response.getStatus());
        assertTrue(MediaType.APPLICATION_XML.isCompatibleWith(MediaType.valueOf(response.getContentType())));
        assertEquals(10, locs(response).size());
    }

    @Test
    public void sitemapIsNegotiatedWithAcceptHeader() throws Exception {
        var response = perform(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items",
                MediaType.valueOf("application/sitemap+xml"));

        assertEquals(200, response.getStatus());
        assertEquals(10, locs(response).size());
    }

    @Test
    public void sitemapWithLimitMinusOneReturnsAllMatchingRecords() throws Exception {
        // small pages, so that search_after paging is exercised with the test data
        ReflectionTestUtils.setField(itemsApi, "allRecordsPageSize", 7);
        try {
            var allLocs = locs(
                    perform("ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?f=sitemap&limit=-1", null));
            var allCount = numberMatched("");
            assertTrue(allCount > 7);
            assertEquals(allCount, allLocs.size());
            assertEquals(allLocs.size(), new HashSet<>(allLocs).size(), "no duplicate records across pages");

            var seriesLocs = locs(perform(
                    "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                            + "/items?f=sitemap&resourceType=series&limit=-1",
                    null));
            var seriesCount = numberMatched("&resourceType=series");
            assertTrue(seriesCount > 0);
            assertTrue(seriesCount < allLocs.size());
            assertEquals(seriesCount, seriesLocs.size());

            // offset and sortby still apply
            var sortedLocs = locs(perform(
                    "ogcapi-records/collections/" + MAIN_COLLECTION_ID
                            + "/items?f=sitemap&limit=-1&offset=3&sortby=-id",
                    null));
            assertEquals(allCount - 3, sortedLocs.size());
            assertEquals(allLocs.reversed().subList(3, allLocs.size()), sortedLocs);
        } finally {
            ReflectionTestUtils.setField(itemsApi, "allRecordsPageSize", 1000);
        }
    }

    @Test
    public void sitemapRejectsInvalidLimit() throws Exception {
        var response = perform("ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?f=sitemap&limit=0", null);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void otherFormatsAreUnchanged() throws Exception {
        // limit=-1 is only supported for sitemaps; other formats still reject it (currently as a 500, as for any
        // value outside the generated API's limit constraints)
        var response = perform("ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?f=json&limit=-1", null);
        assertTrue(response.getStatus() >= 400);

        // without f=..., a client accepting anything still gets JSON
        response = perform("ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items", MediaType.ALL);
        assertEquals(200, response.getStatus());
        assertTrue(MediaType.APPLICATION_JSON.isCompatibleWith(MediaType.valueOf(response.getContentType())));
    }

    private MockHttpServletResponse perform(String endpoint, MediaType accept) throws Exception {
        var request = MockMvcRequestBuilders.get(BASE_URL + endpoint);
        if (accept != null) {
            request.accept(accept);
        }
        return mockMvc.perform(request).andReturn().getResponse();
    }

    private long numberMatched(String extraParams) throws Exception {
        var json = ElasticPgMvcTestHelper.retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items?f=json&limit=1" + extraParams,
                BASE_URL,
                mockMvc);
        return objectMapper
                .readValue(json, OgcApiRecordsGetRecords200ResponseDto.class)
                .getNumberMatched();
    }

    private static List<String> locs(MockHttpServletResponse response) throws Exception {
        var factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        var document = factory.newDocumentBuilder()
                .parse(new ByteArrayInputStream(response.getContentAsString().getBytes(StandardCharsets.UTF_8)));
        var root = document.getDocumentElement();
        assertEquals(SITEMAP_NAMESPACE, root.getNamespaceURI());
        assertEquals("urlset", root.getLocalName());

        var urls = root.getElementsByTagNameNS(SITEMAP_NAMESPACE, "url");
        var result = new ArrayList<String>();
        for (int i = 0; i < urls.getLength(); i++) {
            var loc = ((Element) urls.item(i)).getElementsByTagNameNS(SITEMAP_NAMESPACE, "loc");
            assertEquals(1, loc.getLength());
            result.add(loc.item(0).getTextContent());
        }
        return result;
    }
}
