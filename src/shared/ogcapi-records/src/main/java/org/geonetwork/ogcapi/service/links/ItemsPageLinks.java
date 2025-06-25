/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.links;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.geonetwork.ogcapi.service.configuration.CollectionPageLinksConfiguration;
import org.geonetwork.ogcapi.service.configuration.ItemsPageLinksConfiguration;
import org.geonetwork.ogcapi.service.configuration.OgcApiLinkConfiguration;
import org.geonetwork.ogcapi.service.querybuilder.OgcApiQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

/** Adds in links for the items (plural - /collections/collectionId/items). */
@Component
public class ItemsPageLinks extends BasicLinks {

    @Autowired
    OgcApiLinkConfiguration linkConfiguration;

    @Autowired
    ItemsPageLinksConfiguration itemsPageLinksConfiguration;

    @Autowired
    CollectionPageLinksConfiguration collectionPageLinksConfiguration;

    /**
     * add links to OgcApiRecordsGetRecords200ResponseDto
     *
     * @param nativeWebRequest from user
     * @param collectionId which collection (DB source)
     * @param page where to add the links
     * @param query user's query
     */
    public void addLinks(
            NativeWebRequest nativeWebRequest,
            String collectionId,
            OgcApiRecordsGetRecords200ResponseDto page,
            OgcApiQuery query) {
        addStandardLinks(
                nativeWebRequest,
                linkConfiguration.getOgcApiRecordsBaseUrl(),
                "collections/" + URLEncoder.encode(collectionId, StandardCharsets.UTF_8) + "/items",
                page,
                new ArrayList<String>(
                        itemsPageLinksConfiguration.getMimeFormats().keySet()),
                "self",
                "alternative");

        addCollectionsLinks(nativeWebRequest, collectionId, page);

        addNextPrevious(collectionId, page, query);
    }

    /**
     * add rel=next and rel=previous links to OgcApiRecordsGetRecords200ResponseDto
     *
     * @param collectionId which collection (DB source)
     * @param page where to add the links
     * @param query user's query
     */
    private void addNextPrevious(String collectionId, OgcApiRecordsGetRecords200ResponseDto page, OgcApiQuery query) {
        // previous
        if (query.getStartIndex() != 0) {
            int previous = Math.max(query.getStartIndex() - query.getLimit(), 0);
            var uri = URI.create(linkConfiguration.getOgcApiRecordsBaseUrl())
                    .resolve("collections/" + collectionId + "/items?offset=" + previous);
            var link = new OgcApiRecordsLinkDto()
                    .href(uri)
                    .rel("prev")
                    .type("application/geo+json")
                    .title("Items (prev)");
            page.addLinksItem(link);
        }
        // next
        if (query.getStartIndex() + page.getNumberReturned() < page.getNumberMatched()) {
            int next = query.getStartIndex() + page.getNumberReturned();
            var uri = URI.create(linkConfiguration.getOgcApiRecordsBaseUrl())
                    .resolve("collections/" + collectionId + "/items?offset=" + next);
            var link = new OgcApiRecordsLinkDto()
                    .href(uri)
                    .rel("next")
                    .type("application/geo+json")
                    .title("Items (next)");
            page.addLinksItem(link);
        }
    }

    /**
     * add collection-like links to OgcApiRecordsGetRecords200ResponseDto
     *
     * @param nativeWebRequest from user
     * @param catalogId which collection (DB source)
     * @param page where to add the links
     */
    public void addCollectionsLinks(
            NativeWebRequest nativeWebRequest, String catalogId, OgcApiRecordsGetRecords200ResponseDto page) {
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
     * add links to OgcApiRecordsCatalogDto with special "rel="
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
                        itemsPageLinksConfiguration.getMimeFormats().keySet()),
                selfName,
                altName);
    }
}
