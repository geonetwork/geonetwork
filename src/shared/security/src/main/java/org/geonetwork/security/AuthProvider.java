/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthProvider {
    String clientId;
    String endpoint;
}
