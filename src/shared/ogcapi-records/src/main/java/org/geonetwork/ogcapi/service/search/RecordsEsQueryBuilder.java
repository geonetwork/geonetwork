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
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoBoundingBoxQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
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
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGnElasticDto;
import org.geonetwork.ogcapi.service.configuration.OgcApiSearchConfiguration;
import org.geonetwork.ogcapi.service.cql.CqlToElasticSearch;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.DynamicPropertiesFacade;
import org.geonetwork.ogcapi.service.querybuilder.OgcApiQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** creates an elastic query based on incoming ogcapi-record query. From gn-microservices. */
@Component
// @ConstructorBinding
@Slf4j(topic = "org.geonetwork.ogcapi.service.search")
public class RecordsEsQueryBuilder {

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
    private OgcApiSearchConfiguration configuration;

    @Autowired
    CqlToElasticSearch cqlToElasticSearch;

    @Autowired
    DynamicPropertiesFacade dynamicPropertiesFacade;

    public RecordsEsQueryBuilder(OgcApiSearchConfiguration configuration) {
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
     * This sets up Elastic "outer" Query (i.e. sources, offset, limit). See buildActualQuery() for the actual filter
     * (i.e. * full text).
     *
     * @param elasticSearchRequestBuilder builder (from elastic)
     * @param ogcApiQuery user's query
     * @param sourceFields what fields to return from elastic
     */
    public void buildSearchRequest(
            SearchRequest.Builder elasticSearchRequestBuilder, OgcApiQuery ogcApiQuery, Set<String> sourceFields) {

        elasticSearchRequestBuilder.from(ogcApiQuery.getStartIndex()).size(ogcApiQuery.getLimit());

        if (ogcApiQuery.getSortBy() != null && !ogcApiQuery.getSortBy().isEmpty()) {
            var sorts = new ArrayList<SortOptions>();
            ogcApiQuery.getSortBy().forEach(order -> {
                var isDescending = order.startsWith("-");
                var sortOrder = isDescending ? SortOrder.Desc : SortOrder.Asc;
                var fieldName = order.replaceAll("^[\\+-]", "");
                var userconfig = this.dynamicPropertiesFacade.getUserConfigByOgcProperty(fieldName);

                String elasticFieldName = "uuid";
                if (!fieldName.equals("id")) {
                    // TODO: don't hardcode this - see  OgcApiCollectionsApi
                    var elasticPropertyName = userconfig.getElasticProperty();
                    if (StringUtils.isNotEmpty(userconfig.getSortFieldSuffix())) {
                        elasticPropertyName += "." + userconfig.getSortFieldSuffix();
                    }
                    elasticFieldName = elasticPropertyName;
                }

                var _elasticFieldName = elasticFieldName;
                var sort = new SortOptions.Builder()
                        .field(f -> f.field(_elasticFieldName).order(sortOrder))
                        .build();
                sorts.add(sort);
            });
            elasticSearchRequestBuilder.sort(sorts);
        } else {
            // default sorting
            // TODO: hardcoded id - should be configurable (see OgcApiCollectionsApi)
            var sort = new SortOptions.Builder()
                    .field(f -> f.field("uuid").order(SortOrder.Asc))
                    .build();
            elasticSearchRequestBuilder.sort(sort);
        }

        List<String> sources = new ArrayList<>(defaultSources);
        if (sourceFields != null) {
            sources.addAll(sourceFields);
        } else {
            sources.addAll(configuration.getSources());
        }
        elasticSearchRequestBuilder.source(SourceConfig.of(s -> s.filter(f -> f.includes(sources))));

        elasticSearchRequestBuilder.trackTotalHits(f -> f.enabled(true));

        //        elasticSearchRequestBuilder.query(builder -> buildUnderlyingQuery(builder, ogcApiQuery,
        // collectionFilter));
    }

    public double constrainLong(double x) {
        if (x < -180) return -180;
        if (x > 180) return 180;
        return x;
    }

    public double constrainLat(double x) {
        if (x < -90) return -90;
        if (x > 90) return 90;
        return x;
    }

    /**
     * this builds the actual filter/query.
     *
     * @param ogcApiQuery from user
     * @param collectionFilter from DB source field
     * @return Query for the user's request.
     */
    public Query buildUnderlyingQuery(OgcApiQuery ogcApiQuery, String collectionFilter) throws Exception {

        Query externalIdsQuery = null;
        Query geoQuery = null;
        Query configQuery = null;
        Query collectionQuery = null;
        Query queryablesQuery = null;
        Query dataTimeQuery = null;
        Query typeQuery = null;
        Query idsQuery = null;

        Query textSearchQuery = QueryStringQuery.of(q -> q.query(buildFullTextSearchQuery(ogcApiQuery.getQ())))
                ._toQuery();

        // todo - verify that this is the right place to query...
        if (ogcApiQuery.getExternalIds() != null
                && !ogcApiQuery.getExternalIds().isEmpty()) {
            externalIdsQuery = TermsQuery.of(q -> q.field("metadataIdentifier")
                            .terms(t -> t.value(ogcApiQuery.getExternalIds().stream()
                                    .map(x -> FieldValue.of(x))
                                    .toList())))
                    ._toQuery();
        }

        if (ogcApiQuery.getIds() != null && !ogcApiQuery.getIds().isEmpty()) {
            idsQuery = TermsQuery.of(q -> q.field("uuid")
                            .terms(t -> t.value(ogcApiQuery.getIds().stream()
                                    .map(x -> FieldValue.of(x))
                                    .toList())))
                    ._toQuery();
        }

        if (ogcApiQuery.getBbox() != null && ogcApiQuery.getBbox().size() == 4) {

            Rectangle rectangle = new Rectangle(
                    constrainLong(ogcApiQuery.getBbox().get(0).doubleValue()),
                    constrainLong(ogcApiQuery.getBbox().get(2).doubleValue()),
                    constrainLat(ogcApiQuery.getBbox().get(3).doubleValue()),
                    constrainLat(ogcApiQuery.getBbox().get(1).doubleValue()));

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

        if (ogcApiQuery.getDatetime() != null) {
            var meta = new OgcApiRecordsGnElasticDto();
            meta.elasticPath("resourceTemporalDateRange");
            dataTimeQuery = queryToElastic.createVsDate(meta, ogcApiQuery.getDatetime(), null);
        }

        if (ogcApiQuery.getType() != null && !ogcApiQuery.getType().isEmpty()) {
            var elasticPropertyName = "resourceType";
            typeQuery = TermsQuery.of(t -> t.field(elasticPropertyName)
                            .terms(x -> x.value(ogcApiQuery.getType().stream()
                                    .map(y -> FieldValue.of(y))
                                    .toList())))
                    ._toQuery();
        }

        if (ogcApiQuery.getPropValues() != null && !ogcApiQuery.getPropValues().isEmpty()) {
            queryablesQuery = queryToElastic.getQueryablesQuery(ogcApiQuery);
        }

        var cqlQuery = cqlToElasticSearch.create(ogcApiQuery);

        var filters = Stream.of(
                        textSearchQuery,
                        externalIdsQuery,
                        geoQuery,
                        configQuery,
                        collectionQuery,
                        queryablesQuery,
                        dataTimeQuery,
                        cqlQuery,
                        typeQuery,
                        idsQuery)
                .filter(Objects::nonNull)
                .toList();

        return BoolQuery.of(bq -> bq.must(filters))._toQuery();
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
