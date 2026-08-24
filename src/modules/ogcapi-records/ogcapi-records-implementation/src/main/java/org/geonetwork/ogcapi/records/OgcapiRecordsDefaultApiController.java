/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.records;

import java.util.Optional;
import lombok.SneakyThrows;
import org.geonetwork.ogcapi.records.generated.DefaultApi;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLandingPageDto;
import org.geonetwork.ogcapi.service.ogcapi.OgcApiCollectionsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

/** implements the landing page (/) endpoint */
@RestController
@RequestMapping("${geonetwork.openapi-records.links.base-path:/ogcapi-records}")
public class OgcapiRecordsDefaultApiController implements DefaultApi {

    private final NativeWebRequest request;
    private final OgcApiCollectionsApi collectionsApi;

    @Autowired
    public OgcapiRecordsDefaultApiController(NativeWebRequest request, OgcApiCollectionsApi collectionsApi) {
        this.request = request;
        this.collectionsApi = collectionsApi;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    /**
     * This added as a "nice" for users. OpenAPI `path` must start with a /. That means the landing page will be at
     * `http::localhost:7979/openapi-records/` Going to `http::localhost:7979/openapi-records` (no slash at end) will be
     * a 404.
     *
     * @return normal landing page
     */
    @RequestMapping(method = RequestMethod.GET, value = "")
    @SneakyThrows
    public ResponseEntity<OgcApiRecordsLandingPageDto> getLandingPageNoSlash() {
        return getLandingPage();
    }

    @Override
    @SneakyThrows
    public ResponseEntity<OgcApiRecordsLandingPageDto> getLandingPage() {
        var result = collectionsApi.getLandingPage(getRequest().get());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
