/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class RasterInfo {
  private String description;
  private String type;
  private String crs;
  private List<Double> wgs84Extent;
  private RasterCornerCoordinates rasterCornerCoordinates;
  private Map<String, Object> metadata;
  private List<Integer> size;
  private List<RasterBand> bands;
}
