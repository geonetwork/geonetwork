/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.geonetwork.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;

@ExtendWith(MockitoExtension.class)
class LastLoginListenerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LastLoginListener listener;

    private static InteractiveAuthenticationSuccessEvent event(String username) {
        return new InteractiveAuthenticationSuccessEvent(
                UsernamePasswordAuthenticationToken.authenticated(username, null, null), LastLoginListenerTest.class);
    }

    @Test
    void updatesLastLoginDateOfAuthenticatedUser() {
        when(userRepository.updateLastLoginDate(eq("alice"), anyString())).thenReturn(1);

        listener.onInteractiveLogin(event("alice"));

        verify(userRepository).updateLastLoginDate(eq("alice"), anyString());
    }

    @Test
    void unknownUserIsIgnored() {
        when(userRepository.updateLastLoginDate(eq("ghost"), anyString())).thenReturn(0);

        listener.onInteractiveLogin(event("ghost"));

        verify(userRepository).updateLastLoginDate(eq("ghost"), anyString());
    }
}
