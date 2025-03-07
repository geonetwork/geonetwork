/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.data.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.geonetwork.data.geom.GeomUtil;
import org.geonetwork.index.model.record.Codelist;
import org.geonetwork.index.model.record.FeatureType;
import org.geonetwork.index.model.record.IndexRecord;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DatasetInfo extends BaseDataInfo {
    @Builder.Default
    private DataType dataType = DataType.VECTOR;

    private List<DatasetLayer> layers;
    private Map<String, Object> metadata;

    public IndexRecord toIndexRecord(String datasource, String layer) {
        DatasetLayer datasetLayer = getLayers().get(0);

        Map<String, String> resourceTitle = new HashMap<>();
        resourceTitle.put("default", buildTitle(layer));

        var link = buildDatasourceLink(datasource);

        IndexRecord.IndexRecordBuilder indexRecord = IndexRecord.builder()
                .codelist(
                        "cl_spatialRepresentationType",
                        List.of(Codelist.builder().property("key", "vector").build()))
                .resourceTitle(resourceTitle)
                .formats(List.of(getFormat()))
                .links(List.of(link));

        if (!datasetLayer.getGeometryFields().isEmpty()) {
            String crs = GeomUtil.parseCrsCode(
                    datasetLayer.getGeometryFields().get(0).getCrs());

            if (datasetLayer.getGeometryFields().getFirst().getExtent() != null) {
                calculateIndexRecordGeomInfo(
                        indexRecord,
                        crs,
                        datasetLayer.getGeometryFields().getFirst().getExtent().stream()
                                .map(BigDecimal::doubleValue)
                                .collect(Collectors.toList()),
                        null);
            }

            indexRecord.codelist(
                    "cl_geometricObjectType",
                    List.of(Codelist.builder()
                            .property(
                                    "key",
                                    datasetLayer.getGeometryFields().getFirst().getType())
                            .build()));
        }

        List<FeatureType> featureTypeList = new ArrayList<>();
        datasetLayer.getFields().forEach(f -> {
            FeatureType ft = new FeatureType();
            ft.setTypeName(f.getType());
            ft.setCode(f.getName());

            featureTypeList.add(ft);
        });

        indexRecord.featureTypes(featureTypeList);
        HashMap<String, List<Object>> additionalProperties = new HashMap<>();
        additionalProperties.put("featureCount", List.of(datasetLayer.getFeatureCount()));
        indexRecord.otherProperties(additionalProperties);

        return indexRecord.build();
    }

    /** Remove common special characters from layer name or filename by space. */
    public static String buildTitle(String title) {
        return title.replaceAll("[_\\-\\:]", " ");
    }
}
