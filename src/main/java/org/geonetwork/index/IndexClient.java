/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Index client. */
@Data
@Component
public class IndexClient {
  ElasticsearchClient esClient;
  private String serverUrl;
  private String defaultIndexPrefix;
  private String indexRecordName;

  /** Constructor. */
  public IndexClient(
      @Value("${geonetwork.index.url:'http://localhost:9200'}") String serverUrl,
      @Value("${geonetwork.index.indexPrefix:'gn-'}") String defaultIndexPrefix,
      @Value("${geonetwork.index.indexRecordName:'gn-records'}") String indexRecordName) {
    this.serverUrl = serverUrl;
    this.defaultIndexPrefix = defaultIndexPrefix;
    this.indexRecordName = indexRecordName;

    RestClient restClient = RestClient.builder(HttpHost.create(serverUrl)).build();

    JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper();

    ElasticsearchTransport transport = new RestClientTransport(restClient, jacksonJsonpMapper);

    esClient = new ElasticsearchClient(transport);
  }
}
