/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geonetwork.index.model.record.DateRange;
import org.geonetwork.index.model.record.IndexRecord;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtentConvertTest {

  String jsonStr_poly1 = "[{\n" + "    \"type\": \"Polygon\",\n"
    + "    \"coordinates\": [ [[-17, -34],[51, -34],[51,38],[-17,38],[-17, -34]]]\n"
    + "}]";

  String jsonStr_point1 = "[{\"type\":\"Point\",\"coordinates\":[125,10]}]";
  String jsonStr_multipoly =
    "[{\"type\":\"MultiPolygon\",\"coordinates\":[[[[0,0],[10,0],[10,10],[0,10],[0,0]]],[[[20,20],[30,20],[30,30],[20,30],[20,20]]]]}]";

  @Test
  public void testBBOX_point() throws JsonProcessingException {
    var geom = fromString(jsonStr_point1);
    var bbox = ExtentConvert.BBox.build(geom[0]);

    assertEquals(125, bbox.getXmin());
    assertEquals(125, bbox.getXmax());
    assertEquals(10, bbox.getYmin());
    assertEquals(10, bbox.getYmax());

    var indexRecord = new IndexRecord();
    indexRecord.setGeometries(Arrays.asList(geom));
    var spatialExtent = ExtentConvert.convertSpatialExtent(indexRecord);

    assertEquals(1, spatialExtent.getBbox().size());
    assertEquals(4, spatialExtent.getBbox().get(0).size());
    assertEquals(bbox.getXmin(), spatialExtent.getBbox().get(0).get(0).doubleValue());
    assertEquals(bbox.getYmin(), spatialExtent.getBbox().get(0).get(1).doubleValue());
    assertEquals(bbox.getXmax(), spatialExtent.getBbox().get(0).get(2).doubleValue());
    assertEquals(bbox.getYmax(), spatialExtent.getBbox().get(0).get(3).doubleValue());
  }

  @Test
  public void testBBOX_poly() throws JsonProcessingException {
    var geom = fromString(jsonStr_poly1);
    var bbox = ExtentConvert.BBox.build(geom[0]);

    assertEquals(-17.0, bbox.getXmin());
    assertEquals(51.0, bbox.getXmax());
    assertEquals(-34.0, bbox.getYmin());
    assertEquals(38.0, bbox.getYmax());

    var indexRecord = new IndexRecord();
    indexRecord.setGeometries(Arrays.asList(geom));
    var spatialExtent = ExtentConvert.convertSpatialExtent(indexRecord);

    assertEquals(1, spatialExtent.getBbox().size());
    assertEquals(4, spatialExtent.getBbox().get(0).size());
    assertEquals(bbox.getXmin(), spatialExtent.getBbox().get(0).get(0).doubleValue());
    assertEquals(bbox.getYmin(), spatialExtent.getBbox().get(0).get(1).doubleValue());
    assertEquals(bbox.getXmax(), spatialExtent.getBbox().get(0).get(2).doubleValue());
    assertEquals(bbox.getYmax(), spatialExtent.getBbox().get(0).get(3).doubleValue());
  }

  @Test
  public void testBBOX_multipoly() throws JsonProcessingException {
    var geom = fromString(jsonStr_multipoly);
    var bbox = ExtentConvert.BBox.build(geom[0]);

    assertEquals(0, bbox.getXmin());
    assertEquals(30, bbox.getXmax());
    assertEquals(0, bbox.getYmin());
    assertEquals(30, bbox.getYmax());

    var indexRecord = new IndexRecord();
    indexRecord.setGeometries(Arrays.asList(geom));
    var spatialExtent = ExtentConvert.convertSpatialExtent(indexRecord);

    assertEquals(1, spatialExtent.getBbox().size());
    assertEquals(4, spatialExtent.getBbox().get(0).size());
    assertEquals(bbox.getXmin(), spatialExtent.getBbox().get(0).get(0).doubleValue());
    assertEquals(bbox.getYmin(), spatialExtent.getBbox().get(0).get(1).doubleValue());
    assertEquals(bbox.getXmax(), spatialExtent.getBbox().get(0).get(2).doubleValue());
    assertEquals(bbox.getYmax(), spatialExtent.getBbox().get(0).get(3).doubleValue());
  }

  public HashMap[] fromString(String json) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(json, HashMap[].class);
  }

  @Test
  public void testDateRange_compareDate() {
    var compare = ExtentConvert.compareDate("2000-07-19T21:45:00.000Z", "2006-01-01T12:29:00.000Z");
    assertTrue(compare < 0);
  }

  @Test
  public void testDateRange_computeDateRange_none() {
    var dateRange = ExtentConvert.computeDateRange(null);
    assertNull(dateRange);

    dateRange = ExtentConvert.computeDateRange(new ArrayList<>());
    assertNull(dateRange);
  }

  @Test
  public void testDateRange_computeDateRange_one() {
    var dr1 = new DateRange();
    dr1.setGte("2006-01-01T12:29:00.000Z");
    dr1.setLte("2008-01-08T12:29:00.000Z");

    var dateRange = ExtentConvert.computeDateRange(List.of(dr1));
    assertNotNull(dateRange);

    assertEquals(dr1.getGte(), dateRange.getGte());
    assertEquals(dr1.getLte(), dateRange.getLte());
  }

  @Test
  public void testDateRange_computeDateRange_multi() {
    var dr1 = new DateRange();
    dr1.setGte("2006-01-01T12:29:00.000Z");
    dr1.setLte("2008-01-08T12:29:00.000Z");

    var dr2 = new DateRange();
    dr2.setGte("2000-07-19T21:45:00.000Z");
    dr2.setLte("2000-07-19T21:45:00.000Z");

    var dateRange = ExtentConvert.computeDateRange(Arrays.asList(dr1, dr2));
    assertNotNull(dateRange);

    assertEquals(dr2.getGte(), dateRange.getGte());
    assertEquals(dr1.getLte(), dateRange.getLte());

    var indexRecord = new IndexRecord();
    indexRecord.setResourceTemporalExtentDateRange(Arrays.asList(dr1, dr2));
    var temporalextent = ExtentConvert.convertTemporalExtent(indexRecord);

    assertNotNull(temporalextent);
    assertEquals(1, temporalextent.getInterval().size());
    assertEquals(2, temporalextent.getInterval().get(0).size());
    assertEquals(
      Instant.parse(dateRange.getGte()),
      temporalextent.getInterval().get(0).get(0).toInstant());
    assertEquals(
      Instant.parse(dateRange.getLte()),
      temporalextent.getInterval().get(0).get(1).toInstant());
  }
}
