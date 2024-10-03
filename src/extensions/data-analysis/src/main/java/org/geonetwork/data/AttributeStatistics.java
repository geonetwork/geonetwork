/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Singular;

@Data
@Builder
public class AttributeStatistics {
  @Getter
  @AllArgsConstructor
  public enum StatisticFields {
    MIN("min"),
    MAX("max"),
    ;
    final String name;
  }

  String name;

  @Singular Map<StatisticFields, Object> statistics;
}
