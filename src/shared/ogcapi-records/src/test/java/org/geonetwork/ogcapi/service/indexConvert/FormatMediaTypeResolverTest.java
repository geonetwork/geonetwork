/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.indexConvert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.geonetwork.ogcapi.service.configuration.FormatMediaTypeEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FormatMediaTypeResolverTest {

    private final FormatMediaTypeResolver resolver = new FormatMediaTypeResolver((List<FormatMediaTypeEntry>) null);

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            value = {
                "SHP                              | application/zip",
                "ESRI Shapefile                   | application/zip",
                "  esri shapefile  (.shp)         | application/zip",
                "application/x-shapefile          | application/zip",
                "application/vnd.shp              | application/zip",
                "application/vnd.fgb              | application/vnd.flatgeobuf",
                "FlatGeobuf                       | application/vnd.flatgeobuf",
                "GDB                              | application/x-filegdb",
                "ESRI File Geodatabase (.fgdb)    | application/x-filegdb",
                "ascii (.csv, .txt, .sql)         | text/plain",
                "CSV                              | text/csv",
                "GeoTIFF                          | image/tiff; application=geotiff",
                "GeoTIFF 1.1                      | image/tiff; application=geotiff",
                "Cloud Optimized GeoTIFF (COG)    | image/tiff; application=geotiff; profile=cloud-optimized",
                "TIFF (.tif, .tiff)               | image/tiff",
                "JPEG 2000                        | image/jp2",
                "OGC GeoPackage (.gpkg)           | application/geopackage+sqlite3",
                "GeoJSON                          | application/geo+json",
                "application/vnd.geo+json         | application/geo+json",
                "NetCDF-4                         | application/x-netcdf",
                ".csv                             | text/csv",
                "My data (.las)                   | application/vnd.las",
            })
    public void testKnownFormats(String formatName, String expectedMediaType) {
        assertEquals(expectedMediaType, resolver.resolve(formatName));
    }

    @Test
    public void testMediaTypeNotInVocabularyIsUsedAsIs() {
        assertEquals("application/x-custom+xml", resolver.resolve("application/x-custom+xml"));
    }

    @Test
    public void testUnknownOrEmptyFormats() {
        assertNull(resolver.resolve("ECW"));
        assertNull(resolver.resolve("Some proprietary format"));
        assertNull(resolver.resolve("  "));
        assertNull(resolver.resolve(null));
    }

    @Test
    public void testConfiguredEntriesExtendAndOverrideTheVocabulary() {
        var configured = new FormatMediaTypeResolver(List.of(
                new FormatMediaTypeEntry("image/x-ecw", List.of("ECW", "Enhanced Compression Wavelet")),
                new FormatMediaTypeEntry("application/x-zipped-shp", List.of("SHP"))));

        assertEquals("image/x-ecw", configured.resolve("ecw"));
        assertEquals("image/x-ecw", configured.resolve("Enhanced Compression Wavelet"));
        assertEquals("application/x-zipped-shp", configured.resolve("SHP"));
        // other built-in entries are kept
        assertEquals("application/zip", configured.resolve("ESRI Shapefile"));
    }

    @Test
    public void testConfiguredEntryWithoutMediaTypeIsIgnored() {
        var configured = new FormatMediaTypeResolver(List.of(new FormatMediaTypeEntry(" ", List.of("SHP"))));

        assertEquals("application/zip", configured.resolve("SHP"));
    }
}
