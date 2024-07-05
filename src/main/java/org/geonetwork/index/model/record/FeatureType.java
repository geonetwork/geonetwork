/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

/** Feature type. */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class FeatureType {
  String typeName;
  String definition;
  String code;
  String isAbstract;
  String aliases;

  @Singular("attributeTable")
  List<AttributeTable> attributeTable;
}
