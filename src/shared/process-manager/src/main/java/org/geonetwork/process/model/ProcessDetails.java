/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.process.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class ProcessDetails {
    private String process;
    private Map<String, String> parameters = new HashMap<>();
}
