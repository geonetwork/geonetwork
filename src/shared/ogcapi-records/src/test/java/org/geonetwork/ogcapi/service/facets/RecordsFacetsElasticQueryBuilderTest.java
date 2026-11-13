/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.facets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.geonetwork.ogcapi.service.configuration.CalendarIntervalUnit;
import org.geonetwork.ogcapi.service.configuration.FacetType;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldMapperConfig;
import org.geonetwork.ogcapi.service.configuration.OgcFacetConfig;
import org.geonetwork.ogcapi.service.configuration.SimpleType;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.DynamicPropertiesFacade;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.ElasticTypeInfo;
import org.junit.jupiter.api.Test;

public class RecordsFacetsElasticQueryBuilderTest {

    @Test
    public void dateHistogramHasConfiguredHardBounds() {
        var facet = new OgcFacetConfig();
        facet.setFacetName("temporalExtent");
        facet.setFacetType(FacetType.HISTOGRAM_FIXED_INTERVAL);
        facet.setCalendarIntervalUnit(CalendarIntervalUnit.year);
        facet.setHardBoundsMax("now/y");

        var field = new OgcElasticFieldMapperConfig();
        field.setElasticProperty("resourceTemporalExtentDateRange");

        var builder = new RecordsFacetsElasticQueryBuilder();
        builder.dynamicPropertiesFacade = mock(DynamicPropertiesFacade.class);
        builder.advancedFacetsService = mock(AdvancedFacetsService.class);
        when(builder.dynamicPropertiesFacade.findFieldForFacet(any())).thenReturn(field);
        when(builder.dynamicPropertiesFacade.getByElasticProperty("resourceTemporalExtentDateRange"))
                .thenReturn(new ElasticTypeInfo(field, false, SimpleType.DATE));
        when(builder.advancedFacetsService.determineFacets(any(), any())).thenReturn(List.of(facet));

        var histogram = builder.createElasticAggregationsFromFacetsDefinition(null)
                .get("facet.temporalExtent")
                .dateHistogram();

        assertEquals("now/y", histogram.hardBounds().max().expr());
        assertNull(histogram.hardBounds().min());
    }
}
