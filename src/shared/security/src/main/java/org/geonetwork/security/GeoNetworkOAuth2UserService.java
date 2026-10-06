/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import io.micrometer.common.util.StringUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.Group;
import org.geonetwork.domain.Groupsde;
import org.geonetwork.domain.GroupsdeId;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.User;
import org.geonetwork.domain.Usergroup;
import org.geonetwork.domain.UsergroupId;
import org.geonetwork.domain.repository.GroupRepository;
import org.geonetwork.domain.repository.GroupsdeRepository;
import org.geonetwork.domain.repository.LanguageRepository;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.domain.repository.UsergroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

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
 *
 * <p>3. Optionally, groups and profiles are set from a claim of the token (see
 * {@link GeoNetworkSsoConfiguration.Registration}). Claim values are mapped explicitly and/or with a regex:
 *
 * <pre>
 * geonetwork:
 *   security:
 *     oauth2:
 *       userCreationRegistration:
 *         keycloak:                       # spring.security.oauth2.client.registration id
 *           rolesClaim: groups
 *           rolePattern: '^(?&lt;group&gt;[^:]+):(?&lt;profile&gt;[^:]+)$'   # optional
 *           roleMappings:            # optional
 *             - claimValue: catalogue-editors
 *               group: DRAFTS     # absent = global profile
 *               profile: Editor
 *           createMissingGroups: false
 *           updateGroup: false       # true = IdP replaces all groups and profile at each login
 * </pre>
 */
@Slf4j
@Component
public class GeoNetworkOAuth2UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GeoNetworkUserService geoNetworkUserService;

    @Autowired
    GeoNetworkSsoConfiguration geoNetworkSsoConfiguration;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    GroupsdeRepository groupsdeRepository;

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    UsergroupRepository usergroupRepository;

    @Autowired
    TransactionTemplate transactionTemplate;

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

        // ---- a disabled account can not sign in, same as with the database login ----------
        if (dbUser.isPresent() && Boolean.FALSE.equals(dbUser.get().getIsenabled())) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("user_disabled", username + " account is disabled", null));
        }

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

        // one transaction: a failure must not leave the user without groups or a group without labels
        var current = dbUser.get();
        transactionTemplate.executeWithoutResult(status -> applyRoleMappings(current, oAuth2User, userCreationInfo));
        return current;
    }

    /** Set the groups and profile of the user from the roles claim of the token, see the class documentation. */
    void applyRoleMappings(User user, OAuth2User oAuth2User, GeoNetworkSsoConfiguration.Registration config) {
        if (!config.roleMappingEnabled()) {
            return;
        }
        var claim = oAuth2User.getAttributes().get(config.getRolesClaim());
        Collection<?> claimValues;
        if (claim instanceof Collection<?> c) {
            claimValues = c;
        } else if (claim != null) {
            claimValues = List.of(claim);
        } else if (config.isUpdateGroup()) {
            // IdPs often omit the claim for users without groups: with updateGroup that means "no groups"
            claimValues = List.of();
        } else {
            return;
        }

        Set<UsergroupId> wanted = new LinkedHashSet<>();
        Profile profile = config.isUpdateGroup() ? Profile.RegisteredUser : user.getProfile();
        for (Object value : claimValues) {
            var claimValue = String.valueOf(value);
            for (var mapping : config.getRoleMappings()) {
                if (claimValue.equals(mapping.getClaimValue())) {
                    profile = apply(user, mapping.getGroup(), mapping.getProfile(), config, wanted, profile);
                }
            }
            var pattern = config.compiledRolePattern();
            if (pattern != null) {
                Matcher matcher = pattern.matcher(claimValue);
                if (matcher.matches()) {
                    var mapped = Profile.findProfileIgnoreCase(matcher.group("profile"));
                    if (mapped == null) {
                        log.warn("Unknown profile in claim value '{}', ignored", claimValue);
                        continue;
                    }
                    String group = groupName(matcher);
                    profile = apply(user, group, mapped, config, wanted, profile);
                }
            }
        }

        if (config.isUpdateGroup()) {
            var stale = usergroupRepository.findAllByUserid_Id(user.getId()).stream()
                    .filter(ug -> !wanted.contains(ug.getId()))
                    .toList();
            usergroupRepository.deleteAll(stale);
        }
        for (var id : wanted) {
            if (!usergroupRepository.existsById(id)) {
                usergroupRepository.save(Usergroup.builder()
                        .id(id)
                        .groupid(groupRepository.getReferenceById(id.getGroupid()))
                        .userid(user)
                        .build());
            }
        }
        if (user.getProfile() != profile) {
            user.setProfile(profile);
            userRepository.save(user);
        }
    }

    private static String groupName(Matcher matcher) {
        try {
            var group = matcher.group("group");
            return StringUtils.isBlank(group) ? null : group;
        } catch (IllegalArgumentException e) {
            // pattern has no 'group' named group
            return null;
        }
    }

    /** Registers one group/profile pair (when the group is usable) and returns the raised global profile. */
    private Profile apply(
            User user,
            String groupName,
            Profile mapped,
            GeoNetworkSsoConfiguration.Registration config,
            Set<UsergroupId> wanted,
            Profile current) {
        if (groupName != null && mapped == Profile.Monitor) {
            log.warn("Profile Monitor cannot be set on group '{}', ignored", groupName);
            return current;
        }
        if (groupName != null) {
            // Administrator makes no sense on a group: the group-level equivalent is UserAdmin
            mapped = mapped == Profile.Administrator ? Profile.UserAdmin : mapped;
            var group = findOrCreateGroup(groupName, config.isCreateMissingGroups());
            if (group != null) {
                wanted.add(new UsergroupId(group.getId(), mapped.getId(), user.getId()));
                if (mapped == Profile.Reviewer) {
                    // GN4 looks for an explicit Editor row to allow editing in the group
                    wanted.add(new UsergroupId(group.getId(), Profile.Editor.getId(), user.getId()));
                }
            }
        }
        return raise(current, mapped);
    }

    /**
     * Returns the global profile after mapping {@code mapped}. Monitor is not part of the ladder (it is only included
     * by Administrator), so it only replaces a basic profile and any other profile replaces it.
     */
    static Profile raise(Profile current, Profile mapped) {
        if (current.getAll().contains(mapped)) {
            return current;
        }
        if (mapped == Profile.Monitor) {
            return isBasic(current) ? mapped : current;
        }
        if (current == Profile.Monitor) {
            return isBasic(mapped) ? current : mapped;
        }
        return mapped;
    }

    private static boolean isBasic(Profile profile) {
        return profile == Profile.RegisteredUser || profile == Profile.Guest;
    }

    private Group findOrCreateGroup(String name, boolean create) {
        if (name.length() > 32) {
            log.warn("Group name '{}' is longer than 32 characters, ignored", name);
            return null;
        }
        var existing = groupRepository.findOptionalByName(name);
        if (existing.isPresent()) {
            return existing.get();
        }
        if (!create) {
            log.warn("Group '{}' does not exist and createMissingGroups is false, ignored", name);
            return null;
        }
        var group = groupRepository.save(Group.builder().name(name).build());
        for (var language : languageRepository.findAll()) {
            groupsdeRepository.save(Groupsde.builder()
                    .id(new GroupsdeId(group.getId(), language.getId()))
                    .iddes(group)
                    .label(name)
                    .build());
        }
        return group;
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
