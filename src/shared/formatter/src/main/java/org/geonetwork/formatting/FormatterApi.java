/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

import java.io.OutputStream;
import java.util.*;
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

    /**
     * {mimetype -> { profileName -> FormatterInfo} }
     *
     * @return mimetype -> FormatterInfo
     * @throws Exception schema problem (might be inconsistent schema info)
     */
    public Map<String, Map<String, FormatterInfo>> getAllFormatters() throws Exception {
        var result = new HashMap<String, Map<String, FormatterInfo>>();
        var schemaNames = schemaManager.getSchemas();

        schemaNames.forEach(schemaName -> {
            var schemaInfo = getAvailableFormattersForSchema(schemaName);

            schemaInfo.forEach(formatter -> {
                var formatterId = formatter.getName();
                var mimeType = formatter.getContentType();
                var profileName = formatter.getProfile();
                var officialProfile = formatter.getOfficialProfileName();
                var title = formatter.getTitle();

                if (!result.containsKey(mimeType)) {
                    // no profiles yet
                    result.put(mimeType, new HashMap<>());
                }

                var finfo = result.get(mimeType).get(profileName);
                if (finfo == null) {
                    var profileInfo = new ProfileInfo(formatterId, profileName, officialProfile, title, mimeType);
                    finfo = new FormatterInfo(mimeType, profileInfo);
                    result.get(mimeType).put(profileName, finfo);
                }
                if (!finfo.getMimeType().equals(mimeType)
                        || !finfo.getProfile().getFormatterName().equals(formatterId)
                        ||
                        //                    !finfo.getProfile().getTitle().equals(title) ||
                        !finfo.getProfile().getFormatterProfileName().equals(profileName)
                        || !finfo.getProfile().getOfficialProfileName().equals(officialProfile)
                        || !finfo.getProfile().getMimeType().equals(mimeType)) {
                    throw new RuntimeException("inconsistent profile info!");
                }

                finfo.getSchemas().add(schemaName);
            });
        });

        return result;
    }

    public Map<String, List<ProfileInfo>> getRecordFormattersForMetadataByMediaType(String metadataUuid)
            throws Exception {
        var formatters = getRecordFormattersForMetadata(metadataUuid, true);
        var result = new HashMap<String, List<ProfileInfo>>();

        for (var formatter : formatters) {
            var formatterId = formatter.getName();
            var mimeType = formatter.getContentType();
            var profileName = formatter.getProfile();
            var officialProfile = formatter.getOfficialProfileName();
            var title = formatter.getTitle();

            if (!result.containsKey(mimeType)) {
                result.put(mimeType, new ArrayList<>());
            }
            var finfo = result.get(mimeType);

            var profileInfo = new ProfileInfo(formatterId, profileName, officialProfile, title, mimeType);
            finfo.add(profileInfo);
        }

        return result;
    }

    public List<org.geonetwork.schemas.model.schemaident.Formatter> getRecordFormattersForMetadata(String metadataUuid)
            throws Exception {
        return getRecordFormattersForMetadata(metadataUuid, true);
    }

    public List<org.geonetwork.schemas.model.schemaident.Formatter> getAvailableFormattersForSchema(String schemaId) {
        return formatterFactory.getAvailableFormattersForSchema(schemaId);
    }

    public List<org.geonetwork.schemas.model.schemaident.Formatter> getRecordFormattersForMetadata(
            String metadataUuid, boolean approved) throws Exception {
        Metadata metadata = metadataManager.findMetadataByUuid(metadataUuid, approved);

        if (!metadataAccessManager.canView(metadata.getId())) {
            throw new AccessDeniedException("User is not permitted to access this resource");
        }

        return formatterFactory.getAvailableFormatters(metadata);
    }

    public void getRecordFormattedBy(
            String metadataUuid,
            final String formatterId,
            final String profile,
            boolean approved,
            OutputStream outputStream,
            Map<String, Object> config)
            throws Exception {

        Metadata metadata = metadataManager.findMetadataByUuid(metadataUuid, approved);

        if (!metadataAccessManager.canView(metadata.getId())) {
            throw new AccessDeniedException("User is not permitted to access this resource");
        }

        var formatters = formatterFactory.getAvailableFormatters(metadata).stream()
                .filter(formatter -> formatter.getName().equals(formatterId));
        if (profile != null) {
            formatters = formatters.filter(formatter ->
                    profile.equals(formatter.getOfficialProfileName()) || profile.equals(formatter.getOfficialProfileName()));
        }
        var formatterOptional = formatters.findFirst();

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

        var formatter = formatterFactory.getFormatter(metadata, formatterId);
        formatter.format(metadata, formatterId, outputStream, config);
    }
}
