/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import org.geonetwork.utility.date.ISODate;
import org.springframework.http.HttpStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiError {
    private String timestamp;

    private HttpStatus status;
    private int statusCode;
    private String message;
    private List<String> errors;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
        this.errors = errors;
        this.timestamp = new ISODate().toString();
    }

    public ApiError(HttpStatus status, String message, String error) {
        this(status, message, Arrays.asList(error));
    }

    public ApiError(HttpStatus status, String message) {
        this(status, message, new ArrayList<>());
    }
}
