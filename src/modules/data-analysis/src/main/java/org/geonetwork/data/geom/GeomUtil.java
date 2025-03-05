/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.geom;

import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;

public class GeomUtil {

    public static final int NUMBER_OF_DECIMALS_IN_WGS84 = 6;
    public static final String EPSG_4326 = "EPSG:4326";

    private GeomUtil() {
        // Don't allow to instantiate it
    }

    /**
     * Parses the EPSG crs code from the crs definition from GDAL.
     *
     * <p>If the code can not be parsed, returns EPSG:4326
     *
     * @param crs crs definition from GDAL.
     * @return EPSG crs code.
     */
    public static String parseCrsCode(String crs) {
        Pattern crsPattern = Pattern.compile("[\\s\\S.]*\\\"EPSG\\\",([0-9]*).*");
        Matcher m = crsPattern.matcher(crs);
        if (m.find()) {
            return "EPSG:" + m.group(1);
        }

        return EPSG_4326;
    }

    /**
     * Converts the bboxCoordinates to WGS84 from their original crs.
     *
     * @param crs BBOX coordinates original crs code.
     * @param bboxCoordinates BBOX coordinates list (west, south, east, north).
     * @return Coordinates transformed to WGS84 or null if an error occurs in the conversion.
     */
    public static List<Double> calculateWgs84Bbox(String crs, List<Double> bboxCoordinates) {
        boolean wgs84Crs = crs.equals(EPSG_4326);

        if (!wgs84Crs) {
            try {
                CoordinateReferenceSystem mapCRS = CRS.decode(crs, true);

                double west = bboxCoordinates.get(0);
                double south = bboxCoordinates.get(1);
                double east = bboxCoordinates.get(2);
                double north = bboxCoordinates.get(3);
                ReferencedEnvelope bbox = new ReferencedEnvelope(west, east, south, north, mapCRS);

                ReferencedEnvelope wgs84BBox = bbox.transform(WGS84, true);

                List<Double> wgs84Geom = new ArrayList<>();
                Stream.of(wgs84BBox.getMinX(), wgs84BBox.getMinY(), wgs84BBox.getMaxX(), wgs84BBox.getMaxY())
                        .map(d -> BigDecimal.valueOf(d)
                                .setScale(NUMBER_OF_DECIMALS_IN_WGS84, RoundingMode.HALF_UP)
                                .doubleValue())
                        .forEach(wgs84Geom::add);

                return wgs84Geom;
            } catch (TransformException | FactoryException e) {
                return null;
            }
        }

        return bboxCoordinates;
    }
}
