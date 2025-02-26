/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.security;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "login-endpoint")
public class AuthProviderController {
    AuthProviderService authProviderService;

    @GetMapping(path = "/api/user/auth-providers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuthProvider> getAuthProviders() {
        return authProviderService.getAuthProviders();
    }
}
