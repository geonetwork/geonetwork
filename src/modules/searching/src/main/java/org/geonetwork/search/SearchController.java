/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.search;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonpUtils;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.IOException;
import java.io.StringReader;
import org.geonetwork.index.client.IndexClient;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.search.model.RelatedItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Search controller. */
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

    /** Search. */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String getQuery(
            @RequestParam(defaultValue = "", required = false) String q,
            @RequestParam(defaultValue = "0", required = false) Integer from,
            @RequestParam(defaultValue = "10", required = false) Integer size)
            throws IOException {
        SearchRequest searchRequest = SearchRequest.of(s -> s.index(client.getIndexRecordName())
                .q(q)
                .size(size)
                .from(from)
                .trackTotalHits(tth -> tth.enabled(true)));
        SearchResponse<IndexRecord> searchResponse = runSearch(searchRequest);
        return JsonpUtils.toJsonString(searchResponse, new JacksonJsonpMapper());
    }

    /** Search using POST. */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String query(
            @RequestParam(defaultValue = SelectionManager.SELECTION_METADATA) String bucket,
            @Parameter(
                            description = "Type of related resource. If none, no associated resource returned.",
                            required = false)
                    @RequestParam(name = "relatedType", defaultValue = "")
                    RelatedItemType[] relatedTypes,
            @RequestBody String jsonSearchRequest)
            throws IOException {
        SearchRequest searchRequest = SearchRequest.of(
                b -> b.index(client.getIndexRecordName()).withJson(new StringReader(jsonSearchRequest)));
        SearchResponse<IndexRecord> searchResponse = runSearch(searchRequest);
        return JsonpUtils.toJsonString(searchResponse, new JacksonJsonpMapper());
    }

    /** Get a document from the index. */
    @GetMapping(path = "doc/{uuid:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public IndexRecord getIndexDocument(@PathVariable String uuid) throws Exception {
        SearchRequest searchRequest = SearchRequest.of(s -> s.index(client.getIndexRecordName())
                .query(q -> q.match(m -> m.field("_id").query(uuid)))
                .size(1)
                .from(0)
                .trackTotalHits(tth -> tth.enabled(true)));
        SearchResponse<IndexRecord> searchResponse = runSearch(searchRequest);

        if ((searchResponse.hits().total() != null
                        ? searchResponse.hits().total().value()
                        : 0)
                == 1) {
            return searchResponse.hits().hits().getFirst().source();
        } else {
            throw new Exception(String.format("Index document %s not found or not shared with you.", uuid));
        }
    }

    private SearchResponse<IndexRecord> runSearch(SearchRequest searchRequest) throws IOException {
        searchRequest = SearchUtils.buildRequestWithPermissionFilter(searchRequest);
        SearchResponse<IndexRecord> searchResponse = client.getEsClient().search(searchRequest, IndexRecord.class);
        searchCounter.increment();
        // TODO: Not supported ? return searchResponse;
        return searchResponse;
    }
}
