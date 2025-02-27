/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.data.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.apache.commons.io.FilenameUtils;
import org.geonetwork.data.geom.GeomUtil;
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
    // https://gdal.org/en/stable/tutorials/geotransforms_tut.html
    private List<BigDecimal> geoTransform;
    private List<RasterBand> bands;

    public IndexRecord toIndexRecord(String datasource, String layer) {
        Map<String, String> resourceTitle = new HashMap<>();
        resourceTitle.put("default", FilenameUtils.getBaseName(datasource));

        Link link = new Link();
        Map<String, String> linkName = new HashMap<>();
        linkName.put("default", "source");
        link.setName(linkName);

        Map<String, String> linkUrl = new HashMap<>();
        linkUrl.put("default", datasource);
        link.setUrl(linkUrl);

        IndexRecord indexRecord = IndexRecord.builder()
                .codelist(
                        "cl_spatialRepresentationType",
                        List.of(Codelist.builder().property("key", "grid").build()))
                .resourceTitle(resourceTitle)
                .formats(List.of(getFormat()))
                .links(List.of(link))
                .build();

        String crs = GeomUtil.parseCrsCode(getCrs());
        calculateIndexRecordGeomInfo(indexRecord, crs, getWgs84Extent(), getRasterCornerCoordinates());

        if (getGeoTransform() != null && getGeoTransform().size() == 6) {
            indexRecord.setResolutionDistance(Stream.of(getGeoTransform().get(1))
                    .map(BigDecimal::toString)
                    .toList());
        }

        HashMap<String, List<Object>> additionalProperties = new HashMap<>();
        additionalProperties.put("dimensionSizeX", List.of(getSize().get(0)));
        additionalProperties.put("dimensionSizeY", List.of(getSize().get(1)));
        additionalProperties.put("dimensionsCount", List.of(getSize().size()));
        indexRecord.setOtherProperties(additionalProperties);

        return indexRecord;
    }
}
