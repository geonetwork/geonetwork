/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.model;

import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.data.geom.GeomUtil;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.index.model.record.Link;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

@Data
@SuperBuilder
@Slf4j
public abstract class BaseDataInfo implements Serializable {
    String description;
    String format;
    String formatDescription;

    protected void calculateIndexRecordGeomInfo(
            IndexRecord.IndexRecordBuilder indexRecord,
            String crs,
            List<Double> wgs84Coordinates,
            RasterCornerCoordinates rasterCornerCoordinates) {
        indexRecord.coordinateSystem(List.of(crs));

        if (rasterCornerCoordinates != null) {
            GeometryFactory geometryFactory = new GeometryFactory();
            List<Coordinate> coordinates = List.of(
                            rasterCornerCoordinates.getLowerLeft(),
                            rasterCornerCoordinates.getLowerRight(),
                            rasterCornerCoordinates.getUpperRight(),
                            rasterCornerCoordinates.getUpperLeft(),
                            rasterCornerCoordinates.getLowerLeft())
                    .stream()
                    .map(c -> new Coordinate(
                            c.getFirst().doubleValue(), c.getLast().doubleValue()))
                    .toList();
            Polygon rasterCoordinatesPolygon = geometryFactory.createPolygon(coordinates.toArray(new Coordinate[0]));
            Geometry rasterCoordinatesGeometry = transformGeometry(crs, rasterCoordinatesPolygon);
            if (rasterCoordinatesGeometry != null) {
                indexRecord.shapes(List.of(geometryToJson(rasterCoordinatesGeometry)));
            }
            if (wgs84Coordinates == null) {
                wgs84Coordinates = calculateWgs84Coordinates(crs, coordinates);
            }
        }

        if (wgs84Coordinates != null) {
            indexRecord.geometries(convertToStringList(wgs84Coordinates));
        }
    }

    private Geometry transformGeometry(String crs, Polygon rasterCoordinatesPolygon) {
        if ("EPSG:4326".equals(crs)) {
            return rasterCoordinatesPolygon;
        }
        try {
            CoordinateReferenceSystem datasourceCrs = CRS.decode(crs, false);
            MathTransform transform = CRS.findMathTransform(datasourceCrs, WGS84, true);
            return JTS.transform(rasterCoordinatesPolygon, transform);
        } catch (FactoryException | TransformException e) {
            log.atWarn().log(String.format("Error reprojecting raster coordinates: %s", e.getMessage()));
        }
        return null;
    }

    private String geometryToJson(Geometry geometry) {
        GeometryJSON g = new GeometryJSON();
        ByteArrayOutputStream shape = new ByteArrayOutputStream();
        try {
            g.write(geometry, shape);
        } catch (IOException e) {
            log.atWarn().log(String.format("Error converting raster coordinates to JSON: %s", e.getMessage()));
        }
        return shape.toString(StandardCharsets.UTF_8);
    }

    private List<Double> calculateWgs84Coordinates(String crs, List<Coordinate> coordinates) {
        List<Double> bboxCoordinates = coordinates.stream()
                .map(c -> List.of(c.getX(), c.getY()))
                .flatMap(List::stream)
                .toList();
        if ("EPSG:4326".equals(crs)) {
            return bboxCoordinates;
        }
        return GeomUtil.calculateWgs84Bbox(crs, bboxCoordinates);
    }

    private List<String> convertToStringList(List<Double> coordinates) {
        List<String> stringList = new ArrayList<>();
        for (Double coordinate : coordinates) {
            stringList.add(String.valueOf(coordinate));
        }
        return stringList;
    }

    protected Link buildDatasourceLink(String datasource) {
        Link link = new Link();
        Map<String, String> linkName = new HashMap<>();
        linkName.put("default", "source");
        link.setName(linkName);

        Map<String, String> linkUrl = new HashMap<>();
        linkUrl.put("default", datasource);
        link.setUrl(linkUrl);
        return link;
    }
}
