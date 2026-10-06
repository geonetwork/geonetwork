/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

class AuthProviderServiceTest {
    private static final String BASE_URL = "http://localhost:7979/geonetwork";

    private static InMemoryClientRegistrationRepository repository() {
        return new InMemoryClientRegistrationRepository(ClientRegistration.withRegistrationId("github")
                .clientId("client")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .authorizationUri("https://github.com/login/oauth/authorize")
                .tokenUri("https://github.com/login/oauth/access_token")
                .build());
    }

    @Test
    void oauth2EndpointDoesNotRepeatContextPath() {
        List<AuthProvider> providers = new AuthProviderService(repository(), BASE_URL, "").getAuthProviders();

        assertEquals(1, providers.size());
        assertEquals("github", providers.get(0).getClientId());
        assertEquals(BASE_URL + "/oauth2/authorization/github", providers.get(0).getEndpoint());
    }

    @Test
    void databaseProviderListedFirstWhenEnabled() {
        List<AuthProvider> providers = new AuthProviderService(repository(), BASE_URL, "database").getAuthProviders();

        assertEquals(2, providers.size());
        assertEquals("database", providers.get(0).getClientId());
        assertEquals("github", providers.get(1).getClientId());
    }

    @Test
    void databaseProviderIdIsLowercaseWhateverTheConfiguredCase() {
        List<AuthProvider> providers = new AuthProviderService(null, BASE_URL, "DATABASE").getAuthProviders();

        assertEquals(1, providers.size());
        assertEquals("database", providers.get(0).getClientId());
    }

    @Test
    void noRepositoryAndNoDatabaseGivesEmptyList() {
        assertTrue(new AuthProviderService(null, BASE_URL, "ldap")
                .getAuthProviders()
                .isEmpty());
    }
}
