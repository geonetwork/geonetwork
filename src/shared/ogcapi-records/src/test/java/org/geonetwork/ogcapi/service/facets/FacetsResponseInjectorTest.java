/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.facets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.FiltersBucket;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetResultBucketDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetSummaryDto;
import org.geonetwork.ogcapi.service.configuration.BucketSorting;
import org.geonetwork.ogcapi.service.configuration.BucketSortingDirection;
import org.geonetwork.ogcapi.service.configuration.FacetType;
import org.geonetwork.ogcapi.service.configuration.OgcFacetConfig;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.DynamicPropertiesFacade;
import org.junit.jupiter.api.Test;

public class FacetsResponseInjectorTest {

    @Test
    public void minimumDocumentCountZeroKeepsEmptyBuckets() {
        assertEquals(List.of("2000=1", "2001=0", "2002=2"), handleBuckets(0));
    }

    @Test
    public void minimumDocumentCountOneDropsEmptyBuckets() {
        assertEquals(List.of("2000=1", "2002=2"), handleBuckets(1));
    }

    @Test
    public void filterFacetWithMissingAggregationIsSkipped() {
        var facade = mock(DynamicPropertiesFacade.class);
        when(facade.getFacetConfigs()).thenReturn(List.of(filterFacet("availableInServices"), filterFacet("other")));
        var advancedFacetsService = mock(AdvancedFacetsService.class);
        when(advancedFacetsService.determineFacets(any(), any())).thenAnswer(inv -> inv.getArgument(0));

        var injector = new FacetsResponseInjector();
        injector.dynamicPropertiesFacade = facade;
        injector.advancedFacetsService = advancedFacetsService;

        // only "other" has an aggregation
        var otherAgg = Aggregate.of(
                a -> a.filters(f -> f.buckets(b -> b.keyed(Map.of("a", FiltersBucket.of(fb -> fb.docCount(2)))))));
        SearchResponse<IndexRecord> response = SearchResponse.of(r -> r.took(1)
                .timedOut(false)
                .shards(s -> s.total(1).successful(1).failed(0))
                .hits(h -> h.hits(List.of()))
                .aggregations(Map.of("facet.other", otherAgg)));

        var facets = assertDoesNotThrow(() -> injector.getFacets(response, null));

        assertFalse(facets.containsKey("availableInServices"));
        assertEquals(2, facets.get("other").getBuckets().getFirst().getCount());
    }

    private List<String> handleBuckets(int minOccurs) {
        var result = new OgcApiRecordsFacetSummaryDto();
        result.setBuckets(new ArrayList<>(List.of(bucket("2000", 1), bucket("2001", 0), bucket("2002", 2))));

        new FacetsResponseInjector()
                .handleBuckets(
                        result,
                        20,
                        minOccurs,
                        BucketSorting.VALUE,
                        FacetsResponseInjector.SortType.NUMBER,
                        false,
                        null,
                        BucketSortingDirection.ASCENDING);

        return result.getBuckets().stream()
                .map(b -> b.getValue() + "=" + b.getCount())
                .toList();
    }

    private static OgcApiRecordsFacetResultBucketDto bucket(String value, int count) {
        var b = new OgcApiRecordsFacetResultBucketDto();
        b.setValue(value);
        b.setCount(count);
        return b;
    }

    private static OgcFacetConfig filterFacet(String name) {
        var facet = new OgcFacetConfig();
        facet.setFacetName(name);
        facet.setFacetType(FacetType.FILTER);
        return facet;
    }
}
