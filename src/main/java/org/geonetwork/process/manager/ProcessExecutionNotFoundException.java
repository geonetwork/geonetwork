package org.geonetwork.process.manager;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "process execution not found")
public class ProcessExecutionNotFoundException extends RuntimeException {
}
