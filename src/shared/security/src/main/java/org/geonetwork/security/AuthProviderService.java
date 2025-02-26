/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Component;

@Component
public class AuthProviderService {
    InMemoryClientRegistrationRepository clientRegistrationRepository;
    private String baseUrl;
    private String contextPath;

    public AuthProviderService(
            InMemoryClientRegistrationRepository clientRegistrationRepository,
            @Value("${geonetwork.url}") String baseUrl,
            @Value("${server.servlet.context-path:}") String contextPath) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.contextPath = contextPath;
        this.baseUrl = baseUrl;
    }

    public List<AuthProvider> getAuthProviders() {
        List<AuthProvider> clientId = new ArrayList<>();
        clientRegistrationRepository.forEach(clientRegistration -> clientId.add(AuthProvider.builder()
                .clientId(clientRegistration.getClientId())
                .endpoint(String.format(
                        "%s%s/oauth2/authorization/%s", baseUrl, contextPath, clientRegistration.getRegistrationId()))
                .build()));
        return clientId;
    }
}
