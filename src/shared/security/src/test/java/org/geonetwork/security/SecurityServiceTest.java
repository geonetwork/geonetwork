/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.Setting;
import org.geonetwork.domain.SettingKey;
import org.geonetwork.domain.User;
import org.geonetwork.domain.repository.SettingRepository;
import org.geonetwork.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private SettingRepository settingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IAuthenticationFacade authenticationFacade;

    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        var roleHierarchy = RoleHierarchyImpl.fromHierarchy(
                "ROLE_Administrator > ROLE_UserAdmin\n ROLE_UserAdmin > ROLE_Reviewer\n ROLE_Reviewer > ROLE_Editor\n ROLE_Editor > ROLE_RegisteredUser\n ROLE_RegisteredUser > ROLE_Guest");
        securityService = new SecurityService(settingRepository, userRepository, authenticationFacade, roleHierarchy);
    }

    /** Signs in a user with the given profile, with the same authorities the user detail services build. */
    private void signIn(String username, Profile profile) {
        var authentication = UsernamePasswordAuthenticationToken.authenticated(
                username, null, List.of(new SimpleGrantedAuthority("ROLE_" + profile.name())));
        lenient().when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        lenient().when(authenticationFacade.getUsername()).thenReturn(username);
        lenient()
                .when(userRepository.findOptionalByUsername(username))
                .thenReturn(Optional.of(
                        User.builder().username(username).profile(profile).build()));
    }

    private void minimumProfile(Profile profile) {
        when(settingRepository.findByName(SettingKey.METADATA_BATCH_EDITING_ACCESS_LEVEL))
                .thenReturn(Optional.of(Setting.builder()
                        .name(SettingKey.METADATA_BATCH_EDITING_ACCESS_LEVEL)
                        .value(profile.name())
                        .build()));
    }

    @Test
    void hierarchyRoleIncludesLowerRoles() {
        signIn("editor", Profile.Editor);

        assertTrue(securityService.hasHierarchyRole("Editor"));
        assertTrue(securityService.hasHierarchyRole("RegisteredUser"));
        assertFalse(securityService.hasHierarchyRole("Reviewer"));
    }

    @Test
    void administratorCanBatchEdit() {
        signIn("admin", Profile.Administrator);

        assertTrue(securityService.hasMetadataBatchEditingAccessLevel());
    }

    @Test
    void editorCanBatchEditWhenMinimumIsEditor() {
        signIn("editor", Profile.Editor);
        minimumProfile(Profile.Editor);

        assertTrue(securityService.hasMetadataBatchEditingAccessLevel());
    }

    @Test
    void registeredUserCannotBatchEdit() {
        signIn("user", Profile.RegisteredUser);
        minimumProfile(Profile.Editor);

        assertThrows(SecurityException.class, securityService::hasMetadataBatchEditingAccessLevel);
    }

    @Test
    void authenticatedUserWithoutDatabaseRecordIsDenied() {
        signIn("ghost", Profile.Administrator);
        when(userRepository.findOptionalByUsername("ghost")).thenReturn(Optional.empty());

        assertFalse(securityService.hasMetadataBatchEditingAccessLevel());
    }
}
