/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.plugin;

import java.util.Map;
import org.jdom.Namespace;

public interface CSWPlugin {
    /** Return the list of typenames and corresponding namespace for the plugin. */
    Map<String, Namespace> getCswTypeNames();
}
