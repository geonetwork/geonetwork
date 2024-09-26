/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.process.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.geonetwork.data.DatasetInfo;

@Data
@SuperBuilder
public class DataAnalysisProcessReport extends ProcessReport {
  private DatasetInfo result;
}
