/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.indexConvert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geonetwork.index.model.record.Link;
import org.junit.jupiter.api.Test;

public class OgcApiGeoJsonConverterLinksTest {

    @Test
    public void testNullLinks() {
        assertTrue(OgcApiGeoJsonConverter.convertLinks(null, "eng").isEmpty());
    }

    @Test
    public void testConvertLink() {
        var link = link(Map.of("default", "https://example.com/data.zip"), "application/zip", "WWW:DOWNLOAD");
        link.setName(Map.of("default", "Data", "langfre", "Données"));
        link.setFunction("download");

        var result = OgcApiGeoJsonConverter.convertLinks(List.of(link), "fre");

        assertEquals(1, result.size());
        assertEquals("https://example.com/data.zip", result.getFirst().getHref().toString());
        assertEquals("enclosure", result.getFirst().getRel());
        assertEquals("application/zip", result.getFirst().getType());
        assertEquals("Données", result.getFirst().getTitle());
    }

    @Test
    public void testTypeFallsBackToProtocol() {
        var link = link(Map.of("default", "https://example.com/wms"), null, "OGC:WMS");

        var result = OgcApiGeoJsonConverter.convertLinks(List.of(link), "eng");

        assertEquals("OGC:WMS", result.getFirst().getType());
        assertEquals("related", result.getFirst().getRel());
    }

    @Test
    public void testLanguageSpecificUrl() {
        var link = link(Map.of("default", "https://example.com/en", "langfre", "https://example.com/fr"), null, null);

        var result = OgcApiGeoJsonConverter.convertLinks(List.of(link), "fre");

        assertEquals("https://example.com/fr", result.getFirst().getHref().toString());
    }

    @Test
    public void testInvalidOrMissingUrlsAreSkipped() {
        var noUrl = link(new HashMap<>(), null, "WWW:LINK");
        var blankUrl = link(Map.of("default", "  "), null, "WWW:LINK");
        var invalidUrl = link(Map.of("default", "https://example.com/a b|c"), null, "WWW:LINK");
        var valid = link(Map.of("default", "https://example.com/ok"), null, "WWW:LINK");

        var result = OgcApiGeoJsonConverter.convertLinks(List.of(noUrl, blankUrl, invalidUrl, valid), "eng");

        assertEquals(1, result.size());
        assertEquals("https://example.com/ok", result.getFirst().getHref().toString());
    }

    private static Link link(Map<String, String> url, String mimeType, String protocol) {
        var link = new Link();
        link.setUrl(url);
        link.setMimeType(mimeType);
        link.setProtocol(protocol);
        return link;
    }
}
