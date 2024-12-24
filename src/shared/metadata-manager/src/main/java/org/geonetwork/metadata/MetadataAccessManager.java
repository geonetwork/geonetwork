/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.metadata;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.Operation;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.ReservedOperation;
import org.geonetwork.domain.User;
import org.geonetwork.domain.Usergroup;
import org.geonetwork.security.AuthenticationFacade;
import org.geonetwork.security.user.UserManager;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@AllArgsConstructor
public class MetadataAccessManager implements IMetadataAccessManager {

    private final MetadataManager metadataManager;
    private final UserManager userManager;
    private final AuthenticationFacade authenticationFacade;

    /**
     * Returns true if, and only if, at least one of these conditions is satisfied:
     *
     * <ul>
     *   <li>the user is owner (@see #isOwner)
     *   <li>the user has edit rights over the metadata
     * </ul>
     *
     * @param metadataId The metadata internal identifier
     */
    @Override
    public boolean canEdit(final int metadataId) throws Exception {
        return isOwner(metadataId) || hasEditPermission(metadataId);
    }

    @Override
    public boolean isOwner(final int metadataId) throws Exception {
        try {
            if (!this.authenticationFacade.getAuthentication().isAuthenticated()) {
                return false;
            }

            String currentUsername = this.authenticationFacade.getUsername();
            if (!StringUtils.hasLength(currentUsername)) {
                return false;
            }
            User currentUser = this.userManager.getUserByUsername(currentUsername);

            // --- check if the user is an administrator
            Profile profile = currentUser.getProfile();
            if (profile == Profile.Administrator) {
                return true;
            }

            Metadata metadata = metadataManager.findMetadataById(metadataId);

            // --- check if the user is the metadata owner
            //
            if (currentUser.getId().equals(metadata.getOwner())) {
                return true;
            }

            // --- check if the user is a reviewer or useradmin
            if (profile != Profile.Reviewer && profile != Profile.UserAdmin) {
                return false;
            }

            // --- if there is no group owner then the reviewer cannot review and the useradmin cannot administer
            final Integer groupOwner = metadata.getGroupowner();
            if (groupOwner == null) {
                return false;
            }

            List<Usergroup> userReviewerGroups = this.userManager.getUserGroups(currentUser.getId(), Profile.Reviewer);

            boolean isReviewerInGroupOwner = (userReviewerGroups.stream()
                            .filter(usergroup -> usergroup.getGroupid().getId().equals(groupOwner))
                            .count()
                    > 0);

            return isReviewerInGroupOwner;

        } catch (MetadataNotFoundException ex) {
            return false;
        }
    }

    /**
     * Check if current user can edit the metadata according to the groups where the metadata is editable.
     *
     * @param metadataId The metadata internal identifier
     */
    @Override
    public boolean hasEditPermission(final int metadataId) throws Exception {
        return hasEditingPermissionWithProfile(metadataId);
    }

    /** Returns whether a particular metadata is downloadable. */
    @Override
    public boolean canDownload(final int metadataId) throws Exception {
        if (isOwner(metadataId)) {
            return true;
        }
        int downloadId = ReservedOperation.download.getId();
        // TODO: get IP address --> context.getIpAddress()
        String ipAddress = "";
        Set<Operation> ops = getOperations(metadataId, ipAddress);
        for (Operation op : ops) {
            if (op.getId() == downloadId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canView(int metadataId) throws Exception {
        // TODO: get IP address --> context.getIpAddress()
        String ipAddress = "";
        Set<Operation> hsOper = this.getOperations(metadataId, ipAddress);
        boolean hasViewOperation = (hsOper.stream()
                        .filter(op -> op.getId() == ReservedOperation.view.getId())
                        .count()
                > 0);

        return hasViewOperation;
    }

    /**
     * Given a user(session) a list of groups and a metadata returns all operations that user can perform on that
     * metadata (an set of OPER_XXX as keys). If the user is authenticated the permissions are taken from the groups the
     * user belong. If the user is not authenticated, a dynamic group is assigned depending on user location (0 for
     * internal and 1 for external).
     */
    @Override
    public Set<Operation> getOperations(int metadataId, String ip) throws Exception {
        return getOperations(metadataId, ip, null);
    }

    @Override
    public Set<Operation> getOperations(int metadataId, String ip, Collection<Operation> operations) throws Exception {
        Set<Operation> results;
        // if user is an administrator OR is the owner of the record then allow all operations
        if (isOwner(metadataId)) {
            results = new HashSet<>(this.metadataManager.getAvailableMetadataOperations());
        } else {
            if (operations == null) {
                results = new HashSet<>(getAllOperations(metadataId, ip));
            } else {
                results = new HashSet<>(operations);
            }

            // TODO: Use user session
            /*UserSession us = context.getUserSession();
            if ((us != null) && us.isAuthenticated() && us.getProfile() == Profile.Editor && us.getProfile() == Profile.Reviewer) {
              results.add(operationRepository.findReservedOperation(ReservedOperation.view));
            }*/
        }

        return results;
    }

    /** Returns all operations permitted by the user on a particular metadata. */
    @Override
    public Set<Operation> getAllOperations(int metadataId, String ip) throws Exception {
        // TODO: Implement

        HashSet<Operation> operations = new HashSet<>();
        /*Set<Integer> groups = getUserGroups(context.getUserSession(),
          ip, false);
        for (Operationallowed opAllow : this.metadataManager.getMetadataOperations(mdId)) {
          if (groups.contains(opAllow.getId().getGroupid())) {
            operations.add(operationRepository.findById(opAllow.getId().getOperationId()).get());
          }
        }*/
        return operations;
    }

    /**
     * Check if current user has permission for the metadata according to the groups where the metadata is editable and
     * specific user profile.
     *
     * @param metadataId The metadata internal identifier
     */
    private boolean hasEditingPermissionWithProfile(final int metadataId) throws Exception {
        if (this.authenticationFacade.getAuthentication().isAuthenticated()) {
            return false;
        }

        String currentUsername = this.authenticationFacade.getUsername();
        if (!StringUtils.hasLength(currentUsername)) {
            return false;
        }
        User currentUser = this.userManager.getUserByUsername(currentUsername);

        // Get the groups where the metadata is editable
        List<Integer> metadataEditableGroups = this.metadataManager.getEditableGroups(metadataId);
        if (metadataEditableGroups.isEmpty()) {
            return false;
        }

        // Get the groups where the user is Editor
        List<Usergroup> userEditableGroups = this.userManager.getUserGroups(currentUser.getId());
        List<Integer> userEditableGroupsIds = userEditableGroups.stream()
                .map(usergroup -> usergroup.getId().getGroupid())
                .collect(Collectors.toUnmodifiableList());

        boolean userCanEditMetadata = (userEditableGroupsIds.stream()
                        .filter(metadataEditableGroups::contains)
                        .distinct()
                        .count()
                > 0);

        return userCanEditMetadata;
    }
}
