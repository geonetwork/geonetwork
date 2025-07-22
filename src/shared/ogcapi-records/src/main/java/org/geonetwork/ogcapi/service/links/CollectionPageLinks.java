/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.links;

import io.micrometer.common.util.StringUtils;
import java.net.URI;
import java.util.ArrayList;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.geonetwork.ogcapi.service.configuration.CollectionPageLinksConfiguration;
import org.geonetwork.ogcapi.service.configuration.OgcApiLinkConfiguration;
import org.geonetwork.ogcapi.service.configuration.QueryablesPageLinksConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

/** add links to the /collections/{collectionId} endpoint */
@Component
public class CollectionPageLinks extends BasicLinks {

    @Autowired
    OgcApiLinkConfiguration linkConfiguration;

    @Autowired
    CollectionPageLinksConfiguration collectionPageLinksConfiguration;

    @Autowired
    QueryablesPageLinksConfiguration queryablesPageLinksConfiguration;

    @Autowired
    ItemsPageLinks itemsPageLinks;

    /**
     * add links to a OgcApiRecordsCatalogDto
     *
     * @param nativeWebRequest incoming request
     * @param page page to add to
     */
    public void addLinks(NativeWebRequest nativeWebRequest, OgcApiRecordsCatalogDto page) {
        addStandardLinks(
                nativeWebRequest,
                linkConfiguration.getOgcApiRecordsBaseUrl(),
                "collections/" + page.getId(),
                page,
                new ArrayList<String>(
                        collectionPageLinksConfiguration.getMimeFormats().keySet()),
                "self",
                "alternate");

        addIconLink(page);
        addQueryables(page);
        addItems(nativeWebRequest, page);
    }

    /**
     * add the item links
     *
     * @param nativeWebRequest incoming request
     * @param page page to add to
     */
    private void addItems(NativeWebRequest nativeWebRequest, OgcApiRecordsCatalogDto page) {
        itemsPageLinks.addLinks(nativeWebRequest, page.getId(), page, "items", "items");
    }

    /**
     * add queryables links
     *
     * @param page page to add to
     */
    private void addQueryables(OgcApiRecordsCatalogDto page) {
        var uri = URI.create(linkConfiguration.getOgcApiRecordsBaseUrl())
                .resolve("collections/" + page.getId() + "/queryables?f=json");
        var link = new OgcApiRecordsLinkDto()
                .href(uri)
                .rel("http://www.opengis.net/def/rel/ogc/1.0/queryables")
                .type("application/schema+json")
                .title("Queryables for this collection");
        page.addLinksItem(link);
    }

    /**
     * add GN icon lnk
     *
     * @param page page to add to
     */
    private void addIconLink(OgcApiRecordsCatalogDto page) {
        var catalogUuid = page.getId();
        if (StringUtils.isBlank(catalogUuid)) {
            return;
        }
        // assume its a png
        var uri = URI.create(linkConfiguration.getGnBaseUrl()).resolve("images/logos/" + catalogUuid + ".png");
        var link = new OgcApiRecordsLinkDto().href(uri).rel("icon").type("image/png");

        page.addLinksItem(link);
    }
}
