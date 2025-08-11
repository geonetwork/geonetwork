/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.security.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.User;
import org.geonetwork.domain.Usergroup;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.domain.repository.UsergroupRepository;
import org.geonetwork.utility.date.ISODate;
import org.springframework.stereotype.Service;

@Service
public class UserManager implements IUserManager {

    private final UserRepository userRepository;
    private final UsergroupRepository usergroupRepository;

    public UserManager(final UserRepository userRepository, final UsergroupRepository usergroupRepository) {
        this.userRepository = userRepository;
        this.usergroupRepository = usergroupRepository;
    }

    @Override
    public User getUserByUsername(String username) throws UserNotFoundException {
        Optional<User> userOpt = this.userRepository.findOptionalByUsername(username);

        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            throw new UserNotFoundException(String.format("User '%s' not found", username));
        }
    }

    @Override
    public List<Usergroup> getUserGroups(int userId) {
        return usergroupRepository.findAllByUserid_Id(userId);
    }

    @Override
    public List<Usergroup> getUserGroups(int userId, Profile profile) {
        List<Usergroup> usergroups = getUserGroups(userId);

        return usergroups.stream()
                .filter(usergroup -> usergroup.getId().getProfile().equals(profile.getId()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public User registerUser(
            String username,
            String password,
            String name,
            String surname,
            String email,
            String authType,
            String loginDate,
            String company,
            Profile profile) {
        User newUser = User.builder()
                .isenabled("y")
                .password("")
                .username(username)
                .name(Optional.ofNullable(name).orElse(""))
                .surname(Optional.ofNullable(surname).orElse(""))
                // GN4 `GeonetworkAuthenticationProvider` expects this to be null or empty
                .authtype(authType)
                .email(email == null ? null : Set.of(email))
                .organisation(Optional.ofNullable(company).orElse(""))
                .lastlogindate(loginDate)
                .profile(Optional.ofNullable(profile).orElse(Profile.RegisteredUser))
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public void userLoginEvent(User user) {
        user.setLastlogindate(new ISODate().toString());
        userRepository.save(user);
    }
}
