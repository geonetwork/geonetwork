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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Search",
        description =
                """
    Search endpoints to perform free text search and more complex queries using the Elasticsearch JSON Query DSL.

    Usage:
    * Use `GET /api/search?q=your+search+terms` for simple free text search.
    * Use `POST /api/search` with a JSON body for more complex queries.


    Default search behaviour:
    * Search results are filtered based on the user's permissions.
    * Search results include metadata templates (use `isTemplate: n` filter to only focus on records).


    For more information see [Elasticsearch search operation](https://www.elastic.co/docs/api/doc/elasticsearch/operation/operation-search)
    and [query DSL](https://www.elastic.co/docs/explore-analyze/query-filter/languages/querydsl).
    """)
public class SearchController {

    public static final String API_OPERATION_SEARCH = "Execute a search and return matching documents.";
    private final IndexClient client;
    private final Counter searchCounter;
    private SearchUtils searchUtils;

    @Autowired
    public SearchController(IndexClient client, MeterRegistry meterRegistry, SearchUtils searchUtils) {
        this.client = client;
        this.searchUtils = searchUtils;
        searchCounter = meterRegistry.counter("gn.search.count");
    }

    /** Search. */
    @io.swagger.v3.oas.annotations.Operation(
            summary = API_OPERATION_SEARCH,
            description = "Use GET operation for simple search.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String getQuery(
            @Parameter(
                            description =
                                    "Free text search. For more information see [Elasticsearch query string parameter](https://www.elastic.co/docs/reference/query-languages/query-dsl/query-dsl-query-string-query)",
                            required = false,
                            examples = {
                                @ExampleObject(name = "Full text search on africa", value = "africa"),
                                @ExampleObject(name = "Filter by resource type", value = "+resourceType:dataset"),
                                @ExampleObject(
                                        name = "Focus on records (ie. no template)",
                                        value = "marine +isTemplate:n"),
                                @ExampleObject(name = "Dataset or series", value = "resourceType:(dataset OR series)"),
                                @ExampleObject(name = "Not isHarvested", value = "-isHarvested:false")
                            })
                    @RequestParam(defaultValue = "", required = false)
                    String q,
            @Parameter(
                            description = "The from parameter defines the number of hits to skip, defaulting to 0",
                            required = false)
                    @RequestParam(defaultValue = "0", required = false)
                    Integer from,
            @Parameter(description = "The size parameter is the maximum number of hits to return", required = false)
                    @RequestParam(defaultValue = "10", required = false)
                    Integer size)
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
    @io.swagger.v3.oas.annotations.Operation(
            summary = API_OPERATION_SEARCH,
            description = "Use POST operation for complex queries using the Elasticsearch JSON Query DSL.")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String query(
            // TODO
            @RequestParam(defaultValue = SelectionManager.SELECTION_METADATA) String bucket,
            // TODO
            @Parameter(
                            description = "Type of related resource. If none, no associated resource returned.",
                            required = false)
                    @RequestParam(name = "relatedType", defaultValue = "")
                    RelatedItemType[] relatedTypes,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "JSON payload based on Elasticsearch API.",
                            content =
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            examples = {
                                                @ExampleObject(value = "{\"query\":{\"match\":{\"uuid\":\"ABC\"}}}"),
                                                @ExampleObject(
                                                        value =
                                                                "{\"query\":{\"query_string\":{\"query\":\"+resourceType:dataset\"}}}")
                                            }))
                    @RequestBody
                    String jsonSearchRequest)
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
        searchRequest = searchUtils.buildRequestWithPermissionFilter(searchRequest);
        SearchResponse<IndexRecord> searchResponse = client.getEsClient().search(searchRequest, IndexRecord.class);
        searchCounter.increment();
        return searchResponse;
    }
}
