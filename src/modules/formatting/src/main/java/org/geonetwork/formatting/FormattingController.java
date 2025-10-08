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
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.geonetwork.schemas.model.schemaident.Formatter;
import org.geonetwork.utility.MediaTypeAndProfile;
import org.geonetwork.utility.MediaTypeAndProfileBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = API_CLASS_RECORD_TAG, description = API_CLASS_RECORD_OPS)
@RestController
@RequiredArgsConstructor
public class FormattingController {

    private final FormatterApi formatterApi;
    private final MediaTypeAndProfileBuilder mediaTypeAndProfileBuilder;

    @GetMapping(
            value = "/api/records/formatters",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @io.swagger.v3.oas.annotations.Operation(summary = "Get all available formatters and their mime types.")
    @ResponseBody
    public Map<String, Map<String, FormatterInfo>> getAllFormatters() throws Exception {
        return formatterApi.getAllFormatters();
    }

    @GetMapping(
            value = "/api/records/{metadataUuid}/formatters",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @io.swagger.v3.oas.annotations.Operation(
            summary = "Get available formatter for the metadata record depending on the metadata schema.")
    @ResponseBody
    public List<Formatter> getRecordFormatterList(
            @Parameter(description = API_PARAM_RECORD_UUID, required = true) @PathVariable String metadataUuid,
            @RequestParam(required = false, defaultValue = "true") boolean approved)
            throws Exception {

        return formatterApi.getRecordFormattersForMetadata(metadataUuid, approved);
    }

    @GetMapping(
            value = "/api/records/formatters/{schemaId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @io.swagger.v3.oas.annotations.Operation(
            summary = "Get available formatter for the metadata record depending on the metadata schema.")
    @ResponseBody
    public List<Formatter> getRecordFormatterListForSchema(
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
            @Parameter(description = "Optional language ISO 3 letters code to override HTTP Accept-language header.")
                    @RequestParam(value = "language", required = false)
                    final String iso3lang,
            @Parameter(description = "Download the approved version")
                    @RequestParam(required = false, defaultValue = "true")
                    boolean approved,
            HttpServletResponse servletResponse)
            throws Exception {

        List<Formatter> formatters = formatterApi.getRecordFormattersForMetadata(metadataUuid);

        var query = formatters.stream().filter(formatter -> formatter.getName().equals(formatterId));

        Optional<Formatter> formatterOptional = query.findFirst();

        if (formatterOptional.isEmpty()) {
            throw new FormatterException(
                    String.format("Formatter '%s' not found for metadata record '%s'.", formatterId, metadataUuid));
        }

        String contentType = formatterOptional.get().getContentType();
        MediaTypeAndProfile mediaTypeAndProfile =
                mediaTypeAndProfileBuilder.build(MediaType.valueOf(contentType), null);

        Map<String, Object> formatterConfig = new HashMap<>();
        formatterConfig.put("mediaTypeAndProfile", mediaTypeAndProfile);

        servletResponse.setContentType(contentType);
        formatterApi.getRecordFormattedBy(
                metadataUuid, formatterId, null, approved, servletResponse.getOutputStream(), formatterConfig);
    }
}
