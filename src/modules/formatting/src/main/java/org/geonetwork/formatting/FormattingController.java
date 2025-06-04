/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

import static org.geonetwork.constants.ApiParams.API_CLASS_RECORD_OPS;
import static org.geonetwork.constants.ApiParams.API_CLASS_RECORD_TAG;
import static org.geonetwork.constants.ApiParams.API_PARAM_RECORD_UUID;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

@Tag(name = API_CLASS_RECORD_TAG, description = API_CLASS_RECORD_OPS)
@RestController
@RequiredArgsConstructor
public class FormattingController {

    private final FormatterApi formatterApi;

    @GetMapping(value = "/api/records/{metadataUuid}/formatters", produces = {MediaType.APPLICATION_JSON_VALUE})
    @io.swagger.v3.oas.annotations.Operation(
            summary = "Get available formatter for the metadata record depending on the metadata schema.")
    @ResponseBody
    public Map<String, String> getRecordFormatterList(
            @Parameter(description = API_PARAM_RECORD_UUID, required = true) @PathVariable String metadataUuid,
            @RequestParam(required = false, defaultValue = "true") boolean approved)
            throws Exception {

        return formatterApi.getRecordFormattersForMetadata(metadataUuid, approved);
    }

    @GetMapping(value = "/api/records/formatters/{schemaId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @io.swagger.v3.oas.annotations.Operation(
      summary = "Get available formatter for the metadata record depending on the metadata schema.")
    @ResponseBody
    public Map<String, String> getRecordFormatterListForSchema(
            @Parameter(description = API_PARAM_RECORD_UUID, required = true) @PathVariable String schemaId) {

        return formatterApi.getAvailableFormattersForSchema(schemaId);
    }

    @GetMapping(
            value = {"/api/records/{metadataUuid}/formatters/{formatterId:.+}"},
            produces = {
                MediaType.TEXT_HTML_VALUE,
                MediaType.APPLICATION_XHTML_XML_VALUE,
                "application/pdf",
                MediaType.ALL_VALUE
            })
    @io.swagger.v3.oas.annotations.Operation(summary = "Get a metadata record in a specific format.")
    @ResponseBody
    public void getRecordFormattedBy(
            @Parameter(description = "Formatter type to use.") @PathVariable(value = "formatterId")
                    final String formatterId,
            @Parameter(description = API_PARAM_RECORD_UUID, required = true) @PathVariable String metadataUuid,
            //        @RequestParam(
            //            value = "width",
            //            defaultValue = "_100") final FormatterWidth width,
            @RequestParam(value = "mdpath", required = false) final String mdPath,
            @Parameter(
                            description =
                                    "Optional language ISO 3 letters code to override HTTP Accept-language header.",
                            required = false)
                    @RequestParam(value = "language", required = false)
                    final String iso3lang,
            @RequestParam(value = "output", required = false) FormatType formatType,
            @Parameter(description = "Download the approved version", required = false)
                    @RequestParam(required = false, defaultValue = "true")
                    boolean approved,
            @Parameter(hidden = true) final NativeWebRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse)
            throws Exception {

        String acceptHeader = StringUtils.isBlank(request.getHeader(HttpHeaders.ACCEPT))
                ? MediaType.TEXT_HTML_VALUE
                : request.getHeader(HttpHeaders.ACCEPT);
        if (MediaType.ALL_VALUE.equals(acceptHeader)) {
            acceptHeader = MediaType.TEXT_HTML_VALUE;
        }

        if (formatType == null) {
            formatType = FormatType.findByFormatterKey(formatterId);
        }
        if (formatType == null) {
            formatType = FormatType.find(acceptHeader);
        }
        if (formatType == null) {
            formatType = FormatType.xml;
        }

        servletResponse.setContentType(formatType.contentType);
        formatterApi.getRecordFormattedBy(metadataUuid, formatterId, approved, servletResponse.getOutputStream());
    }
}
