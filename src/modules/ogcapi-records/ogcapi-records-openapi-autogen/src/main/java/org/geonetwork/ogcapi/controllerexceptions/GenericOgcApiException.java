/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
