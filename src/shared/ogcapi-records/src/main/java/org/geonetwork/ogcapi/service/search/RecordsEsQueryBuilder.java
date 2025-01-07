/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.search;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoBoundingBoxQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.util.ObjectBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.geometry.Rectangle;
import org.geonetwork.ogcapi.service.SearchConfiguration;
import org.geonetwork.ogcapi.service.querybuilder.OgcApiQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** creates an elastic query based on incoming ogcapi-record query. From gn-microservices. */
@Component
// @ConstructorBinding
@Slf4j(topic = "org.geonetwork.ogcapi.service.search")
public class RecordsEsQueryBuilder {

    private static final String SORT_BY_SEPARATOR = ",";
    // TODO: Sources depends on output type
    private static final List<String> defaultSources = Arrays.asList(
            "resourceTitleObject",
            "resourceAbstractObject",
            "resourceType",
            "resourceDate",
            "id",
            "metadataIdentifier",
            "schema",
            "link",
            "allKeywords",
            "contact",
            "contactForResource",
            "cl_status",
            "edit",
            "tag",
            "changeDate",
            "createDate",
            "mainLanguage",
            "geom",
            "formats",
            "resourceTemporalDateRange",
            "resourceTemporalExtentDateRange");

    @Autowired
    QueryToElastic queryToElastic;

    @Autowired
    private SearchConfiguration configuration;

    public RecordsEsQueryBuilder(SearchConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Creates a ElasticSearch query for a single record.
     *
     * @param uuid Record uuid.
     * @param collectionFilter Filter to select the record in a collection scope.
     * @param includes List of fields to return (null, retuns all).
     * @return ElasticSearch query.
     */
    public String buildQuerySingleRecord(String uuid, String collectionFilter, List<String> includes) {

        return String.format(
                "{\"from\": %d, \"size\": %d, "
                        + "\"query\": {\"query_string\": "
                        + "{\"query\": \"+_id:\\\"%s\\\" %s %s\"}}, "
                        + "\"_source\": {\"includes\": [%s]}}",
                0,
                1,
                uuid,
                collectionFilter == null ? "" : collectionFilter,
                configuration.getQueryFilter(),
                includes == null ? "\"*\"" : includes.stream().collect(Collectors.joining("\", \"", "\"", "\"")));
    }

    /**
     * This sets up Elastic "outer" Query (i.e. sources, offset, limit). See buildQuery3() for the actual filter (i.e. *
     * full text).
     *
     * @param elasticSearchRequestBuilder builder (from elastic)
     * @param ogcApiQuery user's query
     * @param collectionFilter filter used to define the collection (DB `source` table)
     * @param sourceFields what fields to return from elastic
     */
    public void buildQuery(
            SearchRequest.Builder elasticSearchRequestBuilder,
            OgcApiQuery ogcApiQuery,
            String collectionFilter,
            Set<String> sourceFields) {

        elasticSearchRequestBuilder.from(ogcApiQuery.getStartIndex()).size(ogcApiQuery.getLimit());

        if (ogcApiQuery.getSortBy() != null) {
            ogcApiQuery.getSortBy().forEach(s -> Stream.of(s.split(SORT_BY_SEPARATOR))
                    .forEach(order -> {
                        var isDescending = order.startsWith("-");
                        var sortOrder = isDescending ? SortOrder.Desc : SortOrder.Asc;
                        var fieldName = order.replaceAll("^[\\+-]", "");
                        var sort = new SortOptions.Builder()
                                .field(f -> f.field(fieldName).order(sortOrder))
                                .build();
                        elasticSearchRequestBuilder.sort(sort);
                    }));
        }

        List<String> sources = new ArrayList<>(defaultSources);
        if (sourceFields != null) {
            sources.addAll(sourceFields);
        } else {
            sources.addAll(configuration.getSources());
        }
        elasticSearchRequestBuilder.source(SourceConfig.of(s -> s.filter(f -> f.includes(sources))));

        elasticSearchRequestBuilder.trackTotalHits(f -> f.enabled(true));

        elasticSearchRequestBuilder.query(builder -> buildQuery3(builder, ogcApiQuery, collectionFilter));
    }

    /**
     * this builds the actual filter/query.
     *
     * @param builder from elastic
     * @param ogcApiQuery from user
     * @param collectionFilter from DB source field
     * @return Query for the user's request.
     */
    private ObjectBuilder<Query> buildQuery3(Query.Builder builder, OgcApiQuery ogcApiQuery, String collectionFilter) {

        Query externalIdsQuery = null;
        Query geoQuery = null;
        Query configQuery = null;
        Query collectionQuery = null;
        Query queryablesQuery = null;

        Query textSearchQuery = QueryStringQuery.of(q -> q.query(buildFullTextSearchQuery(ogcApiQuery.getQ())))
                ._toQuery();

        if (ogcApiQuery.getExternalIds() != null
                && !ogcApiQuery.getExternalIds().isEmpty()) {
            externalIdsQuery = TermsQuery.of(q -> q.field("metadataIdentifier")
                            .terms(t -> t.value(ogcApiQuery.getExternalIds().stream()
                                    .map(x -> FieldValue.of(x))
                                    .toList())))
                    ._toQuery();
        }

        if (ogcApiQuery.getBbox() != null && ogcApiQuery.getBbox().size() == 4) {

            Rectangle rectangle = new Rectangle(
                    ogcApiQuery.getBbox().get(0).doubleValue(),
                    ogcApiQuery.getBbox().get(2).doubleValue(),
                    ogcApiQuery.getBbox().get(3).doubleValue(),
                    ogcApiQuery.getBbox().get(1).doubleValue());

            geoQuery = GeoBoundingBoxQuery.of(q -> q.field("geom")
                            .boundingBox(b -> b.trbl(trbl -> trbl.bottomLeft(
                                            x -> x.coords(Arrays.asList(rectangle.getMinX(), rectangle.getMinY())))
                                    .topRight(x -> x.coords(Arrays.asList(rectangle.getMaxX(), rectangle.getMaxY()))))))
                    ._toQuery();
        }

        if (!StringUtils.isBlank(configuration.getQueryFilter())) {
            configQuery = QueryStringQuery.of(q -> q.query("(" + configuration.getQueryFilter() + ")"))
                    ._toQuery();
        }

        if (!StringUtils.isBlank(collectionFilter)) {
            collectionQuery = QueryStringQuery.of(q -> q.query("(" + collectionFilter + ")"))
                    ._toQuery();
        }

        if (ogcApiQuery.getPropValues() != null && !ogcApiQuery.getPropValues().isEmpty()) {
            queryablesQuery = queryToElastic.getQueryablesQuery(ogcApiQuery);
        }

        var filters = Stream.of(
                        textSearchQuery, externalIdsQuery, geoQuery, configQuery, collectionQuery, queryablesQuery)
                .filter(Objects::nonNull)
                .toList();

        builder.bool(b -> b.must(filters));

        return builder;
    }

    /**
     * given some search strings, return a simplified elastic search.
     *
     * @param q from the ogcapi-records query
     * @return elastic text query
     */
    private String buildFullTextSearchQuery(List<String> q) {
        String queryString = "*:*";
        if (q != null && !q.isEmpty()) {
            String values = q.stream().collect(Collectors.joining(" AND "));
            if (StringUtils.isNotEmpty(configuration.getQueryBase())) {
                queryString = configuration.getQueryBase().replaceAll("\\$\\{any\\}", values);
            } else {
                queryString = values;
            }
        }
        return queryString;
    }
}
