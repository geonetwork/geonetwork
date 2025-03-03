/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.security;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    /**
     * Gets the current user's authentication from the security context.
     *
     * @return current user's authentication from the security context.
     */
    Authentication getAuthentication();

    /**
     * Gets the current username from the security context. Might be "" or "anonymousUser" - neither of which are real
     * GN users.
     *
     * @return current username from the security context.
     */
    String getUsername();

    /**
     * returns true iff the current user is anonymous (or not logged in). ie. username is "" or "anonymousUser"
     *
     * @return true iff the current user is anonymous (or not logged in).
     */
    boolean isAnonymous();

    /**
     * true iff there is a logged on user.
     *
     * @return true iff there is a logged on user.
     */
    boolean isAuthenticated();

    /**
     * Gets info about the logged in user (highest profile, username, userid, profile -> groups, group -> profiles.
     *
     * @return constructed GeonetworkAuthority summarizing the user and permissions
     */
    GeonetworkAuthority geonetworkPermissions();
}
