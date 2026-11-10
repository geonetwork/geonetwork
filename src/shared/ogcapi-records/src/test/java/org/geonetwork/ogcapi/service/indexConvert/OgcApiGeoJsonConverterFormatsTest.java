/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.indexConvert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class OgcApiGeoJsonConverterFormatsTest {

    private final FormatMediaTypeResolver resolver = new FormatMediaTypeResolver((List<FormatMediaTypeEntry>) null);

    @Test
    public void testNullOrEmptyFormats() {
        assertTrue(OgcApiGeoJsonConverter.convertFormats(null, resolver).isEmpty());
        assertTrue(OgcApiGeoJsonConverter.convertFormats(List.of(), resolver).isEmpty());
    }

    @Test
    public void testFormatWithMediaType() {
        var result = OgcApiGeoJsonConverter.convertFormats(List.of("SHP"), resolver);

        assertEquals(1, result.size());
        assertEquals("SHP", result.getFirst().getName());
        assertEquals("application/zip", result.getFirst().getMediaType());
    }

    @Test
    public void testUnknownFormatKeepsNameOnly() {
        var result = OgcApiGeoJsonConverter.convertFormats(List.of("ECW"), resolver);

        assertEquals("ECW", result.getFirst().getName());
        assertNull(result.getFirst().getMediaType());
    }

    @Test
    public void testBlankAndDuplicateFormatsAreSkipped() {
        var result = OgcApiGeoJsonConverter.convertFormats(
                Arrays.asList("GeoTIFF", null, " ", "geotiff ", "TIFF (.tif, .tiff)"), resolver);

        assertEquals(2, result.size());
        assertEquals("GeoTIFF", result.get(0).getName());
        assertEquals("image/tiff; application=geotiff", result.get(0).getMediaType());
        assertEquals("TIFF (.tif, .tiff)", result.get(1).getName());
        assertEquals("image/tiff", result.get(1).getMediaType());
    }
}
