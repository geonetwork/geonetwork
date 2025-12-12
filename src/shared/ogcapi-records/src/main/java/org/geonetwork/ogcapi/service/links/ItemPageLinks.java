/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.links;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.formatting.FormatterApi;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.geonetwork.ogcapi.service.configuration.CollectionPageLinksConfiguration;
import org.geonetwork.ogcapi.service.configuration.ItemPageLinksConfiguration;
import org.geonetwork.ogcapi.service.configuration.ItemsPageLinksConfiguration;
import org.geonetwork.ogcapi.service.configuration.OgcApiLinkConfiguration;
import org.geonetwork.utility.MediaTypeAndProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

/** Adds in links for the Item Page (single record). This includes the FormatterApi links. */
@Slf4j
@Component
public class ItemPageLinks extends BasicLinks {

    @Autowired
    OgcApiLinkConfiguration linkConfiguration;

    @Autowired
    ItemPageLinksConfiguration itemPageLinksConfiguration;

    @Autowired
    ItemsPageLinksConfiguration itemsPageLinksConfiguration;

    @Autowired
    CollectionPageLinksConfiguration collectionPageLinksConfiguration;

    @Autowired
    FormatterApi formatterApi;

    static List<MediaTypeAndProfile> formats = null;
    /**
     * Gets available record formatters.
     *
     * @return {mimetype -> { profileName -> FormatterInfo} }
     */
    public List<MediaTypeAndProfile> getFormats() {
        if (formats != null) {
            return formats;
        }
        var result = new ArrayList<MediaTypeAndProfile>();
        try {
            var formatters = formatterApi.getAllFormatters();
            for (var formatter : formatters.entrySet()) {
                var mediaType = MediaType.valueOf(formatter.getKey());
                var profiles = formatter.getValue().values().stream()
                        .map(x -> x.getProfile().getOfficialProfileName())
                        .toList();
                var item = new MediaTypeAndProfile(mediaType, profiles);
                result.add(item);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        formats = result;
        return formats;
    }

    public void addLinks(NativeWebRequest nativeWebRequest, String collectionId, OgcApiRecordsRecordGeoJSONDto page) {
        addLinks(mediaTypeAndProfileBuilder.build(nativeWebRequest), collectionId, page);
    }

    /**
     * add links to the OgcApiRecordsRecordGeoJSONDto page
     *
     * @param mediaTypeAndProfile from user
     * @param collectionId which collection (DB source)
     * @param page where to add the links
     */
    public void addLinks(
            MediaTypeAndProfile mediaTypeAndProfile, String collectionId, OgcApiRecordsRecordGeoJSONDto page) {
        if (mediaTypeAndProfile == null || collectionId == null) {
            return; // no context for links
        }

        addStandardLinks(
                mediaTypeAndProfile,
                linkConfiguration.getOgcApiRecordsBaseUrl(),
                "collections/" + URLEncoder.encode(collectionId, StandardCharsets.UTF_8) + "/items/"
                        + URLEncoder.encode(page.getId(), StandardCharsets.UTF_8),
                page,
                getFormats(),
                "self",
                "alternate");

        addCollectionsLinks(mediaTypeAndProfile, collectionId, page);
        if (page.getProperties().getThumbnails() != null
                && !page.getProperties().getThumbnails().isEmpty()) {
            for (var thumbnail : page.getProperties().getThumbnails()) {
                try {
                    page.addLinksItem(new OgcApiRecordsLinkDto()
                            .rel("preview")
                            .href(new URI(thumbnail.getUrl()))
                            .type(guessImageType(thumbnail.getUrl()))
                            .title(thumbnail.getName()));
                    page.addLinksItem(new OgcApiRecordsLinkDto()
                            .rel("icon")
                            .href(new URI(thumbnail.getUrl()))
                            .type(guessImageType(thumbnail.getUrl()))
                            .title(thumbnail.getName()));
                } catch (URISyntaxException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public static String guessImageType(String url) {
        var result = "image/png";
        if (url.toLowerCase(Locale.ROOT).endsWith(".gif")) {
            result = "image/gif";
        } else if (url.toLowerCase(Locale.ROOT).endsWith(".jpg")) {
            result = "image/jpeg";
        } else if (url.toLowerCase(Locale.ROOT).endsWith(".tiff")) {
            result = "image/tiff";
        }
        return result;
    }

    /**
     * add links to the OgcApiRecordsRecordGeoJSONDto page
     *
     * @param mediaTypeAndProfile from user
     * @param catalogId which collection (DB source)
     * @param page where to add the links
     */
    public void addCollectionsLinks(
            MediaTypeAndProfile mediaTypeAndProfile, String catalogId, OgcApiRecordsRecordGeoJSONDto page) {

        var formats = itemsPageLinksConfiguration.getMimeFormats().keySet().stream()
                .map(x -> new MediaTypeAndProfile(
                        contentNegotiationManager.getMediaTypeMappings().get(x), null))
                .toList();
        addStandardLinks(
                mediaTypeAndProfile,
                linkConfiguration.getOgcApiRecordsBaseUrl(),
                "collections/" + URLEncoder.encode(catalogId, StandardCharsets.UTF_8),
                page,
                formats,
                "collection",
                "collection");
    }

    /**
     * add links to the OgcApiRecordsGetRecords200ResponseDto
     *
     * @param mediaTypeAndProfile from user
     * @param collectionId which collection (DB source)
     * @param page where to add the links
     */
    public void addLinks(
            MediaTypeAndProfile mediaTypeAndProfile, String collectionId, OgcApiRecordsGetRecords200ResponseDto page) {
        addStandardLinks(
                mediaTypeAndProfile,
                linkConfiguration.getOgcApiRecordsBaseUrl(),
                "collections/" + URLEncoder.encode(collectionId, StandardCharsets.UTF_8) + "/items",
                page,
                getFormats(),
                "self",
                "alternate");
    }

    /**
     * addd links to the OgcApiRecordsCatalogDto with custom `rel=..`
     *
     * @param mediaTypeAndProfile from user
     * @param collectionId which collection (DB source)
     * @param page where to add the links
     * @param selfName name to give "rel=self" (i.e. same format name)
     * @param altName name to give "rel=alternate" (i.e. different format name)
     */
    public void addLinks(
            MediaTypeAndProfile mediaTypeAndProfile,
            String collectionId,
            OgcApiRecordsCatalogDto page,
            String selfName,
            String altName) {
        addStandardLinks(
                mediaTypeAndProfile,
                linkConfiguration.getOgcApiRecordsBaseUrl(),
                "collections/" + URLEncoder.encode(collectionId, StandardCharsets.UTF_8) + "/items",
                page,
                getFormats(),
                selfName,
                altName);
    }
}
