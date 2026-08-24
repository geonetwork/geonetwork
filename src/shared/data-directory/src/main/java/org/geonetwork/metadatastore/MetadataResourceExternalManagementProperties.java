/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.metadatastore;

import jakarta.annotation.Nonnull;

public class MetadataResourceExternalManagementProperties {
    private final String id;
    private final String url;
    private ValidationStatus validationStatus = ValidationStatus.UNKNOWN;

    public MetadataResourceExternalManagementProperties(
            @Nonnull String id, @Nonnull String url, @Nonnull ValidationStatus validationStatus) {
        this.id = id;
        this.url = url;
        if (validationStatus != null) {
            this.validationStatus = validationStatus;
        }
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public static enum ValidationStatus {
        // Unknown status - this is the default when null or unknown
        UNKNOWN(0),
        // valid status - indicates that validation was successfull
        VALID(1),
        // incomplete status - indicates that the metadata is incomplete or has validation issues.
        INCOMPLETE(2);
        private final int statusValue;

        private ValidationStatus(int statusValue) {
            this.statusValue = statusValue;
        }

        public static ValidationStatus fromValue(int value) {
            for (ValidationStatus status : values()) {
                if (status.statusValue == value) {
                    return status;
                }
            }
            return null;
        }

        public int getValue() {
            return statusValue;
        }
    }
}
