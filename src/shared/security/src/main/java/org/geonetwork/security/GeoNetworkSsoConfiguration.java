/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.security;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "geonetwork.security.oauth2")
@Getter
public class GeoNetworkSsoConfiguration {

    private final Map<String, Registration> userCreationRegistration = new HashMap<>();

    public GeoNetworkSsoConfiguration() {
        var defaultInfo = new Registration("email", "name", "organization", "surname");
        userCreationRegistration.put("default", defaultInfo);
    }

    public Registration getRegistrationInfo(String registrationId) {
        if (userCreationRegistration.containsKey(registrationId)) {
            return userCreationRegistration.get(registrationId);
        }
        return userCreationRegistration.get("default");
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Registration {
        private String name = "name";
        private String email = "email";
        private String organization = "organization";
        private String surname = "surname";
    }
}
