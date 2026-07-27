/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.controllerexceptions;

/**
 * Thrown when a request contains a parameter value that is not supported (e.g. an unknown sortby/queryable property).
 * Unchecked so it can propagate through functional interfaces (e.g. Elasticsearch query builder lambdas) that don't
 * declare checked exceptions.
 */
public class InvalidParameterException extends RuntimeException {

    public InvalidParameterException(String message) {
        super(message);
    }
}
