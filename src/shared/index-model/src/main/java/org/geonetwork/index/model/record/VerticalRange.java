/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.index.model.record;

import lombok.Data;

/** Vertical range. */
@Data
public class VerticalRange {
    private Double gte;
    private Double lte;
    private String unit;
}
