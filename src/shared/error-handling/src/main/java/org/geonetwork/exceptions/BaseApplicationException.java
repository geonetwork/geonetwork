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

    public BaseApplicationException(String errorCode) {
        this(errorCode, new Object[] {}, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public BaseApplicationException(String errorCode, Object[] args) {
        this(errorCode, args, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public BaseApplicationException(String errorCode, Object[] args, HttpStatus httpStatus) {
        super(errorCode);
        this.errorCode = errorCode;
        this.args = args;
        this.httpStatus = httpStatus;
    }
}
