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

@Data
@Builder
public class RasterCornerCoordinates {
  List<BigDecimal> upperLeft;
  List<BigDecimal> upperRight;
  List<BigDecimal> lowerLeft;
  List<BigDecimal> lowerRight;
  List<BigDecimal> center;
}
