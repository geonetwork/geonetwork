/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
