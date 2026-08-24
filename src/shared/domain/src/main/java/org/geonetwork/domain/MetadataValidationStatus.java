/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.domain;

/**
 * The enumeration of validation status'
 *
 * @author Jesse
 */
public enum MetadataValidationStatus {
    INVALID(0),
    VALID(1),
    NEVER_CALCULATED(-1),
    DOES_NOT_APPLY(-2);

    private final int _id;

    private MetadataValidationStatus(int id) {
        _id = id;
    }

    public int getCode() {
        return _id;
    }
}
