/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.facets;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.ogcapi.service.configuration.FacetType;
import org.geonetwork.ogcapi.service.configuration.OgcFacetConfig;
import org.geonetwork.ogcapi.service.configuration.SimpleType;
import org.geonetwork.ogcapi.service.cql.CqlToElasticSearch;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.ExtraElasticPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "org.geonetwork.ogcapi.service.search")
public class RecordsFacetsElasticQueryBuilder {

    @Autowired
    CqlToElasticSearch cqlToElasticSearch;

    @Autowired
    ExtraElasticPropertiesService extraElasticPropertiesService;

    public Map<String, Aggregation> createElasticAggregationsFromFacetsDefinition() {
        var facets = extraElasticPropertiesService.getFacetConfigs();

        var aggregations = new HashMap<String, Aggregation>();

        var defaultBucketCount = extraElasticPropertiesService.getConfig().getDefaultBucketCount();

        for (var facetInfo : facets) {
            var facetName = facetInfo.getFacetName();
            var elasticProperty = facetInfo.getField().getElasticProperty();
            var type = this.extraElasticPropertiesService
                    .getElasticTypingSystem()
                    .getFinalElasticTypes()
                    .get(elasticProperty)
                    .getType();

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
            h.field(histogramDto.getField().getElasticProperty());
            h.buckets(nBuckets);
            return h;
        }));

        return agg;
    }

    @SuppressWarnings("unused")
    private Aggregation createAggregation_histogram_number_fixedInterval(
            OgcFacetConfig histogramDto, SimpleType simpleType, Integer defaultBucketCount) {
        var agg = Aggregation.of(a -> a.histogram(h -> {
            h.field(histogramDto.getField().getElasticProperty());
            h.keyed(true);
            h.interval(histogramDto.getNumberBucketInterval());
            h.minDocCount(histogramDto.getMinimumDocumentCount());
            return h;
        }));

        return agg;
    }

    @SuppressWarnings("unused")
    private Aggregation createAggregation_histogram_date_fixedBucketCount(
            OgcFacetConfig histogramDto, SimpleType simpleType, Integer defaultBucketCount) {
        var nBuckets = histogramDto.getBucketCount() == null ? defaultBucketCount : histogramDto.getBucketCount();

        var agg = Aggregation.of(a -> a.autoDateHistogram(h -> {
            h.field(histogramDto.getField().getElasticProperty());
            h.buckets(nBuckets);
            return h;
        }));
        return agg;
    }

    @SuppressWarnings("unused")
    private Aggregation createAggregation_histogram_date_fixedInterval(
            OgcFacetConfig histogramDto, SimpleType simpleType, Integer defaultBucketCount) {
        var agg = Aggregation.of(a -> a.dateHistogram(h -> {
            h.field(histogramDto.getField().getElasticProperty());
            h.keyed(false);
            var interval = CalendarInterval._DESERIALIZER.deserialize(
                    histogramDto.getCalendarIntervalUnit().toString(), null);
            h.calendarInterval(interval);
            h.minDocCount(histogramDto.getMinimumDocumentCount());
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
        var agg = Aggregation.of(a -> a.filters(f -> f.filters(ff -> ff.keyed(filters))));

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
            t.field(termsDto.getField().elasticProperty);
            t.minDocCount(_minCount);
            t.size(nBuckets);
            return t;
        }));
        return agg;
    }
}
