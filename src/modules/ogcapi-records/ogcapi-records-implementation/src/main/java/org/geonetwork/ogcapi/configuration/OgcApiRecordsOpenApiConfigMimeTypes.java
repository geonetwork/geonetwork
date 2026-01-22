/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.application.ctrlreturntypes.MimeAndProfilesForResponseType;
import org.geonetwork.application.formatters.MessageWriterUtil;
import org.geonetwork.application.profile.ProfileDefaultsConfiguration;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsSingleRecordResponse;
import org.springframework.context.annotation.Configuration;

/**
 * Sets up OGCAPI-Records mime type responses based on the `FormatterApi`.
 *
 * <p>Endpoints: <br>
 * + /ogcapi-records/collections/{catalogId}/items/{recordId} <br>
 * + /ogcapi-records/collections/{catalogId}/items
 *
 * <p>NOTE: this is mostly hard-coded because there's only 2 endpoints with custom formatters. However, in the future,
 * we could use the @GN5ControllerResponseFormatterType for a more generic solution.
 */
@Slf4j
@Configuration
public class OgcApiRecordsOpenApiConfigMimeTypes implements org.springdoc.core.customizers.OpenApiCustomizer {

    static final String SINGLE_RECORD_ENDPOINT = "/ogcapi-records/collections/{catalogId}/items/{recordId}";
    static final String MULTI_RECORD_ENDPOINT = "/ogcapi-records/collections/{catalogId}/items";

    MessageWriterUtil messageWriterUtil;
    ProfileDefaultsConfiguration profileDefaultsConfiguration;
    MimeAndProfilesForResponseType mimeAndProfilesForResponseType;

    public OgcApiRecordsOpenApiConfigMimeTypes(
            MessageWriterUtil messageWriterUtil,
            ProfileDefaultsConfiguration profileDefaultsConfiguration,
            MimeAndProfilesForResponseType mimeAndProfilesForResponseType) {
        this.messageWriterUtil = messageWriterUtil;
        this.profileDefaultsConfiguration = profileDefaultsConfiguration;
        this.mimeAndProfilesForResponseType = mimeAndProfilesForResponseType;
    }

    /**
     * Looks for message writers that are from the `FormatterApi`: <br>
     * + `IControllerResultFormatter` <br>
     * + for type `OgcApiRecordsSingleRecordResponse`
     *
     * <p>Also sets up `x-profiles` in response to list available profiles, as well as `x-default-profile`.
     *
     * @param openApi from spring
     */
    public void customizeSingleRecord(OpenAPI openApi) throws Exception {

        var content = openApi.getPaths()
                .get(SINGLE_RECORD_ENDPOINT)
                .getGet()
                .getResponses()
                .get("200")
                .getContent();

        var writers = mimeAndProfilesForResponseType.getResponseTypeInfos(OgcApiRecordsSingleRecordResponse.class);

        addToContent(writers, content, OgcApiRecordsSingleRecordResponse.class);
    }

    @SuppressWarnings("unchecked")
    private void addToContent(
            List<MimeAndProfilesForResponseType.ResponseTypeInfo> writers, Content content, Class<?> clazz) {
        // foreach of our IControllerResultFormatter<OgcApiRecordsSingleRecordResponse>
        // we create the `content` if it doesn't exist.
        // we also add the profiles (from the writer) to the output
        // we also add the default profile (from the user configuration)
        for (var writer : writers) {
            var existingContent = content.get(writer.getMimeType().toString());
            if (existingContent == null) {
                content.put(writer.getMimeType().toString(), new MediaType());
                existingContent = content.get(writer.getMimeType().toString());
            }
            var extensions = existingContent.getExtensions();
            if (extensions == null) {
                existingContent.setExtensions(new HashMap<>());
                extensions = existingContent.getExtensions();
            }
            if (!extensions.containsKey("x-format-providers")) {
                extensions.put("x-format-providers", new ArrayList<String>());
            }

            var formatProviders = (List<String>) extensions.get("x-format-providers");
            if (writer.getFormatProviders() != null) {
                formatProviders.addAll(writer.getFormatProviders());
            }

            var profiles = writer.getProfiles();
            if (profiles != null && !profiles.isEmpty()) {

                var xProfiles = (List<String>) extensions.get("x-profiles");
                if (xProfiles == null) {
                    xProfiles = new ArrayList<String>();
                    extensions.put("x-profiles", xProfiles);
                }
                if (profiles != null) {
                    xProfiles.addAll(profiles);
                }
                extensions.put(
                        "x-profile-default",
                        profileDefaultsConfiguration.getDefaultProfile(
                                writer.getMimeType().toString(), clazz));
            }
        }
    }

    public void customizeMultiRecord(OpenAPI openApi) throws Exception {
        var content = openApi.getPaths()
                .get(MULTI_RECORD_ENDPOINT)
                .getGet()
                .getResponses()
                .get("200")
                .getContent();

        List<MimeAndProfilesForResponseType.ResponseTypeInfo> providers =
                mimeAndProfilesForResponseType.getResponseTypeInfos(OgcApiRecordsMultiRecordResponse.class);

        addToContent(providers, content, OgcApiRecordsSingleRecordResponse.class);
    }

    @Override
    public void customise(OpenAPI openApi) {
        log.info("OgcApiRecordsOpenApiConfigMimeTypes: start customizer");

        try {
            customizeSingleRecord(openApi);
            customizeMultiRecord(openApi);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
