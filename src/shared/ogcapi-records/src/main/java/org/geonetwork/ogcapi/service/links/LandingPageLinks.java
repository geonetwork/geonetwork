/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.links;

import io.micrometer.common.util.StringUtils;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfile;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiLandingPageResponse;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLandingPageDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Injects links into the Landing Page response */
@Component
@Slf4j
public class LandingPageLinks extends BasicLinks {

    @Autowired
    CollectionsPageLinks collectionsPageLinks;

    public void addLinks(
            RequestMediaTypeAndProfile requestMediaTypeAndProfile,
            String catalogUuid,
            OgcApiRecordsLandingPageDto landingPage)
            throws Exception {

        super.addLinks(requestMediaTypeAndProfile, landingPage, "", OgcApiLandingPageResponse.class);

        addOpenApiLink(landingPage);
        addIconLink(landingPage, catalogUuid);
        addConformanceLinks(landingPage);
        addRootLinks(requestMediaTypeAndProfile, landingPage);
        collectionsPageLinks.addLinks(requestMediaTypeAndProfile, landingPage);
    }

    /**
     * Adds an icon link (to the GN icon for a DB `source`).
     *
     * @param landingPage where to add
     * @param catalogUuid catalog that the main portal is logged to (can be null)
     */
    private void addIconLink(OgcApiRecordsLandingPageDto landingPage, String catalogUuid) {
        if (StringUtils.isBlank(catalogUuid)) {
            return;
        }
        // assume its a png
        var uri = URI.create(linkConfiguration.getGnBaseUrl()).resolve("images/logos/" + catalogUuid + ".png");
        var link = new OgcApiRecordsLinkDto()
                .href(uri)
                .rel("icon")
                .type("image/png")
                .title("server icon");

        landingPage.addLinksItem(link);

        var link2 = new OgcApiRecordsLinkDto()
                .href(uri)
                .rel("preview")
                .type("image/png")
                .title("server icon");
        landingPage.addLinksItem(link2);
    }
}
