/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.data;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DatasetLayer {
  private String name;
  private String fidColumnName;
  private BigDecimal featureCount;
  private List<DatasetLayerField> fields;
  private List<DatasetLayerGeomField> geometryFields;
  private Map<String, Object> metadata;
}
