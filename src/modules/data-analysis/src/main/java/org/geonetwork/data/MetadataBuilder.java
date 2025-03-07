/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.StringSubstitutor;
import org.geonetwork.data.model.BaseDataInfo;
import org.geonetwork.data.model.DatasetInfo;
import org.geonetwork.data.model.DatasetLayer;
import org.geonetwork.data.model.DatasetLayerField;
import org.geonetwork.data.model.RasterInfo;
import org.geonetwork.editing.BatchEditMode;
import org.geonetwork.editing.BatchEditsService;
import org.geonetwork.editing.model.BatchEditParameter;
import org.geonetwork.utility.legacy.xml.Xml;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class MetadataBuilder {
    private final DataIngesterConfiguration dataIngesterConfiguration;
    private final BatchEditsService batchEditsService;

    private String buildForVectorLayerProperties(
            String template, DataIngesterConfiguration.Resource.Property property, DatasetInfo datasetInfo) {
        Map<String, String> replacements = new HashMap<>();
        DatasetLayer datasetLayer = datasetInfo.getLayers().getFirst();
        switch (property.getName()) {
            case "name":
                replacements.put(
                        "title",
                        // Metadata title can be extracted from a WFS GetCapabilities layer information
                        datasetLayer
                                .getMetadata()
                                .getOrDefault("TITLE", datasetLayer.getName())
                                .toString());
                break;
            case "latLonBoundingBox":
                if (!datasetLayer.getGeometryFields().isEmpty()) {
                    List<BigDecimal> extent =
                            datasetLayer.getGeometryFields().getFirst().getExtent();

                    if (extent != null) {
                        replacements.put("west", extent.get(0).toString());
                        replacements.put("south", extent.get(1).toString());
                        replacements.put("east", extent.get(2).toString());
                        replacements.put("north", extent.get(3).toString());
                    }
                }
                break;
            case "spatialRepresentationType":
                replacements.put("datasetType", "vector");
                break;
            case "vectorSpatialRepresentation":
                if (!datasetLayer.getGeometryFields().isEmpty()) {
                    replacements.put(
                            "geometryType",
                            datasetLayer.getGeometryFields().getFirst().getType());
                }
                replacements.put("featureCount", datasetLayer.getFeatureCount().toString());
                break;
            case "distributionFormat":
                replacements.put("format", datasetInfo.getFormat());
                replacements.put("formatDescription", datasetInfo.getFormatDescription());
                break;
            case "distribution":
                replacements.put("format", datasetInfo.getFormat());
                replacements.put("datasetUrl", datasetInfo.getDescription());
                break;
            case "featureCatalogue":
            case "featureType":
            case "featureTypeColumns":
                replacements.put("featureTypeName", datasetLayer.getName());
                break;
            default:
                break;
        }
        return buildWithPropertySubstitutions(template, replacements);
    }

    private String buildForLayerColumnProperties(
            String template, DataIngesterConfiguration.Resource.Property property, DatasetLayerField field) {
        Map<String, String> replacements = new HashMap<>();
        if ("featureTypeColumns".equals(property.getName())) {
            replacements.put("fieldName", field.getName());
            replacements.put("fieldType", field.getType());
        }
        return buildWithPropertySubstitutions(template, replacements);
    }

    private String buildForRasterLayerProperties(
            String template, DataIngesterConfiguration.Resource.Property property, RasterInfo datasetInfo) {
        Map<String, String> replacements = new HashMap<>();
        switch (property.getName()) {
            case "name":
                replacements.put(
                        "title",
                        datasetInfo
                                .getMetadata()
                                .getOrDefault("TITLE", FilenameUtils.getBaseName(datasetInfo.getDescription()))
                                .toString());
                break;
            case "latLonBoundingBox":
                if (!datasetInfo.getWgs84Extent().isEmpty()) {
                    List<Double> wgs84Extent = datasetInfo.getWgs84Extent();

                    replacements.put("west", wgs84Extent.get(0).toString());
                    replacements.put("south", wgs84Extent.get(1).toString());
                    replacements.put("east", wgs84Extent.get(2).toString());
                    replacements.put("north", wgs84Extent.get(3).toString());
                }
                break;
            case "spatialRepresentationType":
                replacements.put("datasetType", "grid");
                break;
            case "rasterSpatialRepresentation":
                replacements.put(
                        "dimensionSizeX", String.valueOf(datasetInfo.getSize().get(0)));
                replacements.put(
                        "dimensionSizeY", String.valueOf(datasetInfo.getSize().get(1)));
                break;
            case "spatialResolution":
                if (datasetInfo.getGeoTransform() != null
                        && datasetInfo.getGeoTransform().size() == 6) {
                    replacements.put(
                            "spatialResolution",
                            String.valueOf(datasetInfo.getGeoTransform().get(1)));
                }
                break;
            case "distributionFormat":
                replacements.put("format", datasetInfo.getFormat());
                replacements.put("formatDescription", datasetInfo.getFormatDescription());
                break;
            case "distribution":
                replacements.put("format", datasetInfo.getFormat());
                replacements.put("datasetUrl", datasetInfo.getDescription());
                break;
            default:
                break;
        }
        return buildWithPropertySubstitutions(template, replacements);
    }

    public static String buildWithPropertySubstitutions(String template, Map<String, String> replacements) {
        replacements.forEach((key, value) -> replacements.put(key, StringEscapeUtils.escapeXml11(value)));
        StringSubstitutor sub = new StringSubstitutor(replacements, "{{", "}}");
        return sub.replace(template);
    }

    public String buildMetadata(String uuid, String schema, BaseDataInfo datasetInfo, BatchEditMode batchEditMode) {
        List<DataIngesterConfiguration.Resource.Property> properties =
                dataIngesterConfiguration.getResources().getFirst().getProperties();
        List<BatchEditParameter> edits = new ArrayList<>();

        if (datasetInfo instanceof DatasetInfo vectorDataset) {
            buildMetadataFromVectorDataset(schema, vectorDataset, properties, edits);
        } else if (datasetInfo instanceof RasterInfo rasterDataset) {
            buildMetadataFromRasterDataset(schema, rasterDataset, properties, edits);
        }
        try {
            log.atDebug().log(edits.toString());
            return Xml.getString(batchEditsService
                    .applyBatchEdits(
                            new String[] {uuid},
                            null,
                            false,
                            edits.toArray(new BatchEditParameter[0]),
                            null,
                            batchEditMode)
                    .two());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void buildMetadataFromVectorDataset(
            String schema,
            DatasetInfo datasetInfo,
            List<DataIngesterConfiguration.Resource.Property> properties,
            List<BatchEditParameter> edits) {
        properties.forEach(property -> {
            if (property.getContext().equals(DataIngesterConfiguration.Resource.Property.Context.DatasetLayer.name())) {
                property.getOperations().forEach(operation -> {
                    if (operation.getSchema().equals(schema)) {
                        edits.add(BatchEditParameter.builder()
                                .xpath(buildForVectorLayerProperties(operation.getXpath(), property, datasetInfo))
                                .value(String.format(
                                        "<%s>%s</%s>",
                                        operation.getOperation(),
                                        operation
                                                        .getOperation()
                                                        .equals(
                                                                DataIngesterConfiguration.Resource.Property.Operation
                                                                        .OperationType.gn_delete)
                                                ? ""
                                                : buildForVectorLayerProperties(
                                                        operation.getValue(), property, datasetInfo),
                                        operation.getOperation()))
                                .condition(
                                        buildForVectorLayerProperties(operation.getCondition(), property, datasetInfo))
                                .build());
                    }
                });
            } else if (property.getContext()
                    .equals(DataIngesterConfiguration.Resource.Property.Context.DatasetColumns.name())) {
                property.getOperations().forEach(operation -> {
                    datasetInfo.getLayers().getFirst().getFields().forEach(field -> {
                        if (operation.getSchema().equals(schema)) {
                            edits.add(BatchEditParameter.builder()
                                    .xpath(buildForVectorLayerProperties(operation.getXpath(), property, datasetInfo))
                                    .value(String.format(
                                            "<%s>%s</%s>",
                                            operation.getOperation(),
                                            operation
                                                            .getOperation()
                                                            .equals(
                                                                    DataIngesterConfiguration.Resource.Property
                                                                            .Operation.OperationType.gn_delete)
                                                    ? ""
                                                    : buildForLayerColumnProperties(
                                                            operation.getValue(), property, field),
                                            operation.getOperation()))
                                    .condition(buildForLayerColumnProperties(
                                            buildForVectorLayerProperties(
                                                    operation.getCondition(), property, datasetInfo),
                                            property,
                                            field))
                                    .build());
                        }
                    });
                });
            }
        });
    }

    private void buildMetadataFromRasterDataset(
            String schema,
            RasterInfo rasterInfo,
            List<DataIngesterConfiguration.Resource.Property> properties,
            List<BatchEditParameter> edits) {
        properties.forEach(property -> {
            if (property.getContext().equals(DataIngesterConfiguration.Resource.Property.Context.DatasetLayer.name())) {
                property.getOperations().forEach(operation -> {
                    if (operation.getSchema().equals(schema)) {
                        edits.add(BatchEditParameter.builder()
                                .xpath(buildForRasterLayerProperties(operation.getXpath(), property, rasterInfo))
                                .value(String.format(
                                        "<%s>%s</%s>",
                                        operation.getOperation(),
                                        operation
                                                        .getOperation()
                                                        .equals(
                                                                DataIngesterConfiguration.Resource.Property.Operation
                                                                        .OperationType.gn_delete)
                                                ? ""
                                                : buildForRasterLayerProperties(
                                                        operation.getValue(), property, rasterInfo),
                                        operation.getOperation()))
                                .condition(
                                        buildForRasterLayerProperties(operation.getCondition(), property, rasterInfo))
                                .build());
                    }
                });
            }
        });
    }
}
