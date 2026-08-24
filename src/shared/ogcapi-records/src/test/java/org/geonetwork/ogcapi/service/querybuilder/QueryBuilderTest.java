/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsAdvancedFacetDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetFilterDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetsDto;
import org.geonetwork.ogcapi.service.facets.FacetsJsonService;
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
    String filter;
    Map<String, String[]> parameterMap;
    List<String> advancedFacets;

    @BeforeEach
    public void setup() {
        // sets up the AdvancedFacetsBuilder for testing

        AdvancedFacetsBuilder advancedFacetsBuilder = new AdvancedFacetsBuilder(new FacetsJsonService() {
            @Override
            public OgcApiRecordsFacetsDto buildFacets(String catalogId) {
                var result = new OgcApiRecordsFacetsDto();
                result.setId(catalogId);
                result.setTitle("Facets for catalog " + catalogId);
                result.setFacets(Map.of(
                        "keywords", new OgcApiRecordsFacetFilterDto(),
                        "organization", new OgcApiRecordsFacetFilterDto(),
                        "theme", new OgcApiRecordsFacetFilterDto(),
                        "facetname1", new OgcApiRecordsFacetFilterDto(),
                        "facetname2", new OgcApiRecordsFacetFilterDto()));
                return result;
            }
        });

        queryBuilder = new QueryBuilder(new QueryablesService(null), new QueryablesExtractor(), advancedFacetsBuilder);

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
        filter = null;
        parameterMap = new LinkedHashMap<>();
    }

    /** just make sure that all the data is being copied to the Query. */
    @Test
    public void testSimple() throws Exception {
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
    public void testGoodQueryable() throws Exception {
        parameterMap.put("id", new String[] {"ID"});
        var query = buildSampleQuery();

        assertEquals(1, query.getPropValues().size());
        assertTrue(query.getPropValues().containsKey("id"));
        assertEquals("ID", query.getPropValues().get("id"));
    }

    /** test with a good queryable (one in the queryables list) */
    @Test
    public void testBadQueryable() throws Exception {
        parameterMap.put("BAD-QUERYABLE", new String[] {"ID"});
        var query = buildSampleQuery();

        assertEquals(0, query.getPropValues().size());
    }

    /**
     * verify that the advanced facets are being processed
     *
     * @throws Exception shouldnt happen
     */
    @Test
    public void testWithAdvancedFacets() throws Exception {
        advancedFacets = List.of("keywords:20:value_asc");
        var query = buildSampleQuery();

        assertEquals(1, query.getAdvancedFacets().size());
        assertEquals("keywords", query.getAdvancedFacets().get(0).getFacetName());
        assertEquals(Integer.valueOf(20), query.getAdvancedFacets().get(0).getBucketSize());
        assertEquals(
                OgcApiRecordsAdvancedFacetDto.SortingEnum.VALUE_ASC,
                query.getAdvancedFacets().get(0).getSorting());
    }

    /**
     * A `filter` (or `datetime`, or queryable value) arriving at {@link QueryBuilder} is already URL-decoded by Spring
     * MVC / the servlet container. Values that contain a literal `%` (e.g. an OGC CQL `LIKE` wildcard) must be passed
     * through as-is, not decoded again - otherwise `%foo%` is misread as a percent-encoded byte and either throws or is
     * corrupted.
     */
    @Test
    public void testFilterWithLikeWildcardIsNotDoubleDecoded() throws Exception {
        filter = "name LIKE '%foo%'";
        var query = buildSampleQuery();

        assertEquals("name LIKE '%foo%'", query.getFilter());
    }

    public OgcApiQuery buildSampleQuery() throws Exception {
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
                filter,
                "cql2-text",
                null,
                parameterMap,
                advancedFacets);
    }
}
