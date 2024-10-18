/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatasetLayer implements Serializable {
  private String name;
  private String fidColumnName;
  private BigDecimal featureCount;
  private List<DatasetLayerField> fields;
  private List<DatasetLayerGeomField> geometryFields;
  private Map<String, Object> metadata;
}
