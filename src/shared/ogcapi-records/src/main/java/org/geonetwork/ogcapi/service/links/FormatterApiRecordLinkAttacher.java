/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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

/**
 *  This handles adding "special" links to the single-record (`.../items/<itemid>`) based on the `FormatterApi`.
 *
 *
 */
@Component
@AllArgsConstructor
public class FormatterApiRecordLinkAttacher {

    private final OgcApiLinkConfiguration linkConfiguration;
    private final FormatterApi formatterApi;

    public void attachLinks(OgcApiRecordsRecordGeoJSONDto record, String collectionId, Object page) throws Exception {

        var availableFormatters = formatterApi.getRecordFormattersForMetadataByMediaType(record.getId());

        availableFormatters.values().forEach(profileInfos -> {
            var url = linkConfiguration.getOgcApiRecordsBaseUrl() + "collections/"
                    + URLEncoder.encode(collectionId, StandardCharsets.UTF_8)
                    + "/items/" + URLEncoder.encode(record.getId(), StandardCharsets.UTF_8);

            //
            var mimeType = profileInfos.getFirst().getMimeType();
            url += "?f=" + mimeType;
            var link = new OgcApiRecordsLinkDto();
            try {
                link.setHref(new URI(url));
                link.setRel("alternative");
                link.setHreflang("eng");
                link.setType(mimeType);
                link.setProfile(profileInfos.stream()
                        .map(x -> x.getOfficialProfileName())
                        .toList());
                addLink(page, link);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void addLink(Object page, OgcApiRecordsLinkDto link) throws Exception {
        var method = page.getClass().getMethod("addLinksItem", OgcApiRecordsLinkDto.class);
        method.invoke(page, link);
    }
}
