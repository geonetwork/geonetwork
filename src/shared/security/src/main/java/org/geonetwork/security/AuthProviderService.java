/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.stereotype.Component;

@Component
public class AuthProviderService {
    ClientRegistrationRepository clientRegistrationRepository;
    private final String baseUrl;
    private final String localSecurityProvider;

    public AuthProviderService(
            @Autowired(required = false) ClientRegistrationRepository clientRegistrationRepository,
            @Value("${geonetwork.url}") String baseUrl,
            @Value("${geonetwork.security.provider:}") String localSecurityProvider) {
        this.localSecurityProvider = localSecurityProvider;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.baseUrl = baseUrl;
    }

    public List<AuthProvider> getAuthProviders() {
        List<AuthProvider> providerList = new ArrayList<>();
        if ("database".equalsIgnoreCase(localSecurityProvider)) {
            providerList.add(AuthProvider.builder()
                    .id(AuthProvider.TYPE_DATABASE)
                    .type(AuthProvider.TYPE_DATABASE)
                    .name("Database")
                    .build());
        }

        // ClientRegistrationRepository can only look up by id: registrations can be listed only if it is Iterable
        if (!(clientRegistrationRepository instanceof Iterable<?> registrations)) {
            return providerList;
        }

        for (Object registration : registrations) {
            if (registration instanceof ClientRegistration clientRegistration) {
                providerList.add(AuthProvider.builder()
                        .id(clientRegistration.getRegistrationId())
                        .type(AuthProvider.TYPE_OAUTH2)
                        .name(clientRegistration.getClientName())
                        // geonetwork.url already includes the servlet context path
                        .endpoint(String.format(
                                "%s%s/%s",
                                baseUrl,
                                OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI,
                                clientRegistration.getRegistrationId()))
                        .build());
            }
        }
        return providerList;
    }
}
