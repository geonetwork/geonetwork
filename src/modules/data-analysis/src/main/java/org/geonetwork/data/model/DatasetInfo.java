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
import org.geonetwork.index.model.record.Link;

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
        resourceTitle.put("default", layer);

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
                        List.of(Codelist.builder().property("key", "vector").build()))
                .resourceTitle(resourceTitle)
                .formats(List.of(getFormat()))
                .links(List.of(link))
                .build();

        if (datasetLayer.getGeometryFields().size() > 0) {
            String crs = GeomUtil.parseCrsCode(
                    datasetLayer.getGeometryFields().get(0).getCrs());
            calculateIndexRecordGeomInfo(
                    indexRecord,
                    crs,
                    datasetLayer.getGeometryFields().getFirst().getExtent().stream()
                            .map(BigDecimal::doubleValue)
                            .collect(Collectors.toList()),
                    null);
        }

        List<FeatureType> featureTypeList = new ArrayList<>();
        datasetLayer.getFields().forEach(f -> {
            FeatureType ft = new FeatureType();
            ft.setTypeName(f.getType());
            ft.setCode(f.getName());

            featureTypeList.add(ft);
        });

        indexRecord.setFeatureTypes(featureTypeList);
        HashMap<String, List<Object>> additionalProperties = new HashMap<>();
        additionalProperties.put("featureCount", List.of(datasetLayer.getFeatureCount()));
        indexRecord.setOtherProperties(additionalProperties);

        return indexRecord;
    }
}
