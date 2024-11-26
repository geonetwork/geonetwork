/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.org.geonetwork.ogcapi.records.generated.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGeometryCollectionDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGeometryGeoJSONDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLineStringDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsMultiLineStringDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsMultiPointDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsMultiPolygonDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsPointDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsPolygonDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OgcApiRecordsExtraExamplesTest {

    ObjectMapper mapper;

    @BeforeEach
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, true);
        mapper.findAndRegisterModules();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    public void doTest(String fname, Class clazz) throws IOException {
        String json = OgcApiRecordsSpecExamplesTest.readResource("ogcapi_records_extra_examples/" + fname);
        var obj = mapper.readValue(json, clazz);
        var json2 = mapper.writeValueAsString(obj);

        assertEquals(json, json2);
    }

    public Object doTest1(String fname, Class clazz) throws IOException {
        String json = OgcApiRecordsSpecExamplesTest.readResource("ogcapi_records_extra_examples/" + fname);
        var obj = mapper.readValue(json, clazz);
        var json2 = mapper.writeValueAsString(obj);

        return obj;
    }

    // allow string & number as id.
    // NOTE: this will convert the number 123 -> "123" (string)
    @Test
    public void featureGeoJSON_id_number() throws IOException {
        var result = (OgcApiRecordsRecordGeoJSONDto)
                doTest1("featureGeoJSON_id_number.json", OgcApiRecordsRecordGeoJSONDto.class);

        assertEquals("123", result.getId());
    }

    // can read/write a geojson Point
    @Test
    public void GeoJSON_geom_point() throws IOException {
        var result = (OgcApiRecordsGeometryGeoJSONDto)
                doTest1("GeoJSON_geom_point.json", OgcApiRecordsGeometryGeoJSONDto.class);

        assertEquals(OgcApiRecordsPointDto.class, result.getClass());
        assertEquals("Point", result.getType());

        doTest("GeoJSON_geom_point.json", OgcApiRecordsGeometryGeoJSONDto.class);
    }

    // can read/write a geojson LineString
    @Test
    public void GeoJSON_geom_linestring() throws IOException {
        var result = (OgcApiRecordsLineStringDto)
                doTest1("GeoJSON_geom_linestring.json", OgcApiRecordsGeometryGeoJSONDto.class);

        assertEquals(OgcApiRecordsLineStringDto.class, result.getClass());
        assertEquals("LineString", result.getType());

        doTest("GeoJSON_geom_linestring.json", OgcApiRecordsGeometryGeoJSONDto.class);
    }

    // can read/write a geojson Polygon
    @Test
    public void GeoJSON_geom_polygon() throws IOException {
        var result =
                (OgcApiRecordsPolygonDto) doTest1("GeoJSON_geom_polygon.json", OgcApiRecordsGeometryGeoJSONDto.class);

        assertEquals(OgcApiRecordsPolygonDto.class, result.getClass());
        assertEquals("Polygon", result.getType());

        doTest("GeoJSON_geom_polygon.json", OgcApiRecordsGeometryGeoJSONDto.class);
    }

    // can read/write a geojson MultiPoint
    @Test
    public void GeoJSON_geom_multipoint() throws IOException {
        var result = (OgcApiRecordsMultiPointDto)
                doTest1("GeoJSON_geom_multipoint.json", OgcApiRecordsGeometryGeoJSONDto.class);

        assertEquals(OgcApiRecordsMultiPointDto.class, result.getClass());
        assertEquals("MultiPoint", result.getType());

        doTest("GeoJSON_geom_multipoint.json", OgcApiRecordsGeometryGeoJSONDto.class);
    }

    // can read/write a geojson MultiLineString
    @Test
    public void GeoJSON_geom_multilinestring() throws IOException {
        var result = (OgcApiRecordsMultiLineStringDto)
                doTest1("GeoJSON_geom_multilinestring.json", OgcApiRecordsGeometryGeoJSONDto.class);

        assertEquals(OgcApiRecordsMultiLineStringDto.class, result.getClass());
        assertEquals("MultiLineString", result.getType());

        doTest("GeoJSON_geom_multilinestring.json", OgcApiRecordsGeometryGeoJSONDto.class);
    }

    // can read/write a geojson MultiPolygon
    @Test
    public void GeoJSON_geom_multipolygon() throws IOException {
        var result = (OgcApiRecordsMultiPolygonDto)
                doTest1("GeoJSON_geom_multipolygon.json", OgcApiRecordsGeometryGeoJSONDto.class);

        assertEquals(OgcApiRecordsMultiPolygonDto.class, result.getClass());
        assertEquals("MultiPolygon", result.getType());

        doTest("GeoJSON_geom_multipolygon.json", OgcApiRecordsGeometryGeoJSONDto.class);
    }

    // can read/write a geojson GeometryCollection
    @Test
    public void GeoJSON_geom_geometrycollection() throws IOException {
        var result = (OgcApiRecordsGeometryCollectionDto)
                doTest1("GeoJSON_geom_geometrycollection.json", OgcApiRecordsGeometryGeoJSONDto.class);

        assertEquals(OgcApiRecordsGeometryCollectionDto.class, result.getClass());
        assertEquals("GeometryCollection", result.getType());

        doTest("GeoJSON_geom_geometrycollection.json", OgcApiRecordsGeometryGeoJSONDto.class);
    }

    // catalog with itemtype as "record" (single).
    // note: cannot test to json diffs since "record" (single) is converted to ["record"] (list).
    //       See catalog_1b for the full check
    // note: this also checks that "catalog" (for type) is allowed (lower case "c").
    @Test
    public void catalog_1() throws IOException {
        var result = (OgcApiRecordsCatalogDto) doTest1("catalog_1.json", OgcApiRecordsCatalogDto.class);

        assertEquals(OgcApiRecordsCatalogDto.class, result.getClass());
        assertEquals("catalog", result.getType().toString());
        assertEquals(1, result.getItemType().size());
        assertEquals("record", result.getItemType().get(0).toString());

        // doTest("catalog_1.json", OgcApiRecordsCatalogDto.class);
    }

    // see catalog_1
    // 1. this has itemtype as ["record"] (list - not single value)
    // 2. uses big-"C" Catalog
    // 3. re-ordered json so produces the in the same order
    @Test
    public void catalog_1b() throws IOException {
        var result = (OgcApiRecordsCatalogDto) doTest1("catalog_1b.json", OgcApiRecordsCatalogDto.class);

        assertEquals(OgcApiRecordsCatalogDto.class, result.getClass());
        assertEquals("Catalog", result.getType().toString());
        assertEquals(1, result.getItemType().size());
        assertEquals("record", result.getItemType().get(0).toString());

        doTest("catalog_1b.json", OgcApiRecordsCatalogDto.class);
    }
}
