/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.links;

import java.util.ArrayList;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetCollections200ResponseDto;
import org.geonetwork.ogcapi.service.configuration.CollectionsPageLinksConfiguration;
import org.geonetwork.ogcapi.service.configuration.OgcApiLinkConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

/** add links to the /collections endpoint */
@Component
public class CollectionsPageLinks extends BasicLinks {

    @Autowired
    OgcApiLinkConfiguration linkConfiguration;

    @Autowired
    CollectionsPageLinksConfiguration collectionsPageLinksConfiguration;

    @Autowired
    CollectionPageLinks collectionPageLinks;

    /**
     * add links to the OgcApiRecordsGetCollections200ResponseDto page
     *
     * @param nativeWebRequest from user
     * @param page page to add links to
     */
    public void addLinks(NativeWebRequest nativeWebRequest, OgcApiRecordsGetCollections200ResponseDto page) {
        addStandardLinks(
                nativeWebRequest,
                linkConfiguration.getOgcApiRecordsBaseUrl(),
                "collections",
                page,
                new ArrayList<String>(
                        collectionsPageLinksConfiguration.getMimeFormats().keySet()),
                "self",
                "alternative");

        // add in the collection-specific links
        page.getCollections().forEach(x -> collectionPageLinks.addLinks(nativeWebRequest, x));
    }
}
