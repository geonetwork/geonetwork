/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.indexConvert;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.geonetwork.formatting.processor.IndexFormatterProcessor;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.geonetwork.ogcapi.service.links.ItemPageLinks;
import org.geonetwork.utility.MediaTypeAndProfile;
import org.springframework.beans.factory.BeanFactory;

@AllArgsConstructor
public abstract class AbstractOgcApiIndexFormatter implements IndexFormatterProcessor {

    protected final OgcApiGeoJsonConverter ogcApiGeoJsonConverter;
    protected final ObjectMapper objectMapper;
    protected final BeanFactory beanFactory;

    @Override
    public String getTitle() {
        return "OGCAPI-Records GeoJSON output.";
    }

    @Override
    public String getOfficialProfileName() {
        return "http://www.opengis.net/def/profile/OGC/0/ogc-record";
    }

    @Override
    public String getProfileName() {
        return "ogc-record";
    }

    @Override
    public void process(IndexRecord indexRecord, OutputStream out, Map<String, Object> config) throws IOException {
        String lang = "eng";
        if (config == null) {
            config = new HashMap<>();
        }
        var result = ogcApiGeoJsonConverter.convert(indexRecord, lang);

        ItemPageLinks itemPageLinks = beanFactory.getBean(ItemPageLinks.class);
        itemPageLinks.addLinks(
                (MediaTypeAndProfile) config.get("mediaTypeAndProfile"), (String) config.get("collectionId"), result);

        // and the profile link (like the one in the header).  From spec:
        //      {
        //              "href": "http://www.opengis.net/def/profile/OGC/0/ogc-record",
        //              "rel": "profile",
        //              "title": "This is a OARec Record"
        //      },

        try {
            var link = new OgcApiRecordsLinkDto(new URI("http://www.opengis.net/def/profile/OGC/0/ogc-record"));
            link.setRel("profile");
            link.setTitle("this is an ogc-record");
            result.addLinksItem(link);
        } catch (URISyntaxException e) {
            // should never happen
            throw new IOException(e);
        }

        objectMapper.writeValue(out, result);
    }
}
