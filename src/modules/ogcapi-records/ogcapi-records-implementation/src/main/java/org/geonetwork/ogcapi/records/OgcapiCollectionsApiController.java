/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.records;

import lombok.SneakyThrows;
import org.geonetwork.ogcapi.records.generated.CollectionsApi;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.service.ogcapi.OgcApiCollectionsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

@RestController
@RequestMapping("/ogcapi-records")
public class OgcapiCollectionsApiController implements CollectionsApi {

    private final NativeWebRequest request;
    private final OgcApiCollectionsApi collectionsApi;

    @Autowired
    public OgcapiCollectionsApiController(NativeWebRequest request, OgcApiCollectionsApi collectionsApi) {
        this.request = request;
        this.collectionsApi = collectionsApi;
    }

    @SneakyThrows
    public ResponseEntity<OgcApiRecordsCatalogDto> describeCollection(String catalogId) {
        var result = collectionsApi.describeCollection(catalogId);
        return new ResponseEntity<OgcApiRecordsCatalogDto>(result, HttpStatusCode.valueOf(200));
    }
}
