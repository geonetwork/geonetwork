/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.7.0).
 * https://openapi-generator.tech Do not edit the class manually.
 */
package org.geonetwork.search.ogcapi.records.generated;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Generated;
import java.util.Optional;
import org.geonetwork.search.ogcapi.records.generated.model.OgcApiRecordsConfClassesDto;
import org.geonetwork.search.ogcapi.records.generated.model.OgcApiRecordsExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
@Validated
@Tag(name = "OGC API Records", description = "")
public interface ConformanceApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /conformance : information about specifications that this API conforms to A list of all
     * conformance classes specified in a standard that the server conforms to.
     *
     * @return The URIs of all conformance classes supported by the server. (status code 200) or A
     *     server error occurred. (status code 500)
     */
    @Operation(
            operationId = "getConformanceDeclaration",
            summary = "information about specifications that this API conforms to",
            description = "A list of all conformance classes specified in a standard that the server conforms to.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "The URIs of all conformance classes supported by the server.",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OgcApiRecordsConfClassesDto.class)),
                            @Content(
                                    mediaType = "text/html",
                                    schema = @Schema(implementation = OgcApiRecordsConfClassesDto.class))
                        }),
                @ApiResponse(
                        responseCode = "500",
                        description = "A server error occurred.",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OgcApiRecordsExceptionDto.class)),
                            @Content(
                                    mediaType = "text/html",
                                    schema = @Schema(implementation = OgcApiRecordsExceptionDto.class))
                        })
            })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/conformance",
            produces = {"application/json", "text/html"})
    default ResponseEntity<OgcApiRecordsConfClassesDto> getConformanceDeclaration() {

        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"conformsTo\" : ["
                            + " \"http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/core\","
                            + " \"http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/core\" ] }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("text/html"))) {
                    String exampleString = "Custom MIME type example not yet supported: text/html";
                    ApiUtil.setExampleResponse(request, "text/html", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"code\" : \"code\", \"description\" : \"description\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("text/html"))) {
                    String exampleString = "Custom MIME type example not yet supported: text/html";
                    ApiUtil.setExampleResponse(request, "text/html", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
