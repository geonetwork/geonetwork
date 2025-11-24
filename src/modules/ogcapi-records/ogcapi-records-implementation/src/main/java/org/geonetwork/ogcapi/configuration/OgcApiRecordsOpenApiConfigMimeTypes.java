/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.application.ctrlreturntypes.IControllerResultFormatter;
import org.geonetwork.application.formatters.MessageWriterUtil;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsSingleRecordResponse;
import org.geonetwork.ogcapi.service.configuration.ItemPageLinksConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

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
    ItemPageLinksConfiguration itemPageLinksConfiguration;

    public OgcApiRecordsOpenApiConfigMimeTypes(
            MessageWriterUtil messageWriterUtil, ItemPageLinksConfiguration itemPageLinksConfiguration) {
        this.messageWriterUtil = messageWriterUtil;
        this.itemPageLinksConfiguration = itemPageLinksConfiguration;
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
    public void customizeSingleRecord(OpenAPI openApi) {
        var content = openApi.getPaths()
                .get(SINGLE_RECORD_ENDPOINT)
                .getGet()
                .getResponses()
                .get("200")
                .getContent();

        var writers = messageWriterUtil.getAllMessageConverters().stream()
                .filter(x -> x instanceof IControllerResultFormatter)
                .map(x -> ((IControllerResultFormatter) x))
                .filter(x -> x.getInputType().equals(OgcApiRecordsSingleRecordResponse.class))
                .toList();

        addToContent(writers, content);
    }

    @SuppressWarnings("unchecked")
    private void addToContent(List<IControllerResultFormatter> writers, Content content) {
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
            formatProviders.add(writer.getClass().getCanonicalName());

            var profiles = (List<String>) writer.getProvidedProfileNames();
            if (profiles != null && !profiles.isEmpty()) {

                var xProfiles = (List<String>) extensions.get("x-profiles");
                if (xProfiles == null) {
                    xProfiles = new ArrayList<String>();
                    extensions.put("x-profiles", xProfiles);
                }
                xProfiles.addAll(profiles);
                extensions.put("x-profile-default", itemPageLinksConfiguration.getDefaultProfile(writer.getMimeType()));
            }
        }
    }

    public void customizeMultiRecord(OpenAPI openApi) {
        var content = openApi.getPaths()
                .get(MULTI_RECORD_ENDPOINT)
                .getGet()
                .getResponses()
                .get("200")
                .getContent();

        var writers = messageWriterUtil.getAllMessageConverters().stream()
                .filter(x -> x instanceof IControllerResultFormatter)
                .map(x -> ((IControllerResultFormatter) x))
                .filter(x -> x.getInputType().equals(OgcApiRecordsMultiRecordResponse.class))
                .toList();

        addToContent(writers, content);

        var writersNormal = messageWriterUtil.getAllMessageConverters().stream()
                .filter(x -> getInputObject(x) == OgcApiRecordsMultiRecordResponse.class)
                .filter(x -> !(x instanceof IControllerResultFormatter))
                .toList();

        addToContentHttpMessageConverter(writersNormal, content);
    }

    @SuppressWarnings("unchecked")
    private void addToContentHttpMessageConverter(List<HttpMessageConverter<?>> writers, Content content) {
        // foreach of our IControllerResultFormatter<OgcApiRecordsSingleRecordResponse>
        // we create the `content` if it doesn't exist.
        // these type of writers don't have profiles
        for (var writer : writers) {
            var mimeType = writer.getSupportedMediaTypes().get(0);
            var existingContent = content.get(mimeType.toString());
            if (existingContent == null) {
                content.put(mimeType.toString(), new MediaType());
                existingContent = content.get(mimeType.toString());
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
            formatProviders.add(writer.getClass().getCanonicalName());
        }
    }

    private Class<?> getInputObject(HttpMessageConverter<?> writer) {
        var methods = Arrays.stream(writer.getClass().getMethods());
        var writeMethod = methods.filter(x -> x.getName().equals("write")
                        && x.getParameterTypes().length > 0
                        && !x.getParameterTypes()[0].equals(Object.class))
                .findFirst();

        return writeMethod.<Class>map(method -> method.getParameterTypes()[0]).orElse(null);
    }

    @Override
    public void customise(OpenAPI openApi) {
        log.info("OgcApiRecordsOpenApiConfigMimeTypes: start customizer");
        customizeSingleRecord(openApi);
        customizeMultiRecord(openApi);
    }
}
