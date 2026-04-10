/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.selections;

import static org.geonetwork.domain.repository.specification.UsergroupSpecs.hasGroupIds;
import static org.geonetwork.domain.repository.specification.UsergroupSpecs.hasProfile;
import static org.geonetwork.domain.repository.specification.UsergroupSpecs.hasUserId;
import static org.springframework.data.jpa.domain.Specification.unrestricted;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.geonetwork.domain.Language;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.Selection;
import org.geonetwork.domain.Selectionsde;
import org.geonetwork.domain.SelectionsdeId;
import org.geonetwork.domain.User;
import org.geonetwork.domain.Usergroup;
import org.geonetwork.domain.Usersavedselection;
import org.geonetwork.domain.UsersavedselectionId;
import org.geonetwork.domain.repository.LanguageRepository;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.domain.repository.SelectionRepository;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.domain.repository.UsergroupRepository;
import org.geonetwork.domain.repository.UsersavedselectionRepository;
import org.geonetwork.indexing.IndexingService;
import org.geonetwork.utility.legacy.exceptions.ResourceNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserSelectionsService {
    private final SelectionRepository selectionRepository;
    private final UsersavedselectionRepository umsRepository;
    private final UsergroupRepository usergroupRepository;
    private final LanguageRepository langRepository;
    private final MetadataRepository metadataRepository;
    private final IndexingService indexingService;
    private final UserRepository userRepository;

    public List<Selection> retrieveAllSelections() {
        return selectionRepository.findAll();
    }

    public List<String> retrieveUserSelectionItems(
            Integer selectionIdentifier, UserDetails userDetails, Integer userIdentifier)
            throws ResourceNotFoundException {
        checkUserAllowed(userDetails, userIdentifier);

        Optional<Selection> selection = selectionRepository.findById(selectionIdentifier);
        if (selection.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("Selection with id '%d' does not exist.", selectionIdentifier));
        }

        return umsRepository.findMetadatauuidByIdSelectionidAndIdUserid(selectionIdentifier, userIdentifier);
    }

    public Selection addUserSelection(Selection selection) {
        Optional<Selection> existingSelectionById = selectionRepository.findById(selection.getId());
        if (existingSelectionById.isPresent()) {
            throw new IllegalArgumentException(String.format(
                    "A selection with id '%d' already exist. Choose another id or unset it.", selection.getId()));
        }

        Optional<Selection> existingSelection = selectionRepository.findOneByName(selection.getName());
        if (existingSelection.isPresent()) {
            throw new IllegalArgumentException(String.format(
                    "A selection with name '%s' already exist. Choose another name.", selection.getName()));
        }

        // Populate languages if not already set
        java.util.List<Language> allLanguages = langRepository.findAll();
        Set<Selectionsde> labelTranslations = selection.getSelectionsdes();

        for (Language l : allLanguages) {
            labelTranslations.stream()
                    .filter(s -> s.getId().getLangid().equals(l.getId()))
                    .findFirst()
                    .ifPresentOrElse(
                            selectionsde -> {},
                            // TODO: Check
                            () -> labelTranslations.add(
                                    new Selectionsde(new SelectionsdeId(), selection, selection.getName())));
        }
        return selectionRepository.save(selection);
    }

    public void updateUserSelection(Integer selectionIdentifier, Selection selection) throws ResourceNotFoundException {
        Optional<Selection> existingSelection = selectionRepository.findById(selectionIdentifier);

        if (existingSelection.isPresent()) {
            selection.setId(selectionIdentifier);
            selectionRepository.save(selection);
        } else {
            throw new ResourceNotFoundException(
                    String.format("Selection with id '%d' does not exist.", selectionIdentifier));
        }
    }

    public void deleteUserSelection(Integer selectionIdentifier) throws ResourceNotFoundException {
        Optional<Selection> selection = selectionRepository.findById(selectionIdentifier);
        if (selection.isPresent()) {
            umsRepository.deleteAllByIdSelectionid(selectionIdentifier);
            selectionRepository.deleteById(selectionIdentifier);
        } else {
            throw new ResourceNotFoundException(
                    String.format("Selection with id '%d' does not exist.", selectionIdentifier));
        }
    }

    public void addItemsToUserSelection(
            Integer selectionIdentifier, Integer userIdentifier, String[] uuid, UserDetails userDetails)
            throws ResourceNotFoundException {

        // TODO: Check
        /*if (userIdentifier == null) {
          final UserSession us = ApiUtils.getUserSession(httpSession);
          userIdentifier = us.getUserIdAsInt();
        }*/
        User user = checkUserAllowed(userDetails, userIdentifier);

        Optional<Selection> selection = selectionRepository.findById(selectionIdentifier);
        if (selection.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("Selection with id '%d' does not exist.", selectionIdentifier));
        }

        for (String u : uuid) {
            // Check record exist
            if (metadataRepository.existsByUuid(u)) {
                UsersavedselectionId usersavedselectionId =
                        new UsersavedselectionId(u, selectionIdentifier, userIdentifier);
                Usersavedselection e = new Usersavedselection(usersavedselectionId, selection.get(), user);
                umsRepository.save(e);
                indexingService.index(List.of(u));
            } else {
                throw new ResourceNotFoundException(String.format("Metadata with uuid '%s' does not exist.", u));
            }
        }
    }

    public void deleteUserSelectionRecords(
            Integer selectionIdentifier, Integer userIdentifier, String[] uuid, UserDetails userDetails)
            throws ResourceNotFoundException {
        // TODO: Check
        /*if (userIdentifier == null) {
          final UserSession us = ApiUtils.getUserSession(httpSession);
          userIdentifier = us.getUserIdAsInt();
        }*/

        checkUserAllowed(userDetails, userIdentifier);

        Optional<Selection> selection = selectionRepository.findById(selectionIdentifier);
        if (selection.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("Selection with id '%d' does not exist.", selectionIdentifier));
        }

        if (uuid == null || uuid.length == 0) {
            umsRepository.deleteAllByIdSelectionidAndIdUserid(selectionIdentifier, userIdentifier);
        } else {
            for (String u : uuid) {
                UsersavedselectionId e = new UsersavedselectionId(u, selectionIdentifier, userIdentifier);
                umsRepository.deleteById(e);
                indexingService.index(List.of(u));
            }
        }
    }

    private User checkUserAllowed(UserDetails userDetails, Integer userIdentifier) throws ResourceNotFoundException {
        Profile myProfile = userDetails.getAuthorities().stream()
                .findFirst()
                .map(a -> Profile.valueOf(a.getAuthority()))
                .orElse(null);
        Optional<User> myUser = userRepository.findOptionalByUsername(userDetails.getUsername());
        if (myUser.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("User with username '%s' does not exist.", userDetails.getUsername()));
        }

        Integer myUserId = myUser.get().getId();

        if (Profile.Administrator.equals(myProfile)
                || Profile.UserAdmin.equals(myProfile)
                || myUserId.equals(userIdentifier)) {

            Optional<User> user = userRepository.findById(userIdentifier);
            if (user.isEmpty()) {
                throw new ResourceNotFoundException(String.format("User with id '%d' does not exist.", userIdentifier));
            }

            if (!myUserId.equals(userIdentifier) && myProfile == Profile.UserAdmin) {
                // --- retrieve session user groups and check to see whether this user is
                // --- allowed to get this info
                Specification<Usergroup> whereSpec = unrestricted();
                List<Integer> myUserGroupsAsUserAdmin = usergroupRepository.findGroupIds(
                        whereSpec.and(hasUserId(userIdentifier)).and(hasProfile(Profile.UserAdmin)));

                // Now check if the userIdentifier is part of one of those useradmin groups.
                List<Integer> adminlist = usergroupRepository.findGroupIds(
                        whereSpec.and(hasGroupIds(myUserGroupsAsUserAdmin)).and(hasUserId(userIdentifier)));

                if (adminlist.isEmpty()) {
                    throw new IllegalArgumentException(
                            "You don't have rights to do this because the user you want to edit is not part of your group");
                }
            }

            return user.get();
        } else {
            throw new IllegalArgumentException("You don't have rights to do this");
        }
    }
}
