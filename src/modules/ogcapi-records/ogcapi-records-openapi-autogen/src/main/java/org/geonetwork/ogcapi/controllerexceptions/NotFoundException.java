/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.controllerexceptions;

public class NotFoundException extends GenericOgcApiException {

    public NotFoundException(String message) {
        super(404, message);
    }
}
