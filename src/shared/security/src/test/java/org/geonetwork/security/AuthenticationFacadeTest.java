/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@ExtendWith(MockitoExtension.class)
class AuthenticationFacadeTest {

    private static final List<SimpleGrantedAuthority> AUTHORITIES = List.of(new SimpleGrantedAuthority("ROLE_Editor"));

    @Mock
    GeonetworkAuthority.GeonetworkAuthorityBuilder geonetworkAuthorityBuilder;

    @InjectMocks
    AuthenticationFacade authenticationFacade;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    private static void signIn(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private static OAuth2AuthenticationToken oauth2Login(OAuth2User user) {
        return new OAuth2AuthenticationToken(user, AUTHORITIES, "entraid");
    }

    @Test
    void databaseUser() {
        var expected = new GeonetworkAuthority();
        when(geonetworkAuthorityBuilder.build("john")).thenReturn(expected);
        var user = new User("john", "pw", AUTHORITIES);
        signIn(UsernamePasswordAuthenticationToken.authenticated(user, null, AUTHORITIES));

        assertSame(expected, authenticationFacade.geonetworkPermissions());
    }

    @Test
    void oidcUser() {
        var expected = new GeonetworkAuthority();
        when(geonetworkAuthorityBuilder.build("john@example.com")).thenReturn(expected);
        var idToken = new OidcIdToken(
                "token",
                Instant.now(),
                Instant.now().plusSeconds(60),
                Map.of("sub", "abc-123", "preferred_username", "john@example.com"));
        signIn(oauth2Login(new DefaultOidcUser(AUTHORITIES, idToken, "preferred_username")));

        assertSame(expected, authenticationFacade.geonetworkPermissions());
    }

    @Test
    void oauth2User() {
        var expected = new GeonetworkAuthority();
        when(geonetworkAuthorityBuilder.build("johngh")).thenReturn(expected);
        signIn(oauth2Login(new DefaultOAuth2User(AUTHORITIES, Map.of("login", "johngh"), "login")));

        assertSame(expected, authenticationFacade.geonetworkPermissions());
    }
}
