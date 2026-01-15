/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.metadatastore;

import jakarta.persistence.AttributeConverter;
import java.beans.PropertyEditorSupport;
import java.util.Arrays;

public class MetadataResourceVisibilityConverter extends PropertyEditorSupport
        implements AttributeConverter<MetadataResourceVisibility, String> {
    @Override
    public void setAsText(final String visibility) throws IllegalArgumentException {
        MetadataResourceVisibility value = MetadataResourceVisibility.parse(visibility.trim());
        if (value != null) {
            setValue(value);
        } else {
            throw new IllegalArgumentException(String.format(
                    "Unsupported value '%s'. Values are %s.",
                    visibility, Arrays.toString(MetadataResourceVisibility.values())));
        }
    }

    @Override
    public String convertToDatabaseColumn(MetadataResourceVisibility visibility) {
        switch (visibility) {
            case PRIVATE:
                return "P";
            case PUBLIC:
                return "A";
            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported value '%s'. Values are %s.",
                        visibility, Arrays.toString(MetadataResourceVisibility.values())));
        }
    }

    @Override
    public MetadataResourceVisibility convertToEntityAttribute(String s) {
        switch (s) {
            case "P":
                return MetadataResourceVisibility.PRIVATE;
            case "A":
                return MetadataResourceVisibility.PUBLIC;
            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported value '%s'. Values are %s.",
                        s, Arrays.toString(MetadataResourceVisibility.values())));
        }
    }
}
