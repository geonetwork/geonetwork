/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.security;

import static org.geonetwork.security.GeoNetworkUserService.isUserFoundAndEnabled;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.User;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.domain.repository.UsergroupRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

/** Service to map OAuth authorities to GeoNetwork authority. */
@Service
@AllArgsConstructor
@Slf4j
public class OauthAuthoritiesMapperService {
    UserRepository userRepository;
    UsergroupRepository userGroupRepository;
    GeoNetworkUserService geoNetworkUserService;

    public GrantedAuthoritiesMapper userOauthAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                if (authority instanceof OidcUserAuthority) {
                    // Handle OIDC user authority
                } else if (authority instanceof OAuth2UserAuthority oauth2UserAuthority) {
                    Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();
                    // in github, emails are typically hidden, so this will be null and cause issues...
                    String email = userAttributes.get("email") != null
                            ? userAttributes.get("email").toString()
                            : null;

                    Optional<User> oauthUser =
                            (email == null) ? Optional.empty() : userRepository.findOptionalByEmail(email);

                    User user = oauthUser
                            .map(existingUser -> {
                                isUserFoundAndEnabled(email, oauthUser);

                                existingUser.setUsername(Optional.ofNullable(userAttributes.get("name"))
                                        .orElse(email)
                                        .toString());
                                userRepository.save(existingUser);

                                return existingUser;
                            })
                            .orElseGet(() -> {
                                String username = Optional.ofNullable(userAttributes.get("login"))
                                        .orElse(email)
                                        .toString();
                                User newUser = User.builder()
                                        .isenabled("y")
                                        .password("")
                                        .username(username)
                                        .name(Optional.ofNullable(userAttributes.get("name"))
                                                .orElse("")
                                                .toString())
                                        .surname("")
                                        .authtype(authority.getAuthority())
                                        .email(email == null ? null : Set.of(email))
                                        .organisation(Optional.ofNullable(userAttributes.get("company"))
                                                .orElse("")
                                                .toString())
                                        .build();
                                userRepository.save(newUser);
                                return newUser;
                            });

                    mappedAuthorities.add(geoNetworkUserService.buildUserAuthority(user));
                }
            });

            return mappedAuthorities;
        };
    }
}
