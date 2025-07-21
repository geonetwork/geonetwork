/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.querybuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.geonetwork.ogcapi.service.queryables.QueryablesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QueryBuilderTest {

    QueryBuilder queryBuilder;

    String collectionId;
    List<BigDecimal> bbox;
    String datetime;
    Integer limit;
    Integer startindex;
    String type;
    List<String> q;
    List<String> ids;
    List<String> externalids;
    List<String> sortby;
    Map<String, String[]> parameterMap;

    @BeforeEach
    public void setup() {
        queryBuilder = new QueryBuilder();
        queryBuilder.queryablesService = new QueryablesService();
        queryBuilder.queryablesExtractor = new QueryablesExtractor();
        queryBuilder.queryablesExtractor.queryablesService = queryBuilder.queryablesService;

        collectionId = "collectionId";
        bbox = Arrays.asList(new BigDecimal(0), new BigDecimal(0), new BigDecimal(100), new BigDecimal(100));
        datetime = "2024-10-22T21:10:03Z";
        limit = 100;
        startindex = 10;
        type = "type";
        q = Arrays.asList("abc", "def");
        ids = Arrays.asList("id1", "id2");
        externalids = Arrays.asList("ex-id1", "ex-id2");
        sortby = Arrays.asList("sort-p1", "sort-p2");
        parameterMap = new LinkedHashMap<>();
    }

    /** just make sure that all the data is being copied to the Query. */
    @Test
    public void testSimple() {
        OgcApiQuery query = buildSampleQuery();

        assertEquals("collectionId", query.getCollectionId());
        assertIterableEquals(
                query.getBbox(),
                Arrays.asList(Double.valueOf(0), Double.valueOf(0), Double.valueOf(100), Double.valueOf(100)));
        assertEquals("2024-10-22T21:10:03Z", query.getDatetime());
        assertEquals(Integer.valueOf(100), query.getLimit());
        assertEquals(Integer.valueOf(10), query.getStartIndex());
        assertEquals(1, query.getType().size());
        assertEquals("type", query.getType().get(0));
        assertEquals(Arrays.asList("abc", "def"), query.getQ());
        assertEquals(Arrays.asList("id1", "id2"), query.getIds());
        assertEquals(Arrays.asList("ex-id1", "ex-id2"), query.getExternalIds());
        assertEquals(Arrays.asList("sort-p1", "sort-p2"), query.getSortBy());
        assertEquals(new LinkedHashMap<>(), query.getPropValues());
    }

    /** test with a good queryable (one in the queryables list) */
    @Test
    public void testGoodQueryable() {
        parameterMap.put("id", new String[] {"ID"});
        var query = buildSampleQuery();

        assertEquals(1, query.getPropValues().size());
        assertTrue(query.getPropValues().containsKey("id"));
        assertEquals("ID", query.getPropValues().get("id"));
    }

    /** test with a good queryable (one in the queryables list) */
    @Test
    public void testBadQueryable() {
        parameterMap.put("BAD-QUERYABLE", new String[] {"ID"});
        var query = buildSampleQuery();

        assertEquals(0, query.getPropValues().size());
    }

    public OgcApiQuery buildSampleQuery() {
        return queryBuilder.buildFromRequest(
                collectionId,
                bbox,
                datetime,
                limit,
                startindex,
                Arrays.asList(type),
                q,
                ids,
                externalids,
                sortby,
                null,
                "cql2-text",
                null,
                parameterMap);
    }
}
