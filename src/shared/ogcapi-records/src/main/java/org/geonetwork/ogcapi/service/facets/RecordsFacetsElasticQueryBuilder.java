/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.facets;

import static org.geonetwork.ogcapi.service.configuration.BucketSorting.COUNT;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.util.NamedValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsAdvancedFacetDto;
import org.geonetwork.ogcapi.service.configuration.BucketSorting;
import org.geonetwork.ogcapi.service.configuration.BucketSortingDirection;
import org.geonetwork.ogcapi.service.configuration.FacetType;
import org.geonetwork.ogcapi.service.configuration.OgcFacetConfig;
import org.geonetwork.ogcapi.service.configuration.SimpleType;
import org.geonetwork.ogcapi.service.cql.CqlToElasticSearch;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.DynamicPropertiesFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "org.geonetwork.ogcapi.service.search")
public class RecordsFacetsElasticQueryBuilder {

    @Autowired
    CqlToElasticSearch cqlToElasticSearch;

    @Autowired
    DynamicPropertiesFacade dynamicPropertiesFacade;

    @Autowired
    AdvancedFacetsService advancedFacetsService;

    /**
     * merge configurations for a set of facets.
     *
     * @param advancedFacets null = use all pre-configured, empty = don't do any facets, otherwise only return these
     *     facets
     */
    public Map<String, Aggregation> createElasticAggregationsFromFacetsDefinition(
            List<OgcApiRecordsAdvancedFacetDto> advancedFacets) {

        // empty advanced facets means "use the default ones defined in the configuration"
        if (advancedFacets != null && advancedFacets.isEmpty()) {
            return new HashMap<>(); // no facets
        }

        var facetsOriginal = dynamicPropertiesFacade.getFacetConfigs();

        List<OgcFacetConfig> facets = advancedFacetsService.determineFacets(facetsOriginal, advancedFacets);

        var aggregations = new HashMap<String, Aggregation>();

        var defaultBucketCount = dynamicPropertiesFacade.getDefaultFacetsBucketCount();

        for (var facetInfo : facets) {
            var facetName = facetInfo.getFacetName();
            var correspondingField = dynamicPropertiesFacade.findFieldForFacet(facetInfo);
            var elasticProperty = correspondingField.getElasticProperty();
            var elasticPropertyInfo = this.dynamicPropertiesFacade.getByElasticProperty(elasticProperty);
            if (elasticPropertyInfo == null) {
                continue;
            }
            var type = elasticPropertyInfo.getType();

            if (facetInfo.getFacetType() == FacetType.TERM) {
                var agg = createAggregation_terms(facetInfo, defaultBucketCount);
                aggregations.put("facet." + facetName, agg);
            } else if (facetInfo.getFacetType() == FacetType.FILTER) {
                var agg = createAggregation_filter(facetInfo);
                aggregations.put("facet." + facetName, agg);
            } else if (facetInfo.getFacetType() == FacetType.HISTOGRAM_FIXED_INTERVAL
                    || facetInfo.getFacetType() == FacetType.HISTOGRAM_FIXED_BUCKET_COUNT) {
                var agg = createAggregation_histogram(facetInfo, type, defaultBucketCount);
                aggregations.put("facet." + facetName, agg);
            } else {
                throw new RuntimeException("Unknown facet type: " + facetInfo.getFacetType());
            }
        }
        return aggregations;
    }

    /**
     * differenciates between; 1. number-based histogram + fixed interval (bucket interval length) + fixed bucket count
     * (number of buckets to return) 2. date-based histogram + fixed interval (bucket duration length) + fixed bucket
     * count (number of buckets to return)
     *
     * @param histogramDto definition of the histogram
     * @param simpleType type of data
     * @param defaultBucketCount defined default # of buckets
     * @return elastic histogram Aggregration
     */
    private Aggregation createAggregation_histogram(
            OgcFacetConfig histogramDto, SimpleType simpleType, Integer defaultBucketCount) {

        if (simpleType == SimpleType.DOUBLE || simpleType == SimpleType.INTEGER) {
            if (histogramDto.getFacetType() == FacetType.HISTOGRAM_FIXED_INTERVAL) {
                return createAggregation_histogram_number_fixedInterval(histogramDto, simpleType, defaultBucketCount);
            } else {
                return createAggregation_histogram_number_fixedBucketCount(
                        histogramDto, simpleType, defaultBucketCount);
            }
        } else {
            if (histogramDto.getFacetType() == FacetType.HISTOGRAM_FIXED_INTERVAL) {
                return createAggregation_histogram_date_fixedInterval(histogramDto, simpleType, defaultBucketCount);
            } else {
                return createAggregation_histogram_date_fixedBucketCount(histogramDto, simpleType, defaultBucketCount);
            }
        }
    }

    @SuppressWarnings("unused")
    private Aggregation createAggregation_histogram_number_fixedBucketCount(
            OgcFacetConfig histogramDto, SimpleType simpleType, Integer defaultBucketCount) {
        var nBuckets = histogramDto.getBucketCount() == null ? defaultBucketCount : histogramDto.getBucketCount();

        var agg = Aggregation.of(a -> a.variableWidthHistogram(h -> {
            var correspondingField = dynamicPropertiesFacade.findFieldForFacet(histogramDto);
            h.field(correspondingField.getElasticProperty());
            h.buckets(nBuckets);

            // complex; need pipeline to support or sort later

            return h;
        }));

        return agg;
    }

    @SuppressWarnings("unused")
    private Aggregation createAggregation_histogram_number_fixedInterval(
            OgcFacetConfig histogramDto, SimpleType simpleType, Integer defaultBucketCount) {
        var agg = Aggregation.of(a -> a.histogram(h -> {
            var correspondingField = dynamicPropertiesFacade.findFieldForFacet(histogramDto);

            h.field(correspondingField.getElasticProperty());
            h.keyed(false);
            h.interval(histogramDto.getNumberBucketInterval());
            h.minDocCount(histogramDto.getMinimumDocumentCount());

            var bucketSorting = getBucketSorting(histogramDto.getBucketSorting());
            var elasticSecondaryDirection = getElasticSortingDirection(histogramDto.getBucketSortingDirection());

            if (bucketSorting == COUNT) {
                var dirElastic = getElasticSortingDirection(histogramDto.getBucketSortingDirection());
                h.order(List.of(
                        NamedValue.of("_count", dirElastic), NamedValue.of("_key", elasticSecondaryDirection) // tie
                        ));
            } else {
                var dirElastic = getElasticSortingDirection(histogramDto.getBucketSortingDirection());
                h.order(List.of(
                        NamedValue.of("_key", dirElastic), NamedValue.of("_count", elasticSecondaryDirection) // tie
                        ));
            }

            return h;
        }));

        return agg;
    }

    @SuppressWarnings("unused")
    private Aggregation createAggregation_histogram_date_fixedBucketCount(
            OgcFacetConfig histogramDto, SimpleType simpleType, Integer defaultBucketCount) {
        var nBuckets = histogramDto.getBucketCount() == null ? defaultBucketCount : histogramDto.getBucketCount();

        var agg = Aggregation.of(a -> a.autoDateHistogram(h -> {
            var correspondingField = dynamicPropertiesFacade.findFieldForFacet(histogramDto);

            h.field(correspondingField.getElasticProperty());
            h.buckets(nBuckets);

            // sort later - need pipeline

            return h;
        }));
        return agg;
    }

    @SuppressWarnings("unused")
    private Aggregation createAggregation_histogram_date_fixedInterval(
            OgcFacetConfig histogramDto, SimpleType simpleType, Integer defaultBucketCount) {
        var agg = Aggregation.of(a -> a.dateHistogram(h -> {
            var correspondingField = dynamicPropertiesFacade.findFieldForFacet(histogramDto);

            h.field(correspondingField.getElasticProperty());
            h.keyed(false);
            var interval = CalendarInterval._DESERIALIZER.deserialize(
                    histogramDto.getCalendarIntervalUnit().toString(), null);
            h.calendarInterval(interval);
            h.minDocCount(histogramDto.getMinimumDocumentCount());

            var bucketSorting = getBucketSorting(histogramDto.getBucketSorting());
            var elasticSecondaryDirection = getElasticSortingDirection(histogramDto.getBucketSortingDirection());

            if (bucketSorting == COUNT) {
                var dirElastic = getElasticSortingDirection(histogramDto.getBucketSortingDirection());
                h.order(List.of(
                        NamedValue.of("_count", dirElastic), NamedValue.of("_key", elasticSecondaryDirection) // tie
                        ));
            } else {
                var dirElastic = getElasticSortingDirection(histogramDto.getBucketSortingDirection());
                h.order(List.of(
                        NamedValue.of("_key", dirElastic), NamedValue.of("_count", elasticSecondaryDirection) // tie
                        ));
            }

            return h;
        }));

        return agg;
    }

    @SuppressWarnings("UnusedVariable")
    private Aggregation createAggregation_filter(OgcFacetConfig filterDto) {
        var filters = new HashMap<String, Query>();
        for (var f : filterDto.getFilters()) {
            var filterName = f.getFilterName();
            var filterValueOgc = f.getFilterEquationCql();

            Query query = null;
            try {
                query = cqlToElasticSearch.create(filterValueOgc);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            filters.put(filterName, query);
        }
        var agg = Aggregation.of(a -> {
            a.filters(f -> f.filters(ff -> ff.keyed(filters)));

            // can pipeline to sort.... but its complicated and doesn't make much sense here

            return a;
        });

        return agg;
    }

    public Aggregation createAggregation_terms(OgcFacetConfig termsDto, Integer defaultBucketCount) {
        var minCount = termsDto.getMinimumDocumentCount();
        if (minCount <= 0) {
            minCount = 1;
        }
        var _minCount = minCount; // effectively final
        var nBuckets = termsDto.getBucketCount() == null ? defaultBucketCount : termsDto.getBucketCount();
        var agg = Aggregation.of(a -> a.terms(t -> {
            var correspondingField = dynamicPropertiesFacade.findFieldForFacet(termsDto);

            t.field(correspondingField.elasticProperty);
            t.minDocCount(_minCount);
            t.size(nBuckets);

            var bucketSorting = getBucketSorting(termsDto.getBucketSorting());

            var elasticSecondaryDirection = getElasticSortingDirection(termsDto.getBucketSortingDirection());

            if (bucketSorting == COUNT) {
                var dirElastic = getElasticSortingDirection(termsDto.getBucketSortingDirection());
                t.order(List.of(
                        NamedValue.of("_count", dirElastic), NamedValue.of("_key", elasticSecondaryDirection) // tie
                        ));
            } else {
                var dirElastic = getElasticSortingDirection(termsDto.getBucketSortingDirection());
                t.order(List.of(
                        NamedValue.of("_key", dirElastic), NamedValue.of("_count", elasticSecondaryDirection) // tie
                        ));
            }

            return t;
        }));
        return agg;
    }

    public BucketSorting getBucketSorting(BucketSorting sorting) {
        return sorting == null ? BucketSorting.COUNT : sorting;
    }

    public SortOrder getElasticSortingDirection(BucketSortingDirection direction) {
        var dir = direction == null ? BucketSortingDirection.DESCENDING : direction;
        var dirElastic = dir == BucketSortingDirection.DESCENDING ? SortOrder.Desc : SortOrder.Asc;
        return dirElastic;
    }
}
