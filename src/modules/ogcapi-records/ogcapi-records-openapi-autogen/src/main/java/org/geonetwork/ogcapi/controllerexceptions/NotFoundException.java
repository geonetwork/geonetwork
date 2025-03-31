/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.controllerexceptions;

public class NotFoundException extends GenericOgcApiException {

    public NotFoundException(String message) {
        super(404, message);
    }
}
