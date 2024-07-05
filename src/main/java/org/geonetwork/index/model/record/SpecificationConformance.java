/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/** Specification conformance. */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SpecificationConformance {

  private String link;
  private String title;
  private String explanation;
  private String date;
  private String pass;
}
