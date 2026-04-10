/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.List;
import org.geonetwork.domain.Usergroup;
import org.geonetwork.domain.UsergroupId;
import org.geonetwork.domain.UsergroupId_;
import org.geonetwork.domain.Usergroup_;
import org.springframework.data.jpa.domain.Specification;

/** Implementation object for methods in {@link UsergroupRepositoryCustom}. */
public class UsergroupRepositoryCustomImpl implements UsergroupRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Integer> findGroupIds(Specification<Usergroup> spec) {
        return findIdsBy(spec, UsergroupId_.groupid);
    }

    private List<Integer> findIdsBy(Specification<Usergroup> spec, SingularAttribute<UsergroupId, Integer> groupId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
        Root<Usergroup> from = query.from(Usergroup.class);
        query.select(from.get(Usergroup_.id).get(groupId));
        Predicate predicate = spec.toPredicate(from, query, builder);
        query.where(predicate);
        query.distinct(true);
        return entityManager.createQuery(query).getResultList();
    }
}
