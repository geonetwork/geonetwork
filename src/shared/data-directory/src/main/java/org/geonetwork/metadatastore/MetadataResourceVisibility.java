/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.metadatastore;

import jakarta.persistence.Convert;
import java.util.Comparator;

/** Metadata resource visibility. */
@Convert(converter = MetadataResourceVisibilityConverter.class)
public enum MetadataResourceVisibility {
    /** Accessible by all */
    PUBLIC("public"),
    /** Accessible to user with download privilege */
    PRIVATE("private");

    public static final Comparator<MetadataResource> sortByFileName = new Comparator<MetadataResource>() {
        @Override
        public int compare(MetadataResource o1, MetadataResource o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };
    final String value;

    MetadataResourceVisibility(final String value) {
        this.value = value;
    }

    public static MetadataResourceVisibility parse(String value) {
        for (MetadataResourceVisibility metadataResourceVisibility : MetadataResourceVisibility.values()) {
            if (metadataResourceVisibility.toString().equals(value)) {
                return metadataResourceVisibility;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
