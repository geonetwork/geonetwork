package org.geonetwork.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.geonetwork.editing.BatchEditsService;
import org.geonetwork.editing.model.BatchEditParameter;
import org.geonetwork.schemas.utility.Xml;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class MetadataBuilder {
  private final DataIngesterConfiguration dataIngesterConfiguration;
  private final BatchEditsService batchEditsService;

  private String buildForLayerProperties(
      String template,
      DataIngesterConfiguration.Resource.Property property,
      DatasetInfo datasetInfo) {
    Map<String, String> replacements = new HashMap<>();
    DatasetLayer datasetLayer = datasetInfo.getLayers().getFirst();
    switch (property.getName()) {
      case "name":
        replacements.put(
            "title",
            datasetLayer.getMetadata().getOrDefault("TITLE", datasetLayer.getName()).toString());
        break;
      case "latLonBoundingBox":
        if (!datasetLayer.getGeometryFields().isEmpty()) {
          List<BigDecimal> extent = datasetLayer.getGeometryFields().getFirst().getExtent();
          replacements.put("west", extent.get(0).toString());
          replacements.put("south", extent.get(1).toString());
          replacements.put("east", extent.get(2).toString());
          replacements.put("north", extent.get(3).toString());
        }
        break;
      case "spatialRepresentationType":
        replacements.put("datasetType", "vector");
        break;
      case "vectorSpatialRepresentation":
        if (!datasetLayer.getGeometryFields().isEmpty()) {
          replacements.put("geometryType", datasetLayer.getGeometryFields().getFirst().getType());
        }
        replacements.put("featureCount", datasetLayer.getFeatureCount().toString());
        break;
      case "distributionFormat":
        replacements.put("format", datasetInfo.getFormat());
        replacements.put("formatDescription", datasetInfo.getFormatDescription());
        break;
      case "featureCatalogue":
      case "featureType":
      case "featureTypeColumns":
        replacements.put("featureTypeName", datasetLayer.getName());
        break;
    }
    return buildWithPropertySubstitutions(template, replacements);
  }

  private String buildForLayerColumnProperties(
      String template,
      DataIngesterConfiguration.Resource.Property property,
      DatasetLayerField field) {
    Map<String, String> replacements = new HashMap<>();
    if ("featureTypeColumns".equals(property.getName())) {
      replacements.put("fieldName", field.getName());
      replacements.put("fieldType", field.getType());
    }
    return buildWithPropertySubstitutions(template, replacements);
  }

  private String buildWithPropertySubstitutions(String template, Map<String, String> replacements) {
    StringSubstitutor sub = new StringSubstitutor(replacements, "{{", "}}");
    String result = sub.replace(template);
    log.debug(replacements.keySet().toString());
    log.debug(replacements.values().toString());
    log.debug(result);
    return result;
  }

  public String buildMetadata(String uuid, DatasetInfo datasetInfo) {
    List<DataIngesterConfiguration.Resource.Property> properties =
        dataIngesterConfiguration.getResources().getFirst().getProperties();
    List<BatchEditParameter> edits = new ArrayList<>();
    properties.forEach(
        property -> {
          if (property
              .getContext()
              .equals(DataIngesterConfiguration.Resource.Property.Context.DatasetLayer.name())) {
            property
                .getOperations()
                .forEach(
                    operation -> {
                      if (operation.getSchema().equals("iso19115-3.2018")) {
                        edits.add(
                            new BatchEditParameter(
                                buildForLayerProperties(
                                    operation.getXpath(), property, datasetInfo),
                                String.format(
                                    "<%s>%s</%s>",
                                    operation.getOperation(),
                                    operation
                                            .getOperation()
                                            .equals(
                                                DataIngesterConfiguration.Resource.Property
                                                    .Operation.OperationType.gn_delete)
                                        ? ""
                                        : buildForLayerProperties(
                                            operation.getValue(), property, datasetInfo),
                                    operation.getOperation()),
                                operation.getCondition()));
                      }
                    });
          } else if (property
              .getContext()
              .equals(DataIngesterConfiguration.Resource.Property.Context.DatasetColumns.name())) {
            property
                .getOperations()
                .forEach(
                    operation -> {
                      datasetInfo
                          .getLayers()
                          .getFirst()
                          .getFields()
                          .forEach(
                              field -> {
                                if (operation.getSchema().equals("iso19115-3.2018")) {
                                  edits.add(
                                      new BatchEditParameter(
                                          buildForLayerProperties(
                                              operation.getXpath(), property, datasetInfo),
                                          String.format(
                                              "<%s>%s</%s>",
                                              operation.getOperation(),
                                              operation
                                                      .getOperation()
                                                      .equals(
                                                          DataIngesterConfiguration.Resource
                                                              .Property.Operation.OperationType
                                                              .gn_delete)
                                                  ? ""
                                                  : buildForLayerColumnProperties(
                                                      operation.getValue(), property, field),
                                              operation.getOperation()),
                                          buildForLayerColumnProperties(
                                              buildForLayerProperties(
                                                  operation.getCondition(), property, datasetInfo),
                                              property,
                                              field)));
                                }
                              });
                    });
          }
        });
    try {
      return Xml.getString(
          batchEditsService
              .applyBatchEdits(
                  new String[] {uuid},
                  null,
                  false,
                  edits.toArray(new BatchEditParameter[0]),
                  null,
                  true)
              .two());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
