/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.User;
import org.geonetwork.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

class DatabaseUserAuthoritiesTest {

    @Test
    @SuppressWarnings("deprecation")
    void databaseUserKeepsGeonetworkAuthorityAndGetsProfileRole() {
        var userRepository = mock(UserRepository.class);
        var geoNetworkUserService = mock(GeoNetworkUserService.class);
        var user = User.builder()
                .username("editor")
                .password("secret")
                .isenabled(true)
                .profile(Profile.Editor)
                .build();
        var gnAuthority = new GeonetworkAuthority();
        when(userRepository.findOptionalByUsernameOrEmailAndAuthtypeIsNull("editor", "editor"))
                .thenReturn(Optional.of(user));
        when(geoNetworkUserService.buildUserAuthority(user)).thenReturn(gnAuthority);
        var service = new DatabaseUserDetailsService(
                DatabaseUserAuthProperties.USERNAME_OR_EMAIL,
                NoOpPasswordEncoder.getInstance(),
                geoNetworkUserService,
                userRepository);

        var authorities = service.loadUserByUsername("editor").getAuthorities();

        assertTrue(authorities.contains(gnAuthority));
        assertTrue(authorities.stream().map(GrantedAuthority::getAuthority).anyMatch("ROLE_Editor"::equals));
    }
}
