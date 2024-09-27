/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.geonetwork.editing.BatchEditsService;
import org.geonetwork.editing.model.BatchEditParameter;
import org.geonetwork.schemas.utility.Xml;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MetadataBuilder {
  DataIngesterConfiguration dataIngesterConfiguration;
  BatchEditsService batchEditsService;

  private String buildPropertyValue(
      String template,
      DataIngesterConfiguration.Resource.Property property,
      DatasetInfo datasetInfo) {

    Map<String, String> replacements = new HashMap<>();
    if ("DatasetLayer".equals(property.getContext())) {
      DatasetLayer datasetLayer = datasetInfo.getLayers().getFirst();
      switch (property.getName()) {
        case "name":
          replacements.put(
              "title",
              datasetLayer.getMetadata().getOrDefault("TITLE", datasetLayer.getName()).toString());
          break;
        case "latLonBoundingBox":
          if (!datasetLayer.getGeometryFields().isEmpty()) {
            replacements.put(
                "west", datasetLayer.getGeometryFields().getFirst().getExtent().get(0).toString());
            replacements.put(
                "south", datasetLayer.getGeometryFields().getFirst().getExtent().get(1).toString());
            replacements.put(
                "east", datasetLayer.getGeometryFields().getFirst().getExtent().get(2).toString());
            replacements.put(
                "north", datasetLayer.getGeometryFields().getFirst().getExtent().get(3).toString());
          }
          break;
        case "spatialRepresentationType":
          replacements.put("datasetType", "vector"); // TODO: grid, tabular
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
      }
    }

    // TODO: An empty replacement should not be inserted?
    StringSubstitutor sub = new StringSubstitutor(replacements, "{{", "}}");
    return sub.replace(template);
  }

  public String buildMetadata(String uuid, DatasetInfo datasetInfo) {
    // TODO: We could pass a list of properties to set ? eg. update only latLonBoundingBox
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
                                operation.getXpath(),
                                String.format(
                                    "<%s>%s</%s>",
                                    operation.getOperation(),
                                    operation
                                            .getOperation()
                                            .equals(
                                                DataIngesterConfiguration.Resource.Property
                                                    .Operation.OperationType.gn_delete)
                                        ? ""
                                        : buildPropertyValue(
                                            operation.getValue(), property, datasetInfo),
                                    operation.getOperation()),
                                operation.getCondition()));
                      }
                    });
          }
        });

    try {
      String result =
          Xml.getString(
              batchEditsService
                  .applyBatchEdits(
                      List.of(uuid).toArray(String[]::new),
                      null,
                      false,
                      edits.toArray(BatchEditParameter[]::new),
                      null,
                      true)
                  .two());
      System.out.println(result);
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
