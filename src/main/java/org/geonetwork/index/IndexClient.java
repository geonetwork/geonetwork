/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.index;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class IndexClient {
    private String serverUrl;

    private String defaultIndexPrefix;

    private String indexRecordName;

    ElasticsearchClient esClient;

    public IndexClient(
        @Value("${geonetwork.index.url:'http://localhost:9200'}")
        String serverUrl,
        @Value("${geonetwork.index.indexPrefix:'gn-'}")
        String defaultIndexPrefix,
        @Value("${geonetwork.index.indexRecordName:'gn-records'}")
        String indexRecordName) {
        this.serverUrl = serverUrl;
        this.defaultIndexPrefix = defaultIndexPrefix;
        this.indexRecordName = indexRecordName;

        RestClient restClient = RestClient
            .builder(HttpHost.create(serverUrl))
            .build();

        JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper();

        ElasticsearchTransport transport = new RestClientTransport(
            restClient,
            jacksonJsonpMapper
        );

        esClient = new ElasticsearchClient(transport);
    }
}
