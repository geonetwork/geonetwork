/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.geonetwork.domain.Group;
import org.geonetwork.domain.Language;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.User;
import org.geonetwork.domain.Usergroup;
import org.geonetwork.domain.UsergroupId;
import org.geonetwork.domain.repository.GroupRepository;
import org.geonetwork.domain.repository.GroupsdeRepository;
import org.geonetwork.domain.repository.LanguageRepository;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.domain.repository.UsergroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

class GeoNetworkOAuth2UserServiceTest {

    private static final String EDITORS = "catalogue-editors";

    private final UserRepository userRepository = mock(UserRepository.class);
    private final GroupRepository groupRepository = mock(GroupRepository.class);
    private final GroupsdeRepository groupsdeRepository = mock(GroupsdeRepository.class);
    private final LanguageRepository languageRepository = mock(LanguageRepository.class);
    private final UsergroupRepository usergroupRepository = mock(UsergroupRepository.class);
    private final GeoNetworkOAuth2UserService service = new GeoNetworkOAuth2UserService();
    private final User user = User.builder().id(7).username("alice").build();

    @BeforeEach
    void setUp() {
        service.userRepository = userRepository;
        service.groupRepository = groupRepository;
        service.groupsdeRepository = groupsdeRepository;
        service.languageRepository = languageRepository;
        service.usergroupRepository = usergroupRepository;
        existingGroup("DRAFTS", 10);
        existingGroup("SAMPLE", 11);
    }

    private void existingGroup(String name, int id) {
        when(groupRepository.findOptionalByName(name))
                .thenReturn(Optional.of(Group.builder().id(id).name(name).build()));
    }

    private static GeoNetworkSsoConfiguration.Registration config(Map<String, Object> props) {
        var registration = new GeoNetworkSsoConfiguration.Registration();
        new Binder(new MapConfigurationPropertySource(props)).bind("r", Bindable.ofInstance(registration));
        return registration;
    }

    private static OAuth2User tokenWith(Object groups) {
        var attributes =
                groups == null ? Map.<String, Object>of("sub", "alice") : Map.of("sub", "alice", "groups", groups);
        return new DefaultOAuth2User(List.of(new SimpleGrantedAuthority("x")), attributes, "sub");
    }

    private List<UsergroupId> savedRows() {
        var captor = ArgumentCaptor.forClass(Usergroup.class);
        verify(usergroupRepository, org.mockito.Mockito.atLeast(0)).save(captor.capture());
        return captor.getAllValues().stream().map(Usergroup::getId).toList();
    }

    private static Map<String, Object> mapping(
            Map<String, Object> props, int i, String claim, String group, String profile) {
        props.put("r.role-mappings[" + i + "].claim-value", claim);
        if (group != null) props.put("r.role-mappings[" + i + "].group", group);
        if (profile != null) props.put("r.role-mappings[" + i + "].profile", profile);
        return props;
    }

    @Test
    void partialRegistrationKeepsDefaults() {
        var registration = config(Map.of("r.roles-claim", "roles"));

        assertEquals("roles", registration.getRolesClaim());
        assertEquals("email", registration.getEmail());
        assertTrue(registration.getRoleMappings().isEmpty());
        assertEquals(false, registration.roleMappingEnabled());
    }

    @Test
    void oneClaimValueGivesSeveralGroupProfilePairs() {
        var props = new java.util.HashMap<String, Object>();
        mapping(props, 0, EDITORS, "DRAFTS", "Editor");
        mapping(props, 1, EDITORS, "SAMPLE", "Reviewer");

        service.applyRoleMappings(user, tokenWith(List.of(EDITORS)), config(props));

        assertEquals(
                List.of(
                        new UsergroupId(10, Profile.Editor.getId(), 7),
                        new UsergroupId(11, Profile.Reviewer.getId(), 7),
                        // Reviewer implies an explicit Editor row, as GN4 writes it
                        new UsergroupId(11, Profile.Editor.getId(), 7)),
                savedRows());
        assertEquals(Profile.Reviewer, user.getProfile());
        verify(userRepository).save(user);
    }

    @Test
    void noMatchOrMissingClaimChangesNothing() {
        var props = new java.util.HashMap<String, Object>();
        mapping(props, 0, EDITORS, "DRAFTS", "Editor");

        service.applyRoleMappings(user, tokenWith(List.of("other")), config(props));
        service.applyRoleMappings(user, tokenWith(null), config(props));

        verify(usergroupRepository, never()).save(any());
        verify(usergroupRepository, never()).deleteAll(anyList());
        assertEquals(Profile.RegisteredUser, user.getProfile());
    }

    @Test
    void updateGroupTreatsMissingClaimAsNoGroups() {
        var stale = Usergroup.builder()
                .id(new UsergroupId(11, Profile.Reviewer.getId(), 7))
                .build();
        when(usergroupRepository.findAllByUserid_Id(7)).thenReturn(List.of(stale));
        var props = new java.util.HashMap<String, Object>();
        mapping(props, 0, EDITORS, "DRAFTS", "Editor");
        props.put("r.update-group", "true");
        user.setProfile(Profile.Reviewer);

        service.applyRoleMappings(user, tokenWith(null), config(props));

        verify(usergroupRepository).deleteAll(List.of(stale));
        assertEquals(Profile.RegisteredUser, user.getProfile());
    }

    @Test
    void emptyProfileInMappingDefaultsToEditor() {
        var props = new java.util.HashMap<String, Object>();
        mapping(props, 0, EDITORS, "DRAFTS", "");

        assertEquals(Profile.Editor, config(props).getRoleMappings().get(0).getProfile());
    }

    @Test
    void patternWithAndWithoutGroup() {
        var props = new java.util.HashMap<String, Object>();
        props.put("r.role-pattern", "^(?<group>[^:]+):(?<profile>[^:]+)$");

        service.applyRoleMappings(user, tokenWith(List.of("DRAFTS:editor")), config(props));

        assertEquals(List.of(new UsergroupId(10, Profile.Editor.getId(), 7)), savedRows());

        var global = new java.util.HashMap<String, Object>();
        global.put("r.role-pattern", "^gn-(?<profile>.+)$");
        var other = User.builder().id(8).username("bob").build();
        service.applyRoleMappings(other, tokenWith("gn-Reviewer"), config(global));

        assertEquals(Profile.Reviewer, other.getProfile());
        verify(usergroupRepository, org.mockito.Mockito.times(1)).save(any());
    }

    @Test
    void administratorWithoutGroupIsAGlobalAdministratorWithNoGroupRows() {
        var props = new java.util.HashMap<String, Object>();
        mapping(props, 0, "gn-admins", null, "Administrator");

        service.applyRoleMappings(user, tokenWith(List.of("gn-admins")), config(props));

        verify(usergroupRepository, never()).save(any());
        assertEquals(Profile.Administrator, user.getProfile());
        verify(userRepository).save(user);
    }

    @Test
    void administratorOnAGroupBecomesUserAdmin() {
        var props = new java.util.HashMap<String, Object>();
        mapping(props, 0, EDITORS, "DRAFTS", "Administrator");

        service.applyRoleMappings(user, tokenWith(List.of(EDITORS)), config(props));

        assertEquals(List.of(new UsergroupId(10, Profile.UserAdmin.getId(), 7)), savedRows());
        assertEquals(Profile.UserAdmin, user.getProfile());
    }

    @Test
    void missingGroupIsSkippedUnlessCreationIsEnabled() {
        when(groupRepository.findOptionalByName("NEW")).thenReturn(Optional.empty());
        when(groupRepository.save(any(Group.class))).thenAnswer(i -> {
            Group g = i.getArgument(0);
            g.setId(99);
            return g;
        });
        when(languageRepository.findAll())
                .thenReturn(List.of(Language.builder().id("eng").build()));
        var props = new java.util.HashMap<String, Object>();
        mapping(props, 0, EDITORS, "NEW", "Editor");

        service.applyRoleMappings(user, tokenWith(List.of(EDITORS)), config(props));
        verify(usergroupRepository, never()).save(any());
        // a skipped group must not raise the global profile either
        assertEquals(Profile.RegisteredUser, user.getProfile());

        props.put("r.create-missing-groups", "true");
        service.applyRoleMappings(user, tokenWith(List.of(EDITORS)), config(props));

        assertEquals(List.of(new UsergroupId(99, Profile.Editor.getId(), 7)), savedRows());
        verify(groupsdeRepository).save(any());
    }

    @Test
    void updateGroupRemovesOtherRowsAndDowngrades() {
        user.setProfile(Profile.Administrator);
        var stale = Usergroup.builder()
                .id(new UsergroupId(11, Profile.Reviewer.getId(), 7))
                .build();
        when(usergroupRepository.findAllByUserid_Id(7)).thenReturn(List.of(stale));
        var props = new java.util.HashMap<String, Object>();
        mapping(props, 0, EDITORS, "DRAFTS", "Editor");
        props.put("r.update-group", "true");

        service.applyRoleMappings(user, tokenWith(List.of(EDITORS)), config(props));

        verify(usergroupRepository).deleteAll(List.of(stale));
        assertEquals(List.of(new UsergroupId(10, Profile.Editor.getId(), 7)), savedRows());
        assertEquals(Profile.Editor, user.getProfile());
    }

    @Test
    void monitorOnlyReplacesBasicProfilesAndIsReplacedByOthers() {
        assertEquals(Profile.Monitor, GeoNetworkOAuth2UserService.raise(Profile.RegisteredUser, Profile.Monitor));
        assertEquals(Profile.Editor, GeoNetworkOAuth2UserService.raise(Profile.Editor, Profile.Monitor));
        assertEquals(Profile.Administrator, GeoNetworkOAuth2UserService.raise(Profile.Administrator, Profile.Monitor));
        assertEquals(Profile.Editor, GeoNetworkOAuth2UserService.raise(Profile.Monitor, Profile.Editor));
        assertEquals(Profile.Administrator, GeoNetworkOAuth2UserService.raise(Profile.Monitor, Profile.Administrator));
        assertEquals(Profile.Monitor, GeoNetworkOAuth2UserService.raise(Profile.Monitor, Profile.RegisteredUser));
    }

    @Test
    void monitorOnAGroupIsIgnored() {
        var props = new java.util.HashMap<String, Object>();
        mapping(props, 0, EDITORS, "DRAFTS", "Monitor");
        user.setProfile(Profile.Editor);

        service.applyRoleMappings(user, tokenWith(List.of(EDITORS)), config(props));

        verify(usergroupRepository, never()).save(any());
        assertEquals(Profile.Editor, user.getProfile());
    }

    @Test
    void disabledUserCanNotSignInAndIsNotModified() {
        var disabled = User.builder().id(9).username("alice").isenabled(false).build();
        when(userRepository.findOptionalByUsername("alice")).thenReturn(Optional.of(disabled));
        var props = new java.util.HashMap<String, Object>();
        mapping(props, 0, EDITORS, "DRAFTS", "Editor");

        assertThrows(
                org.springframework.security.oauth2.core.OAuth2AuthenticationException.class,
                () -> service.getOrCreate(tokenWith(List.of(EDITORS)), config(props)));

        verify(userRepository, never()).save(any());
        verify(usergroupRepository, never()).save(any());
        assertEquals(Profile.RegisteredUser, disabled.getProfile());
    }

    @Test
    void reservedGroupsAreIgnoredByIdAndByName() {
        existingGroup("all", 1);
        var props = new java.util.HashMap<String, Object>();
        mapping(props, 0, EDITORS, "all", "Editor");
        mapping(props, 1, EDITORS, "GUEST", "Editor");
        mapping(props, 2, EDITORS, "intranet", "Editor");

        service.applyRoleMappings(user, tokenWith(List.of(EDITORS)), config(props));

        verify(usergroupRepository, never()).save(any());
        verify(groupRepository, never()).save(any());
        assertEquals(Profile.RegisteredUser, user.getProfile());
    }

    @Test
    void rolePatternMustReallyDefineTheProfileGroup() {
        var registration = new GeoNetworkSsoConfiguration.Registration();
        // the text is only inside a regex comment, it is not a group
        assertThrows(IllegalArgumentException.class, () -> registration.setRolePattern("(?#(?<profile>)^.*$"));
    }

    @Test
    void compiledRolePatternCanNotBeBoundDirectly() {
        var registration = config(Map.of("r.compiled-role-pattern", "^(?<group>.+)$"));

        assertEquals(null, registration.compiledRolePattern());
    }

    @Test
    void oidcUserNameAttributeFallsBackToSub() {
        assertEquals("sub", GeoNetworkOAuth2UserService.userNameAttribute(null));
        assertEquals("sub", GeoNetworkOAuth2UserService.userNameAttribute(" "));
        assertEquals("login", GeoNetworkOAuth2UserService.userNameAttribute("login"));
    }

    @Test
    void patternWithoutProfileGroupIsRejected() {
        var registration = new GeoNetworkSsoConfiguration.Registration();
        assertThrows(IllegalArgumentException.class, () -> registration.setRolePattern("^(?<group>.+)$"));
    }
}
