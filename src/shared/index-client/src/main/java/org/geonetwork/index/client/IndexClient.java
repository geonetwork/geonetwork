/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.client;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/** Index client. */
@Data
@Component
@Slf4j(topic = "org.geonetwork.tasks.index")
public class IndexClient {
  private final ElasticsearchClient esClient;
  private final ElasticsearchAsyncClient esAsynchClient;

  private String serverUrl;
  private String defaultIndexPrefix;
  private String indexRecordName;
  private Integer maxResultWindow;
  private Long totalFieldsLimit;
  private CredentialsProvider credentialsProvider;

  /** Constructor. */
  public IndexClient(
      @Value("${geonetwork.index.url:'http://localhost:9200'}") String serverUrl,
      @Value("${geonetwork.index.username}") String username,
      @Value("${geonetwork.index.password}") String password,
      @Value("${geonetwork.index.indexPrefix:'gn-'}") String defaultIndexPrefix,
      @Value("${geonetwork.index.indexRecordName:'gn-records'}") String indexRecordName,
      @Value("${geonetwork.index.elasticsearch.settings.maxResultWindow:50000}")
          Integer maxResultWindow,
      @Value("${geonetwork.index.elasticsearch.settings.mapping.totalFields:10000}")
          Long totalFieldsLimit,
      @Value("${geonetwork.indexing.requestimeout:45000}") int requestTimeout) {
    this.serverUrl = serverUrl;
    this.defaultIndexPrefix = defaultIndexPrefix;
    this.indexRecordName = indexRecordName;
    this.maxResultWindow = maxResultWindow;
    this.totalFieldsLimit = totalFieldsLimit;

    if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
      credentialsProvider = new BasicCredentialsProvider();
      credentialsProvider.setCredentials(
          AuthScope.ANY, new UsernamePasswordCredentials(username, password));
    }

    RestClientBuilder builder =
        RestClient.builder(HttpHost.create(serverUrl))
            .setRequestConfigCallback(
                requestConfigBuilder -> requestConfigBuilder.setSocketTimeout(requestTimeout))
            .setHttpClientConfigCallback(
                new RestClientBuilder.HttpClientConfigCallback() {
                  @Override
                  public HttpAsyncClientBuilder customizeHttpClient(
                      HttpAsyncClientBuilder httpClientBuilder) {
                    httpClientBuilder.disableAuthCaching();
                    return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                  }
                });

    RestClient restClient = builder.build();

    JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper();

    ElasticsearchTransport transport = new RestClientTransport(restClient, jacksonJsonpMapper);

    esClient = new ElasticsearchClient(transport);
    esAsynchClient = new ElasticsearchAsyncClient(transport);
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
          DeleteIndexResponse delete =
              esClient.indices().delete(deleteBuilder -> deleteBuilder.index(indexRecordName));
          if (delete.acknowledged()) {
            log.atDebug().log("Index {} deleted", indexRecordName);
          }
        } catch (Exception e) {
          log.atError()
              .log("Errors while deleting index {}. Error is: {}", indexRecordName, e.getMessage());
        }
      }
      esClient
          .indices()
          .create(
              indexBuilder ->
                  indexBuilder
                      .index(indexRecordName)
                      .settings(
                          settingsBuilder ->
                              settingsBuilder
                                  .maxResultWindow(maxResultWindow)
                                  .mapping(
                                      mappingBuilder ->
                                          mappingBuilder.totalFields(
                                              b -> b.limit(totalFieldsLimit)))
                                  .analysis(a -> a.withJson(jsonAnalysis)))
                      .mappings(m -> m.withJson(jsonMapping)));
    } catch (IOException e) {
      log.atError()
          .log("Errors while creating index {}. Error is: {}", indexRecordName, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
