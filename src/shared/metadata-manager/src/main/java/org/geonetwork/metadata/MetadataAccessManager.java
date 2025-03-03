/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.metadata;

import io.micrometer.common.util.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.Operation;
import org.geonetwork.domain.Operationallowed;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.ReservedGroup;
import org.geonetwork.domain.ReservedOperation;
import org.geonetwork.domain.repository.GroupRepository;
import org.geonetwork.domain.repository.OperationRepository;
import org.geonetwork.security.IAuthenticationFacade;
import org.springframework.stereotype.Component;

/**
 * Handles the access to a metadata depending on the metadata/group.
 *
 * <p>See GN4's AccessManager.
 */
@Component
@AllArgsConstructor
@Slf4j
public class MetadataAccessManager implements IMetadataAccessManager {

    private final MetadataManager metadataManager;
    private final IAuthenticationFacade authenticationFacade;
    private final GroupRepository groupRepository;
    private final OperationRepository operationRepository;
    private final IntranetHelper intranetHelper;

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

    /**
     * Return true if the current user is:
     *
     * <ul>
     *   <li>administrator
     *   <li>the metadata owner (the user who created the record)
     *   <li>reviewer in the group the metadata was created
     * </ul>
     *
     * Note: old GeoNetwork was also restricting editing on harvested record. This is not restricted on the server side
     * anymore. If a record is harvested it could be edited by default but the client application may restrict this
     * condition.
     *
     * @param metadataId The metadata internal identifier
     */
    @Override
    public boolean isOwner(final int metadataId) throws Exception {
        try {
            if (!this.authenticationFacade.getAuthentication().isAuthenticated()) {
                return false;
            }

            String currentUsername = this.authenticationFacade.getUsername();
            if (StringUtils.isBlank(currentUsername) || this.authenticationFacade.isAnonymous()) {
                return false;
            }

            var authentication = this.authenticationFacade.geonetworkPermissions();

            // --- check if the user is an administrator
            Profile profile = authentication.getHighestProfile();
            if (profile == Profile.Administrator) {
                return true;
            }

            Metadata metadata = metadataManager.findMetadataById(metadataId);

            // --- check if the user is the metadata owner
            //
            if (authentication.getUserId().equals(metadata.getOwner())) {
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

            var reviewerGroups = this.authenticationFacade
                    .geonetworkPermissions()
                    .getProfileGroups()
                    .get(Profile.Reviewer);
            if (reviewerGroups == null) {
                return false;
            }

            return reviewerGroups.contains(groupOwner);

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
        return hasEditingPermissionWithProfile(metadataId, Profile.Editor);
    }

    /** Returns whether a particular metadata is downloadable. */
    @Override
    public boolean canDownload(final int metadataId) throws Exception {
        if (isOwner(metadataId)) {
            return true;
        }
        int downloadId = ReservedOperation.download.getId();
        // TODO: get IP address --> context.getIpAddress()
        String ipAddress = intranetHelper.currentRequestIpAddress();
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
        String ipAddress = intranetHelper.currentRequestIpAddress();
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
            // NOTE: this can never do anything because it checks that a profile is 2 different things
            /*UserSession us = context.getUserSession();
            if ((us != null) && us.isAuthenticated() && us.getProfile() == Profile.Editor && us.getProfile() == Profile.Reviewer) {
              results.add(operationRepository.findReservedOperation(ReservedOperation.view));
            }*/
        }

        return results;
    }

    /**
     * Returns all groups accessible by the user (a set of ids).
     *
     * <p>1. admin -> all groups (from DB `groups` table) 2. All users have group.all 3. Determine if request is from
     * intranet. If so, group.intranet 4. If signed in, + add group.guest + find all the groups the user is in (DB table
     * `usergroups`) + if editingGroupsOnly, then Profile for that group must be >= Profile.Editor
     *
     * @param editingGroupsOnly TODO
     */
    public Set<Integer> getUserGroups(String ip, boolean editingGroupsOnly) throws Exception {

        Set<Integer> hs = new HashSet<>();

        // add All (1) network group
        hs.add(ReservedGroup.all.getId());

        if (ip != null && intranetHelper.isIntranet(ip)) {
            hs.add(ReservedGroup.intranet.getId());
        }

        var gnPermissions = authenticationFacade.geonetworkPermissions();

        if (authenticationFacade.isAuthenticated()) {
            // add (-1) GUEST group
            hs.add(ReservedGroup.guest.getId());

            if (gnPermissions.isAdmin()) {
                // admin - ALL groups
                List<Integer> allGroupIds =
                        groupRepository.findAll().stream().map(x -> x.getId()).toList();
                hs.addAll(allGroupIds);
            } else {
                // go through their groups and add them
                for (var profileGroup : gnPermissions.getProfileGroups().entrySet()) {
                    var profile = profileGroup.getKey();
                    if (!editingGroupsOnly || profile.compareTo(Profile.Editor) > 0) {
                        hs.add(profile.getId());
                    }
                }
            }
        }

        return hs;
    }

    /** Returns all operations permitted by the user on a particular metadata. */
    @Override
    public Set<Operation> getAllOperations(int metadataId, String ip) throws Exception {
        HashSet<Operation> operations = new HashSet<>();
        var groups = getUserGroups(ip, false);
        for (Operationallowed opAllow : this.metadataManager.getMetadataOperations(metadataId)) {
            if (groups.contains(opAllow.getId().getGroupid())) {
                operations.add(operationRepository
                        .findById(opAllow.getId().getOperationid())
                        .get());
            }
        }
        return operations;
    }

    /**
     * Check if current user has permission for the metadata according to the groups where the metadata is editable and
     * specific user profile.
     *
     * @param metadataId The metadata internal identifier
     * @param profile The specific profile the user must have on the editable-group
     */
    private boolean hasEditingPermissionWithProfile(final int metadataId, Profile profile) throws Exception {
        if (this.authenticationFacade.getAuthentication().isAuthenticated()) {
            return false;
        }

        var authority = this.authenticationFacade.geonetworkPermissions();
        if (this.authenticationFacade.isAnonymous() || StringUtils.isBlank(authority.getUsername())) {
            return false;
        }

        // what groups have permission to edit?
        List<Integer> metadataEditableGroups = this.metadataManager.getEditableGroups(metadataId);
        if (metadataEditableGroups.isEmpty()) {
            return false; // no one can edit by profile (might be admin or owner)
        }

        // get the groups that the user has `profile` permission on
        var auth = authenticationFacade.geonetworkPermissions();
        var userGroups = auth.getProfileGroups().get(profile);

        var intersection = new ArrayList<>(metadataEditableGroups);
        intersection.retainAll(userGroups);

        return !intersection.isEmpty();
    }
}
