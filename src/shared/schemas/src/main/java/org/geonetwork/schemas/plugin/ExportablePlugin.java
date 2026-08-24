/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.schemas.plugin;

import java.util.Map;

/** Created by francois on 01/12/15. */
public interface ExportablePlugin {
    /**
     * Return the list of format to export to. The key are the path to the XSL transformation to apply eg.
     * convert/to19139.xsl and the value is the file name.
     */
    Map<String, String> getExportFormats();
}
