/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.links;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.geonetwork.ogcapi.service.configuration.CollectionPageLinksConfiguration;
import org.geonetwork.ogcapi.service.configuration.ItemPageLinksConfiguration;
import org.geonetwork.ogcapi.service.configuration.OgcApiLinkConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

@Component
public class ItemPageLinks extends BasicLinks {

    @Autowired
    OgcApiLinkConfiguration linkConfiguration;

    @Autowired
    ItemPageLinksConfiguration itemPageLinksConfiguration;

    @Autowired
    CollectionPageLinksConfiguration collectionPageLinksConfiguration;

    /**
     * add links to the OgcApiRecordsRecordGeoJSONDto page
     *
     * @param nativeWebRequest from user
     * @param collectionId which collection (DB source)
     * @param page where to add the links
     */
    public void addLinks(NativeWebRequest nativeWebRequest, String collectionId, OgcApiRecordsRecordGeoJSONDto page) {
        addStandardLinks(
                nativeWebRequest,
                linkConfiguration.getOgcApiRecordsBaseUrl(),
                "collections/" + URLEncoder.encode(collectionId, StandardCharsets.UTF_8) + "/items/"
                        + URLEncoder.encode(page.getId(), StandardCharsets.UTF_8),
                page,
                new ArrayList<String>(
                        itemPageLinksConfiguration.getMimeFormats().keySet()),
                "self",
                "alternative");

        addCollectionsLinks(nativeWebRequest, collectionId, page);
    }

    /**
     * add links to the OgcApiRecordsRecordGeoJSONDto page
     *
     * @param nativeWebRequest from user
     * @param catalogId which collection (DB source)
     * @param page where to add the links
     */
    public void addCollectionsLinks(
            NativeWebRequest nativeWebRequest, String catalogId, OgcApiRecordsRecordGeoJSONDto page) {
        addStandardLinks(
                nativeWebRequest,
                linkConfiguration.getOgcApiRecordsBaseUrl(),
                "collections/" + URLEncoder.encode(catalogId, StandardCharsets.UTF_8),
                page,
                new ArrayList<String>(
                        collectionPageLinksConfiguration.getMimeFormats().keySet()),
                "collection",
                "collection");
    }

    /**
     * add links to the OgcApiRecordsGetRecords200ResponseDto
     *
     * @param nativeWebRequest from user
     * @param collectionId which collection (DB source)
     * @param page where to add the links
     */
    public void addLinks(
            NativeWebRequest nativeWebRequest, String collectionId, OgcApiRecordsGetRecords200ResponseDto page) {
        addStandardLinks(
                nativeWebRequest,
                linkConfiguration.getOgcApiRecordsBaseUrl(),
                "collections/" + URLEncoder.encode(collectionId, StandardCharsets.UTF_8) + "/items",
                page,
                new ArrayList<String>(
                        itemPageLinksConfiguration.getMimeFormats().keySet()),
                "self",
                "alternative");
    }

    /**
     * addd links to the OgcApiRecordsCatalogDto with custom `rel=..`
     *
     * @param nativeWebRequest from user
     * @param collectionId which collection (DB source)
     * @param page where to add the links
     * @param selfName name to give "rel=self" (i.e. same format name)
     * @param altName name to give "rel=alternative" (i.e. different format name)
     */
    public void addLinks(
            NativeWebRequest nativeWebRequest,
            String collectionId,
            OgcApiRecordsCatalogDto page,
            String selfName,
            String altName) {
        addStandardLinks(
                nativeWebRequest,
                linkConfiguration.getOgcApiRecordsBaseUrl(),
                "collections/" + URLEncoder.encode(collectionId, StandardCharsets.UTF_8) + "/items",
                page,
                new ArrayList<String>(
                        itemPageLinksConfiguration.getMimeFormats().keySet()),
                selfName,
                altName);
    }
}
