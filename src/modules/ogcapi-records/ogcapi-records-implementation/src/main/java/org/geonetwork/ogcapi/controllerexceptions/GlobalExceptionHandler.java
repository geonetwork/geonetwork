/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.controllerexceptions;

import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * Global exception handling - will wrap exceptions in the OgcApiRecordsExceptionDto
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handle UndeclaredThrowableException, which occur from @SneakyThrows. Actual exception is inside
   * UndeclaredThrowableException
   *
   * @param e UndeclaredThrowableException exception (server error)
   * @return ResponseEntity<OgcApiRecordsExceptionDto> with message and status code 500
   */
  @ExceptionHandler(value = UndeclaredThrowableException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<OgcApiRecordsExceptionDto> handleException(UndeclaredThrowableException e) {
    Throwable ee = e;
    if (e.getCause() != null) {
      ee = e.getCause();
    }
    var isGeneric = GenericOgcApiException.class.isAssignableFrom(ee.getClass());
    if (isGeneric) {
      return handleException((GenericOgcApiException) ee);
    }
    return handleException((Exception) ee);
  }

  @ExceptionHandler(value = GenericOgcApiException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<OgcApiRecordsExceptionDto> handleException(GenericOgcApiException e) {
    OgcApiRecordsExceptionDto exception =
      new OgcApiRecordsExceptionDto().code(e.getCode() + "").description(e.getMessage());
    return new ResponseEntity<OgcApiRecordsExceptionDto>(exception, HttpStatusCode.valueOf(e.getCode()));
  }

  @ExceptionHandler(value = Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<OgcApiRecordsExceptionDto> handleException(Exception e) {
    OgcApiRecordsExceptionDto exception =
      new OgcApiRecordsExceptionDto().code("500").description(e.getMessage());
    return new ResponseEntity<OgcApiRecordsExceptionDto>(exception, HttpStatusCode.valueOf(500));
  }
}
