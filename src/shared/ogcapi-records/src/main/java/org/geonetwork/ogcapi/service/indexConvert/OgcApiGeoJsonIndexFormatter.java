/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.geonetwork.formatting.processor.IndexFormatterProcessor;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.service.links.ItemPageLinks;
import org.geonetwork.utility.MediaTypeAndProfile;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

/** simple wrapper around OgcApiGeoJsonIndexFormatter so it can be used as an output format. */
@Component
@AllArgsConstructor
public class OgcApiGeoJsonIndexFormatter implements IndexFormatterProcessor {

    private final OgcApiGeoJsonConverter ogcApiGeoJsonConverter;
    private final ObjectMapper objectMapper;
    private final BeanFactory beanFactory;

    @Override
    public String getName() {
        return "OgcApiGeoJson";
    }

    @Override
    public String getContentType() {
        return "application/geo+json";
    }

    @Override
    public String getTitle() {
        return "OGCAPI-Records GeoJSON output.";
    }

    @Override
    public String getOfficialProfileName() {
        return "http://www.opengis.net/def/profile/OGC/0/ogc-catalog";
    }

    @Override
    public String getProfileName() {
        return "ogc-catalog";
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

        objectMapper.writeValue(out, result);
    }
}
