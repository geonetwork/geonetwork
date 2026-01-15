/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.ogcapi;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import org.geonetwork.index.client.ElasticWithUserPermissions;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.controllerexceptions.NotFoundException;
import org.geonetwork.ogcapi.records.generated.model.*;
import org.geonetwork.ogcapi.service.dataaccess.CatalogApi;
import org.geonetwork.ogcapi.service.dataaccess.SimpleElastic;
import org.geonetwork.ogcapi.service.facets.FacetsResponseInjector;
import org.geonetwork.ogcapi.service.facets.RecordsFacetsElasticQueryBuilder;
import org.geonetwork.ogcapi.service.indexConvert.OgcApiGeoJsonConverter;
import org.geonetwork.ogcapi.service.links.FormatterApiRecordLinkAttacher;
import org.geonetwork.ogcapi.service.links.ItemPageLinks;
import org.geonetwork.ogcapi.service.links.ItemsPageLinks;
import org.geonetwork.ogcapi.service.querybuilder.OgcApiQuery;
import org.geonetwork.ogcapi.service.search.RecordsEsQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

/** High level implementation for the ogcapi-records "Items" endpoints. */
@Component
public class OgcApiItemsApi {

    @Autowired
    FacetsResponseInjector facetsInjector;

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
    RecordsFacetsElasticQueryBuilder recordsFacetsBuilder;

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
    public OgcApiRecordsGetRecords200ResponseDto getRecords(OgcApiQuery ogcApiQuery) throws Exception {
        var records = getRecordsFromElastic(ogcApiQuery);
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

        facetsInjector.injectFacets(response, records);

        return response;
    }

    /**
     * lower level access to get records from Elastic.
     *
     * @param ogcApiQuery query from user
     * @return elastic search response to the query
     * @throws Exception issue with elastic query
     */
    protected SearchResponse<IndexRecord> getRecordsFromElastic(OgcApiQuery ogcApiQuery) throws Exception {
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
                    s.aggregations(recordsFacetsBuilder.createElasticAggregationsFromFacetsDefinition());
                    return s;
                },
                IndexRecord.class);

        return searchResponse;
    }
}
