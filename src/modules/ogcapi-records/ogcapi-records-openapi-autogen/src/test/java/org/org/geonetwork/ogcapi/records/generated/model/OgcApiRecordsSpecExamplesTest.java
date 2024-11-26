/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.org.geonetwork.ogcapi.records.generated.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFeatureCollectionGeoJSONDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFormatDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkTemplateDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsSchemeDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsThemeDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsTimeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OgcApiRecordsSpecExamplesTest {

    ObjectMapper mapper;

    public static String readResource(String fname) throws IOException {
        String data = new String(
                ClassLoader.getSystemClassLoader().getResourceAsStream(fname).readAllBytes());
        return data.trim();
    }

    @BeforeEach
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, true);
        mapper.findAndRegisterModules();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }

    public void doTest(String fname, Class clazz) throws IOException {
        String json = readResource("ogcapi_records_spec_examples/" + fname);
        var obj = mapper.readValue(json, clazz);
        var json2 = mapper.writeValueAsString(obj);

        assertEquals(json, json2);
    }

    @Test
    public void example1_date() throws IOException {
        doTest("example1_date.json", OgcApiRecordsTimeDto.class);
    }

    @Test
    public void example2_timestamp() throws IOException {
        doTest("example2_timestamp.json", OgcApiRecordsTimeDto.class);
    }

    @Test
    public void example3_interval() throws IOException {
        doTest("example3_interval.json", OgcApiRecordsTimeDto.class);
    }

    @Test
    public void example4_intervalTimestamps() throws IOException {
        doTest("example4_intervalTimestamps.json", OgcApiRecordsTimeDto.class);
    }

    @Test
    public void example5_intervalTimestamps() throws IOException {
        doTest("example5-intervalHalf.json", OgcApiRecordsTimeDto.class);
    }

    // Keywords and themes Example
    @Test
    public void example6a_keywords() throws IOException {
        doTest("example6a-keywords.json", String[].class);
    }

    // Keywords and themes Example
    @Test
    public void example6b_themes() throws IOException {
        doTest("example6b-themes.json", OgcApiRecordsThemeDto[].class);
    }

    // Formats Example
    @Test
    public void exampleexample7_formats() throws IOException {
        doTest("example7-formats.json", OgcApiRecordsFormatDto[].class);
    }

    // Associations simple and Link Template Example
    @Test
    public void example8a_links() throws IOException {
        doTest("example8a_links.json", OgcApiRecordsLinkDto[].class);
    }

    // Associations simple and Link Template Example
    @Test
    public void example8b_linkTemplates() throws IOException {
        doTest("example8b_linkTemplates.json", OgcApiRecordsLinkTemplateDto[].class);
    }

    // Templated link with in-line dictionary of substitution variables:
    @Test
    public void example9_templatedLink() throws IOException {
        doTest("example9-templatedLink.json", OgcApiRecordsLinkTemplateDto.class);
    }

    // Templated link with varBase URI for retrieving substitution variable definitions:
    @Test
    public void example10_templatedLink2() throws IOException {
        doTest("example10-templatedLink2.json", OgcApiRecordsLinkTemplateDto.class);
    }

    @Test
    public void example11_schemas() throws IOException {
        doTest("example11-schemas.json", OgcApiRecordsSchemeDto[].class);
    }

    // Records Example in GeoJSON
    @Test
    public void example12_geojson() throws IOException {
        doTest("example12-geojson.json", OgcApiRecordsFeatureCollectionGeoJSONDto.class);
    }

    // Record Example in GeoJSON
    @Test
    public void example13_geojson() throws IOException {
        doTest("example13-geojson.json", OgcApiRecordsRecordGeoJSONDto.class);
    }
}
