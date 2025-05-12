/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.security;

import io.micrometer.common.util.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.domain.repository.UsergroupRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Simple GrantedAuthority implementation for GN user security concerns.
 *
 * <p>1. GeonetworkAuthority + simple class for capturing standard security info about a user + username + userId +
 * highest profile + profile groups + map from Profile -> list of group ids that the user has that Profile
 *
 * <p>2. GeonetworkAuthority.builder + gets the needed info from the database and builds the info up + if the user DB is
 * modified, the user should login/logout to refresh
 *
 * <p>You can use #getProfileGroups() or #getGroupProfiles() to access user info. They are "inverses" of each other.
 *
 * <p>#getProfileGroups().get(Profile.editor) --> list of groups that the user has editor permission (profile)
 *
 * <p>#getGroupProfiles().get(100) --> list of profiles that the user has for that group
 */
@NoArgsConstructor
public class GeonetworkAuthority implements GrantedAuthority {

    /** default for anonymous */
    public static final GeonetworkAuthority GUESTAUTHORITY;

    static {
        GUESTAUTHORITY = new GeonetworkAuthority();
        GUESTAUTHORITY.setHighestProfile(Profile.Guest);
    }

    /** maybe null for GUEST */
    @Getter
    @Setter
    String username;

    /** always >= Profile.Guest */
    @Getter
    @Setter
    Profile highestProfile;

    /** maybe null for GUEST */
    @Getter
    @Setter
    Integer userId;

    /**
     * map from Profile -> list of group ids that the user has that Profile (see DB table `usergroup`) opposite of
     * groupProfiles
     */
    Map<Profile, List<Integer>> profileGroups = new HashMap<>();

    /**
     * map from GroupID -> list of Profiles that the user has that Group (see DB table `usergroup`). opposite of
     * profileGroups
     */
    Map<Integer, List<Profile>> groupProfiles = new HashMap<>();

    @Override
    public String getAuthority() {
        return "gn";
    }

    public Map<Profile, List<Integer>> getProfileGroups() {
        return Collections.unmodifiableMap(profileGroups);
    }

    public Map<Integer, List<Profile>> getGroupProfiles() {
        return Collections.unmodifiableMap(groupProfiles);
    }

    public void addProfileGroup(Profile profile, List<Integer> groups) {
        profileGroups.put(profile, groups);
    }

    public void addGroupProfile(Integer groupId, List<Profile> profiles) {
        groupProfiles.put(groupId, profiles);
    }

    public boolean isAnonymous() {
        return username == null;
    }

    public boolean isAdmin() {
        return highestProfile.equals(Profile.Administrator);
    }

    /** Builds a GeonetworkAuthority from the database. */
    @Component
    @AllArgsConstructor
    public static class GeonetworkAuthorityBuilder {

        UserRepository userRepository;
        UsergroupRepository usergroupRepository;

        /**
         * builds a GeonetworkAuthority from the database.
         *
         * @param username DB table `users`.
         * @return GeonetworkAuthority from the database.
         */
        public GeonetworkAuthority build(String username) {
            if (StringUtils.isBlank(username)) {
                return GeonetworkAuthority.GUESTAUTHORITY;
            }
            var userOptional = userRepository.findOptionalByUsername(username); // should always exist

            if (userOptional.isEmpty()) {
                return GeonetworkAuthority.GUESTAUTHORITY; // anonymous
            }

            var user = userOptional.get();

            GeonetworkAuthority authority = new GeonetworkAuthority();
            authority.username = user.getUsername();
            authority.userId = user.getId();

            var usergroups = usergroupRepository.findAllByUserid_Id(user.getId());

            Profile highestProfile = Profile.RegisteredUser;

            for (var usergroup : usergroups) {
                var profile = Profile.values()[usergroup.getId().getProfile()];
                if (highestProfile.compareTo(profile) >= 0) {
                    highestProfile = profile;
                }
                var groupid = usergroup.getId().getGroupid();
                if (!authority.getProfileGroups().containsKey(profile)) {
                    authority.addProfileGroup(profile, new ArrayList<>());
                }
                if (!authority.getGroupProfiles().containsKey(groupid)) {
                    authority.addGroupProfile(groupid, new ArrayList<>());
                }
                var list = authority.getProfileGroups().get(profile);
                list.add(groupid);
                var list2 = authority.getGroupProfiles().get(groupid);
                list2.add(profile);
            }

            authority.setHighestProfile(highestProfile);
            if (user.getProfile().compareTo(highestProfile) < 0) {
                authority.setHighestProfile(user.getProfile());
            }

            return authority;
        }
    }
}
