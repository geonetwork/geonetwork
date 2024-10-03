/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.process.manager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import org.springframework.batch.core.Job;

public interface IProcess {
  String getName();

  List<ProcessParameter> getParameters();

  /** Spring Batch */
  @JsonIgnore
  Job getJob();
}
