/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.search;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonpUtils;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.geonetwork.index.IndexClient;
import org.geonetwork.index.model.record.IndexRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final IndexClient client;
    private final Counter searchCounter;

    @Autowired
    public SearchController(IndexClient client, MeterRegistry meterRegistry) {
        this.client = client;
        searchCounter = meterRegistry.counter("gn.search.count");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String search(
        @RequestParam(defaultValue = "", required = false)
        String q,
        @RequestParam(defaultValue = "0", required = false)
        Integer from,
        @RequestParam(defaultValue = "10", required = false)
        Integer size
    ) throws IOException {
        SearchRequest searchRequest = SearchRequest.of(s ->
            s.index(client.getIndexRecordName())
                .q(q)
                .size(size)
                .from(from)
                .trackTotalHits(tth -> tth.enabled(true))
        );
        searchRequest = SearchUtils.buildRequestWithPermissionFilter(searchRequest);
        SearchResponse<IndexRecord> searchResponse = client.getEsClient().search(searchRequest, IndexRecord.class);
        searchCounter.increment();
        return JsonpUtils.toJsonString(searchResponse, new JacksonJsonpMapper());
        // return searchResponse;
        // TODO: Return SearchResponse instead
    }
}
