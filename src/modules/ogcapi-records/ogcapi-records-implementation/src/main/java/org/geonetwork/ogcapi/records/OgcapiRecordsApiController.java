/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.records;

import java.util.Optional;
import org.geonetwork.ogcapi.records.generated.DefaultApi;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLandingPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

@RestController
@RequestMapping("/ogcapi-records")
public class OgcapiRecordsApiController implements DefaultApi {

    private final NativeWebRequest request;

    @Autowired
    public OgcapiRecordsApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    public ResponseEntity<OgcApiRecordsLandingPageDto> getLandingPage() {

        var result = new OgcApiRecordsLandingPageDto();
        // result.title("my title");
        result.description("my description");

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
