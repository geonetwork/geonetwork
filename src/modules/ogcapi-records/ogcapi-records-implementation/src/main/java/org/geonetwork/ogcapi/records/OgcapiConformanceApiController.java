/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.records;

import lombok.SneakyThrows;
import org.geonetwork.ogcapi.records.generated.ConformanceApi;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsConfClassesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

/** implements the /conformance endpoint. See ConformanceApi */
@RestController
@RequestMapping("${geonetwork.openapi-records.links.base-path:/ogcapi-records}")
public class OgcapiConformanceApiController implements ConformanceApi {

    @Autowired
    public OgcapiConformanceApiController(NativeWebRequest request) {}

    @Override
    @SneakyThrows
    public ResponseEntity<OgcApiRecordsConfClassesDto> getConformanceDeclaration() {
        var result = new OgcApiRecordsConfClassesDto();

        result.addConformsToItem("http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/core");
        result.addConformsToItem("http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/searchable-catalogue");
        result.addConformsToItem("http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/record-collection");
        result.addConformsToItem("http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/records-api");
        result.addConformsToItem("http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/html");
        result.addConformsToItem("http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/json");
        result.addConformsToItem("http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/sorting");
        result.addConformsToItem("http://www.opengis.net/spec/ogcapi-features-3/1.0/conf/queryables");
        result.addConformsToItem("http://www.opengis.net/spec/ogcapi-features-3/1.0/conf/queryables-query-parameters");
        result.addConformsToItem("http://www.opengis.net/spec/ogcapi-records-2/1.0/conf/simple");

        return new ResponseEntity<OgcApiRecordsConfClassesDto>(result, HttpStatusCode.valueOf(200));
    }
}
