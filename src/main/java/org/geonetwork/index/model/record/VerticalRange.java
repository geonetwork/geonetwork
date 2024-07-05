/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import lombok.Data;

/** Vertical range. */
@Data
public class VerticalRange {
  private Integer gte;
  private Integer lte;
  private String unit;
}
