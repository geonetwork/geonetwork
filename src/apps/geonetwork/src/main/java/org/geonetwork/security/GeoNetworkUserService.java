/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class GeoNetworkUserService {
    GeonetworkAuthority.GeonetworkAuthorityBuilder geonetworkAuthorityBuilder;

    public static boolean isUserFoundAndEnabled(String username, Optional<User> user) {
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username + " is not a valid username");
        }

        if (!user.get().getIsenabled()) {
            throw new UsernameNotFoundException(username + " account is disabled");
        }

        return true;
    }

    public GeonetworkAuthority buildUserAuthority(org.geonetwork.domain.User currentUser) {
        return geonetworkAuthorityBuilder.build(currentUser.getUsername());
    }
}
