/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.links;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import org.geonetwork.formatting.FormatterApi;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.geonetwork.ogcapi.service.configuration.OgcApiLinkConfiguration;
import org.springframework.stereotype.Component;

/** This handles adding "special" links to the single-record (`.../items/<itemid>`) based on the `FormatterApi`. */
@Component
@AllArgsConstructor
public class FormatterApiRecordLinkAttacher {

    private final OgcApiLinkConfiguration linkConfiguration;
    private final FormatterApi formatterApi;

    /**
     * Attaches the FormatterApi links to the current record.
     *
     * @param record controller response object to put the links in
     * @param collectionId which collection does this record belong to (to fill in all the links)
     * @throws Exception something went wrong
     */
    public void attachLinks(OgcApiRecordsRecordGeoJSONDto record, String collectionId) throws Exception {

        var availableFormatters = formatterApi.getRecordFormattersForMetadataByMediaType(record.getId());

        availableFormatters.values().forEach(profileInfos -> {
            // base url to the individual record
            var url = linkConfiguration.getOgcApiRecordsBaseUrl() + "collections/"
                    + URLEncoder.encode(collectionId, StandardCharsets.UTF_8)
                    + "/items/" + URLEncoder.encode(record.getId(), StandardCharsets.UTF_8);

            // add mime-type to link (?f=...)
            var mimeType = profileInfos.getFirst().getMimeType();
            url += "?f=" + mimeType;
            var link = new OgcApiRecordsLinkDto();
            try {
                link.setHref(new URI(url));
                link.setRel("alternate");
                link.setHreflang("eng");
                link.setType(mimeType);
                link.setProfile(profileInfos.stream()
                        .map(x -> x.getOfficialProfileName())
                        .toList());
                addLink(record, link);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * via reflection
     *
     * @param page object to attach link to
     * @param link link to attach
     * @throws Exception wrong object?
     */
    public void addLink(Object page, OgcApiRecordsLinkDto link) throws Exception {
        var method = page.getClass().getMethod("addLinksItem", OgcApiRecordsLinkDto.class);
        method.invoke(page, link);
    }
}
