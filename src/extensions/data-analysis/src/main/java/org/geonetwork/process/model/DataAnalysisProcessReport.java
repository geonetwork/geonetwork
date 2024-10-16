/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.process.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.geonetwork.data.model.DatasetInfo;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DataAnalysisProcessReport extends ProcessReport {
  private DatasetInfo result;
}
