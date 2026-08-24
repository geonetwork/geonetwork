/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/** Date range details. */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DateRangeDetailsInfo {
    String date;
    String frame;
    String calendarEraName;
    String indeterminatePosition;
}
