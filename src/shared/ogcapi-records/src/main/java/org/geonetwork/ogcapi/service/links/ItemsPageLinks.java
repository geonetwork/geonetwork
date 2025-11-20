/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.links;

import java.net.URI;
import org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfile;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiCollectionResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiLandingPageResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsCollectionsResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponse;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.geonetwork.ogcapi.service.querybuilder.OgcApiQuery;
import org.springframework.stereotype.Component;

/** Adds in links for the items (plural - /collections/collectionId/items). */
@Component
public class ItemsPageLinks extends BasicLinks {

    public void addLinks(
            RequestMediaTypeAndProfile requestMediaTypeAndProfile,
            String collectionId,
            OgcApiRecordsGetRecords200ResponseDto page,
            OgcApiQuery query)
            throws Exception {

        addLinks(requestMediaTypeAndProfile, page, "", OgcApiLandingPageResponse.class);
        addLinks(
                requestMediaTypeAndProfile,
                page,
                "collections/" + collectionId + "/items",
                OgcApiRecordsMultiRecordResponse.class);
        addLinks(requestMediaTypeAndProfile, page, "collections/" + collectionId, OgcApiCollectionResponse.class);
        addLinks(requestMediaTypeAndProfile, page, "collections", OgcApiRecordsCollectionsResponse.class);

        addNextPrevious(requestMediaTypeAndProfile, collectionId, page, query);
    }

    /**
     * add rel=next and rel=previous links to OgcApiRecordsGetRecords200ResponseDto
     *
     * @param requestMediaTypeAndProfile info about the userrequest
     * @param collectionId which collection (DB source)
     * @param page where to add the links
     * @param query user's query
     */
    private void addNextPrevious(
            RequestMediaTypeAndProfile requestMediaTypeAndProfile,
            String collectionId,
            OgcApiRecordsGetRecords200ResponseDto page,
            OgcApiQuery query) {
        // previous
        if (query.getStartIndex() != 0) {
            int previous = Math.max(query.getStartIndex() - query.getLimit(), 0);
            var uri = URI.create(linkConfiguration.getOgcApiRecordsBaseUrl())
                    .resolve("collections/" + collectionId + "/items?offset="
                            + previous + "&limit="
                            + query.getLimit() + "&f="
                            + requestMediaTypeAndProfile.getRequestMediaType());
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
                    .resolve("collections/" + collectionId + "/items?offset=" + next + "&limit=" + query.getLimit());
            var link = new OgcApiRecordsLinkDto()
                    .href(uri)
                    .rel("next")
                    .type("application/geo+json")
                    .title("Items (next)");
            page.addLinksItem(link);
        }
    }
}
