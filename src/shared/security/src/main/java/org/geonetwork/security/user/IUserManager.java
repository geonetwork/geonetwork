/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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

    User registerUser(
            String username,
            String password,
            String name,
            String surname,
            String email,
            String authType,
            String loginDate,
            String company,
            Profile profile);

    void userLoginEvent(User user);
}
