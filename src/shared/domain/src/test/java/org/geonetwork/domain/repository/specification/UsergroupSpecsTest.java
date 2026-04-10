/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.domain.repository.specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.Usergroup;
import org.geonetwork.domain.UsergroupId;
import org.geonetwork.domain.UsergroupId_;
import org.geonetwork.domain.Usergroup_;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class UsergroupSpecsTest {

    @Mock
    private Root<Usergroup> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder cb;

    @Test
    void hasGroupIds_shouldReturnInPredicate() {
        List<Integer> groupIds = List.of(1, 2, 3);
        Path<UsergroupId> idPath = mockPath();
        Path<Integer> groupIdPath = mockPath();
        Predicate inPredicate = mock(Predicate.class);

        when(root.get(Usergroup_.id)).thenReturn(idPath);
        when(idPath.get(UsergroupId_.groupid)).thenReturn(groupIdPath);
        when(groupIdPath.in(groupIds)).thenReturn(inPredicate);

        Specification<Usergroup> spec = UsergroupSpecs.hasGroupIds(groupIds);
        Predicate result = spec.toPredicate(root, query, cb);

        assertNotNull(result);
        verify(root).get(Usergroup_.id);
        verify(idPath).get(UsergroupId_.groupid);
        verify(groupIdPath).in(groupIds);
    }

    @Test
    void hasUserId_shouldReturnEqualPredicate() {
        int userId = 123;
        Path<UsergroupId> idPath = mockPath();
        Path<Integer> userIdPath = mockPath();
        Predicate equalPredicate = mock(Predicate.class);
        Expression<Integer> literalExpression = mockExpression();

        when(root.get(Usergroup_.id)).thenReturn(idPath);
        when(idPath.get(UsergroupId_.userid)).thenReturn(userIdPath);
        when(cb.literal(userId)).thenReturn(literalExpression);
        when(cb.equal(userIdPath, literalExpression)).thenReturn(equalPredicate);

        Specification<Usergroup> spec = UsergroupSpecs.hasUserId(userId);
        Predicate result = spec.toPredicate(root, query, cb);

        assertNotNull(result);
        verify(root).get(Usergroup_.id);
        verify(idPath).get(UsergroupId_.userid);
        verify(cb).literal(userId);
        verify(cb).equal(userIdPath, literalExpression);
    }

    @Test
    void hasProfile_shouldReturnEqualPredicate() {
        Profile profile = Profile.Administrator;
        Path<UsergroupId> idPath = mockPath();
        Path<Integer> profilePath = mockPath();
        Predicate equalPredicate = mock(Predicate.class);
        Expression<Integer> literalExpression = mockExpression();

        when(root.get(Usergroup_.id)).thenReturn(idPath);
        when(idPath.get(UsergroupId_.profile)).thenReturn(profilePath);
        when(cb.literal(profile.getId())).thenReturn(literalExpression);
        when(cb.equal(profilePath, literalExpression)).thenReturn(equalPredicate);

        Specification<Usergroup> spec = UsergroupSpecs.hasProfile(profile);
        Predicate result = spec.toPredicate(root, query, cb);

        assertNotNull(result);
        verify(root).get(Usergroup_.id);
        verify(idPath).get(UsergroupId_.profile);
        verify(cb).literal(profile.getId());
        verify(cb).equal(profilePath, literalExpression);
    }

    @SuppressWarnings("unchecked")
    private <T> Path<T> mockPath() {
        return mock(Path.class);
    }

    @SuppressWarnings("unchecked")
    private <T> Expression<T> mockExpression() {
        return mock(Expression.class);
    }
}
