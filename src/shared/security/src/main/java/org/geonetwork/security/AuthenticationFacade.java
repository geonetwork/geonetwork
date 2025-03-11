/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.security;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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

        if (auth.getPrincipal() instanceof User user) {
            return geonetworkAuthorityBuilder.build(user.getUsername());
        } else if (auth.getPrincipal() instanceof String) {
            return GeonetworkAuthority.GUESTAUTHORITY; // username == "anonymousUser"
        }

        throw new RuntimeException("don't know what type of user is logged in!!");
    }
}
