/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseApplicationException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final Object[] args;

    public BaseApplicationException(String errorMessage) {
        this("", new Object[] {}, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public BaseApplicationException(String errorMessage, HttpStatus httpStatus) {
        this("", new Object[] {}, errorMessage, httpStatus);
    }

    public BaseApplicationException(String errorCode, Object[] args) {
        this(errorCode, args, "", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public BaseApplicationException(String errorCode, Object[] args, HttpStatus httpStatus) {
        this(errorCode, args, "", httpStatus);
    }

    private BaseApplicationException(String errorCode, Object[] args, String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.args = args;
        this.httpStatus = httpStatus;
    }
}
