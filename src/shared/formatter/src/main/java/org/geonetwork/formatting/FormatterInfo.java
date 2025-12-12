/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.formatting;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormatterInfo {

    String mimeType;
    Set<String> schemas;
    ProfileInfo profile;

    public FormatterInfo(String mimeType, ProfileInfo profile) {
        this.mimeType = mimeType;
        this.profile = profile;
        schemas = new HashSet<>();
    }
}
