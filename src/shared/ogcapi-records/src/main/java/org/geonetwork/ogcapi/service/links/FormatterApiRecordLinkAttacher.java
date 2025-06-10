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

@Component
@AllArgsConstructor
public class FormatterApiRecordLinkAttacher {

    private final OgcApiLinkConfiguration linkConfiguration;
    private final FormatterApi formatterApi;

    public void attachLinks(OgcApiRecordsRecordGeoJSONDto record, String collectionId, Object page) throws Exception {

        var availableFormatters =
                formatterApi.getRecordFormattersForMetadata(record.getId());
        availableFormatters.forEach(formatter -> {
            var url = linkConfiguration.getOgcApiRecordsBaseUrl() + "collections/"
                    + URLEncoder.encode(collectionId, StandardCharsets.UTF_8)
                    + "/items/" + URLEncoder.encode(record.getId(), StandardCharsets.UTF_8);

            url += "?f=" + formatter.getName();
            var link = new OgcApiRecordsLinkDto();
            try {
                link.setHref(new URI(url));
                link.setRel("self");
                link.setHreflang("eng");
                link.setType("application/UNKNOWN_MIME_TYPE");
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
