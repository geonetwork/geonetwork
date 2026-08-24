/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.index.client;

import static com.fasterxml.jackson.databind.DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/** Index client. */
@Data
@Component
@Slf4j
public class IndexClient implements InitializingBean {
    private final ElasticsearchClient esClient;
    private final ElasticsearchAsyncClient esAsynchClient;

    private String serverUrl;
    private String defaultIndexPrefix;
    private String indexRecordName;
    private boolean createIfEmpty;
    private Integer maxResultWindow;
    private Long totalFieldsLimit;
    private CredentialsProvider credentialsProvider;

    /**
     * Constructor.
     *
     * <p>objectMapper comes from a spring bean - cf. WebConfig.json. It MUST be configured with
     * .enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
     */
    public IndexClient(
            @Value("${geonetwork.index.url:'http://localhost:9200'}") String serverUrl,
            @Value("${geonetwork.index.username}") String username,
            @Value("${geonetwork.index.password}") String password,
            @Value("${geonetwork.index.indexPrefix:'gn-'}") String defaultIndexPrefix,
            @Value("${geonetwork.index.indexRecordName:'gn-records'}") String indexRecordName,
            @Value("${geonetwork.index.createIfEmpty:true}") boolean createIfEmpty,
            @Value("${geonetwork.index.elasticsearch.settings.maxResultWindow:50000}") Integer maxResultWindow,
            @Value("${geonetwork.index.elasticsearch.settings.mapping.totalFields:10000}") Long totalFieldsLimit,
            @Value("${geonetwork.indexing.requestimeout:45000}") int requestTimeout
            //      ,
            //            ObjectMapper objectMapper) {
            ) {
        this.serverUrl = serverUrl;
        this.defaultIndexPrefix = defaultIndexPrefix;
        this.indexRecordName = indexRecordName;
        this.createIfEmpty = createIfEmpty;
        this.maxResultWindow = maxResultWindow;
        this.totalFieldsLimit = totalFieldsLimit;

        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        }

        RestClientBuilder builder = RestClient.builder(HttpHost.create(serverUrl))
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder.setSocketTimeout(requestTimeout))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        httpClientBuilder.disableAuthCaching();
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        RestClient restClient = builder.build();

        JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper(objectMapper());

        ElasticsearchTransport transport = new RestClientTransport(restClient, jacksonJsonpMapper);

        esClient = new ElasticsearchClient(transport);
        esAsynchClient = new ElasticsearchAsyncClient(transport);
    }

    public ObjectMapper objectMapper() {

        var result = JsonMapper.builder()
                .enable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
                .enable(UNWRAP_SINGLE_VALUE_ARRAYS)
                .build();

        result.configure(UNWRAP_SINGLE_VALUE_ARRAYS, true);

        result.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        result.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, true);
        result.findAndRegisterModules();
        result.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        result.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return result;
    }

    /** Create index. */
    public void setupIndex(boolean dropIfExists) {
        InputStream jsonAnalysis;
        InputStream jsonMapping;
        try {
            jsonMapping = new ClassPathResource("index/records-mapping.json").getInputStream();
            jsonAnalysis = new ClassPathResource("index/records-analysis.json").getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            if (dropIfExists) {
                try {
                    BooleanResponse exists = esClient.indices().exists(e -> e.index(indexRecordName));
                    if (exists != null && exists.value()) {
                        DeleteIndexResponse delete =
                                esClient.indices().delete(deleteBuilder -> deleteBuilder.index(indexRecordName));
                        if (delete.acknowledged()) {
                            log.atDebug().log("Index {} deleted", indexRecordName);
                        }
                    }
                } catch (Exception e) {
                    log.atWarn().log("Could not delete existing index {}: {}", indexRecordName, e.getMessage());
                }
            }
            esClient.indices().create(indexBuilder -> indexBuilder
                    .index(indexRecordName)
                    .settings(settingsBuilder -> settingsBuilder
                            .maxResultWindow(maxResultWindow)
                            .mapping(mappingBuilder ->
                                    mappingBuilder.totalFields(b -> b.limit(String.valueOf(totalFieldsLimit))))
                            .analysis(a -> a.withJson(jsonAnalysis)))
                    .mappings(m -> m.withJson(jsonMapping)));
        } catch (IOException e) {
            log.atError().log("Errors while creating index {}. Error is: {}", indexRecordName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the configured index either does not exist or contains zero documents.
     *
     * @return true if index is missing or empty, false if index exists and contains records
     */
    public boolean isIndexMissingOrEmpty() {
        try {
            BooleanResponse exists = esClient.indices().exists(e -> e.index(indexRecordName));
            if (!exists.value()) {
                return true;
            }
            esClient.indices().refresh(r -> r.index(indexRecordName));
            CountResponse count = esClient.count(c -> c.index(indexRecordName));
            return count.count() == 0;
        } catch (Exception e) {
            log.atWarn().log("Could not determine status of index '{}': {}", indexRecordName, e.getMessage());
            return true;
        }
    }

    /** Initializes the index on startup if createIfEmpty is true and the index is missing or empty. */
    @Override
    public void afterPropertiesSet() {
        if (createIfEmpty) {
            try {
                if (isIndexMissingOrEmpty()) {
                    log.atInfo().log(
                            "Index '{}' is missing or empty and createIfEmpty is true. Initializing index mappings...",
                            indexRecordName);
                    setupIndex(true);
                    log.atInfo().log("Index '{}' successfully initialized.", indexRecordName);
                }
            } catch (Exception e) {
                log.atWarn()
                        .log(
                                "Could not automatically initialize index '{}' on startup: {}",
                                indexRecordName,
                                e.getMessage());
            }
        }
    }
}
