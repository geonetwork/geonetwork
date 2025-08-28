/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.facets;

import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.google.common.base.Strings;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.records.generated.model.*;
import org.geonetwork.ogcapi.service.configuration.*;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.ExtraElasticPropertiesService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// puts facets inside the GeoJSON response
@Component
public class FacetsResponseInjector {

    @Autowired
    HistogramBucketReJiggler histogramBucketReJiggler;

    @Autowired
    ExtraElasticPropertiesService extraElasticPropertiesService;

    public enum SortType {
        STRING,
        NUMBER,
        DATE
    }

    public static final int DEFAULT_MAX_BUCKETS = 20;

    /**
     * Setup the facets portion of the response.
     *
     * @param webResponse put the facets in here (goes to user)
     * @param searchResponse from elastic
     */
    public void injectFacets(
            OgcApiRecordsGetRecords200ResponseDto webResponse, SearchResponse<IndexRecord> searchResponse) {

        var facets = extraElasticPropertiesService.getFacetConfigs();

        if (facets == null || facets.isEmpty()) {
            return; // nothing to doOgcApiItemsApi.java
        }
        var result = new HashMap<String, OgcApiRecordsFacetSummaryDto>();

        for (var f : facets) {
            var name = f.getFacetName();

            var summary =
                    setupSummary(extraElasticPropertiesService.getFacetsDefaultBucketCount(), name, f, searchResponse);

            result.put(name, summary);
        }

        webResponse.setFacets(result);
    }

    private OgcApiRecordsFacetSummaryDto setupSummary(
            Integer defaultBucketCount,
            String name,
            OgcFacetConfig config,
            SearchResponse<IndexRecord> searchResponse) {

        if (config.getFacetType() == FacetType.FILTER) {
            return setupSummary_filter(name, searchResponse, config, defaultBucketCount);
        } else if (config.getFacetType() == FacetType.HISTOGRAM_FIXED_BUCKET_COUNT
                || config.getFacetType() == FacetType.HISTOGRAM_FIXED_INTERVAL) {
            return setupSummary_histogram(name, searchResponse, config, defaultBucketCount);
        } else if (config.getFacetType() == FacetType.TERM) {
            return setupSummary_terms(name, searchResponse, config, defaultBucketCount);
        } else {
            throw new RuntimeException(
                    "Unsupported facet type: " + config.getClass().getSimpleName());
        }
    }

    private @NotNull OgcApiRecordsFacetSummaryDto setupSummary_terms(
            String name,
            SearchResponse<IndexRecord> searchResponse,
            OgcFacetConfig termsDto,
            Integer defaultBucketCount) {
        var result = new OgcApiRecordsFacetSummaryDto();
        result.setType(FacetType.ogcString(termsDto.getFacetType()));

        result.setProperty(termsDto.getField().getOgcProperty());

        var buckets = new ArrayList<OgcApiRecordsFacetResultBucketDto>();

        var agg = searchResponse.aggregations().get("facet." + name);
        var stringTermsAgg = ((StringTermsAggregate) agg._get());
        var moreDocs = stringTermsAgg.sumOtherDocCount() > 0;
        @SuppressWarnings("unchecked")
        var elasticBuckets = ((List<StringTermsBucket>) stringTermsAgg.buckets()._get());
        for (var elasticBucket : elasticBuckets) {
            var b = new OgcApiRecordsFacetResultBucketDto();
            b.setCount((int) elasticBucket.docCount());
            b.setValue(elasticBucket.key()._get().toString());
            buckets.add(b);
        }
        result.setBuckets(buckets);
        handleBuckets(
                result,
                defaultBucketCount,
                termsDto.getMinimumDocumentCount(),
                termsDto.getBucketSorting(),
                SortType.STRING,
                moreDocs,
                termsDto.getBucketCount());
        return result;
    }

    private @NotNull OgcApiRecordsFacetSummaryDto setupSummary_histogram(
            String name,
            SearchResponse<IndexRecord> searchResponse,
            OgcFacetConfig histogramDto,
            Integer defaultBucketCount) {
        var result = new OgcApiRecordsFacetSummaryDto();
        result.setType(FacetType.ogcString(histogramDto.getFacetType()));

        result.setProperty(histogramDto.getField().getOgcProperty());

        List<OgcApiRecordsFacetResultBucketDto> buckets;

        var agg = searchResponse.aggregations().get("facet." + name)._get();

        if (agg instanceof HistogramAggregate _histogramAgg) {
            buckets = handleHistogramBuckets_HistogramAggregate(histogramDto, _histogramAgg);
        } else if (agg instanceof VariableWidthHistogramAggregate _variableWidthAgg) {
            buckets = handleHistogramBuckets_VariableHistogramAggregate(histogramDto, _variableWidthAgg);
        } else if (agg instanceof DateHistogramAggregate _dateHistogramAgg) {
            buckets = handleHistogramBuckets_VariableHistogramAggregate_Date(histogramDto, _dateHistogramAgg);
        } else if (agg instanceof AutoDateHistogramAggregate _autoDateHistogramAgg) {
            buckets = handleHistogramBuckets_VariableHistogramAggregate_Date_fixedBucketNumber(
                    histogramDto, _autoDateHistogramAgg);
        } else {
            throw new RuntimeException(
                    "unknown aggregation type: " + agg.getClass().getSimpleName());
        }

        SortType sortType = SortType.NUMBER;
        var ogcProperty = histogramDto.getField().getOgcProperty();
        var type = this.extraElasticPropertiesService
                .getElasticTypingSystem()
                .getFinalElasticTypes()
                .get(ogcProperty)
                .getType();
        if (type == SimpleType.DATE) {
            sortType = SortType.DATE;
        }

        result.setBuckets(buckets);
        handleBuckets(
                result,
                defaultBucketCount,
                0,
                histogramDto.getBucketSorting(),
                sortType,
                false,
                histogramDto.getBucketCount());
        return result;
    }

    private List<OgcApiRecordsFacetResultBucketDto> handleHistogramBuckets_HistogramAggregate(
            OgcFacetConfig histogramDto, HistogramAggregate agg) {
        List<OgcApiRecordsFacetResultBucketDto> buckets = new ArrayList<>();
        @SuppressWarnings("unchecked")
        var elasticBuckets = ((Map<String, HistogramBucket>) agg.buckets()._get());
        for (var elasticBucket : elasticBuckets.entrySet()) {
            var b = new OgcApiRecordsFacetResultBucketDto();
            b.setCount((int) elasticBucket.getValue().docCount());

            b.setMin(Double.toString(elasticBucket.getValue().key()));
            b.setMax(Double.toString(elasticBucket.getValue().key() + histogramDto.getNumberBucketInterval()));

            b.setValue(Double.toString(elasticBucket.getValue().key()));
            buckets.add(b);
        }
        return buckets;
    }

    @SuppressWarnings("UnusedVariable")
    private List<OgcApiRecordsFacetResultBucketDto>
            handleHistogramBuckets_VariableHistogramAggregate_Date_fixedBucketNumber(
                    OgcFacetConfig histogramDto, AutoDateHistogramAggregate agg) {
        List<OgcApiRecordsFacetResultBucketDto> buckets = new ArrayList<>();
        @SuppressWarnings("unchecked")
        var elasticBuckets = (List<DateHistogramBucket>) agg.buckets()._get();
        for (var elasticBucket : elasticBuckets) {
            var b = new OgcApiRecordsFacetResultBucketDto();
            b.setCount((int) elasticBucket.docCount());

            var lowerDate = Instant.ofEpochMilli(elasticBucket.key());
            var upperDate = addTimeVariable(lowerDate, agg.interval());

            b.setMin(lowerDate.toString());
            b.setMax(upperDate.toString());
            b.setValue(Long.toString(elasticBucket.key()));

            buckets.add(b);
        }
        buckets = histogramBucketReJiggler.reJiggle(buckets, histogramDto);
        return buckets;
    }

    private Instant addTimeVariable(Instant i, Time interval) {
        var zoned = ZonedDateTime.ofInstant(i, ZoneId.of("UTC"));

        Pattern pattern = Pattern.compile("(\\d+)(.+)"); // Matches one or more digits
        Matcher matcher = pattern.matcher(interval.time());
        if (!matcher.find()) {
            throw new RuntimeException("invalid time format from elastic: " + interval.time());
        }
        int n = Integer.parseInt(matcher.group(1));
        String unit = matcher.group(2);
        if (n == 0 || Strings.isNullOrEmpty(unit)) {
            throw new RuntimeException("invalid time format from elastic: " + interval.time());
        }
        if (unit.equals("years") || unit.equals("y")) {
            return zoned.plus(n, ChronoUnit.YEARS).toInstant();
        } else if (unit.equals("months") || unit.equals("M")) {
            return zoned.plus(n, ChronoUnit.MONTHS).toInstant();
        } else if (unit.equals("days") || unit.equals("d")) {
            return zoned.plus(n, ChronoUnit.DAYS).toInstant();
        } else if (unit.equals("hours") || unit.equals("h")) {
            return zoned.plus(n, ChronoUnit.HOURS).toInstant();
        } else if (unit.equals("minutes") || unit.equals("m")) {
            return zoned.plus(n, ChronoUnit.MINUTES).toInstant();
        } else if (unit.equals("seconds") || unit.equals("s")) {
            return zoned.plus(n, ChronoUnit.SECONDS).toInstant();
        } else {
            throw new RuntimeException("invalid time format from elastic: " + interval.time());
        }
    }

    private List<OgcApiRecordsFacetResultBucketDto> handleHistogramBuckets_VariableHistogramAggregate_Date(
            OgcFacetConfig histogramDto, DateHistogramAggregate agg) {
        List<OgcApiRecordsFacetResultBucketDto> buckets = new ArrayList<>();
        @SuppressWarnings("unchecked")
        var elasticBuckets = (List<DateHistogramBucket>) agg.buckets()._get();
        for (var elasticBucket : elasticBuckets) {
            var b = new OgcApiRecordsFacetResultBucketDto();
            b.setCount((int) elasticBucket.docCount());

            var lowerDate = Instant.ofEpochMilli(elasticBucket.key());
            var upperDate = addTime(lowerDate, histogramDto.getCalendarIntervalUnit());

            b.setMin(lowerDate.toString());
            b.setMax(upperDate.toString());
            b.setValue(Long.toString(elasticBucket.key()));

            buckets.add(b);
        }
        return buckets;
    }

    private Instant addTime(Instant i, CalendarIntervalUnit unit) {
        var zoned = ZonedDateTime.ofInstant(i, ZoneId.of("UTC"));

        var s = unit.toString();
        if (s.equalsIgnoreCase("year")) {
            return zoned.plus(1, ChronoUnit.YEARS).toInstant();
        } else if (s.equalsIgnoreCase("month")) {
            return zoned.plus(1, ChronoUnit.MONTHS).toInstant();
        } else if (s.equalsIgnoreCase("week")) {
            return zoned.plus(1, ChronoUnit.WEEKS).toInstant();
        } else if (s.equalsIgnoreCase("day")) {
            return zoned.plus(1, ChronoUnit.DAYS).toInstant();
        } else if (s.equalsIgnoreCase("hour")) {
            return zoned.plus(1, ChronoUnit.HOURS).toInstant();
        } else if (s.equalsIgnoreCase("minute")) {
            return zoned.plus(1, ChronoUnit.MINUTES).toInstant();
        } else if (s.equalsIgnoreCase("second")) {
            return zoned.plus(1, ChronoUnit.SECONDS).toInstant();
        } else if (s.equalsIgnoreCase("quarter")) {
            return zoned.plus(3, ChronoUnit.MONTHS).toInstant();
        } else {
            throw new RuntimeException("unknown chronounit type: " + s);
        }
    }

    private List<OgcApiRecordsFacetResultBucketDto> handleHistogramBuckets_VariableHistogramAggregate(
            OgcFacetConfig histogramDto, VariableWidthHistogramAggregate agg) {
        List<OgcApiRecordsFacetResultBucketDto> buckets = new ArrayList<>();

        @SuppressWarnings("unchecked")
        var elasticBuckets = (List<VariableWidthHistogramBucket>) agg.buckets()._get();
        for (var elasticBucket : elasticBuckets) {
            var b = new OgcApiRecordsFacetResultBucketDto();
            b.setCount((int) elasticBucket.docCount());

            b.setMin(String.valueOf(elasticBucket.min()));
            b.setMax(String.valueOf(elasticBucket.max()));
            b.setValue(b.getMin());

            buckets.add(b);
        }
        buckets = histogramBucketReJiggler.reJiggle(buckets, histogramDto);
        return buckets;
    }

    @SuppressWarnings("unused")
    private @NotNull OgcApiRecordsFacetSummaryDto setupSummary_filter(
            String name,
            SearchResponse<IndexRecord> searchResponse,
            OgcFacetConfig filterDto,
            Integer defaultBucketCount) {
        var result = new OgcApiRecordsFacetSummaryDto();
        result.setType(FacetType.ogcString(filterDto.getFacetType()));
        var buckets = new ArrayList<OgcApiRecordsFacetResultBucketDto>();

        var agg = searchResponse.aggregations().get("facet." + name);
        var filtersAgg = ((FiltersAggregate) agg._get());
        var bucketsMap = filtersAgg.buckets().keyed();
        for (var bucket : bucketsMap.entrySet()) {
            var bucketOgc = new OgcApiRecordsFacetResultBucketDto();
            bucketOgc.setValue(bucket.getKey());
            bucketOgc.setCount((int) bucket.getValue().docCount());
            buckets.add(bucketOgc);
        }
        result.setBuckets(buckets);
        return result;
    }

    /**
     * We are give a direct translation of the elastic search results. However, ogcapi-records defines a few things like
     * defaultBucketCount, ordering, and minimum number of matched documents.
     *
     * <p>We: 1. order the buckets correctly 2. remove buckets without enough matched documents 3. remove buckets if
     * there are too many (>defaultBucketCount)
     *
     * @param result all buckets
     * @param defaultBucketCount default # of buckets
     * @param minOccurs bucket must contain at lease this number of items
     * @param sortedBy sorting option (alphabetical for term or count of documents)
     * @param moreDocs there were more documents that were not returned by elastic
     * @param bucketCount how many buckets are configured?
     */
    private void handleBuckets(
            OgcApiRecordsFacetSummaryDto result,
            Integer defaultBucketCount,
            Integer minOccurs,
            BucketSorting sortedBy,
            SortType sortType,
            boolean moreDocs,
            Integer bucketCount) {

        var originalNumberOfBuckets = result.getBuckets().size();
        if (defaultBucketCount == null || defaultBucketCount <= 0) {
            defaultBucketCount = DEFAULT_MAX_BUCKETS;
        }

        var nBuckets = (bucketCount == null || bucketCount <= 0) ? defaultBucketCount : bucketCount;

        if (minOccurs == null || minOccurs == 0 || minOccurs <= 0) {
            minOccurs = 1;
        }
        if (sortedBy == null) {
            sortedBy = BucketSorting.COUNT;
        }

        // sort
        if (sortedBy == BucketSorting.COUNT) {
            // elastic does this by default, but we double check here
            result.getBuckets().sort(Comparator.comparingInt(x -> x.getCount()));
            Collections.reverse(result.getBuckets()); // descending
        } else {
            if (sortType == SortType.NUMBER) {
                result.getBuckets().sort(Comparator.comparingDouble(x -> Double.parseDouble(x.getValue())));
            } else if (sortType == SortType.STRING) {
                result.getBuckets().sort(Comparator.comparing(x -> x.getValue()));
            } else if (sortType == SortType.DATE) {
                result.getBuckets().sort(Comparator.comparing(x -> x.getValue()));
            }
            Collections.reverse(result.getBuckets()); // descending
        }

        var _minOccurs = minOccurs; // effectively final
        // min occurs
        var newBuckets = new ArrayList<OgcApiRecordsFacetResultBucketDto>(result.getBuckets().stream()
                .filter(x -> x.getCount() >= _minOccurs)
                .toList());

        var nDocsRemoved = result.getBuckets().stream()
                .filter(x -> x.getCount() < _minOccurs)
                .mapToInt(x -> x.getCount())
                .sum();

        // max # of buckets
        if (newBuckets.size() > nBuckets) {
            newBuckets.subList(nBuckets, newBuckets.size()).clear();
        }
        result.setBuckets(newBuckets);

        // more documents not represented in these facet
        result.setMore(moreDocs || (newBuckets.size() < originalNumberOfBuckets && nDocsRemoved > 0));
    }
}
