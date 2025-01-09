/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.security.user;

import java.util.List;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.User;
import org.geonetwork.domain.Usergroup;

public interface IUserManager {
    User getUserByUsername(String username) throws UserNotFoundException;

    List<Usergroup> getUserGroups(int userId);

    List<Usergroup> getUserGroups(int userId, Profile profile);
}
