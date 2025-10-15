/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.security;

import static org.geonetwork.security.GeoNetworkUserService.isUserFoundAndEnabled;

import java.util.Collections;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.domain.repository.UserRepository;
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

@Slf4j
public class DatabaseUserDetailsService extends AbstractUserDetailsAuthenticationProvider
        implements UserDetailsService {

    public static final String HIGHEST_PROFILE = "highest_profile";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "username";
    public static final String GN_AUTHORITY = "gn";

    @Setter
    @Getter
    private DatabaseUserAuthProperties checkUsernameOrEmail;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final GeoNetworkUserService geoNetworkUserService;

    public DatabaseUserDetailsService(
            @Value("${geonetwork.security.databaseUserAuthProperty: 'USERNAME_OR_EMAIL'}")
                    DatabaseUserAuthProperties checkUsernameOrEmail,
            PasswordEncoder passwordEncoder,
            GeoNetworkUserService geoNetworkUserService,
            UserRepository userRepository) {
        this.checkUsernameOrEmail = checkUsernameOrEmail;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.geoNetworkUserService = geoNetworkUserService;
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
                || !passwordEncoder.matches(authentication.getCredentials().toString(), gnDetails.getPassword())) {
            log.atWarn().log("Authentication failed: wrong password provided");
            throw new BadCredentialsException("Authentication failed: wrong password provided");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        Optional<org.geonetwork.domain.User> user =
                switch (checkUsernameOrEmail) {
                    case USERNAME -> userRepository.findOptionalByUsernameAndAuthtypeIsNull(username);
                    case EMAIL -> userRepository.findOptionalByEmailAndAuthtypeIsNull(username);
                    default -> userRepository.findOptionalByUsernameOrEmailAndAuthtypeIsNull(username, username);
                };

        isUserFoundAndEnabled(username, user);

        org.geonetwork.domain.User currentUser = user.get();

        var authority = geoNetworkUserService.buildUserAuthority(user.get());

        return org.springframework.security.core.userdetails.User.withUsername(currentUser.getUsername())
                .password(currentUser.getPassword())
                .authorities(Collections.singletonList(authority))
                //  .roles(currentUser.getProfile().name())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return retrieveUser(username, null);
    }
}
