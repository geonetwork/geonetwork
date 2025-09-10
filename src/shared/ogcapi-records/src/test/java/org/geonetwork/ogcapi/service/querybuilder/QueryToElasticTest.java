/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.querybuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.elastic.clients.elasticsearch._types.GeoBounds;
import co.elastic.clients.elasticsearch._types.TopRightBottomLeftGeoBounds;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGnElasticDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonPropertyDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonSchemaDto;
import org.geonetwork.ogcapi.service.queryables.QueryablesService;
import org.geonetwork.ogcapi.service.search.QueryToElastic;
import org.junit.jupiter.api.Test;

/**
 * These test cases are quite complex because they are doing the full-process instead of very small parts.
 *
 * <p>1. They build a queryables Service that has the test-case defined Queryables + this is done so we can more easily
 * control the queryable configuration + this is done via a mock 2. We then create a Query 3. We then use
 * `QueryToElastic` to add the queryable elastic query to the main (SearchSourceBuilder) query.
 *
 * <p>4. We then check to see if the added query makes sense.
 *
 * <p>
 *
 * <p>NOTE: IF THESE TEST CASES FAIL, IT COULD BE THAT THE `QUERYTOELASTIC` CLASS WAS MODIFIED TO PRODUCE A "BETTER"
 * QUERY. THESE TEST CASES SHOULD BE UPDATED. MOSTLY THIS WOULD INVOLVE THE VERY END OF THE TEST CASE - THE PART THAT
 * VERIFIES THE ADDED QUERY!
 */
@SuppressWarnings("unchecked")
public class QueryToElasticTest {

    public QueryablesService initQueryablesService(String pname, OgcApiRecordsJsonPropertyDto property) {

        var queryables = new OgcApiRecordsJsonSchemaDto();
        queryables.setProperties(new LinkedHashMap<>());
        queryables.getProperties().put(pname, property);

        QueryablesService queryablesService = new QueryablesService(null) {
            @Override
            public OgcApiRecordsJsonSchemaDto buildQueryables(String collectionId) {
                return queryables;
            }

            @Override
            public OgcApiRecordsJsonSchemaDto getFullQueryables(String collectionId) {
                return queryables;
            }
        };

        return queryablesService;
    }

    /** tests the "id" - should result in a simple multi-match */
    @Test
    public void test_id() {

        // setup queryable
        var jsonProperty = new OgcApiRecordsJsonPropertyDto();
        var paths = new ArrayList<OgcApiRecordsGnElasticDto>();
        var info1 = new OgcApiRecordsGnElasticDto();
        info1.setElasticPath("uuid");
        info1.setElasticColumnType(OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.KEYWORD);
        paths.add(info1);
        jsonProperty.setxGnElastic(paths);
        var queryableService = initQueryablesService("id", jsonProperty);

        // setup service
        QueryToElastic queryToElastic = new QueryToElastic();
        queryToElastic.queryablesService = queryableService;

        // create simple query for id=abc
        var query = buildQuery(queryableService, "id", "abc");

        // the QueryToElastic builds on top of an already existing SearchSourceBuilder
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // the mainquery for the source builder is always a boolQuery
        var mainQuery = QueryBuilders.boolQuery();
        sourceBuilder.query(mainQuery);

        // add the queryables search to the boolQuery
        var q = queryToElastic.getQueryablesQuery(query);

        assertEquals("Bool", q._kind().toString());

        var bq = (BoolQuery) q._get();
        assertEquals(1, bq.must().size());

        // extract just the created query
        var createdQuery = bq.must().get(0)._get();

        // test the created elastic query
        // should be a SimpleQueryStringQuery (default str match type)
        // for field "uuid",
        // query text is "abc*"

        assertEquals(SimpleQueryStringQuery.class, createdQuery.getClass());
        var simpleQueryStringQuery = (SimpleQueryStringQuery) createdQuery;

        assertEquals("abc*", simpleQueryStringQuery.query());
        assertTrue(simpleQueryStringQuery.fields().contains("uuid"));
        assertEquals(1, simpleQueryStringQuery.fields().size());
    }

    /** tests the "title" - should result in a multi-match with two columns. also, check for multi-lingual expansion. */
    @Test
    public void test_multi() {

        // setup queryable
        var jsonProperty = new OgcApiRecordsJsonPropertyDto();
        var paths = new ArrayList<OgcApiRecordsGnElasticDto>();
        var info1 = new OgcApiRecordsGnElasticDto()
                .elasticPath("resourceTitleObject.default")
                .elasticColumnType(OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.TEXT);

        paths.add(info1);
        var info2 = new OgcApiRecordsGnElasticDto()
                .elasticPath("resourceTitleObject.lang${lang3iso}")
                .elasticColumnType(OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.TEXT);

        paths.add(info2);
        jsonProperty.setxGnElastic(paths);
        var queryableService = initQueryablesService("title", jsonProperty);

        // setup service
        QueryToElastic queryToElastic = new QueryToElastic();
        queryToElastic.queryablesService = queryableService;

        // create simple query for id=abc
        var query = buildQuery(queryableService, "title", "abc");

        // add the queryables search to the boolQuery
        var q = queryToElastic.getQueryablesQuery(query);

        var mmq = (SimpleQueryStringQuery) ((BoolQuery) q._get()).must().get(0)._get();
        assertEquals(2, mmq.fields().size());

        // extract just the created query
        var createdQuery = mmq;

        // test the created elastic query
        // should be a SimpleQueryStringQuery (default str match type)
        // for field "uuid",
        // query text is "abc"
        // and minimumShouldMatch should be 1.
        assertEquals(SimpleQueryStringQuery.class, createdQuery.getClass());
        var simpleQueryStringQuery = (SimpleQueryStringQuery) createdQuery;

        assertEquals("abc*", simpleQueryStringQuery.query());
        assertTrue(simpleQueryStringQuery.fields().contains("resourceTitleObject.default"));

        // if this fails, it could be doing "better" multilingual language type injection
        assertTrue(simpleQueryStringQuery.fields().contains("resourceTitleObject.lang*"));
        assertEquals(2, simpleQueryStringQuery.fields().size());

        assertEquals("1", simpleQueryStringQuery.minimumShouldMatch());
    }

    /** tests date types - should result in a range query */
    @Test
    public void test_date() {

        // setup queryable
        var jsonProperty = new OgcApiRecordsJsonPropertyDto();
        var paths = new ArrayList<OgcApiRecordsGnElasticDto>();
        var info1 = new OgcApiRecordsGnElasticDto()
                .elasticPath("created")
                .elasticColumnType(OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.DATE);
        paths.add(info1);
        jsonProperty.setxGnElastic(paths);
        var queryableService = initQueryablesService("created", jsonProperty);

        // setup service
        QueryToElastic queryToElastic = new QueryToElastic();
        queryToElastic.queryablesService = queryableService;

        // create simple query for id=abc
        var query = buildQuery(queryableService, "created", "2023-10-22T21:10:03Z/2024-10-22T21:10:03Z");

        // the QueryToElastic builds on top of an already existing SearchSourceBuilder
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // the mainquery for the source builder is always a boolQuery
        var mainQuery = QueryBuilders.boolQuery();
        sourceBuilder.query(mainQuery);

        // add the queryables search to the boolQuery
        var q = queryToElastic.getQueryablesQuery(query);

        // extract just the created query
        var mmq = (RangeQuery) ((BoolQuery) q._get()).must().get(0)._get();

        var createdQuery = mmq;

        // test the created elastic query
        // should be a RangeQueryBuilder
        // for field "created",
        // from: "2023-10-22T21:10:03Z"
        //  to :2024-10-22T21:10:03Z
        assertEquals(RangeQuery.class, createdQuery.getClass());
        var rangeQueryBuilder = (RangeQuery) createdQuery;
        assertEquals("created", rangeQueryBuilder.field());

        assertEquals("2023-10-22T21:10:03Z", rangeQueryBuilder.gte().toString());
        assertEquals("2024-10-22T21:10:03Z", rangeQueryBuilder.lte().toString());
    }

    /** tests date types - should result in a range query, with from=null */
    @Test
    public void test_date_nolower() {

        // setup queryable
        var jsonProperty = new OgcApiRecordsJsonPropertyDto();
        var paths = new ArrayList<OgcApiRecordsGnElasticDto>();
        var info1 = new OgcApiRecordsGnElasticDto()
                .elasticPath("created")
                .elasticColumnType(OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.DATE);
        paths.add(info1);
        jsonProperty.setxGnElastic(paths);
        var queryableService = initQueryablesService("created", jsonProperty);

        // setup service
        QueryToElastic queryToElastic = new QueryToElastic();
        queryToElastic.queryablesService = queryableService;

        // create simple query for id=abc
        var query = buildQuery(queryableService, "created", "../2024-10-22T21:10:03Z");

        // the QueryToElastic builds on top of an already existing SearchSourceBuilder
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // the mainquery for the source builder is always a boolQuery
        var mainQuery = QueryBuilders.boolQuery();
        sourceBuilder.query(mainQuery);

        // add the queryables search to the boolQuery
        var q = queryToElastic.getQueryablesQuery(query);

        var rangeQuery = (RangeQuery) ((BoolQuery) q._get()).must().get(0)._get();

        // test the created elastic query
        // should be a RangeQueryBuilder
        // for field "created",
        // from: "2023-10-22T21:10:03Z"
        //  to :2024-10-22T21:10:03Z
        assertEquals(RangeQuery.class, rangeQuery.getClass());
        var rangeQueryBuilder = (RangeQuery) rangeQuery;
        assertEquals("created", rangeQueryBuilder.field());

        assertNull(rangeQueryBuilder.from());
        assertEquals("2024-10-22T21:10:03Z", rangeQueryBuilder.lte().toString());
    }

    /** tests date types - should result in a range query, with to=null */
    @Test
    public void test_date_noupper() {

        // setup queryable
        var jsonProperty = new OgcApiRecordsJsonPropertyDto();
        var paths = new ArrayList<OgcApiRecordsGnElasticDto>();
        var info1 = new OgcApiRecordsGnElasticDto()
                .elasticPath("created")
                .elasticColumnType(OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.DATE);
        paths.add(info1);
        jsonProperty.setxGnElastic(paths);
        var queryableService = initQueryablesService("created", jsonProperty);

        // setup service
        QueryToElastic queryToElastic = new QueryToElastic();
        queryToElastic.queryablesService = queryableService;

        // create simple query for id=abc
        var query = buildQuery(queryableService, "created", "2023-10-22T21:10:03Z/..");

        // the QueryToElastic builds on top of an already existing SearchSourceBuilder
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // the mainquery for the source builder is always a boolQuery
        var mainQuery = QueryBuilders.boolQuery();
        sourceBuilder.query(mainQuery);

        // add the queryables search to the boolQuery
        var q = queryToElastic.getQueryablesQuery(query);

        var rangeQuery = (RangeQuery) ((BoolQuery) q._get()).must().get(0)._get();

        // test the created elastic query
        // should be a RangeQueryBuilder
        // for field "created",
        // from: "2023-10-22T21:10:03Z"
        //  to :2024-10-22T21:10:03Z
        assertEquals(RangeQuery.class, rangeQuery.getClass());
        var rangeQueryBuilder = (RangeQuery) rangeQuery;
        assertEquals("created", rangeQueryBuilder.field());

        assertNull(rangeQueryBuilder.to());
        assertEquals("2023-10-22T21:10:03Z", rangeQueryBuilder.gte().toString());
    }

    /** tests date types - should result in a range query */
    @Test
    public void test_geo() {

        // setup queryable
        var jsonProperty = new OgcApiRecordsJsonPropertyDto();
        var paths = new ArrayList<OgcApiRecordsGnElasticDto>();
        var info1 = new OgcApiRecordsGnElasticDto()
                .elasticPath("geo")
                .elasticColumnType(OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.GEO);
        paths.add(info1);
        jsonProperty.setxGnElastic(paths);
        var queryableService = initQueryablesService("geo", jsonProperty);

        // setup service
        QueryToElastic queryToElastic = new QueryToElastic();
        queryToElastic.queryablesService = queryableService;

        // create simple query for id=abc
        var query = buildQuery(queryableService, "geo", "1,2,11,12");

        // the QueryToElastic builds on top of an already existing SearchSourceBuilder
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // the mainquery for the source builder is always a boolQuery
        var mainQuery = QueryBuilders.boolQuery();
        sourceBuilder.query(mainQuery);

        // add the queryables search to the boolQuery
        var q = queryToElastic.getQueryablesQuery(query);

        var geoQuery =
                (GeoBoundingBoxQuery) ((BoolQuery) q._get()).must().get(0)._get();

        // test the created elastic query
        // should be a
        // for field "geom",

        assertEquals(GeoBoundingBoxQuery.class, geoQuery.getClass());
        var geoShapeQueryBuilder = geoQuery;

        assertEquals("geo", geoShapeQueryBuilder.field());
        //    assertEquals(ShapeRelation.INTERSECTS, geoShapeQueryBuilder..relation());

        assertEquals(GeoBounds.class, geoShapeQueryBuilder.boundingBox().getClass());
        var rect =
                (TopRightBottomLeftGeoBounds) geoShapeQueryBuilder.boundingBox()._get();
        assertEquals(1, ((List<Double>) rect.bottomLeft()._get()).get(0), 0);
        assertEquals(2, ((List<Double>) rect.bottomLeft()._get()).get(1), 0);

        assertEquals(11, ((List<Double>) rect.topRight()._get()).get(0), 0);
        assertEquals(12, ((List<Double>) rect.topRight()._get()).get(1), 0);
    }

    /** tests nested query types - should result in a nested query, inside and OR boolean */
    @Test
    public void test_nested() {

        // setup queryable
        var jsonProperty = new OgcApiRecordsJsonPropertyDto();
        var paths = new ArrayList<OgcApiRecordsGnElasticDto>();
        var info1 = new OgcApiRecordsGnElasticDto()
                .elasticPath("contact.organisationObject.default")
                .elasticColumnType(OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.TEXT);

        info1.setElasticQueryType(OgcApiRecordsGnElasticDto.ElasticQueryTypeEnum.NESTED);
        paths.add(info1);
        var info2 = new OgcApiRecordsGnElasticDto()
                .elasticPath("contact.organisationObject.lang${lang3iso}")
                .elasticColumnType(OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.TEXT);

        info2.setElasticQueryType(OgcApiRecordsGnElasticDto.ElasticQueryTypeEnum.NESTED);
        paths.add(info2);
        jsonProperty.setxGnElastic(paths);
        var queryableService = initQueryablesService("contacts", jsonProperty);

        // setup service
        QueryToElastic queryToElastic = new QueryToElastic();
        queryToElastic.queryablesService = queryableService;

        // create simple query for id=abc
        var query = buildQuery(queryableService, "contacts", "jody");

        // the QueryToElastic builds on top of an already existing SearchSourceBuilder
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // the mainquery for the source builder is always a boolQuery
        var mainQuery = QueryBuilders.boolQuery();
        sourceBuilder.query(mainQuery);

        // add the queryables search to the boolQuery
        var q = queryToElastic.getQueryablesQuery(query);

        var boolQuery = (BoolQuery) ((BoolQuery) q._get()).must().get(0)._get();

        // extract just the created query
        var createdQuery = boolQuery;

        assertEquals(BoolQuery.class, createdQuery.getClass());
        var boolQueryBuilder = (BoolQuery) createdQuery;

        assertEquals("1", boolQueryBuilder.minimumShouldMatch());
        assertEquals(2, boolQueryBuilder.should().size());
        assertEquals(NestedQuery.class, boolQueryBuilder.should().get(0)._get().getClass());
        assertEquals(NestedQuery.class, boolQueryBuilder.should().get(1)._get().getClass());

        var nested = (NestedQuery) boolQueryBuilder.should().get(0)._get();

        // you cannot access boolQueryBuilder.path, so we do a hack
        assertEquals("contact", nested.path());

        assertEquals(MatchQuery.class, nested.query()._get().getClass());
        var matchQuery = (MatchQuery) nested.query()._get();
        assertEquals("contact.organisationObject.default", matchQuery.field());
        assertEquals("jody", matchQuery.query()._get());

        var nested2 = (NestedQuery) boolQueryBuilder.should().get(1)._get();

        assertTrue(nested2.toString().contains("contact"));
        assertEquals(MatchQuery.class, nested2.query()._get().getClass());
        var matchQuery2 = (MatchQuery) nested2.query()._get();
        assertEquals("contact.organisationObject.lang*", matchQuery2.field());
        assertEquals("jody", matchQuery2.query()._get());
    }

    // -------------------------------------------------------------------------------

    /**
     * simple query building for a <queryable>=<search value>
     *
     * @param queryablesService service
     * @param pname param name
     * @param pvalue param value
     * @return OgcApiQuery
     */
    public OgcApiQuery buildQuery(QueryablesService queryablesService, String pname, String pvalue) {
        // setup QueryBuilder
        var queryBuilder = new QueryBuilder();
        queryBuilder.queryablesService = queryablesService;
        queryBuilder.queryablesExtractor = new QueryablesExtractor();
        queryBuilder.queryablesExtractor.queryablesService = queryablesService;

        Map<String, String[]> paramMap = new LinkedHashMap<>();
        paramMap.put(pname, new String[] {pvalue});

        // setup Query
        var query = queryBuilder.buildFromRequest(
                "abc", null, null, null, null, null, null, null, null, null, null, "cql2-text", null, paramMap);

        return query;
    }
}
