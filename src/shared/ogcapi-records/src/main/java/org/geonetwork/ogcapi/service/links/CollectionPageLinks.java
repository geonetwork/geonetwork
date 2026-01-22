/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.links;

import io.micrometer.common.util.StringUtils;
import java.net.URI;
import org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfile;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiCollectionResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponse;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.springframework.stereotype.Component;

/** add links to the /collections/{collectionId} endpoint */
@Component
public class CollectionPageLinks extends BasicLinks {

    public void addAllLinks(RequestMediaTypeAndProfile requestMediaTypeAndProfile, OgcApiRecordsCatalogDto page)
            throws Exception {
        addRootLinks(requestMediaTypeAndProfile, page);
        addIconLink(page);
        addQueryables(page);
        // collections adds too much complexity in the (single) collection sub-page.
        // its not required and pygeoapi doesn't include it either...
        //        addCollections(requestMediaTypeAndProfile, page);
        addLinks(requestMediaTypeAndProfile, page, page.getId());
    }

    //    private void addCollections(RequestMediaTypeAndProfile requestMediaTypeAndProfile, OgcApiRecordsCatalogDto
    // page)
    //            throws Exception {
    //        super.addLinks(requestMediaTypeAndProfile, page, "collections", OgcApiRecordsCollectionsResponse.class);
    //    }

    public void addLinks(RequestMediaTypeAndProfile requestMediaTypeAndProfile, Object page, String collectionId)
            throws Exception {
        super.addLinks(requestMediaTypeAndProfile, page, "collections/" + collectionId, OgcApiCollectionResponse.class);

        // handle "http://www.opengis.net/def/rel/ogc/1.0/ogc-catalog" as well as "self"/"relative"
        var requestMediaType2 = requestMediaTypeAndProfile.toBuilder().build(); // clone
        Class<?> clazz = Object.class;
        if (!requestMediaTypeAndProfile.getResponseClass().equals(OgcApiCollectionResponse.class)) {
            clazz = OgcApiCollectionResponse.class;
        }
        requestMediaType2.setResponseClass(clazz);
        super.addLinks(requestMediaType2, page, "collections/" + collectionId, OgcApiCollectionResponse.class);

        // add items links
        super.addLinks(
                requestMediaTypeAndProfile,
                page,
                "collections/" + collectionId + "/items",
                OgcApiRecordsMultiRecordResponse.class);
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
        var link = new OgcApiRecordsLinkDto()
                .href(uri)
                .rel("icon")
                .type("image/png")
                .title("collection icon");

        page.addLinksItem(link);

        var link2 = new OgcApiRecordsLinkDto()
                .href(uri)
                .rel("preview")
                .type("image/png")
                .title("collection icon");
        page.addLinksItem(link2);
    }
}
