/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.data;

import java.util.List;

/** Data analyzer provides information about a data file. */
public interface DataAnalyzer {
  String getName();

  String getVersion();

  List<DataFormat> getFormats();

  String getStatus();

  /**
   * Return a list of layers.
   *
   * <pre>
   *   ogrinfo -ro  https://sdi.eea.europa.eu/webdav/datastore/public/coe_t_emerald_p_2022-2023_v01_r00/Emerald_2023_SPECIES.csv
   * </pre>
   */
  List<String> getDatasourceLayers(String dataSource);

  /**
   * Return information about the layer.
   *
   * <pre>
   *   ogrinfo -ro -json https://sdi.eea.europa.eu/webdav/datastore/public/coe_t_emerald_p_2022-2023_v01_r00/Emerald_2023_SPECIES.csv Emerald_2023_SPECIES
   * </pre>
   */
  Object getLayerProperties(String dataSource, String layer);
}
