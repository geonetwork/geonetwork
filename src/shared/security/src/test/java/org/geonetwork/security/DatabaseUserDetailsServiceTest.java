/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;
import org.geonetwork.domain.User;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.domain.repository.UsergroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Disabled
class DatabaseUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsergroupRepository userGroupRepository;

    @SuppressWarnings("unused")
    @Mock
    private GeoNetworkUserService geoNetworkUserService;

    @InjectMocks
    private DatabaseUserDetailsService databaseUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        databaseUserDetailsService.setCheckUsernameOrEmail(DatabaseUserAuthProperties.USERNAME_OR_EMAIL);
    }

    @Test
    void testRetrieveUser_ValidUsername() {
        User mockUser = new User();
        mockUser.setUsername("validUser");
        mockUser.setPassword("password");
        mockUser.setIsenabled(true);
        when(userRepository.findOptionalByUsernameOrEmailAndAuthtypeIsNull("validUser", "validUser"))
                .thenReturn(Optional.of(mockUser));

        UserDetails userDetails = databaseUserDetailsService.retrieveUser("validUser", null);

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("validUser", userDetails.getUsername());
    }

    @Test
    void testRetrieveUser_InvalidUsername() {
        when(userRepository.findOptionalByUsernameOrEmailAndAuthtypeIsNull("invalidUser", "invalidUser"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> {
                    databaseUserDetailsService.retrieveUser("invalidUser", null);
                },
                "invalidUser is not a valid username");
    }

    @Test
    void testRetrieveUser_DisabledAccount() {
        User mockUser = new User();
        mockUser.setUsername("disabledUser");
        mockUser.setIsenabled(false);
        when(userRepository.findOptionalByUsernameOrEmailAndAuthtypeIsNull("disabledUser", "disabledUser"))
                .thenReturn(Optional.of(mockUser));

        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> {
                    databaseUserDetailsService.retrieveUser("disabledUser", null);
                },
                "disabledUser account is disabled");
    }

    @Test
    void testRetrieveUser_ValidEmail() {
        User mockUser = new User();
        mockUser.setUsername("validEmailUser");
        mockUser.setPassword("password");
        mockUser.setIsenabled(true);
        when(userRepository.findOptionalByUsernameOrEmailAndAuthtypeIsNull(
                        "validEmail@example.com", "validEmail@example.com"))
                .thenReturn(Optional.of(mockUser));
        when(userGroupRepository.findAllByUserid_Id(mockUser.getId())).thenReturn(Collections.emptyList());

        UserDetails userDetails = databaseUserDetailsService.retrieveUser("validEmail@example.com", null);

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("validEmailUser", userDetails.getUsername());
    }

    @Test
    void testRetrieveUser_EmailAuthDisabled() {
        databaseUserDetailsService.setCheckUsernameOrEmail(DatabaseUserAuthProperties.USERNAME);
        User mockUser = new User();
        mockUser.setUsername("validEmailUser");
        mockUser.setPassword("password");
        mockUser.setIsenabled(true);
        when(userRepository.findOptionalByUsernameAndAuthtypeIsNull("validEmailUser"))
                .thenReturn(Optional.of(mockUser));
        when(userGroupRepository.findAllByUserid_Id(mockUser.getId())).thenReturn(Collections.emptyList());

        UserDetails userDetails = databaseUserDetailsService.retrieveUser("validEmailUser", null);
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("validEmailUser", userDetails.getUsername());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            databaseUserDetailsService.retrieveUser("validEmail@example.com", null);
        });
        databaseUserDetailsService.setCheckUsernameOrEmail(DatabaseUserAuthProperties.USERNAME_OR_EMAIL);
    }

    @Test
    void testRetrieveUser_InvalidEmail() {
        when(userRepository.findOptionalByEmailAndAuthtypeIsNull("invalidEmail@example.com"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            databaseUserDetailsService.retrieveUser("invalidEmail@example.com", null);
        });
    }
}
