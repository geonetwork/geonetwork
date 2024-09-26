/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.process.tasks;

import java.util.Optional;
import org.geonetwork.data.DataAnalyzerException;
import org.geonetwork.data.DatasetInfo;
import org.geonetwork.data.gdal.GdalDataAnalyzer;
import org.geonetwork.process.model.DataAnalysisProcessReport;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class DatasetAnalysisTasklet implements Tasklet {
  private GdalDataAnalyzer dataAnalyzer;

  public DatasetAnalysisTasklet(GdalDataAnalyzer dataAnalyzer) {
    this.dataAnalyzer = dataAnalyzer;
  }

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    Thread.sleep(15000);
    JobParameters parameters = contribution.getStepExecution().getJobExecution().getJobParameters();

    DataAnalysisProcessReport dataAnalysisProcessReport;

    try {
      Optional<DatasetInfo> layerProperties =
          dataAnalyzer.getLayerProperties(
              parameters.getString("datasource"), parameters.getString("layer"));

      dataAnalysisProcessReport =
          DataAnalysisProcessReport.builder()
              .status("COMPLETED")
              .result(layerProperties.get())
              .build();

    } catch (DataAnalyzerException ex) {
      dataAnalysisProcessReport =
          DataAnalysisProcessReport.builder().status("ERROR").errorMessage(ex.getMessage()).build();
    }

    // set variable in JobExecutionContext
    chunkContext
        .getStepContext()
        .getStepExecution()
        .getJobExecution()
        .getExecutionContext()
        .put("result", dataAnalysisProcessReport);

    return RepeatStatus.FINISHED;
  }
}
