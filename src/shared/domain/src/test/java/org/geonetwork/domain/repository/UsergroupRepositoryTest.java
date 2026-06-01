/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.geonetwork.domain.Group;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.User;
import org.geonetwork.domain.Usergroup;
import org.geonetwork.domain.UsergroupId;
import org.geonetwork.domain.repository.specification.UsergroupSpecs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@DataJpaTest
class UsergroupRepositoryTest {

    @Configuration
    @EnableAutoConfiguration
    @EntityScan("org.geonetwork.domain")
    @EnableJpaRepositories(basePackageClasses = UsergroupRepository.class)
    static class TestConfig {}

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsergroupRepository usergroupRepository;

    private User user1;
    private Group group1;
    private Group group2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .username("user1")
                .password("password")
                .isenabled(true)
                .profile(Profile.RegisteredUser)
                .build();
        entityManager.persist(user1);

        group1 = Group.builder().name("group1").build();
        entityManager.persist(group1);

        group2 = Group.builder().name("group2").build();
        entityManager.persist(group2);

        Usergroup ug1 = new Usergroup();
        ug1.setId(new UsergroupId(group1.getId(), Profile.RegisteredUser.getId(), user1.getId()));
        ug1.setUserid(user1);
        ug1.setGroupid(group1);
        entityManager.persist(ug1);

        Usergroup ug2 = new Usergroup();
        ug2.setId(new UsergroupId(group2.getId(), Profile.Editor.getId(), user1.getId()));
        ug2.setUserid(user1);
        ug2.setGroupid(group2);
        entityManager.persist(ug2);

        entityManager.flush();
    }

    @Test
    void findGroupIds_shouldReturnMatchingGroupIds() {
        Specification<Usergroup> spec = UsergroupSpecs.hasUserId(user1.getId());
        List<Integer> groupIds = usergroupRepository.findGroupIds(spec);

        assertEquals(2, groupIds.size());
        assertTrue(groupIds.contains(group1.getId()));
        assertTrue(groupIds.contains(group2.getId()));
    }

    @Test
    void findGroupIds_shouldReturnFilteredGroupIds() {
        Specification<Usergroup> spec = UsergroupSpecs.hasProfile(Profile.Editor);
        List<Integer> groupIds = usergroupRepository.findGroupIds(spec);

        assertEquals(1, groupIds.size());
        assertTrue(groupIds.contains(group2.getId()));
    }

    @Test
    void findGroupIds_shouldReturnEmptyListWhenNoMatches() {
        Specification<Usergroup> spec = UsergroupSpecs.hasUserId(999);
        List<Integer> groupIds = usergroupRepository.findGroupIds(spec);

        assertTrue(groupIds.isEmpty());
    }

    @Test
    void findGroupIds_shouldReturnDistinctGroupIds() {
        // Add another usergroup for the same group but different profile
        Usergroup ug3 = new Usergroup();
        ug3.setId(new UsergroupId(group1.getId(), Profile.Editor.getId(), user1.getId()));
        ug3.setUserid(user1);
        ug3.setGroupid(group1);
        entityManager.persist(ug3);
        entityManager.flush();

        Specification<Usergroup> spec = UsergroupSpecs.hasUserId(user1.getId());
        List<Integer> groupIds = usergroupRepository.findGroupIds(spec);

        // Should still be 2 even though user1 is in group1 twice (with different profiles)
        assertEquals(2, groupIds.size());
        assertTrue(groupIds.contains(group1.getId()));
        assertTrue(groupIds.contains(group2.getId()));
    }
}
