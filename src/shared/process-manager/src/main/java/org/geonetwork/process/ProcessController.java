/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.process;

import java.util.List;
import org.geonetwork.process.manager.IProcess;
import org.geonetwork.process.manager.ProcessManager;
import org.geonetwork.process.model.ProcessDetails;
import org.geonetwork.process.model.ProcessReport;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/process")
public class ProcessController {
  private final ProcessManager processManager;

  public ProcessController(final ProcessManager processManager) {
    this.processManager = processManager;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('Administrator')")
  public List<IProcess> list() {
    return processManager.getProcesses();
  }

  @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('Administrator')")
  public ResponseEntity<Long> execute(@RequestBody ProcessDetails processDetails)
      throws JobInstanceAlreadyCompleteException,
          JobExecutionAlreadyRunningException,
          JobParametersInvalidException,
          JobRestartException {
    IProcess process = processManager.getProcess(processDetails.getProcess());
    if (process == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    Long processExecutionId = processManager.execute(process, processDetails);

    return new ResponseEntity<>(processExecutionId, HttpStatus.OK);
  }

  @GetMapping(path = "/status/{executionJobId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('Administrator')")
  public ResponseEntity<BatchStatus> analysisExecutionStatus(@PathVariable Long executionJobId) {
    return new ResponseEntity<>(
        processManager.processExecutionStatus(executionJobId), HttpStatus.OK);
  }

  @GetMapping(path = "/result/{executionJobId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('Administrator')")
  public ResponseEntity<ProcessReport> analysisExecutionResult(@PathVariable Long executionJobId) {
    return new ResponseEntity<>(
        processManager.processExecutionResult(executionJobId), HttpStatus.OK);
  }
}
