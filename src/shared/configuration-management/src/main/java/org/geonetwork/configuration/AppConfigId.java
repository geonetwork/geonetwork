/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.configuration;

import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppConfigId implements Serializable {
    @Size(max = 255)
    private String app;

    @Size(max = 255)
    private String profile;

    @Size(max = 255)
    private String label;

    @Size(max = 255)
    private String configParam;
}
