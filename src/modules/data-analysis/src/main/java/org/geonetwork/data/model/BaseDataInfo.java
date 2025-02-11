/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.geonetwork.data.geom.GeomUtil;
import org.geonetwork.index.model.record.IndexRecord;

@Data
@SuperBuilder
public abstract class BaseDataInfo implements Serializable {
    String description;
    String format;
    String formatDescription;

    /**
     * Converts the BBOX coordinates from GDAL to WGS84 and updates IndexRecord with the WGS84 crs and WGS84 bbox.
     *
     * If the conversion fails, it stores the original information from GDAL.
     *
     * @param indexRecord IndexRecord information to updat.e
     * @param crs original GDAL dataset / raster crs code.
     * @param bboxCoordinates GDAL dataset / raster BBOX.
     */
    protected void calculateIndexRecordGeomInfo(IndexRecord indexRecord, String crs, List<Double> bboxCoordinates) {
        List<Double> wgs84Coordinates = GeomUtil.calculateWgs84Bbox(crs, bboxCoordinates);

        boolean useGeomInfoFromGdal = (wgs84Coordinates == null);

        if (!useGeomInfoFromGdal) {
            indexRecord.setCoordinateSystem(List.of("EPSG:4326"));
            List<String> wgs84Geom = new ArrayList<>();
            wgs84Geom.add(String.valueOf(wgs84Coordinates.get(0)));
            wgs84Geom.add(String.valueOf(wgs84Coordinates.get(1)));
            wgs84Geom.add(String.valueOf(wgs84Coordinates.get(2)));
            wgs84Geom.add(String.valueOf(wgs84Coordinates.get(3)));
            indexRecord.setGeometries(wgs84Geom);
        } else {
            indexRecord.setCoordinateSystem(List.of(crs));
            indexRecord.setGeometries(bboxCoordinates.stream()
                .map(d -> d.toString())
                .collect(Collectors.toList()));
        }
    }
}
