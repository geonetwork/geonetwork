/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.process.manager;

import java.util.List;
import org.geonetwork.process.model.ProcessDetails;
import org.geonetwork.process.model.ProcessReport;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Component;

@Component
public class ProcessManager {
    private final List<IProcess> processes;
    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;

    public ProcessManager(
            final List<IProcess> processes, final JobLauncher jobLauncher, final JobExplorer jobExplorer) {
        this.processes = processes;
        this.jobLauncher = jobLauncher;
        this.jobExplorer = jobExplorer;
    }

    /** Gets a process by name. */
    public IProcess getProcess(String processName) {
        IProcess process = processes.stream()
                .filter(p -> p.getName().equals(processName))
                .findFirst()
                .orElse(null);

        if (process == null) {
            throw new ProcessNotFoundException();
        }

        return process;
    }

    /** Gets the list of processes available. */
    public List<IProcess> getProcesses() {
        return processes;
    }

    /** Executes a process with the provided parameters and returns the Spring Batch execution identifier. */
    public Long execute(IProcess process, ProcessDetails processDetails)
            throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
                    JobParametersInvalidException, JobRestartException {
        Job job = process.getJob();

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder(jobExplorer).getNextJobParameters(job);

        processDetails.getParameters().forEach((k, v) -> jobParametersBuilder.addString(k, v.toString()));

        JobParameters jobParameters = jobParametersBuilder.toJobParameters();
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);

        return jobExecution.getId();
    }

    /** Returns a process execution status, using the process execution identifier. */
    public BatchStatus processExecutionStatus(Long executionJobId) throws ProcessExecutionNotFoundException {
        JobExecution jobExecution = jobExplorer.getJobExecution(executionJobId);
        if (jobExecution == null) {
            throw new ProcessExecutionNotFoundException();
        }

        return jobExecution.getStatus();
    }

    /**
     * Returns a report of a process execution, using the process execution identifier.
     *
     * <p>The process should be in a COMPLETED status, otherwise returns null.
     */
    public ProcessReport processExecutionResult(Long executionJobId) {
        JobExecution jobExecution = jobExplorer.getJobExecution(executionJobId);

        if (jobExecution == null) {
            throw new ProcessExecutionNotFoundException();
        }

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            return (ProcessReport) jobExecution.getExecutionContext().get("result");
        } else {
            // TODO: Throw proper exception
            return null;
        }
    }
}
