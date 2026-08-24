/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
