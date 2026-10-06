/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.geonetwork.domain.Profile;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "geonetwork.security.oauth2")
@Getter
public class GeoNetworkSsoConfiguration {

    private final Map<String, Registration> userCreationRegistration = new HashMap<>();

    public GeoNetworkSsoConfiguration() {
        userCreationRegistration.put("default", new Registration());
    }

    public Registration getRegistrationInfo(String registrationId) {
        if (userCreationRegistration.containsKey(registrationId)) {
            return userCreationRegistration.get(registrationId);
        }
        return userCreationRegistration.get("default");
    }

    @Getter
    @Setter
    public static class Registration {
        private String name = "name";
        private String email = "email";
        private String organization = "organization";
        private String surname = "surname";

        /** Name of the top-level token claim holding the user roles/groups. */
        private String rolesClaim = "groups";

        /** Explicit mappings from a claim value to a group and profile. */
        private List<RoleMapping> roleMappings = new ArrayList<>();

        /** Create the catalogue group when a mapping or pattern refers to a group that does not exist. */
        private boolean createMissingGroups = false;

        /** When true the IdP is the only source of groups and profile: they are replaced at each login. */
        private boolean updateGroup = false;

        private String rolePattern;

        // derived from rolePattern: not a property, so it can not be bound without validation
        @Getter(AccessLevel.NONE)
        @Setter(AccessLevel.NONE)
        private Pattern compiledRolePattern;

        /**
         * Optional regex applied to each claim value. Named group {@code profile} is required, named group
         * {@code group} is optional (no group = global profile). Compiled here so a bad value fails at startup.
         */
        public void setRolePattern(String regex) {
            if (regex == null || regex.isBlank()) {
                this.rolePattern = null;
                this.compiledRolePattern = null;
                return;
            }
            var compiled = Pattern.compile(regex);
            if (!compiled.namedGroups().containsKey("profile")) {
                throw new IllegalArgumentException("rolePattern must define a named group 'profile': " + regex);
            }
            this.rolePattern = regex;
            this.compiledRolePattern = compiled;
        }

        public Pattern compiledRolePattern() {
            return compiledRolePattern;
        }

        public boolean roleMappingEnabled() {
            return compiledRolePattern != null || !roleMappings.isEmpty();
        }
    }

    @Getter
    @Setter
    public static class RoleMapping {
        private String claimValue;

        /** Optional. Absent means the profile is set globally on the user. */
        private String group;

        private Profile profile = Profile.Editor;

        public void setProfile(Profile profile) {
            // an empty "profile:" in the configuration binds to null
            this.profile = profile == null ? Profile.Editor : profile;
        }
    }
}
