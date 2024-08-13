/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.security;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.Usergroup;
import org.geonetwork.repository.UserRepository;
import org.geonetwork.repository.UsergroupRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

@Slf4j(topic = "org.geonetwork.security")
public class DatabaseUserDetailsService extends AbstractUserDetailsAuthenticationProvider
    implements UserDetailsService {

  public static final String HIGHEST_PROFILE = "highest_profile";
  public static final String USER_ID = "user_id";
  public static final String GN_AUTHORITY = "gn";

  @Setter @Getter private DatabaseUserAuthProperties checkUsernameOrEmail;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final UsergroupRepository userGroupRepository;

  public DatabaseUserDetailsService(
      @Value("${geonetwork.security.databaseUserAuthProperty: 'USERNAME_OR_EMAIL'}")
          DatabaseUserAuthProperties checkUsernameOrEmail,
      PasswordEncoder passwordEncoder,
      UserRepository userRepository,
      UsergroupRepository userGroupRepository) {
    this.checkUsernameOrEmail = checkUsernameOrEmail;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.userGroupRepository = userGroupRepository;
  }

  @Override
  protected void additionalAuthenticationChecks(
      UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {

    User gnDetails = (User) userDetails;

    if (authentication.getCredentials() == null) {
      log.atWarn().log("Authentication failed: no credentials provided");
      throw new BadCredentialsException("Authentication failed: no credentials provided");
    }

    if (authentication.getCredentials().toString().isEmpty()
        || StringUtils.isEmpty(gnDetails.getPassword())
        || !passwordEncoder.matches(
            authentication.getCredentials().toString(), gnDetails.getPassword())) {
      log.atWarn().log("Authentication failed: wrong password provided");
      throw new BadCredentialsException("Authentication failed: wrong password provided");
    }
  }

  @Override
  protected UserDetails retrieveUser(
      String username, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    Optional<org.geonetwork.domain.User> user =
        switch (checkUsernameOrEmail) {
          case USERNAME -> userRepository.findOptionalByUsernameAndAuthtypeIsNull(username);
          case EMAIL -> userRepository.findOptionalByEmailAndAuthtypeIsNull(username);
          default ->
              userRepository.findOptionalByUsernameOrEmailAndAuthtypeIsNull(username, username);
        };

    if (user.isEmpty()) {
      throw new UsernameNotFoundException(username + " is not a valid username");
    }

    if ("n".equals(user.get().getIsenabled())) {
      throw new UsernameNotFoundException(username + " account is disabled");
    }

    List<Usergroup> userGroups = userGroupRepository.findAllByUserid_Id(user.get().getId());

    Map<String, List<Integer>> attributesToCast =
        userGroups.stream()
            .collect(
                Collectors.groupingBy(
                    ug -> Profile.values()[ug.getId().getProfile()].name(),
                    Collectors.mapping(ug -> ug.getGroupid().getId(), Collectors.toList())));

    String mainUserProfile = user.get().getProfile().name();
    Map<String, Object> attributes = new HashMap<>(attributesToCast);
    attributes.put(USER_ID, user.get().getId());
    attributes.put(HIGHEST_PROFILE, mainUserProfile);
    attributes.putIfAbsent(Profile.UserAdmin.name(), Collections.emptyList());
    attributes.putIfAbsent(Profile.Reviewer.name(), Collections.emptyList());
    attributes.putIfAbsent(Profile.RegisteredUser.name(), Collections.emptyList());
    attributes.putIfAbsent(Profile.Editor.name(), Collections.emptyList());

    OAuth2UserAuthority authority = new OAuth2UserAuthority(GN_AUTHORITY, attributes);

    return org.springframework.security.core.userdetails.User.withUsername(user.get().getUsername())
        .password(user.get().getPassword())
        .authorities(Collections.singletonList(authority))
        .roles(mainUserProfile)
        .build();
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return retrieveUser(username, null);
  }
}
