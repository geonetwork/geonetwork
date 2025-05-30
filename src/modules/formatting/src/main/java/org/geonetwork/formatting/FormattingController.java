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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.domain.Metadata;
import org.geonetwork.metadata.IMetadataAccessManager;
import org.geonetwork.metadata.IMetadataManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

@RequestMapping(value = {"/api/records/{metadataUuid}/formatters"})
@Tag(name = API_CLASS_RECORD_TAG, description = API_CLASS_RECORD_OPS)
@RestController
@RequiredArgsConstructor
public class FormattingController {
    private final IMetadataManager metadataManager;
    private final IMetadataAccessManager metadataAccessManager;
    private final FormatterFactory formatterFactory;

    //    private static final String PARAM_LANGUAGE_ALL_VALUES = "all";

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @io.swagger.v3.oas.annotations.Operation(
            summary = "Get available formatter for the metadata record depending on the metadata schema.")
    @ResponseBody
    public List<String> getRecordFormatterList(
            @Parameter(description = API_PARAM_RECORD_UUID, required = true) @PathVariable String metadataUuid,
            @RequestParam(required = false, defaultValue = "true") boolean approved)
            throws Exception {
        Metadata metadata = metadataManager.findMetadataByUuid(metadataUuid, approved);

        if (!metadataAccessManager.canView(metadata.getId())) {
            throw new AccessDeniedException("User is not permitted to access this resource");
        }

        return formatterFactory.getAvailableFormatters(metadata);
    }

    @GetMapping(
            value = {"{formatterId:.+}"},
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

        Metadata metadata = metadataManager.findMetadataByUuid(metadataUuid, approved);

        if (!metadataAccessManager.canView(metadata.getId())) {
            throw new AccessDeniedException("User is not permitted to access this resource");
        }
        // TODO     if (approved) {
        //            metadata =
        // ApplicationContextHolder.get().getBean(MetadataRepository.class).findOneByUuid(metadataUuid);
        //        }

        if (!formatterFactory.getAvailableFormatters(metadata).contains(formatterId)) {
            throw new FormatterException(String.format(
              "Formatter not found. Hint: Available formatters for %s standard are: %s",
              metadata.getSchemaid(), formatterFactory.getAvailableFormatters(metadata)));
        }

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

        //        String language;
        //        if (StringUtils.isNotEmpty(iso3lang)) {
        //            if (PARAM_LANGUAGE_ALL_VALUES.equalsIgnoreCase(iso3lang)) {
        //                language = iso3lang;
        // TODO           } else if (languageUtils.getUiLanguages().contains(iso3lang)) {
        //                language = isoLanguagesMapper.iso639_2T_to_iso639_2B(iso3lang);
        //            } else {
        //                language = languageUtils.getDefaultUiLanguage();
        //            }
        //        } else {
        //                        language = isoLanguagesMapper.iso639_2T_to_iso639_2B(locale.getISO3Language());
        //        }

        // TODO      Boolean hideWithheld = !context.getBean(AccessManager.class).canEdit(context,
        // String.valueOf(metadata.getId()));

        //  TODO Cache?

        servletResponse.setContentType(formatType.contentType);

        Formatter formatter = formatterFactory.getFormatter(metadata, formatterId);
        formatter.format(metadata, formatterId, servletResponse.getOutputStream());
    }
}
