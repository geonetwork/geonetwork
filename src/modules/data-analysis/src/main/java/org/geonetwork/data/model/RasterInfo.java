/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.data.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.geonetwork.index.model.record.Codelist;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.index.model.record.Link;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class RasterInfo extends BaseDataInfo {
    @Builder.Default
    private DataType dataType = DataType.RASTER;

    private String crs;
    private List<Double> wgs84Extent;
    private RasterCornerCoordinates rasterCornerCoordinates;
    private Map<String, Object> metadata;
    private List<Integer> size;
    private List<RasterBand> bands;

    public IndexRecord toIndexRecord(String datasource, String layer) {
        Map<String, String> resourceTitle = new HashMap<>();
        resourceTitle.put("default", layer);

        Link link = new Link();
        Map<String, String> linkName = new HashMap<>();
        linkName.put("default", "source");
        link.setName(linkName);

        Map<String, String> linkUrl = new HashMap<>();
        linkUrl.put("default", datasource);
        link.setUrl(linkUrl);

        IndexRecord indexRecord = IndexRecord.builder().codelist("cl_spatialRepresentationType",
                List.of(Codelist.builder().property("key", "grid").build()))
            .resourceTitle(resourceTitle)
            .formats(List.of(getFormat()))
            .links(List.of(link))
            .build();


        String crs = getCrs();
        Pattern crsPattern = Pattern.compile("[\\s\\S.]*\\\"EPSG\\\",([0-9]*).*");
        Matcher m = crsPattern.matcher(crs);
        if (m.find()) {
            crs = "EPSG:" + m.group(1);
        }
        indexRecord.setCoordinateSystem(List.of(crs));
        indexRecord.setGeometries(getWgs84Extent().stream().map(c -> c.toString()).collect(Collectors.toList()));

        indexRecord.handleUnrecognizedField("dimensionSizeX", getSize().get(0));
        indexRecord.handleUnrecognizedField("dimensionSizeY", getSize().get(1));

        return indexRecord;
    }
}
