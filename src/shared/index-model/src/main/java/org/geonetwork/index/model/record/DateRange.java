/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import lombok.Data;

/** Date range. */
@Data
public class DateRange {

    private String gte;
    private String lte;
}
