/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.process.manager;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessParameter {
    private String name;
    private String type;
}
