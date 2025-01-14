/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.controllerexceptions;

import lombok.Getter;

@Getter
public class GenericOgcApiException extends Exception {
    int code;

    public GenericOgcApiException(String message) {
        super(message);
    }

    public GenericOgcApiException(int code, String message) {
        super(message);
        this.code = code;
    }
}
