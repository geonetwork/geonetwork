/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data;

import org.geonetwork.exceptions.BaseApplicationException;
import org.springframework.http.HttpStatus;

public class DataAnalyzerException extends BaseApplicationException {
    public DataAnalyzerException(String errorCode) {
        super(errorCode);
    }

    public DataAnalyzerException(String errorCode, Object[] args) {
        super(errorCode, args);
    }

    public DataAnalyzerException(String errorCode, Object[] args, HttpStatus httpStatus) {
        super(errorCode, args, httpStatus);
    }
}
