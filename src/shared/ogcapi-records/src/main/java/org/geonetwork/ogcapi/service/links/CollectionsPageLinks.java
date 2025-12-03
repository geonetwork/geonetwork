/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.links;

import org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfile;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsCollectionsResponse;
import org.springframework.stereotype.Component;

/** add links to the /collections endpoint */
@Component
public class CollectionsPageLinks extends BasicLinks {

    public void addAllLinks(RequestMediaTypeAndProfile requestMediaTypeAndProfile, Object page) throws Exception {
        addRootLinks(requestMediaTypeAndProfile, page);
        addLinks(requestMediaTypeAndProfile, page);
    }

    public void addLinks(RequestMediaTypeAndProfile requestMediaTypeAndProfile, Object page) throws Exception {
        super.addLinks(requestMediaTypeAndProfile, page, "collections", OgcApiRecordsCollectionsResponse.class);
    }
}
