/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.process.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class ProcessDetails {
  private String process;
  private Map<String, String> parameters = new HashMap<>();
}
