/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.records;

import lombok.SneakyThrows;
import org.geonetwork.ogcapi.records.generated.CollectionsApi;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetCollections200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonSchemaDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.geonetwork.ogcapi.service.ogcapi.OgcApiCollectionsApi;
import org.geonetwork.ogcapi.service.ogcapi.OgcApiItemsApi;
import org.geonetwork.ogcapi.service.queryables.QueryablesService;
import org.geonetwork.ogcapi.service.querybuilder.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * implements the /collections and /collections/{collectionID} endpoints implements the
 * /collections/{collectionID}/items and /collections/{collectionID}/items/{itemid} endpoints
 */
@RestController
@RequestMapping("/ogcapi-records")
public class OgcapiCollectionsApiController implements CollectionsApi {

  private final NativeWebRequest request;
  private final OgcApiCollectionsApi collectionsApi;
  private final OgcApiItemsApi itemsApi;
  private final QueryablesService queryablesService;
  private final QueryBuilder queryBuilder;

  @Autowired
  public OgcapiCollectionsApiController(
    NativeWebRequest request,
    OgcApiCollectionsApi collectionsApi,
    OgcApiItemsApi itemsApi,
    QueryablesService queryablesService,
    QueryBuilder queryBuilder) {
    this.request = request;
    this.collectionsApi = collectionsApi;
    this.itemsApi = itemsApi;
    this.queryablesService = queryablesService;
    this.queryBuilder = queryBuilder;
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
    return new ResponseEntity<OgcApiRecordsGetCollections200ResponseDto>(result, HttpStatusCode.valueOf(200));
  }

  @Override
  @SneakyThrows
  public ResponseEntity<OgcApiRecordsRecordGeoJSONDto> getRecord(String catalogId, String recordId) {
    var result = itemsApi.getRecord(catalogId, recordId);

    return new ResponseEntity<OgcApiRecordsRecordGeoJSONDto>(result, HttpStatusCode.valueOf(200));
  }

  @Override
  @SneakyThrows
  public ResponseEntity<OgcApiRecordsGetRecords200ResponseDto> getRecords(
    String catalogId,
    List<BigDecimal> bbox,
    String datetime,
    Integer limit,
    Integer offset,
    List<String> q,
    List<String> type,
    List<String> externalId,
    List<String> ids,
    List<String> sortby) {
    var query = queryBuilder.buildFromRequest(
      catalogId, bbox, datetime, limit, offset, type, q, ids, externalId, sortby, request.getParameterMap());
    var result = itemsApi.getRecords(query);
    return new ResponseEntity<OgcApiRecordsGetRecords200ResponseDto>(result, HttpStatusCode.valueOf(200));
  }

  @Override
  public ResponseEntity<OgcApiRecordsJsonSchemaDto> getQueryables(String catalogId) {
    var result = queryablesService.buildQueryables(catalogId);
    return new ResponseEntity<OgcApiRecordsJsonSchemaDto>(result, HttpStatusCode.valueOf(200));
  }
}
