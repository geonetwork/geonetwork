/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MetadataBuilder {
  DataIngesterConfiguration dataIngesterConfiguration;

  public void buildMetadata(DatasetInfo datasetInfo) {
    System.out.println(datasetInfo);
    // build metadata
  }
}
