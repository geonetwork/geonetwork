/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatasetInfo implements Serializable {
  private String description;
  private String format;
  private String formatDescription;
  private List<DatasetLayer> layers;
  private Map<String, Object> metadata;
}
