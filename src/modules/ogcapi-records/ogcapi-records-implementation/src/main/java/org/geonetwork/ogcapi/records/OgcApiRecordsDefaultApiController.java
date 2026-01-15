/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.records;

import java.util.Map;
import java.util.Optional;
import lombok.SneakyThrows;
import org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfileBuilder;
import org.geonetwork.application.ctrlreturntypes.UriHelper;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiLandingPageResponse;
import org.geonetwork.ogcapi.records.generated.DefaultApi;
import org.geonetwork.ogcapi.service.configuration.OgcApiLinkConfiguration;
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
public class OgcApiRecordsDefaultApiController implements DefaultApi {

    private final NativeWebRequest request;
    private final RequestMediaTypeAndProfileBuilder requestMediaTypeAndProfileBuilder;
    private final OgcApiLinkConfiguration linkConfiguration;

    @Autowired
    public OgcApiRecordsDefaultApiController(
            NativeWebRequest request,
            RequestMediaTypeAndProfileBuilder requestMediaTypeAndProfileBuilder,
            OgcApiLinkConfiguration linkConfiguration) {
        this.request = request;
        this.requestMediaTypeAndProfileBuilder = requestMediaTypeAndProfileBuilder;
        this.linkConfiguration = linkConfiguration;
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
    public ResponseEntity<?> getLandingPageNoSlash() {
        return getLandingPage();
    }

    @Override
    @SneakyThrows
    @RequestMapping(
            method = RequestMethod.GET,
            value = DefaultApi.PATH_GET_LANDING_PAGE,
            produces = {"text/html", "application/json", "*/*"})
    public ResponseEntity<?> getLandingPage() {
        var requestInfo = requestMediaTypeAndProfileBuilder.build(request, OgcApiLandingPageResponse.class);
        var jsonUri = UriHelper.createUri(linkConfiguration.getOgcApiRecordsBaseUrl(), "", Map.of("f", "json"));

        var result = new OgcApiLandingPageResponse(requestInfo, jsonUri);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
