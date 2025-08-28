/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.facets;

import static org.geonetwork.ogcapi.service.configuration.SimpleType.DATE;

import java.math.BigDecimal;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.ogcapi.records.generated.model.*;
import org.geonetwork.ogcapi.service.configuration.BucketSorting;
import org.geonetwork.ogcapi.service.configuration.FacetType;
import org.geonetwork.ogcapi.service.configuration.OgcFacetConfig;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.ElasticTypingSystem;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.ExtraElasticPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class FacetsJsonService {

    @Autowired
    public ExtraElasticPropertiesService extraElasticPropertiesService;

    @Autowired
    public ElasticTypingSystem elasticTypingSystem;

    public OgcApiRecordsFacetsDto buildFacets(String catalogId) {
        var result = new OgcApiRecordsFacetsDto();
        result.setId(catalogId);
        result.setTitle("Facets for catalog " + catalogId);
        result.setFacets(new HashMap<>());

        for (var facet : extraElasticPropertiesService.getFacetConfigs()) {
            if (facet.getFacetType() == FacetType.TERM) {
                var f = createTerm(facet);
                result.getFacets().put(facet.getFacetName(), f);
            } else if (facet.getFacetType() == FacetType.FILTER) {
                var f = createFilter(facet);
                result.getFacets().put(facet.getFacetName(), f);
            } else if (facet.getFacetType() == FacetType.HISTOGRAM_FIXED_BUCKET_COUNT
                    || facet.getFacetType() == FacetType.HISTOGRAM_FIXED_INTERVAL) {
                var f = createHistogram(facet);
                result.getFacets().put(facet.getFacetName(), f);
            } else {
                throw new RuntimeException("Unknown facet type: " + facet.getFacetType());
            }
        }

        return result;
    }

    private OgcApiRecordsFacetFilterDto createFilter(OgcFacetConfig facet) {
        var result = new OgcApiRecordsFacetFilterDto();
        result.setProperty(facet.getField().getOgcProperty());
        result.setType("term");
        result.setBucketCount(facet.getBucketCount());
        var sortedBy = OgcApiRecordsFacetSortedByDto.COUNT;
        if (facet.getBucketSorting() == BucketSorting.VALUE) {
            sortedBy = OgcApiRecordsFacetSortedByDto.VALUE;
        }
        result.setSortedBy(sortedBy);
        result.setxElasticProperty(facet.getField().getElasticProperty());
        result.setFilters(new HashMap<>());
        for (var filter : facet.getFilters()) {
            result.getFilters().put(filter.getFilterName(), filter.getFilterEquation());
        }
        return result;
    }

    private OgcApiRecordsFacetHistogramDto createHistogram(OgcFacetConfig facet) {
        var result = new OgcApiRecordsFacetHistogramDto();
        result.setProperty(facet.getField().getOgcProperty());
        result.setType("histogram");
        result.setBucketCount(facet.getBucketCount());
        result.setxElasticProperty(facet.getField().getElasticProperty());

        var sortedBy = OgcApiRecordsFacetSortedByDto.COUNT;
        if (facet.getBucketSorting() == BucketSorting.VALUE) {
            sortedBy = OgcApiRecordsFacetSortedByDto.VALUE;
        }
        result.setSortedBy(sortedBy);
        result.setxElasticProperty(facet.getField().getElasticProperty());

        var bucketType = OgcApiRecordsFacetHistogramDto.BucketTypeEnum.FIXED_BUCKET_COUNT;
        if (facet.getFacetType() == FacetType.HISTOGRAM_FIXED_INTERVAL) {
            bucketType = OgcApiRecordsFacetHistogramDto.BucketTypeEnum.FIXED_INTERVAL;
        }
        result.setBucketType(bucketType);

        if (facet.getCalendarIntervalUnit() != null) {
            result.setxIntervalCalendarInterval(facet.getCalendarIntervalUnit().toString());
        }
        if (facet.getNumberBucketInterval() != null) {
            result.setxIntervalNumber(new BigDecimal(facet.getNumberBucketInterval()));
        }

        var dataType = OgcApiRecordsFacetHistogramDto.XElasticDatatypeEnum.NUMBER;
        if (elasticTypingSystem
                        .getFinalElasticTypes()
                        .get(facet.getField().getElasticProperty())
                        .getType()
                == DATE) {
            dataType = OgcApiRecordsFacetHistogramDto.XElasticDatatypeEnum.DATE;
        }
        result.setxElasticDatatype(dataType);

        result.setxMinimumDocCount(facet.getMinimumDocumentCount());

        return result;
    }

    private OgcApiRecordsFacetTermsDto createTerm(OgcFacetConfig facet) {
        var result = new OgcApiRecordsFacetTermsDto();
        result.setProperty(facet.getField().getOgcProperty());
        result.setType("term");
        result.setBucketCount(facet.getBucketCount());
        var sortedBy = OgcApiRecordsFacetSortedByDto.COUNT;
        if (facet.getBucketSorting() == BucketSorting.VALUE) {
            sortedBy = OgcApiRecordsFacetSortedByDto.VALUE;
        }
        result.setSortedBy(sortedBy);
        result.setxElasticProperty(facet.getField().getElasticProperty());

        result.setMinOccurs(facet.getMinimumDocumentCount());
        return result;
    }
}
