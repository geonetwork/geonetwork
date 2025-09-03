/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.facets;

import static org.geonetwork.ogcapi.service.facets.FacetsResponseInjector.DEFAULT_MAX_BUCKETS;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetFilterDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetHistogramDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetTermsDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetsDto;
import org.geonetwork.ogcapi.service.cql.CqlToElasticSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "org.geonetwork.ogcapi.service.search")
public class RecordsFacetsElasticQueryBuilder {

    @Autowired
    CqlToElasticSearch cqlToElasticSearch;

    public Map<String, Aggregation> createElasticAggregationsFromFacetsDefinition(OgcApiRecordsFacetsDto facets) {
        var aggregations = new HashMap<String, Aggregation>();

        var defaultBucketCount =
                facets.getDefaultBucketCount() == null ? DEFAULT_MAX_BUCKETS : facets.getDefaultBucketCount();

        for (var facetInfo : facets.getFacets().entrySet()) {
            var facetName = facetInfo.getKey();
            var facetValue = facetInfo.getValue();

            if (facetValue instanceof OgcApiRecordsFacetTermsDto termsDto) {
                var agg = createAggregation_terms(termsDto, defaultBucketCount);
                aggregations.put("facet." + facetName, agg);
            } else if (facetValue instanceof OgcApiRecordsFacetFilterDto filterDto) {
                var agg = createAggregation_filter(filterDto, facets);
                aggregations.put("facet." + facetName, agg);
            } else if (facetValue instanceof OgcApiRecordsFacetHistogramDto histogramDto) {
                var agg = createAggregation_histogram(histogramDto, facets, defaultBucketCount);
                aggregations.put("facet." + facetName, agg);
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
     * @param facets facets response from elastic
     * @param defaultBucketCount defined default # of buckets
     * @return elastic histogram Aggregration
     */
    private Aggregation createAggregation_histogram(
            OgcApiRecordsFacetHistogramDto histogramDto, OgcApiRecordsFacetsDto facets, Integer defaultBucketCount) {
        if (histogramDto.getxElasticDatatype() == OgcApiRecordsFacetHistogramDto.XElasticDatatypeEnum.NUMBER) {
            if (histogramDto.getBucketType() == OgcApiRecordsFacetHistogramDto.BucketTypeEnum.FIXED_INTERVAL) {
                return createAggregation_histogram_number_fixedInterval(histogramDto, facets, defaultBucketCount);
            } else {
                return createAggregation_histogram_number_fixedBucketCount(histogramDto, facets, defaultBucketCount);
            }
        } else {
            if (histogramDto.getBucketType() == OgcApiRecordsFacetHistogramDto.BucketTypeEnum.FIXED_INTERVAL) {
                return createAggregation_histogram_date_fixedInterval(histogramDto, facets, defaultBucketCount);
            } else {
                return createAggregation_histogram_date_fixedBucketCount(histogramDto, facets, defaultBucketCount);
            }
        }
    }

    @SuppressWarnings("unused")
    private Aggregation createAggregation_histogram_number_fixedBucketCount(
            OgcApiRecordsFacetHistogramDto histogramDto, OgcApiRecordsFacetsDto facets, Integer defaultBucketCount) {
        var nBuckets = histogramDto.getBucketCount() == null ? defaultBucketCount : histogramDto.getBucketCount();

        var agg = Aggregation.of(a -> a.variableWidthHistogram(h -> {
            h.field(histogramDto.getxElasticProperty());
            h.buckets(nBuckets);
            return h;
        }));

        return agg;
    }

    @SuppressWarnings("unused")
    private Aggregation createAggregation_histogram_number_fixedInterval(
            OgcApiRecordsFacetHistogramDto histogramDto, OgcApiRecordsFacetsDto facets, Integer defaultBucketCount) {
        var agg = Aggregation.of(a -> a.histogram(h -> {
            h.field(histogramDto.getxElasticProperty());
            h.keyed(true);
            h.interval(histogramDto.getxIntervalNumber().doubleValue());
            if (histogramDto.getxMinimumDocCount() != null && histogramDto.getxMinimumDocCount() >= 0) {
                h.minDocCount(histogramDto.getxMinimumDocCount());
            }
            return h;
        }));

        return agg;
    }

    @SuppressWarnings("unused")
    private Aggregation createAggregation_histogram_date_fixedBucketCount(
            OgcApiRecordsFacetHistogramDto histogramDto, OgcApiRecordsFacetsDto facets, Integer defaultBucketCount) {
        var nBuckets = histogramDto.getBucketCount() == null ? defaultBucketCount : histogramDto.getBucketCount();

        var agg = Aggregation.of(a -> a.autoDateHistogram(h -> {
            h.field(histogramDto.getxElasticProperty());
            h.buckets(nBuckets);
            return h;
        }));
        return agg;
    }

    @SuppressWarnings("unused")
    private Aggregation createAggregation_histogram_date_fixedInterval(
            OgcApiRecordsFacetHistogramDto histogramDto, OgcApiRecordsFacetsDto facets, Integer defaultBucketCount) {
        var agg = Aggregation.of(a -> a.dateHistogram(h -> {
            h.field(histogramDto.getxElasticProperty());
            h.keyed(false);
            var interval =
                    CalendarInterval._DESERIALIZER.deserialize(histogramDto.getxIntervalCalendarInterval(), null);
            h.calendarInterval(interval);
            if (histogramDto.getxMinimumDocCount() != null && histogramDto.getxMinimumDocCount() >= 0) {
                h.minDocCount(histogramDto.getxMinimumDocCount());
            }
            return h;
        }));

        return agg;
    }

    @SuppressWarnings("UnusedVariable")
    private Aggregation createAggregation_filter(OgcApiRecordsFacetFilterDto filterDto, OgcApiRecordsFacetsDto facets) {
        var filters = new HashMap<String, Query>();
        for (var f : filterDto.getFilters().entrySet()) {
            var filterName = f.getKey();
            var filterValueOgc = f.getValue();

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

    public Aggregation createAggregation_terms(OgcApiRecordsFacetTermsDto termsDto, Integer defaultBucketCount) {
        var minCount = termsDto.getMinOccurs();
        if (minCount <= 0) {
            minCount = 1;
        }
        var _minCount = minCount; // effectively final
        var nBuckets = termsDto.getBucketCount() == null ? defaultBucketCount : termsDto.getBucketCount();
        var agg = Aggregation.of(a -> a.terms(t -> {
            t.field(termsDto.getxElasticProperty());
            t.minDocCount(_minCount);
            t.size(nBuckets);
            return t;
        }));
        return agg;
    }
}
