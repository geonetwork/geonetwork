/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/** implementation of IAuthenticationFacade */
@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Autowired
    GeonetworkAuthority.GeonetworkAuthorityBuilder geonetworkAuthorityBuilder;

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getUsername() {
        Authentication auth = this.getAuthentication();
        if (auth.isAuthenticated()) {
            return auth.getName();
        }
        return "";
    }

    @Override
    public boolean isAnonymous() {
        var username = getUsername();
        return StringUtils.isBlank(username) || username.equals("anonymousUser");
    }

    @Override
    public boolean isAuthenticated() {
        Authentication auth = this.getAuthentication();
        return auth.isAuthenticated();
    }

    @Override
    public GeonetworkAuthority geonetworkPermissions() {
        if (isAnonymous()) {
            return GeonetworkAuthority.GUESTAUTHORITY;
        }

        var auth = getAuthentication();
        if (auth == null) {
            return GeonetworkAuthority.GUESTAUTHORITY; // shouldn't happen!
        }

        // getName() is the GN database username for every login type (form login, OAuth2, OIDC);
        // an unknown name gets the guest authority from the builder
        return geonetworkAuthorityBuilder.build(auth.getName());
    }
}
