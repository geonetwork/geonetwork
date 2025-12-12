/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.index.model.record;

import lombok.Data;

/** Coordinate reference system. */
@Data
public class CrsDetails {
    private String code;
    private String codeSpace;
    private String name;
    private String url;
}
