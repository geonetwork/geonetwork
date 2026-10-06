/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "authentication")
public class AuthProviderController {
    AuthProviderService authProviderService;

    @Operation(
            summary = "Get available authentication providers",
            description =
                    """
      The catalogue support 2 types of authentication providers:
      * Database provider: users are stored in the database and authenticated against it.
      * OAuth2 providers: users are authenticated against an external OAuth2 provider.

      The list of providers depends on your application configuration. It relies on:
      * `geonetwork.security.provider` property for local providers (database)
      * `security.oauth2.client.registration.*` properties for oauth2 providers (e.g. google, github, orcid, etc.)

      [How to configure Oauth providers?](https://docs.spring.io/spring-security/reference/servlet/oauth2/login/core.html)
      """)
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "List of authentication providers returned.",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        array = @ArraySchema(schema = @Schema(implementation = AuthProvider.class))))
            })
    @GetMapping(path = "/api/user/auth-providers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuthProvider> getAuthProviders() {
        return authProviderService.getAuthProviders();
    }
}
