/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
