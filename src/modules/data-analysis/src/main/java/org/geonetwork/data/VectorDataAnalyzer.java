/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.data;

import jakarta.validation.constraints.Max;
import java.util.List;

/** Vector data analyzer provides information about a vector or tabular data file. */
public interface VectorDataAnalyzer extends DataAnalyzer {

    /**
     * Return min/max values for the given attribute.
     *
     * <p>Can be used to compute temporal extent for a time attribute or a vertical extent for a depth/elevation
     * attribute.
     *
     * <pre>
     *  ogrinfo -ro -features https://sdi.eea.europa.eu/webdav/datastore/public/coe_t_emerald_p_2022-2023_v01_r00/Emerald_2023_SPECIES.csv -sql "SELECT MIN(DATAQUALITY) AS min, MAX(DATAQUALITY) AS max FROM Emerald_2023_SPECIES"
     * </pre>
     */
    Object getAttributesStatistics(String dataSource, String layer, List<String> attribute);

    /**
     * Return unique values for the given attribute.
     *
     * <pre>
     *   ogrinfo -ro -features https://sdi.eea.europa.eu/webdav/datastore/public/coe_t_emerald_p_2022-2023_v01_r00/Emerald_2023_SPECIES.csv -sql "SELECT DISTINCT DATAQUALITY AS value FROM Emerald_2023_SPECIES ORDER BY DATAQUALITY LIMIT 10"
     * </pre>
     */
    Object getAttributeUniqueValues(String dataSource, String layer, String attribute, int limit);

    /**
     * Return first features for preview.
     *
     * <pre>
     *  ogrinfo -ro -features  -limit 1 https://sdi.eea.europa.eu/webdav/datastore/public/coe_t_emerald_p_2022-2023_v01_r00/Emerald_2023_SPECIES.csv  Emerald_2023_SPECIES
     * </pre>
     */
    Object getFeatures(String dataSource, String layer, @Max(10) int limit);
}
