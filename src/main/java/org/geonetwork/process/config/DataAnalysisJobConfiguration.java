/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.process.config;

import org.geonetwork.data.gdal.GdalDataAnalyzer;
import org.geonetwork.process.manager.DataAnalysisProcess;
import org.geonetwork.process.tasks.DatasetAnalysisTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

/** Spring Batch configuration for data analysis jobs. */
@Configuration
public class DataAnalysisJobConfiguration extends DefaultBatchConfiguration {

  @Bean
  public Job dataAnalysisProcessJob(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      GdalDataAnalyzer dataAnalyzer,
      DataAnalysisProcess dataAnalysisProcess) {
    return new JobBuilder(dataAnalysisProcess.getName() + "Job", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(process(jobRepository, transactionManager, dataAnalyzer, dataAnalysisProcess))
        .build();
  }

  @Bean
  protected Step process(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      GdalDataAnalyzer dataAnalyzer,
      DataAnalysisProcess dataAnalysisProcess) {
    return new StepBuilder(dataAnalysisProcess + "Step", jobRepository)
        .tasklet(datasetAnalysisTasklet(dataAnalyzer), transactionManager)
        .taskExecutor(getTaskExecutor())
        .build();
  }

  @Bean
  public DatasetAnalysisTasklet datasetAnalysisTasklet(GdalDataAnalyzer dataAnalyzer) {
    return new DatasetAnalysisTasklet(dataAnalyzer);
  }

  @Override
  protected TaskExecutor getTaskExecutor() {
    // Run tasks asynch
    return new SimpleAsyncTaskExecutor();
  }
}
