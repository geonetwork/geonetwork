/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.geonetwork.index.model.record.DateRange;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsExtentSpatialDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsExtentTemporalDto;

/** handles converting the extent from the elastic json index to ogcapi-record format. */
public class ExtentConvert {

    /**
     * given an elastic json index record, create an ogcapi spatial extent (or null).
     *
     * @param indexRecord elastic json index record
     * @return ogcapi spatial extent (or null).
     */
    public static OgcApiRecordsExtentSpatialDto convertSpatialExtent(IndexRecord indexRecord) {
        var geometries = indexRecord.getGeometries();
        if (geometries == null || geometries.isEmpty()) {
            return null;
        }

        var bboxes = geometries.stream().map(x -> BBox.build(x)).toList();

        var bbox = BBox.join(bboxes);

        List<List<BigDecimal>> bbox_ogc = new ArrayList<List<BigDecimal>>();
        var bbox_ogc_inner = new ArrayList<BigDecimal>();
        bbox_ogc_inner.add(new BigDecimal(bbox.xmin));
        bbox_ogc_inner.add(new BigDecimal(bbox.ymin));
        bbox_ogc_inner.add(new BigDecimal(bbox.xmax));
        bbox_ogc_inner.add(new BigDecimal(bbox.ymax));
        bbox_ogc.add(bbox_ogc_inner);

        var result = new OgcApiRecordsExtentSpatialDto();
        result.setBbox(bbox_ogc);

        return result;
    }

    /**
     * given an elastic index record, create an ogcapi-records temporal extent.
     *
     * @param indexRecord elastic index record
     * @return ogcapi-records temporal extent
     */
    public static OgcApiRecordsExtentTemporalDto convertTemporalExtent(IndexRecord indexRecord) {
        var dateRanges = indexRecord.getResourceTemporalDateRange();
        if (dateRanges == null || dateRanges.isEmpty()) {
            dateRanges = indexRecord.getResourceTemporalExtentDateRange();
        }
        if (dateRanges == null || dateRanges.isEmpty()) {
            return null;
        }
        var daterange = computeDateRange(dateRanges);
        OffsetDateTime offset1 = null;
        OffsetDateTime offset2 = null;
        if (daterange.getGte() != null) {
            offset1 = OffsetDateTime.parse(daterange.getGte());
        }
        if (daterange.getLte() != null) {
            offset2 = OffsetDateTime.parse(daterange.getLte());
        }
        var result = new OgcApiRecordsExtentTemporalDto();
        result.addIntervalItem(Arrays.asList(offset1, offset2));

        return result;
    }

    /**
     * simple date compare
     *
     * @param d1 date1
     * @param d2 date2
     * @return comparison between the two
     */
    public static int compareDate(String d1, String d2) {
        var i1 = Instant.parse(d1);
        var i2 = Instant.parse(d2);
        return i1.compareTo(i2);
    }

    /**
     * given some date ranges, create a single date range that encompasses them all.
     *
     * @param dates list of dateranges
     * @return single date range that encompasses them all (or null)
     */
    public static DateRange computeDateRange(List<DateRange> dates) {
        if (dates == null || dates.isEmpty()) {
            return null;
        }
        if (dates.size() == 1) {
            return dates.get(0);
        }
        var dateRange = new DateRange();
        // Clone
        dateRange.setLte(dates.get(0).getLte());
        dateRange.setGte(dates.get(0).getGte());

        for (var dr : dates) {
            if (!isBlank(dr.getLte())) {
                if (compareDate(dateRange.getLte(), dr.getLte()) < 0) {
                    dateRange.setLte(dr.getLte());
                }
            }
            if (!isBlank(dr.getGte())) {
                if (compareDate(dateRange.getGte(), dr.getGte()) > 0) {
                    dateRange.setGte(dr.getGte());
                }
            }
        }
        return dateRange;
    }

    /** internal class just o make things easier for elastic/ogcapi geometries and bounding boxes. */
    @Getter
    @Setter
    public static class BBox {

        public double xmin;
        public double xmax;
        public double ymin;
        public double ymax;

        public BBox() {
            xmin = Double.MAX_VALUE;
            xmax = -Double.MAX_VALUE;
            ymin = Double.MAX_VALUE;
            ymax = -Double.MAX_VALUE;
        }

        public BBox(double xmin, double xmax, double ymin, double ymax) {
            this.xmin = xmin;
            this.xmax = xmax;
            this.ymin = ymin;
            this.ymax = ymax;
        }

        /**
         * merge 2 bboxes
         *
         * @param other other bbox
         * @return bbox that encloses both
         */
        public static BBox join(List other) {
            var result = new BBox();
            for (Object box : other) {
                result = result.join((BBox) box);
            }
            return result;
        }

        /**
         * Build BBOX from simple list of coordinates. first ordinate -> X 2nd ordinate -> Y
         *
         * @param listCoordinates - [ [1,2], [3,4], ..]
         * @return BBOX
         */
        @SuppressWarnings("unchecked")
        public static BBox buildFromSimpleListCoords(List listCoordinates) {
            var result = new BBox();
            List<List<Number>> coordinateList = (List<List<Number>>) listCoordinates;
            for (var coordinate : coordinateList) {
                if (coordinate.size() < 2) {
                    continue; // bad coordinate
                }
                var x = coordinate.get(0).doubleValue();
                var y = coordinate.get(1).doubleValue();
                result.xmin = Math.min(x, result.xmin);
                result.xmax = Math.max(x, result.xmax);
                result.ymin = Math.min(y, result.ymin);
                result.ymax = Math.max(y, result.ymax);
            }
            return result;
        }

        /**
         * calculate the bbox of a geojson geometry.
         *
         * <p>Example of a single-ring polygon;
         *
         * <p>{ "type": "Polygon", "coordinates": [ [ [-17, -34], [51, -34], [51, 38], [-17, 38], [-17, -34] ] ] }
         *
         * @param nestedListCoordinates either a list of list of Double, or a more deeply nested list.
         * @return bbox
         */
        @SuppressWarnings("unchecked")
        public static BBox build(List nestedListCoordinates) {
            if (nestedListCoordinates == null || nestedListCoordinates.isEmpty()) {
                return new BBox();
            }
            // is this a list of coordinates?
            if (!(nestedListCoordinates.get(0) instanceof List firstList)) {
                // single coordinate - i.e. point
                var list = new ArrayList<List<Number>>();
                list.add(nestedListCoordinates);
                return buildFromSimpleListCoords(list);
            }
            if (!(firstList.get(0) instanceof List secondList)) {
                return new BBox(); // bad!
            }
            if (secondList.get(0) instanceof Number) {
                return buildFromSimpleListCoords(firstList);
            }

            // ok, this is a nested list
            var items = nestedListCoordinates.stream().map(x -> build((List) x)).toList();
            return join(items);
        }

        /**
         * build a bbox from an GeoJson geometry node.
         *
         * @param map GeoJson node for the geometry
         * @return bbox that encloses it
         */
        public static BBox build(Map map) {
            if (map == null
                    || !map.containsKey("type")
                    || !map.containsKey("coordinates")
                    || !(map.get("coordinates") instanceof List coordinates)) {
                return null;
            }

            return build(coordinates);
        }

        /**
         * join 2 bboxes together
         *
         * @param other the other bbox
         * @return bbox that encloses the 2 bboxes.
         */
        public BBox join(BBox other) {
            return new BBox(
                    Math.min(xmin, other.xmin),
                    Math.max(xmax, other.xmax),
                    Math.min(ymin, other.ymin),
                    Math.max(ymax, other.ymax));
        }
    }
}
