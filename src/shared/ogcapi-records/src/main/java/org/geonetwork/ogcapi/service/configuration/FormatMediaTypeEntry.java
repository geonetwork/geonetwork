/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.configuration;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** a media type and the format names that map to it, e.g. {@code application/zip} for "SHP", "ESRI Shapefile". */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormatMediaTypeEntry {

    private String mediaType;

    /** case-insensitive */
    private List<String> aliases = new ArrayList<>();
}
