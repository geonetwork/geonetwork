/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataFormat {
  String name;
  String dataType;
  String rwFlag;
  String description;
}
