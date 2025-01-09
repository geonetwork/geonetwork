/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.ogcapi;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Set;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.controllerexceptions.NotFoundException;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.geonetwork.ogcapi.service.dataaccess.CatalogApi;
import org.geonetwork.ogcapi.service.dataaccess.SimpleElastic;
import org.geonetwork.ogcapi.service.indexConvert.OgcApiGeoJsonConverter;
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
    CatalogApi catalogApi;

    @Autowired
    RecordsEsQueryBuilder recordsEsQueryBuilder;

    @Autowired
    private SimpleElastic simpleElastic;

    @Autowired
    private OgcApiGeoJsonConverter geoJsonConverter;

    @Autowired
    private ItemsPageLinks itemsPageLinks;

    @Autowired
    private ItemPageLinks itemPageLinks;

    @Autowired
    private NativeWebRequest nativeWebRequest;

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
        var records = getRecords2(ogcApiQuery);
        var totalNumHits = records.hits().total().value();
        var indexRecords = records.hits().hits().stream().map(x -> x.source()).toList();
        var features = indexRecords.stream()
                .map(x -> geoJsonConverter.convert(x, null))
                .toList();
        var response = new OgcApiRecordsGetRecords200ResponseDto();
        response.setFeatures(features);
        response.numberMatched((int) totalNumHits);
        response.numberReturned(features.size());
        response.setTimeStamp(OffsetDateTime.now(ZoneId.of("UTC")));
        itemsPageLinks.addLinks(nativeWebRequest, ogcApiQuery.getCollectionId(), response, ogcApiQuery);
        return response;
    }

    /**
     * lower level access to get records from Elastic.
     *
     * @param ogcApiQuery query from user
     * @return elastic search response to the query
     * @throws Exception issue with elastic query
     */
    public SearchResponse<IndexRecord> getRecords2(OgcApiQuery ogcApiQuery) throws Exception {
        var source = catalogApi.getSource(ogcApiQuery.getCollectionId());
        String collectionFilter = catalogApi.retrieveCollectionFilter(source);

        SearchResponse<IndexRecord> searchResponse = simpleElastic.search(
                s -> {
                    recordsEsQueryBuilder.buildQuery(s, ogcApiQuery, collectionFilter, Set.of("*"));
                    s.index(simpleElastic.getRecordIndexName());
                    return s;
                },
                IndexRecord.class);

        return searchResponse;
    }
}
