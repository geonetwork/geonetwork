/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.plugin;

import java.util.Map;

/** Created by francois on 01/12/15. */
public interface ExportablePlugin {
  /**
   * Return the list of format to export to. The key are the path to the XSL transformation to apply
   * eg. convert/to19139.xsl and the value is the file name.
   */
  Map<String, String> getExportFormats();
}
