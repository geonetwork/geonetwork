/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.security;

import static org.geonetwork.security.GeoNetworkUserService.isUserFoundAndEnabled;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.User;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.security.user.UserManager;
import org.geonetwork.utility.date.ISODate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(name = "geonetwork.security.provider", havingValue = "ldap")
public class LdapAuthoritiesMapperService {
    UserRepository userRepository;
    UserManager userManager;
    GeoNetworkUserService geoNetworkUserService;
    String defaultUserGroup;
    Profile defaultUserProfile;
    String usernameAttribute;
    String emailAttribute;
    String nameAttribute;
    String surnameAttribute;

    public LdapAuthoritiesMapperService(
            @Value("${geonetwork.security.createdUser.group: ''}") String defaultUserGroup,
            @Value("${geonetwork.security.createdUser.profile: 'Guest'}") String defaultUserProfile,
            @Value("${geonetwork.security.ldap.attributes.username: 'uid'}") String usernameAttribute,
            @Value("${geonetwork.security.ldap.attributes.email: ''}") String emailAttribute,
            @Value("${geonetwork.security.ldap.attributes.name: 'cn'}") String nameAttribute,
            @Value("${geonetwork.security.ldap.attributes.surname: ''}") String surnameAttribute,
            UserRepository userRepository,
            UserManager userManager,
            GeoNetworkUserService geoNetworkUserService) {
        this.defaultUserGroup = defaultUserGroup;
        this.defaultUserProfile = Profile.valueOf(defaultUserProfile);

        this.usernameAttribute = usernameAttribute;
        this.emailAttribute = emailAttribute;
        this.nameAttribute = nameAttribute;
        this.surnameAttribute = surnameAttribute;

        this.userRepository = userRepository;
        this.userManager = userManager;
        this.geoNetworkUserService = geoNetworkUserService;
    }

    private String getLdapAttribute(String attributeName, DirContextOperations ctx, String defaultValue) {
        try {
            return ctx.getStringAttribute(attributeName);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public UserDetailsContextMapper ldapUserContextMapper() {
        var profile = this.defaultUserProfile;
        return new UserDetailsContextMapper() {
            @Override
            public UserDetails mapUserFromContext(
                    DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
                Optional<User> ldapUser = userRepository.findOptionalByUsername(username);
                User user = ldapUser.map(existingUser -> {
                            isUserFoundAndEnabled(username, ldapUser);

                            existingUser.setUsername(getLdapAttribute(usernameAttribute, ctx, username));
                            userManager.userLoginEvent(existingUser);

                            return existingUser;
                        })
                        .orElseGet(() -> userManager.registerUser(
                                username,
                                "",
                                getLdapAttribute(nameAttribute, ctx, username),
                                getLdapAttribute(surnameAttribute, ctx, ""),
                                getLdapAttribute(emailAttribute, ctx, ""),
                                // FIXME If set GN4 try to create JWT user
                                //  "LDAP"
                                "",
                                new ISODate().toString(),
                                null,
                                profile));

                OAuth2UserAuthority authority = geoNetworkUserService.buildUserAuthority(user);

                return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                        .authorities(Collections.singletonList(authority))
                        .password("")
                        .roles(user.getProfile().name())
                        .build();
            }

            @Override
            public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {}
        };
    }
}
