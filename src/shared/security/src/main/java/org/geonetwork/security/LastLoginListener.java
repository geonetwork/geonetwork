/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.utility.date.ISODate;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Records the last login date of a user after an interactive sign in (form login or OAuth2/OIDC).
 *
 * <p>The event is published by Spring Security only after the credentials have been verified. HTTP Basic requests do
 * not publish it, so API clients that send credentials on every call do not update the date.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LastLoginListener {

    private final UserRepository userRepository;

    @EventListener
    @Transactional
    public void onInteractiveLogin(InteractiveAuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        if (userRepository.updateLastLoginDate(username, new ISODate().toString()) == 0) {
            log.debug("Last login date not updated, user '{}' not found", username);
        }
    }
}
