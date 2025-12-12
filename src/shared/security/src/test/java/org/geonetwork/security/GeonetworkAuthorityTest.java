/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.User;
import org.geonetwork.domain.Usergroup;
import org.geonetwork.domain.UsergroupId;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.domain.repository.UsergroupRepository;
import org.junit.Test;

public class GeonetworkAuthorityTest {

    @Test
    public void testNoUsername() {
        var builder = createBuilder(null, null);
        var result = builder.build(null);
        assertEquals(Profile.Guest, result.getHighestProfile());
        assertEquals(0, result.getProfileGroups().size());
    }

    @Test
    public void testInvalidUsername() {
        var builder = createBuilder(createUser("dave"), null);
        var result = builder.build("bob");
        assertEquals(Profile.Guest, result.getHighestProfile());
        assertEquals(0, result.getProfileGroups().size());
    }

    @Test
    public void testFullSimple() {
        var ug1id = new UsergroupId(100, Profile.Editor.getId(), 666);
        var ug1 = new Usergroup();
        ug1.setId(ug1id);

        var usergroups = Arrays.asList(ug1);
        var builder = createBuilder(createUser("dave"), usergroups);
        var result = builder.build("dave");

        assertEquals(Profile.Editor, result.getHighestProfile());
        assertEquals(1, result.getProfileGroups().size());
        var editorGroups = result.getProfileGroups().get(Profile.Editor);
        assertEquals(1, editorGroups.size());
        assertEquals(Integer.valueOf(100), editorGroups.get(0));

        assertEquals(1, result.getGroupProfiles().size());
        var groups100 = result.getGroupProfiles().get(100);
        assertEquals(1, groups100.size());
        assertEquals(Profile.Editor, groups100.get(0));
    }

    public User createUser(String username) {
        var user = new User();
        user.setUsername(username);
        user.setId(666);
        return user;
    }

    public GeonetworkAuthority.GeonetworkAuthorityBuilder createBuilder(User user, List<Usergroup> usergroups) {
        var userRepository = mock(UserRepository.class);
        if (user != null) {
            when(userRepository.findOptionalByUsername(user.getUsername())).thenReturn(Optional.of(user));
        }

        var usergroupRepository = mock(UsergroupRepository.class);
        if (usergroups != null) {
            when(usergroupRepository.findAllByUserid_Id(user.getId())).thenReturn(usergroups);
        }
        var builder = new GeonetworkAuthority.GeonetworkAuthorityBuilder(userRepository, usergroupRepository);
        return builder;
    }
}
