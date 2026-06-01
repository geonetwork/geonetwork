/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository.specification;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.Usergroup;
import org.geonetwork.domain.UsergroupId_;
import org.geonetwork.domain.Usergroup_;
import org.springframework.data.jpa.domain.Specification;

public final class UsergroupSpecs {

    private UsergroupSpecs() {
        // don't permit instantiation
    }

    public static Specification<Usergroup> hasGroupIds(final List<Integer> groupId) {
        return (root, query, cb) -> {
            Path<Integer> grpIdAttributePath = root.get(Usergroup_.id).get(UsergroupId_.groupid);
            Predicate grpIdInPredicate = grpIdAttributePath.in(groupId);
            return grpIdInPredicate;
        };
    }

    public static Specification<Usergroup> hasUserId(final int userId) {
        return (root, query, cb) -> {
            Path<Integer> userIdAttributePath = root.get(Usergroup_.id).get(UsergroupId_.userid);
            Predicate userIdEqualPredicate = cb.equal(userIdAttributePath, cb.literal(userId));
            return userIdEqualPredicate;
        };
    }

    /**
     * Specification for retrieving all the Usergroups with a given profile.
     *
     * @param profile The {@link Profile} to filter the Usergroups.
     * @return the query.
     */
    public static Specification<Usergroup> hasProfile(final Profile profile) {
        return (root, query, cb) -> {
            Path<Integer> profileIdAttributePath = root.get(Usergroup_.id).get(UsergroupId_.profile);
            Predicate profileIdEqualPredicate = cb.equal(profileIdAttributePath, cb.literal(profile.getId()));
            return profileIdEqualPredicate;
        };
    }
}
