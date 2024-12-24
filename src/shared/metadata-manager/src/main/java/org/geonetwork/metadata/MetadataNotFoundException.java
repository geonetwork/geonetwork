/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.metadata;

public class MetadataNotFoundException extends Exception {
    public MetadataNotFoundException() {
        super();
    }

    public MetadataNotFoundException(String message) {
        super(message);
    }

    public MetadataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetadataNotFoundException(Throwable cause) {
        super(cause);
    }
}
