/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.geonetwork.domain.Metadata;
import org.geonetwork.metadata.IMetadataAccessManager;
import org.geonetwork.metadata.IMetadataManager;
import org.geonetwork.schemas.SchemaManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/** High level api for The FormatterApi. */
@Component
@RequiredArgsConstructor
public class FormatterApi {

    private final IMetadataManager metadataManager;
    private final IMetadataAccessManager metadataAccessManager;
    private final FormatterFactory formatterFactory;
    private final SchemaManager schemaManager;

    public Map<String, FormatterInfo> getAllFormatters() throws Exception {
        var result = new HashMap<String, FormatterInfo>();
        var schemaNames = schemaManager.getSchemas();

        schemaNames.forEach(schemaName -> {
          List<org.geonetwork.schemas.model.schemaident.Formatter> schemaInfo = getAvailableFormattersForSchema(schemaName);
          schemaInfo.forEach(formatter -> {
            var formatterName = formatter.getName();
            var mimeType = formatter.getContentType();
            if (!result.containsKey(formatterName)) {
              var formatterInfo = new FormatterInfo();
              formatterInfo.setMimeType(mimeType);
              result.put(formatterName, formatterInfo);
            }
            var finfo = result.get(formatterName);
            if (!finfo.getMimeType().equals(mimeType)) {
              throw new RuntimeException("inconsistent formatter mime type - " + formatterName + ", mime="
                + finfo.getMimeType() + ", other mime=" + mimeType);
            }
            finfo.getSchemas().add(schemaName);
          });
        });

        return result;
    }

    public List<org.geonetwork.schemas.model.schemaident.Formatter> getRecordFormattersForMetadata(String metadataUuid) throws Exception {
        return getRecordFormattersForMetadata(metadataUuid, true);
    }

    public List<org.geonetwork.schemas.model.schemaident.Formatter> getAvailableFormattersForSchema(String schemaId) {
        return formatterFactory.getAvailableFormattersForSchema(schemaId);
    }

    public List<org.geonetwork.schemas.model.schemaident.Formatter> getRecordFormattersForMetadata(String metadataUuid, boolean approved) throws Exception {
        Metadata metadata = metadataManager.findMetadataByUuid(metadataUuid, approved);

        if (!metadataAccessManager.canView(metadata.getId())) {
            throw new AccessDeniedException("User is not permitted to access this resource");
        }

        return formatterFactory.getAvailableFormatters(metadata);
    }

    public void getRecordFormattedBy(
            String metadataUuid, final String formatterId, boolean approved, OutputStream outputStream)
            throws Exception {

        Metadata metadata = metadataManager.findMetadataByUuid(metadataUuid, approved);

        if (!metadataAccessManager.canView(metadata.getId())) {
            throw new AccessDeniedException("User is not permitted to access this resource");
        }

        Optional<org.geonetwork.schemas.model.schemaident.Formatter> formatterOptional = formatterFactory.getAvailableFormatters(metadata).stream().filter(formatter -> formatter.getProfile().equals(formatterId)).findFirst();

        if (!formatterOptional.isPresent()) {
            throw new FormatterException(String.format(
                    "Formatter not found. Hint: Available formatters for %s standard are: %s",
                    metadata.getSchemaid(), formatterFactory.getAvailableFormatters(metadata)));
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

        Formatter formatter = formatterFactory.getFormatter(metadata, formatterId);
        formatter.format(metadata, formatterId, outputStream);
    }
}
