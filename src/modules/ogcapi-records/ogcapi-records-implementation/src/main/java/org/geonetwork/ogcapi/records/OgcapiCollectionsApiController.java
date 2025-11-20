/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.records;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;
import org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfileBuilder;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiCollectionResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsCollectionsResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsSingleRecordResponse;
import org.geonetwork.ogcapi.records.generated.CollectionsApi;
import org.geonetwork.ogcapi.records.generated.model.*;
import org.geonetwork.ogcapi.service.dataaccess.SimpleElastic;
import org.geonetwork.ogcapi.service.facets.FacetsJsonService;
import org.geonetwork.ogcapi.service.facets.FacetsResponseInjector;
import org.geonetwork.ogcapi.service.ogcapi.OgcApiItemsApi;
import org.geonetwork.ogcapi.service.queryables.QueryablesService;
import org.geonetwork.ogcapi.service.querybuilder.QueryBuilder;
import org.geonetwork.ogcapi.service.sortables.SortablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * implements the /collections and /collections/{collectionID} endpoints implements the
 * /collections/{collectionID}/items and /collections/{collectionID}/items/{itemid} endpoints
 */
@RestController
@RequestMapping("${geonetwork.openapi-records.links.base-path:/ogcapi-records}")
public class OgcapiCollectionsApiController implements CollectionsApi {

    private final NativeWebRequest request;
    private final OgcApiItemsApi itemsApi;
    private final QueryablesService queryablesService;
    private final QueryBuilder queryBuilder;
    private final FacetsJsonService facetsService;
    private final SortablesService sortablesService;

    private final SimpleElastic simpleElastic;

    private final FacetsResponseInjector facetsInjector;

    private final RequestMediaTypeAndProfileBuilder requestMediaTypeAndProfileBuilder;

    @Autowired
    public OgcapiCollectionsApiController(
            NativeWebRequest request,
            OgcApiItemsApi itemsApi,
            QueryablesService queryablesService,
            QueryBuilder queryBuilder,
            FacetsJsonService facetsService,
            SortablesService sortablesService,
            SimpleElastic simpleElastic,
            FacetsResponseInjector facetsInjector,
            RequestMediaTypeAndProfileBuilder requestMediaTypeAndProfileBuilder) {
        this.request = request;
        this.itemsApi = itemsApi;
        this.queryablesService = queryablesService;
        this.queryBuilder = queryBuilder;
        this.facetsService = facetsService;
        this.sortablesService = sortablesService;
        this.simpleElastic = simpleElastic;
        this.facetsInjector = facetsInjector;
        this.requestMediaTypeAndProfileBuilder = requestMediaTypeAndProfileBuilder;
    }

    @Override
    @SneakyThrows
    public ResponseEntity<?> describeCollection(String catalogId) {
        //        var result = collectionsApi.describeCollection(catalogId);
        var requestInfo = requestMediaTypeAndProfileBuilder.build(request, OgcApiCollectionResponse.class);
        var result = new OgcApiCollectionResponse(catalogId, requestInfo);
        return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
    }

    @Override
    @SneakyThrows
    @RequestMapping(
            method = RequestMethod.GET,
            value = CollectionsApi.PATH_GET_COLLECTIONS,
            produces = {"application/json", "text/html", "*/*"})
    public ResponseEntity<?> getCollections() {
        var requestInfo = requestMediaTypeAndProfileBuilder.build(request, OgcApiRecordsCollectionsResponse.class);
        var result = new OgcApiRecordsCollectionsResponse(requestInfo);
        return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
    }

    @Override
    @SneakyThrows
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/collections/{catalogId}/items/{recordId}",
            produces = {"application/geo+json", "text/html", "application/json", "*/*"})
    public ResponseEntity<?> getRecord(String catalogId, String recordId, List<String> profile) {

        var indexRecord = simpleElastic.getOne(recordId);
        var requestInfo = requestMediaTypeAndProfileBuilder.build(request, OgcApiRecordsSingleRecordResponse.class);

        var response = new OgcApiRecordsSingleRecordResponse(catalogId, recordId, indexRecord, requestInfo);
        return new ResponseEntity<OgcApiRecordsSingleRecordResponse>(response, HttpStatusCode.valueOf(200));
    }

    @Override
    @SneakyThrows
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/collections/{catalogId}/items",
            produces = {"application/geo+json", "text/html", "application/json", "*/*"})
    public ResponseEntity<?> getRecords(
            String catalogId,
            List<BigDecimal> bbox,
            String datetime,
            Integer limit,
            Integer offset,
            List<String> q,
            List<String> type,
            List<String> externalIds,
            List<String> ids,
            List<String> sortby,
            String filter,
            String filterLang,
            String filterCrs,
            List<String> profile) {
        var query = queryBuilder.buildFromRequest(
                catalogId,
                bbox,
                datetime,
                limit,
                offset,
                type,
                q,
                ids,
                externalIds,
                sortby,
                filter,
                filterLang,
                filterCrs,
                request.getParameterMap());

        var requestInfo = requestMediaTypeAndProfileBuilder.build(request, OgcApiRecordsMultiRecordResponse.class);

        var records = itemsApi.getRecordsFromElastic(query);

        var facetInfo = facetsInjector.getFacets(records);
        var totalNumHits = records.hits().total().value();
        var indexRecords = records.hits().hits().stream().map(x -> x.source()).toList();

        var result = new OgcApiRecordsMultiRecordResponse();
        result.setFacetInfo(facetInfo);
        result.setCatalogId(catalogId);
        result.setTotalHits(totalNumHits);
        result.setRecordsCount(indexRecords.size());
        result.setUserQuery(query);
        result.setRequestMediaTypeAndProfile(requestInfo);

        var items = indexRecords.stream()
                .map(ir -> new OgcApiRecordsSingleRecordResponse(catalogId, ir.getUuid(), ir, requestInfo))
                .toList();
        result.setRecords(items);

        return new ResponseEntity<OgcApiRecordsMultiRecordResponse>(result, HttpStatusCode.valueOf(200));
    }

    @Override
    public ResponseEntity<OgcApiRecordsJsonSchemaDto> getQueryables(String catalogId) {
        var result = queryablesService.buildQueryables(catalogId);
        return new ResponseEntity<OgcApiRecordsJsonSchemaDto>(result, HttpStatusCode.valueOf(200));
    }

    @Override
    public ResponseEntity<OgcApiRecordsFacetsDto> getFacets(
            @Parameter(
                            name = "catalogId",
                            description = "local identifier of a catalog",
                            required = true,
                            in = ParameterIn.PATH)
                    @PathVariable("catalogId")
                    String catalogId) {
        var result = facetsService.buildFacets(catalogId);
        return new ResponseEntity<OgcApiRecordsFacetsDto>(result, HttpStatusCode.valueOf(200));
    }

    @Override
    public ResponseEntity<OgcApiRecordsJsonSchemaDto> getSortables(String catalogId) {
        var result = sortablesService.buildSortables(catalogId);
        return new ResponseEntity<OgcApiRecordsJsonSchemaDto>(result, HttpStatusCode.valueOf(200));
    }
}
