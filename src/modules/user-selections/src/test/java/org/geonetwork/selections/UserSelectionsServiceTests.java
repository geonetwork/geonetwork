/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.selections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.Selection;
import org.geonetwork.domain.User;
import org.geonetwork.domain.Usersavedselection;
import org.geonetwork.domain.repository.LanguageRepository;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.domain.repository.SelectionRepository;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.domain.repository.UsersavedselectionRepository;
import org.geonetwork.indexing.IndexingService;
import org.geonetwork.utility.legacy.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class UserSelectionsServiceTests {

    @InjectMocks
    private UserSelectionsService userSelectionsService;

    @Mock
    private SelectionRepository selectionRepository;

    @Mock
    private UsersavedselectionRepository umsRepository;

    @Mock
    private LanguageRepository langRepository;

    @Mock
    private MetadataRepository metadataRepository;

    @Mock
    private IndexingService indexingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetails userDetails;

    private User adminUser;
    private User regularUser;

    @BeforeEach
    public void setUp() {
        adminUser = User.builder()
                .id(1)
                .username("admin")
                .profile(Profile.Administrator)
                .build();
        regularUser = User.builder()
                .id(2)
                .username("user")
                .profile(Profile.RegisteredUser)
                .build();
    }

    @Test
    public void testRetrieveAllSelections() {
        List<Selection> list = new ArrayList<>();
        Selection selection1 =
                Selection.builder().id(1).name("Test Selection 1").build();
        Selection selection2 =
                Selection.builder().id(2).name("Test Selection 2").build();
        list.add(selection1);
        list.add(selection2);

        when(selectionRepository.findAll()).thenReturn(list);

        List<Selection> selections = userSelectionsService.retrieveAllSelections();

        assertEquals(2, selections.size());
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testRetrieveUserSelectionItemsSuccess() throws ResourceNotFoundException {
        Integer selectionId = 1;
        Integer userId = 1;
        when(userDetails.getUsername()).thenReturn("admin");
        when(userDetails.getAuthorities()).thenReturn((Set) Set.of(new SimpleGrantedAuthority("Administrator")));
        when(userRepository.findOptionalByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));
        when(selectionRepository.findById(selectionId))
                .thenReturn(Optional.of(Selection.builder().id(selectionId).build()));
        when(umsRepository.findMetadataUuidBySelectionIdAndUserId(selectionId, userId))
                .thenReturn(List.of("uuid1", "uuid2"));

        List<String> items = userSelectionsService.retrieveUserSelectionItems(selectionId, userDetails, userId);

        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testRetrieveUserSelectionItemsSelectionNotFound() {
        Integer selectionId = 1;
        Integer userId = 1;
        when(userDetails.getUsername()).thenReturn("admin");
        when(userDetails.getAuthorities()).thenReturn((Set) Set.of(new SimpleGrantedAuthority("Administrator")));
        when(userRepository.findOptionalByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));
        when(selectionRepository.findById(selectionId)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userSelectionsService.retrieveUserSelectionItems(selectionId, userDetails, userId));
    }

    @Test
    public void testAddUserSelectionSuccess() {
        Selection selection = Selection.builder().id(1).name("New Selection").build();
        when(selectionRepository.findById(1)).thenReturn(Optional.empty());
        when(selectionRepository.findOneByName("New Selection")).thenReturn(Optional.empty());
        when(langRepository.findAll()).thenReturn(Collections.emptyList());
        when(selectionRepository.save(selection)).thenReturn(selection);

        Selection created = userSelectionsService.addUserSelection(selection);

        assertNotNull(created);
        assertEquals("New Selection", created.getName());
        verify(selectionRepository, times(1)).save(selection);
    }

    @Test
    public void testAddUserSelectionIdExists() {
        Selection selection = Selection.builder().id(1).name("New Selection").build();

        when(selectionRepository.findById(1)).thenReturn(Optional.of(selection));

        assertThrows(IllegalArgumentException.class, () -> userSelectionsService.addUserSelection(selection));
    }

    @Test
    public void testUpdateUserSelectionSuccess() throws ResourceNotFoundException {
        Integer selectionId = 1;
        Selection selection = Selection.builder().name("Updated Name").build();
        when(selectionRepository.findById(selectionId))
                .thenReturn(Optional.of(Selection.builder().id(selectionId).build()));

        userSelectionsService.updateUserSelection(selectionId, selection);

        verify(selectionRepository, times(1)).save(selection);
        assertEquals(selectionId, selection.getId());
    }

    @Test
    public void testDeleteUserSelectionSuccess() throws ResourceNotFoundException {
        Integer selectionId = 1;
        when(selectionRepository.findById(selectionId))
                .thenReturn(Optional.of(Selection.builder().id(selectionId).build()));

        userSelectionsService.deleteUserSelection(selectionId);

        verify(umsRepository, times(1)).deleteAllBySelectionId(selectionId);
        verify(selectionRepository, times(1)).deleteById(selectionId);
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testAddItemsToUserSelectionSuccess() throws ResourceNotFoundException {
        Integer selectionId = 1;
        Integer userId = 1;
        String[] uuids = {"uuid1"};
        Selection selection = Selection.builder().id(selectionId).build();

        when(userDetails.getUsername()).thenReturn("admin");
        when(userDetails.getAuthorities()).thenReturn((Set) Set.of(new SimpleGrantedAuthority("Administrator")));
        when(userRepository.findOptionalByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));
        when(selectionRepository.findById(selectionId)).thenReturn(Optional.of(selection));
        when(metadataRepository.existsByUuid("uuid1")).thenReturn(true);

        userSelectionsService.addItemsToUserSelection(selectionId, userId, uuids, userDetails);

        verify(umsRepository, times(1)).save(any(Usersavedselection.class));
        verify(indexingService, times(1)).index(any());
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testDeleteUserSelectionRecordsSuccess() throws ResourceNotFoundException {
        Integer selectionId = 1;
        Integer userId = 1;
        String[] uuids = {"uuid1"};

        when(userDetails.getUsername()).thenReturn("admin");
        when(userDetails.getAuthorities()).thenReturn((Set) Set.of(new SimpleGrantedAuthority("Administrator")));
        when(userRepository.findOptionalByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));
        when(selectionRepository.findById(selectionId))
                .thenReturn(Optional.of(Selection.builder().id(selectionId).build()));

        userSelectionsService.deleteUserSelectionRecords(selectionId, userId, uuids, userDetails);

        verify(umsRepository, times(1)).deleteById(any());
        verify(indexingService, times(1)).index(any());
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testDeleteAllUserSelectionRecordsSuccess() throws ResourceNotFoundException {
        Integer selectionId = 1;
        Integer userId = 1;

        when(userDetails.getUsername()).thenReturn("admin");
        when(userDetails.getAuthorities()).thenReturn((Set) Set.of(new SimpleGrantedAuthority("Administrator")));
        when(userRepository.findOptionalByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));
        when(selectionRepository.findById(selectionId))
                .thenReturn(Optional.of(Selection.builder().id(selectionId).build()));

        userSelectionsService.deleteUserSelectionRecords(selectionId, userId, null, userDetails);

        verify(umsRepository, times(1)).deleteAllBySelectionIdAndUserId(selectionId, userId);
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testCheckUserAllowedUnauthorized() {
        Integer selectionId = 1;
        Integer userId = 1; // trying to access admin's selection

        when(userDetails.getUsername()).thenReturn("user");
        when(userDetails.getAuthorities()).thenReturn((Set) Set.of(new SimpleGrantedAuthority("RegisteredUser")));
        when(userRepository.findOptionalByUsername("user")).thenReturn(Optional.of(regularUser));

        assertThrows(
                IllegalArgumentException.class,
                () -> userSelectionsService.retrieveUserSelectionItems(selectionId, userDetails, userId));
    }
}
