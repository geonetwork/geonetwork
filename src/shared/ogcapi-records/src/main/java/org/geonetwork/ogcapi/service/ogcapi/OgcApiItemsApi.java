/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.ogcapi;

import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.controllerexceptions.NotFoundException;
import org.geonetwork.ogcapi.records.generated.model.*;
import org.geonetwork.ogcapi.service.dataaccess.CatalogApi;
import org.geonetwork.ogcapi.service.dataaccess.ElasticWithUserPermissions;
import org.geonetwork.ogcapi.service.dataaccess.SimpleElastic;
import org.geonetwork.ogcapi.service.indexConvert.OgcApiGeoJsonConverter;
import org.geonetwork.ogcapi.service.links.FormatterApiRecordLinkAttacher;
import org.geonetwork.ogcapi.service.links.ItemPageLinks;
import org.geonetwork.ogcapi.service.links.ItemsPageLinks;
import org.geonetwork.ogcapi.service.querybuilder.OgcApiQuery;
import org.geonetwork.ogcapi.service.search.RecordsEsQueryBuilder;
import org.geonetwork.ogcapi.service.search.RecordsFacetsBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

/** High level implementation for the ogcapi-records "Items" endpoints. */
@Component
public class OgcApiItemsApi {

    //    private @NotNull OgcApiRecordsFacetSummaryDto getOgcApiRecordsFacetSummaryDto(
    //            Integer defaultBucketCount,
    //            String name,
    //            SearchResponse<IndexRecord> searchResponse,
    //            OgcApiRecordsFacetHistogramDto histogramDto) {
    //        var result = new OgcApiRecordsFacetSummaryDto();
    //        result.setType(histogramDto.getType());
    //        result.setProperty(histogramDto.getProperty());
    //        var buckets = new ArrayList<OgcApiRecordsFacetResultBucketDto>();
    //
    //        var agg = searchResponse.aggregations().get("facet." + name);
    //        var histoAgg = ((HistogramAggregate) agg._get());
    //        @SuppressWarnings("unchecked")
    //        var elasticBuckets = ((Map<String, HistogramBucket>) histoAgg.buckets()._get());
    //        for (var elasticBucket : elasticBuckets.entrySet()) {
    //            var b = new OgcApiRecordsFacetResultBucketDto();
    //            b.setCount((int) elasticBucket.getValue().docCount());
    //            b.setValue(Double.toString(elasticBucket.getValue().key()));
    //            buckets.add(b);
    //        }
    //        result.setBuckets(buckets);
    //        handleBuckets(
    //                result,
    //                defaultBucketCount,
    //                0,
    //                histogramDto.getSortedBy(),
    //                SortType.NUMBER,
    //                false,
    //                histogramDto.getBucketCount());
    //        return result;
    //    }

    public enum SortType {
        STRING,
        NUMBER
    }

    @Autowired
    CatalogApi catalogApi;

    @Autowired
    RecordsEsQueryBuilder recordsEsQueryBuilder;

    @Autowired
    private SimpleElastic simpleElastic;

    @Autowired
    private ElasticWithUserPermissions elasticWithUserPermissions;

    @Autowired
    private OgcApiGeoJsonConverter geoJsonConverter;

    @Autowired
    private ItemsPageLinks itemsPageLinks;

    @Autowired
    private ItemPageLinks itemPageLinks;

    @Autowired
    private NativeWebRequest nativeWebRequest;

    @Autowired
    FormatterApiRecordLinkAttacher formatterApiRecordLinkAttacher;

    @Autowired
    RecordsFacetsBuilder recordsFacetsBuilder;

    /**
     * gets a record in a collection. collections/{collectionid}/items/{itemid} endpoint
     *
     * @param collectionId which collection (not needed)
     * @param recordId which record
     * @return geojson feature
     * @throws NotFoundException record not found
     * @throws IOException problem getting record
     */
    public OgcApiRecordsRecordGeoJSONDto getRecord(String collectionId, String recordId)
            throws NotFoundException, IOException {
        var recordIndexRecord = simpleElastic.getOne(recordId);
        var result = geoJsonConverter.convert(recordIndexRecord, null);

        itemPageLinks.addLinks(nativeWebRequest, collectionId, result);

        try {
            formatterApiRecordLinkAttacher.attachLinks(result, collectionId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * given an OGCAPI-records query, execute and return the results. collections/{collectionid}/items endpoint
     *
     * @param ogcApiQuery query from user
     * @return set of GeoJson features
     * @throws Exception problem with the query.
     */
    public OgcApiRecordsGetRecords200ResponseDto getRecords(OgcApiQuery ogcApiQuery, OgcApiRecordsFacetsDto facets)
            throws Exception {
        var records = getRecordsFromElastic(ogcApiQuery, facets);
        var totalNumHits = records.hits().total().value();
        var indexRecords = records.hits().hits().stream().map(x -> x.source()).toList();
        var features = indexRecords.stream()
                .map(x -> geoJsonConverter.convert(x, null))
                .toList();
        var response = new OgcApiRecordsGetRecords200ResponseDto();
        response.setType(OgcApiRecordsGetRecords200ResponseDto.TypeEnum.FEATURE_COLLECTION);
        response.setFeatures(features);
        response.numberMatched((int) totalNumHits);
        response.numberReturned(features.size());
        response.setTimeStamp(OffsetDateTime.now(ZoneId.of("UTC")));
        itemsPageLinks.addLinks(nativeWebRequest, ogcApiQuery.getCollectionId(), response, ogcApiQuery);
        features.stream().forEach(x -> itemPageLinks.addLinks(nativeWebRequest, ogcApiQuery.getCollectionId(), x));

        features.stream().forEach(x -> {
            try {
                formatterApiRecordLinkAttacher.attachLinks(x, ogcApiQuery.getCollectionId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        injectFacets(response, records, facets);

        return response;
    }

    /**
     * Setup the facets portion of the response.
     *
     * @param webResponse put the facets in here (goes to user)
     * @param searchResponse from elastic
     * @param facets facet's configuration
     */
    private void injectFacets(
            OgcApiRecordsGetRecords200ResponseDto webResponse,
            SearchResponse<IndexRecord> searchResponse,
            OgcApiRecordsFacetsDto facets) {
        if (facets.getFacets() == null && !facets.getFacets().isEmpty()) {
            return; // nothing to doOgcApiItemsApi.java
        }
        var result = new HashMap<String, OgcApiRecordsFacetSummaryDto>();

        for (var f : facets.getFacets().entrySet()) {
            var name = f.getKey();
            var config = f.getValue();
            var summary = setupSummary(facets.getDefaultBucketCount(), name, config, searchResponse);

            result.put(name, summary);
        }

        webResponse.setFacets(result);
    }

    private OgcApiRecordsFacetSummaryDto setupSummary(
            Integer defaultBucketCount,
            String name,
            OgcApiRecordsFacetDto config,
            SearchResponse<IndexRecord> searchResponse) {
        switch (config) {
            case OgcApiRecordsFacetFilterDto filterDto -> {
                return setupSummary_filter(name, searchResponse, filterDto, defaultBucketCount);
            }
            case OgcApiRecordsFacetHistogramDto histogramDto -> {
                return setupSummary_histogram(name, searchResponse, histogramDto, defaultBucketCount);
            }
            case OgcApiRecordsFacetTermsDto termsDto -> {
                return setupSummary_terms(name, searchResponse, termsDto, defaultBucketCount);
            }
            default -> throw new RuntimeException(
                    "Unsupported facet type: " + config.getClass().getSimpleName());
        }
    }

    private @NotNull OgcApiRecordsFacetSummaryDto setupSummary_terms(
            String name,
            SearchResponse<IndexRecord> searchResponse,
            OgcApiRecordsFacetTermsDto termsDto,
            Integer defaultBucketCount) {
        var result = new OgcApiRecordsFacetSummaryDto();
        result.setType(termsDto.getType());
        result.setProperty(termsDto.getProperty());

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
                termsDto.getMinOccurs(),
                termsDto.getSortedBy(),
                SortType.STRING,
                moreDocs,
                termsDto.getBucketCount());
        return result;
    }

    private @NotNull OgcApiRecordsFacetSummaryDto setupSummary_histogram(
            String name,
            SearchResponse<IndexRecord> searchResponse,
            OgcApiRecordsFacetHistogramDto histogramDto,
            Integer defaultBucketCount) {
        var result = new OgcApiRecordsFacetSummaryDto();
        result.setType(histogramDto.getType());
        result.setProperty(histogramDto.getProperty());
        var buckets = new ArrayList<OgcApiRecordsFacetResultBucketDto>();

        var agg = searchResponse.aggregations().get("facet." + name);
        var histoAgg = ((HistogramAggregate) agg._get());
        @SuppressWarnings("unchecked")
        var elasticBuckets = ((Map<String, HistogramBucket>) histoAgg.buckets()._get());
        for (var elasticBucket : elasticBuckets.entrySet()) {
            var b = new OgcApiRecordsFacetResultBucketDto();
            b.setCount((int) elasticBucket.getValue().docCount());
            b.setValue(Double.toString(elasticBucket.getValue().key()));
            buckets.add(b);
        }
        result.setBuckets(buckets);
        handleBuckets(
                result,
                defaultBucketCount,
                0,
                histogramDto.getSortedBy(),
                SortType.NUMBER,
                false,
                histogramDto.getBucketCount());
        return result;
    }

    @SuppressWarnings("unused")
    private @NotNull OgcApiRecordsFacetSummaryDto setupSummary_filter(
            String name,
            SearchResponse<IndexRecord> searchResponse,
            OgcApiRecordsFacetFilterDto filterDto,
            Integer defaultBucketCount) {
        var result = new OgcApiRecordsFacetSummaryDto();
        result.setType(filterDto.getType());
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
            OgcApiRecordsFacetSortedByDto sortedBy,
            SortType sortType,
            boolean moreDocs,
            Integer bucketCount) {
        var originalNumberOfBuckets = result.getBuckets().size();
        if (defaultBucketCount == null || defaultBucketCount <= 0) {
            defaultBucketCount = Integer.MAX_VALUE;
        }

        var nBuckets = (bucketCount == null || bucketCount <= 0) ? defaultBucketCount : bucketCount;

        if (minOccurs == null || minOccurs == 0 || minOccurs <= 0) {
            minOccurs = 1;
        }
        if (sortedBy == null) {
            sortedBy = OgcApiRecordsFacetSortedByDto.COUNT;
        }

        // sort
        if (sortedBy == OgcApiRecordsFacetSortedByDto.COUNT) {
            // elastic does this by default, but we double check here
            result.getBuckets().sort(Comparator.comparingInt(x -> x.getCount()));
            Collections.reverse(result.getBuckets()); // descending
        } else {
            if (sortType == SortType.NUMBER) {
                result.getBuckets().sort(Comparator.comparingDouble(x -> Double.parseDouble(x.getValue())));
            } else { // STRING
                result.getBuckets().sort(Comparator.comparing(x -> x.getValue()));
            }
            Collections.reverse(result.getBuckets()); // descending
        }

        var _minOccurs = minOccurs; // effectively final
        // min occurs
        var newBuckets = new ArrayList<OgcApiRecordsFacetResultBucketDto>(result.getBuckets().stream()
                .filter(x -> x.getCount() >= _minOccurs)
                .toList());

        // max # of buckets
        if (newBuckets.size() > nBuckets) {
            newBuckets.subList(nBuckets, newBuckets.size()).clear();
        }
        result.setBuckets(newBuckets);

        // more documents not represented in these facet
        result.setMore(moreDocs || newBuckets.size() < originalNumberOfBuckets);
    }

    /**
     * lower level access to get records from Elastic.
     *
     * @param ogcApiQuery query from user
     * @return elastic search response to the query
     * @throws Exception issue with elastic query
     */
    protected SearchResponse<IndexRecord> getRecordsFromElastic(OgcApiQuery ogcApiQuery, OgcApiRecordsFacetsDto facets)
            throws Exception {
        var source = catalogApi.getSource(ogcApiQuery.getCollectionId());
        String collectionFilter = catalogApi.retrieveCollectionFilter(source);

        SearchResponse<IndexRecord> searchResponse = simpleElastic.search(
                s -> {
                    recordsEsQueryBuilder.buildSearchRequest(s, ogcApiQuery, Set.of("*"));
                    Query query = null;
                    try {
                        query = recordsEsQueryBuilder.buildUnderlyingQuery(ogcApiQuery, collectionFilter);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    query = elasticWithUserPermissions.createPermissionQuery(query);
                    s.query(query);
                    s.index(simpleElastic.getRecordIndexName());
                    s.aggregations(recordsFacetsBuilder.getAggregations(facets));
                    return s;
                },
                IndexRecord.class);

        return searchResponse;
    }
}
