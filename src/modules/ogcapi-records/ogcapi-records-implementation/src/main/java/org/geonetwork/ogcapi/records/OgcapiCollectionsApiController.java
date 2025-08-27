/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.records;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;
import org.geonetwork.ogcapi.records.generated.CollectionsApi;
import org.geonetwork.ogcapi.records.generated.model.*;
import org.geonetwork.ogcapi.service.facets.FacetsJsonService;
import org.geonetwork.ogcapi.service.ogcapi.OgcApiCollectionsApi;
import org.geonetwork.ogcapi.service.ogcapi.OgcApiItemsApi;
import org.geonetwork.ogcapi.service.queryables.QueryablesService;
import org.geonetwork.ogcapi.service.querybuilder.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    private final OgcApiCollectionsApi collectionsApi;
    private final OgcApiItemsApi itemsApi;
    private final QueryablesService queryablesService;
    private final QueryBuilder queryBuilder;
    private final FacetsJsonService facetsService;

    @Autowired
    public OgcapiCollectionsApiController(
            NativeWebRequest request,
            OgcApiCollectionsApi collectionsApi,
            OgcApiItemsApi itemsApi,
            QueryablesService queryablesService,
            QueryBuilder queryBuilder,
            FacetsJsonService facetsService) {
        this.request = request;
        this.collectionsApi = collectionsApi;
        this.itemsApi = itemsApi;
        this.queryablesService = queryablesService;
        this.queryBuilder = queryBuilder;
        this.facetsService = facetsService;
    }

    @Override
    @SneakyThrows
    public ResponseEntity<OgcApiRecordsCatalogDto> describeCollection(String catalogId) {
        var result = collectionsApi.describeCollection(catalogId);
        return new ResponseEntity<OgcApiRecordsCatalogDto>(result, HttpStatusCode.valueOf(200));
    }

    @Override
    @SneakyThrows
    public ResponseEntity<OgcApiRecordsGetCollections200ResponseDto> getCollections() {
        var result = collectionsApi.getCollections();
        return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
    }

    @Override
    @SneakyThrows
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/collections/{catalogId}/items/{recordId}",
            produces = {"application/geo+json", "text/html", "application/json", "*/*"})
    public ResponseEntity<OgcApiRecordsRecordGeoJSONDto> getRecord(
            String catalogId, String recordId, List<String> profile) {
        var result = itemsApi.getRecord(catalogId, recordId);

        HttpHeaders responseHeaders = new HttpHeaders();
        if (profile != null && !profile.isEmpty()) {
            responseHeaders.addAll("GN5.OGCAPI-RECORDS.REQUEST-PROFILES", profile);
        }
        responseHeaders.addAll("GN5.OGCAPI-RECORDS.REQUEST-COLLECTIONID", Collections.singletonList(catalogId));
        responseHeaders.addAll("GN5.OGCAPI-RECORDS.REQUEST-RECORDID", Collections.singletonList(recordId));

        return new ResponseEntity<OgcApiRecordsRecordGeoJSONDto>(result, responseHeaders, HttpStatusCode.valueOf(200));
    }

    @Override
    @SneakyThrows
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/collections/{catalogId}/items",
            produces = {"application/geo+json", "text/html", "application/json", "*/*"})
    public ResponseEntity<OgcApiRecordsGetRecords200ResponseDto> getRecords(
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
            String filterCrs) {
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
        var result = itemsApi.getRecords(query);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("GN5.OGCAPI-RECORDS.REQUEST-OFFSET", offset.toString());

        return new ResponseEntity<OgcApiRecordsGetRecords200ResponseDto>(
                result, responseHeaders, HttpStatusCode.valueOf(200));
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
}
