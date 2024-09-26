/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data;

import java.util.Optional;

/** Raster data analyzer provides information about a raster data file. */
public interface RasterDataAnalyzer extends DataAnalyzer {
  Optional<RasterInfo> getRasterProperties(String rasterSource);
}
