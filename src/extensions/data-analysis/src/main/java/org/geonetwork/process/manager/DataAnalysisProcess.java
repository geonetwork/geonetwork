/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.process.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DataAnalysisProcess implements IProcess {

  private final ApplicationContext applicationContext;

  public DataAnalysisProcess(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public String getName() {
    return "dataAnalysisProcess";
  }

  @Override
  public List<ProcessParameter> getParameters() {
    List<ProcessParameter> params = new ArrayList<ProcessParameter>();
    params.add(ProcessParameter.builder().name("datasource").type("String").build());
    params.add(ProcessParameter.builder().name("layer").type("String").build());

    return params;
  }

  @Override
  public Job getJob() {
    return (Job) applicationContext.getBean(getName() + "Job");
  }
}
