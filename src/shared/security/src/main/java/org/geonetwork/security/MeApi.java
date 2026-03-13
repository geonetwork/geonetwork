/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import static org.geonetwork.security.DatabaseUserDetailsService.USER_NAME;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "authentication")
public class MeApi {

    @Operation(
            summary = "Get information about me",
            description =
                    """
* If not authenticated, return status 204 (NO_CONTENT)
* else return user information.

This operation is used to know if current user is authenticated or not.
""")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Authenticated. Return user details."),
                @ApiResponse(
                        responseCode = "204",
                        description = "Not authenticated.",
                        content = {@Content(schema = @Schema(hidden = true))})
            })
    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("permitAll")
    public ResponseEntity<Me> user(Authentication authentication, @AuthenticationPrincipal UserDetails userDetails) {
        String userName = "";
        if (authentication instanceof OAuth2AuthenticationToken) {
            userName = ((OAuth2UserAuthority) authentication.getAuthorities().toArray()[0])
                    .getAttributes()
                    .get(USER_NAME)
                    .toString();
        } else if (userDetails != null) {
            userName = userDetails.getUsername();
        }
        if (StringUtils.isNotEmpty(userName)) {
            return ResponseEntity.ok(Me.builder().username(userName).build());
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
