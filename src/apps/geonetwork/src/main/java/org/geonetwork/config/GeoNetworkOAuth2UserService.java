/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.config;

import io.micrometer.common.util.StringUtils;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.geonetwork.domain.User;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.security.GeoNetworkUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * See https://docs.spring.io/spring-security/site/docs/5.2.12.RELEASE/reference/html/oauth2.html for more details.
 *
 * <p>This extends the OidcUserService and DefaultOAuth2UserService/OAuth2UserService.
 *
 * <p>This will: 1. Create the User in the GN DB if it doesn't exist + username will be the providers #.getName() +
 * typically you override this in application.yml. This will change it to the `login` attribute:
 *
 * <p>security: oauth2: client: registration: github: clientId: Iv23libHkqBUI94qreiG clientSecret:
 * 333af274bd8ae91909f680770eb5074bd8f447d4 # setup the provider (github) so we use the github username (called `login`)
 * as the GN DB username # github defaults to "id" - which is a large number. provider: github: user-name-attribute:
 * login
 *
 * <p>2. User authorities (i.e. ADMIN) are then added from the GN DB
 *
 * <p>We return a new OIDC/OAuth2 user with the authorities modified.
 */
@Component
public class GeoNetworkOAuth2UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GeoNetworkUserService geoNetworkUserService;

    @Autowired
    GeoNetworkSsoConfiguration geoNetworkSsoConfiguration;

    /**
     * FOR OIDC
     *
     * @return lambda for OidcUserService
     */
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {
            // Delegate to the default implementation for loading a user
            OidcUser oidcUser = delegate.loadUser(userRequest);

            // i.e. "github"
            var ssoRegistrationName = userRequest.getClientRegistration().getRegistrationId();
            var userCreationInfo = geoNetworkSsoConfiguration.getRegistrationInfo(ssoRegistrationName);

            var dbUser = getOrCreate(oidcUser, userCreationInfo);

            // ---- get the "gn" authorities for the user  ----------

            // NOTE: we are doing the authorities here because they are based on
            //       what is in the GN DB and are not simply "mapping" authorities from
            //       one vocabulary to another.

            var userAuthority = geoNetworkUserService.buildUserAuthority(dbUser);

            String userNameAttributeName = userRequest
                    .getClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUserNameAttributeName();

            // rebuild the user with the new authorities
            var modifiedOidcUser = new DefaultOidcUser(
                    Collections.singletonList(userAuthority),
                    oidcUser.getIdToken(),
                    oidcUser.getUserInfo(),
                    userNameAttributeName);

            return modifiedOidcUser;
        };
    }

    /**
     * FOR OAUTH2
     *
     * @return lambda for DefaultOAuth2UserService
     */
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> userService() {
        final var delegate = new DefaultOAuth2UserService();

        return (userRequest) -> {
            // Delegate to the default implementation for loading a user
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) delegate.loadUser(userRequest);

            // i.e. "github"
            var ssoRegistrationName = userRequest.getClientRegistration().getRegistrationId();
            var userCreationInfo = geoNetworkSsoConfiguration.getRegistrationInfo(ssoRegistrationName);

            var dbUser = getOrCreate(oAuth2User, userCreationInfo);

            // ---- get the "gn" authorities for the user  ----------

            // NOTE: we are doing the authorities here because they are based on
            //       what is in the GN DB and are not simply "mapping" authorities from
            //       one vocabulary to another.

            var userAuthority = geoNetworkUserService.buildUserAuthority(dbUser);

            String userNameAttributeName = userRequest
                    .getClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUserNameAttributeName();

            // rebuild the user with the new authorities
            var modifiedOAuth2User = new DefaultOAuth2User(
                    Collections.singletonList(userAuthority), oAuth2User.getAttributes(), userNameAttributeName);

            return modifiedOAuth2User;
        };
    }

    /**
     * Given a User (either from OIDC or OAuth2), get the user from the database or create a new user in the database.
     * username --> from the OIDC/OAuth2 User#getName() -- configure in your application.yml (see above)
     *
     * <p>NOTE: OIDC-user is a subclass of OAuth2User, so this works for both cases.
     *
     * @param oAuth2User User (either from OIDC or OAuth2)
     * @param userCreationInfo Info about how to take info from the OAuth2User to the GN User
     * @return user from DB (might be created if doesnt already exist)
     */
    public User getOrCreate(OAuth2User oAuth2User, GeoNetworkSsoConfiguration.Registration userCreationInfo) {
        if (oAuth2User == null || StringUtils.isBlank(oAuth2User.getName())) {
            throw new RuntimeException("User name not found!");
        }

        var username = oAuth2User.getName();

        // attempt to load user
        var dbUser = userRepository.findOptionalByUsername(username);

        // ---- user is missing, create them ----------
        if (dbUser.isEmpty()) {
            // create the user in the DB
            var email = getValue(oAuth2User, userCreationInfo.getEmail());
            var name = getValue(oAuth2User, userCreationInfo.getName());
            var surname = getValue(oAuth2User, userCreationInfo.getSurname());
            var company = getValue(oAuth2User, userCreationInfo.getOrganization());
            User newUser = User.builder()
                    .isenabled(true)
                    .password("")
                    .username(username)
                    .name(Optional.ofNullable(name).orElse(""))
                    .surname(Optional.ofNullable(surname).orElse(""))
                    // GN4 `GeonetworkAuthenticationProvider` expects this to be null or empty
                    .authtype(null)
                    .email(email == null ? null : Set.of(email))
                    .organisation(Optional.ofNullable(company).orElse(""))
                    .build();
            var user = userRepository.save(newUser);
            dbUser = Optional.of(user);
        }

        return dbUser.get();
    }

    public String getValue(OAuth2User oAuth2User, String value) {
        if (value == null) {
            return null;
        }
        var result = oAuth2User.getAttributes().get(value);
        if (result == null) {
            return null;
        }
        var resultStr = result.toString();
        if (StringUtils.isBlank(resultStr)) {
            return null;
        }
        return resultStr;
    }
}
