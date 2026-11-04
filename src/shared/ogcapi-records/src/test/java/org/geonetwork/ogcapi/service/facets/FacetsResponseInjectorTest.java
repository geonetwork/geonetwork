/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.facets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.FiltersBucket;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import java.util.List;
import java.util.Map;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.service.configuration.FacetType;
import org.geonetwork.ogcapi.service.configuration.OgcFacetConfig;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.DynamicPropertiesFacade;
import org.junit.jupiter.api.Test;

class FacetsResponseInjectorTest {

    private static OgcFacetConfig filterFacet(String name) {
        var facet = new OgcFacetConfig();
        facet.setFacetName(name);
        facet.setFacetType(FacetType.FILTER);
        return facet;
    }

    @Test
    void filterFacetWithMissingAggregationIsSkipped() {
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
}
