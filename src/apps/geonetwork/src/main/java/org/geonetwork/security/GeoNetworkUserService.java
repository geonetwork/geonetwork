/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.security;

import static org.geonetwork.security.DatabaseUserDetailsService.GN_AUTHORITY;
import static org.geonetwork.security.DatabaseUserDetailsService.HIGHEST_PROFILE;
import static org.geonetwork.security.DatabaseUserDetailsService.USER_ID;
import static org.geonetwork.security.DatabaseUserDetailsService.USER_NAME;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.User;
import org.geonetwork.domain.Usergroup;
import org.geonetwork.domain.repository.UsergroupRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class GeoNetworkUserService {
    UsergroupRepository userGroupRepository;

    public static boolean isUserFoundAndEnabled(String username, Optional<User> user) {
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username + " is not a valid username");
        }

        if ("n".equals(user.get().getIsenabled())) {
            throw new UsernameNotFoundException(username + " account is disabled");
        }

        return true;
    }

    public OAuth2UserAuthority buildUserAuthority(org.geonetwork.domain.User currentUser) {
        List<Usergroup> userGroups = userGroupRepository.findAllByUserid_Id(currentUser.getId());

        Map<String, List<Integer>> attributesToCast = userGroups.stream()
                .collect(Collectors.groupingBy(
                        ug -> Profile.values()[ug.getId().getProfile()].name(),
                        Collectors.mapping(ug -> ug.getGroupid().getId(), Collectors.toList())));

        String mainUserProfile = currentUser.getProfile().name();
        Map<String, Object> attributes = new HashMap<>(attributesToCast);
        attributes.put(USER_ID, currentUser.getId());
        attributes.put(USER_NAME, currentUser.getUsername());
        attributes.put(HIGHEST_PROFILE, mainUserProfile);
        attributes.putIfAbsent(Profile.UserAdmin.name(), Collections.emptyList());
        attributes.putIfAbsent(Profile.Reviewer.name(), Collections.emptyList());
        attributes.putIfAbsent(Profile.RegisteredUser.name(), Collections.emptyList());
        attributes.putIfAbsent(Profile.Editor.name(), Collections.emptyList());

        OAuth2UserAuthority authority = new OAuth2UserAuthority(GN_AUTHORITY, attributes);
        return authority;
    }
}
