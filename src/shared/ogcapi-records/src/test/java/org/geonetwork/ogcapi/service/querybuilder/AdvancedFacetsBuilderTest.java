/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.querybuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsAdvancedFacetDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetFilterDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetsDto;
import org.geonetwork.ogcapi.service.facets.FacetsJsonService;
import org.junit.jupiter.api.Test;

/** tests the advanced facets builder. */
public class AdvancedFacetsBuilderTest {

    /**
     * test from the specification, i.e. "&facets=keywords:20:value_asc,organization:10"
     *
     * <p>full definition, name&buckets only
     *
     * @throws Exception shouldn't happen
     */
    @Test
    public void test_spec1() throws Exception {
        var facets = Arrays.asList("keywords:20:value_asc", "organization:10");

        var result = parse(facets);

        assertEquals(2, result.size());

        assertEquals("keywords", result.get(0).getFacetName());
        assertEquals(Integer.valueOf(20), result.get(0).getBucketSize());
        assertEquals(
                OgcApiRecordsAdvancedFacetDto.SortingEnum.VALUE_ASC,
                result.get(0).getSorting());

        assertEquals("organization", result.get(1).getFacetName());
        assertEquals(Integer.valueOf(10), result.get(1).getBucketSize());
        assertNull(result.get(1).getSorting());
    }

    /**
     * test from the specification, i.e. "&facets=keywords:20:value_asc,organization:10,theme::count_desc"
     *
     * <p>partial definition - name and sorting only
     *
     * @throws Exception shouldn't happen
     */
    @Test
    public void test_spec2() throws Exception {
        var facets = Arrays.asList("keywords:20:value_asc", "organization:10", "theme::count_desc");

        var result = parse(facets);

        assertEquals(3, result.size());

        assertEquals("keywords", result.get(0).getFacetName());
        assertEquals(Integer.valueOf(20), result.get(0).getBucketSize());
        assertEquals(
                OgcApiRecordsAdvancedFacetDto.SortingEnum.VALUE_ASC,
                result.get(0).getSorting());

        assertEquals("organization", result.get(1).getFacetName());
        assertEquals(Integer.valueOf(10), result.get(1).getBucketSize());
        assertNull(result.get(1).getSorting());

        assertEquals("theme", result.get(2).getFacetName());
        assertNull(result.get(2).getBucketSize());
        assertEquals(
                OgcApiRecordsAdvancedFacetDto.SortingEnum.COUNT_DESC,
                result.get(2).getSorting());
    }

    /**
     * from spec - no sorting information, but valid ":" at end
     *
     * @throws Exception shouldnt happen
     */
    @Test
    public void test_spec3() throws Exception {
        var facets = List.of("theme:10:");

        var result = parse(facets);

        assertEquals(1, result.size());

        assertEquals("theme", result.get(0).getFacetName());
        assertEquals(Integer.valueOf(10), result.get(0).getBucketSize());
        assertNull(result.get(0).getSorting());
    }

    /**
     * tests returns no parsed facets if none in request.
     *
     * <p>&facets= -> no facets in result <br>
     * no mention of &facets in request ---> all facets in result.
     *
     * @throws Exception shouldnt happen
     */
    @Test
    public void test_spec4_no_facets() throws Exception {
        var facets = new ArrayList<String>();
        var result = parse(facets);
        assertEquals(0, result.size());

        facets = null;
        result = parse(facets);
        assertNull(result);
    }

    /** not it spec, but implied. Just the name of the facet. "theme", "theme:" "theme::" */
    @Test
    public void test_only_name() throws Exception {

        var facets = List.of("theme");
        var result = parse(facets);
        assertEquals(1, result.size());

        assertEquals("theme", result.get(0).getFacetName());
        assertNull(result.get(0).getBucketSize());
        assertNull(result.get(0).getSorting());

        facets = List.of("theme:");
        result = parse(facets);
        assertEquals(1, result.size());

        assertEquals("theme", result.get(0).getFacetName());
        assertNull(result.get(0).getBucketSize());
        assertNull(result.get(0).getSorting());

        facets = List.of("theme::");
        result = parse(facets);
        assertEquals(1, result.size());

        assertEquals("theme", result.get(0).getFacetName());
        assertNull(result.get(0).getBucketSize());
        assertNull(result.get(0).getSorting());
    }

    /** bad number of buckets - either not-a-number or <0 */
    @Test
    public void test_bad_buckets() {

        assertThrows(Throwable.class, () -> {
            var facets = List.of("theme:-1");
            parse(facets);
        });

        assertThrows(Throwable.class, () -> {
            var facets = List.of("theme:a");
            parse(facets);
        });
    }

    /** test invalid sorting name */
    @Test
    public void test_bad_sorting() {

        assertThrows(Throwable.class, () -> {
            var facets = List.of("theme::alsdkf");
            parse(facets);
        });
    }

    /** test that a facet name that is not listed in the /collections/{collectionId}/items endpoint information. */
    @Test
    public void test_bad_facetName() {

        assertThrows(Throwable.class, () -> {
            var facets = List.of("abc");
            parse(facets);
        });

        assertThrows(Throwable.class, () -> {
            var facets = List.of("abc:1:count_desc");
            parse(facets);
        });
    }

    /**
     * utility to do the parsing (i.e. setup infrastructure)
     *
     * @param fromRequest coming into controllers
     * @return parsed list of facets configured
     * @throws Exception error occurred
     */
    public List<OgcApiRecordsAdvancedFacetDto> parse(List<String> fromRequest) throws Exception {
        AdvancedFacetsBuilder builder = new AdvancedFacetsBuilder(new FacetsJsonService() {
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

        return builder.buildAdvancedFacets("catalogId", fromRequest);
    }
}
