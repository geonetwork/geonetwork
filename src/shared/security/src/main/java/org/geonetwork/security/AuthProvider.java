/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthProvider {
    public static final String TYPE_DATABASE = "database";
    public static final String TYPE_OAUTH2 = "oauth2";

    @Schema(description = "Provider identifier. For OAuth2 providers, the registration id (e.g. github).")
    String id;

    @Schema(
            description = "Provider type.",
            allowableValues = {TYPE_DATABASE, TYPE_OAUTH2})
    String type;

    @Schema(description = "Name to display for the provider.")
    String name;

    @Schema(description = "URL to start the login. Only for OAuth2 providers.")
    String endpoint;
}
