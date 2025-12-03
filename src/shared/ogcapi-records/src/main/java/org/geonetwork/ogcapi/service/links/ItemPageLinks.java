/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.links;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfile;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsSingleRecordResponse;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.springframework.stereotype.Component;

/** Adds in links for the Item Page (single record). This includes the FormatterApi links. */
@Slf4j
@Component
public class ItemPageLinks extends BasicLinks {

    public void addAllLinks(
            RequestMediaTypeAndProfile requestMediaTypeAndProfile,
            String collectionId,
            OgcApiRecordsRecordGeoJSONDto page)
            throws Exception {
        addRootLinks(requestMediaTypeAndProfile, page);
        addLinks(
                requestMediaTypeAndProfile,
                page,
                "collections/" + collectionId + "/items/" + page.getId(),
                OgcApiRecordsSingleRecordResponse.class);
        addThumbnails(page);
    }

    private void addThumbnails(OgcApiRecordsRecordGeoJSONDto page) {
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
}
