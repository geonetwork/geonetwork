/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.index.model.record;

import lombok.Data;

/** Date range details. */
@Data
public class DateRangeDetails {

    private DateRangeDetailsInfo start;
    private DateRangeDetailsInfo end;
}
