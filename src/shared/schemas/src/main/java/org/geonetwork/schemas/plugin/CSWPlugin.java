/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.schemas.plugin;

import java.util.Map;
import org.jdom.Namespace;

public interface CSWPlugin {
    /** Return the list of typenames and corresponding namespace for the plugin. */
    Map<String, Namespace> getCswTypeNames();
}
