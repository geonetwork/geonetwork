/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.querybuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import java.util.Arrays;
import java.util.List;
import org.geonetwork.ogcapi.service.configuration.OgcApiSearchConfiguration;
import org.geonetwork.ogcapi.service.cql.CqlToElasticSearch;
import org.geonetwork.ogcapi.service.search.RecordsEsQueryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;

// from gn-mircoservices
class RecordsEsQueryBuilderTest {
    @MockitoBean
    OgcApiSearchConfiguration configuration;

    @BeforeEach
    public void setUp() {
        String[] fields = {"resourceTitleObject"};
        this.configuration = Mockito.mock(OgcApiSearchConfiguration.class);
        given(this.configuration.getSources()).willReturn(Arrays.asList(fields));
        given(this.configuration.getQueryFilter()).willReturn("+isTemplate:n");
    }

    @Test
    void buildQuerySingleRecord() {
        RecordsEsQueryBuilder queryBuilder = new RecordsEsQueryBuilder(configuration);
        String query = queryBuilder.buildQuerySingleRecord("abc", null, null);

        assertEquals(
                "{\"from\": 0, \"size\": 1, \"query\": {\"query_string\": {\"query\": \"+_id:\\\"abc\\\"  +isTemplate:n\"}}, \"_source\": {\"includes\": [\"*\"]}}",
                query);

        query = queryBuilder.buildQuerySingleRecord("abc", "+source:uio", null);

        assertEquals(
                "{\"from\": 0, \"size\": 1, \"query\": {\"query_string\": {\"query\": \"+_id:\\\"abc\\\" +source:uio +isTemplate:n\"}}, \"_source\": {\"includes\": [\"*\"]}}",
                query);

        String[] fields = {"resourceType", "cl_status"};
        query = queryBuilder.buildQuerySingleRecord("abc", "+source:uio", Arrays.asList(fields));

        assertEquals(
                "{\"from\": 0, \"size\": 1, \"query\": {\"query_string\": {\"query\": \"+_id:\\\"abc\\\" +source:uio +isTemplate:n\"}}, \"_source\": {\"includes\": [\"resourceType\", \"cl_status\"]}}",
                query);
    }

    @Test
    void escapeQueryString() {
        assertEquals(
                "Périmètres de remembrement urbain \\(PRU\\) \\- Service de visualisation REST",
                RecordsEsQueryBuilder.escapeQueryString(
                        "Périmètres de remembrement urbain (PRU) - Service de visualisation REST"));
        // field:value and balanced quotes are kept as query syntax
        assertEquals(
                "resourceTitleObject.default:\"a b\" d* e? f",
                RecordsEsQueryBuilder.escapeQueryString("resourceTitleObject.default:\"a b\" d* e? <f>"));
        // unbalanced quote is escaped
        assertEquals("a\\\"b", RecordsEsQueryBuilder.escapeQueryString("a\"b"));
    }

    @Test
    void defaultSortWithoutSearchIsUuid() {
        var sorts = buildSorts(new OgcApiQuery());

        assertEquals(List.of("uuid asc"), sorts);
    }

    @Test
    void defaultSortWithSearchIsRelevance() {
        var query = new OgcApiQuery();
        query.setQ(List.of("urban atlas"));

        assertEquals(List.of("_score desc", "uuid asc"), buildSorts(query));
    }

    @Test
    void explicitSortWinsOverRelevance() {
        var query = new OgcApiQuery();
        query.setQ(List.of("urban atlas"));
        query.setSortBy(List.of("-id"));

        assertEquals(List.of("uuid desc"), buildSorts(query));
    }

    @Test
    void sortByRelevance() {
        var query = new OgcApiQuery();
        query.setQ(List.of("urban atlas"));

        query.setSortBy(List.of("relevance"));
        assertEquals(List.of("_score desc", "uuid asc"), buildSorts(query));

        query.setSortBy(List.of("+relevance"));
        assertEquals(List.of("_score asc", "uuid asc"), buildSorts(query));

        // no extra tie-break when id is already part of the sort
        query.setSortBy(List.of("-relevance", "-id"));
        assertEquals(List.of("_score desc", "uuid desc"), buildSorts(query));
    }

    @Test
    void onlyTextSearchIsScored() throws Exception {
        var queryBuilder = new RecordsEsQueryBuilder(configuration);
        ReflectionTestUtils.setField(queryBuilder, "cqlToElasticSearch", Mockito.mock(CqlToElasticSearch.class));
        var query = new OgcApiQuery();
        query.setQ(List.of("urban atlas"));
        query.setType(List.of("series"));

        BoolQuery bool =
                queryBuilder.buildUnderlyingQuery(query, "+groupPublished:all").bool();

        assertEquals(1, bool.must().size());
        assertEquals("urban atlas", bool.must().get(0).queryString().query());
        // config filter, collection filter and type
        assertEquals(3, bool.filter().size());
        assertTrue(bool.filter().stream().anyMatch(f -> f.isTerms()));
    }

    private List<String> buildSorts(OgcApiQuery query) {
        var builder = new SearchRequest.Builder();
        new RecordsEsQueryBuilder(configuration).buildSearchRequest(builder, query, null);
        return builder.build().sort().stream().map(this::describe).toList();
    }

    private String describe(SortOptions sort) {
        if (sort.isScore()) {
            return "_score " + (sort.score().order() == SortOrder.Desc ? "desc" : "asc");
        }
        return sort.field().field() + " " + (sort.field().order() == SortOrder.Desc ? "desc" : "asc");
    }
}
