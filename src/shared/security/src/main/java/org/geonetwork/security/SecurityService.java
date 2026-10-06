/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import java.util.Collection;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.Setting;
import org.geonetwork.domain.SettingKey;
import org.geonetwork.domain.User;
import org.geonetwork.domain.repository.SettingRepository;
import org.geonetwork.domain.repository.UserRepository;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("securityService")
@Slf4j
@AllArgsConstructor
public class SecurityService {

    private final SettingRepository settingRepository;
    private final UserRepository userRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final RoleHierarchy roleHierarchy;

    public boolean isAdmin() {
        if (!authenticationFacade.isAuthenticated()) {
            return false;
        }
        var authentication = this.authenticationFacade.geonetworkPermissions();
        Profile profile = authentication.getHighestProfile();
        return profile == Profile.Administrator;
    }

    public boolean hasMetadataBatchEditingAccessLevel() {
        if (!authenticationFacade.getAuthentication().isAuthenticated()) {
            return false;
        }

        String currentUsername = authenticationFacade.getUsername();
        if (!StringUtils.hasLength(currentUsername)) {
            return false;
        }

        // the profile comes from the database, so it also works for OAuth2/OIDC users
        Optional<Profile> profile =
                userRepository.findOptionalByUsername(currentUsername).map(User::getProfile);
        if (profile.isEmpty()) {
            return false;
        }

        // --- check if the user is an administrator
        if (profile.get() == Profile.Administrator) {
            return true;
        }

        Optional<Setting> accessLevel = settingRepository.findByName(SettingKey.METADATA_BATCH_EDITING_ACCESS_LEVEL);

        String minimumProfile = accessLevel.isPresent() ? accessLevel.get().getValue() : Profile.Editor.name();

        if (hasHierarchyRole(minimumProfile)) {
            return true;
        } else {
            throw new SecurityException(String.format(
                    "Minimum required profile for %s is %s",
                    SettingKey.METADATA_BATCH_EDITING_ACCESS_LEVEL, minimumProfile));
        }
    }

    /**
     * Checks if the current user has a role using the role hierarchy.
     *
     * @param role Role to check.
     * @return true if the current user has a role using the role hierarchy, otherwise false.
     */
    public boolean hasHierarchyRole(String role) {

        Collection<? extends GrantedAuthority> authorities =
                authenticationFacade.getAuthentication().getAuthorities();

        Collection<? extends GrantedAuthority> hierarchyAuthorities =
                roleHierarchy.getReachableGrantedAuthorities(authorities);

        for (GrantedAuthority authority : hierarchyAuthorities) {
            if (authority.getAuthority().equals("ROLE_" + role)) {
                return true;
            }
        }
        return false;
    }
}
