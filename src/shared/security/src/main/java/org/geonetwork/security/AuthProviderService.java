/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Component;

@Component
public class AuthProviderService {
    InMemoryClientRegistrationRepository clientRegistrationRepository;
    private final String baseUrl;
    private final String contextPath;
    private final String localSecurityProvider;

    public AuthProviderService(
            @Autowired(required = false) InMemoryClientRegistrationRepository clientRegistrationRepository,
            @Value("${geonetwork.url}") String baseUrl,
            @Value("${server.servlet.context-path:}") String contextPath,
            @Value("${geonetwork.security.provider:}") String localSecurityProvider) {
        this.localSecurityProvider = localSecurityProvider;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.contextPath = contextPath;
        this.baseUrl = baseUrl;
    }

    public List<AuthProvider> getAuthProviders() {
        List<AuthProvider> providerList = new ArrayList<>();
        if ("database".equalsIgnoreCase(localSecurityProvider)) {
            providerList.add(
                    AuthProvider.builder().clientId(localSecurityProvider).build());
        }

        if (clientRegistrationRepository == null) {
            return providerList;
        }

        clientRegistrationRepository.forEach(clientRegistration -> providerList.add(AuthProvider.builder()
                .clientId(clientRegistration.getRegistrationId())
                .endpoint(String.format(
                        "%s%s/oauth2/authorization/%s", baseUrl, contextPath, clientRegistration.getRegistrationId()))
                .build()));
        return providerList;
    }
}
