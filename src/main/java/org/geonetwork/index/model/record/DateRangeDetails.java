/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */


package org.geonetwork.index.model.record;

import lombok.Data;

@Data
public class DateRangeDetails {

    private DateRangeDetailsInfo start;
    private DateRangeDetailsInfo end;
}
