/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RasterCornerCoordinates implements Serializable {
    List<BigDecimal> upperLeft;
    List<BigDecimal> upperRight;
    List<BigDecimal> lowerLeft;
    List<BigDecimal> lowerRight;
    List<BigDecimal> center;
}
