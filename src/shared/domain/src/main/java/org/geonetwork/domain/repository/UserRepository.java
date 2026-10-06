/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import java.util.Optional;
import org.geonetwork.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findOptionalByUsername(String username);

    Optional<User> findOptionalByUsernameAndAuthtypeIsNull(String username);

    Optional<User> findOptionalByEmailAndAuthtypeIsNull(String email);

    Optional<User> findOptionalByEmail(String email);

    Optional<User> findOptionalByUsernameOrEmailAndAuthtypeIsNull(String username, String email);

    /** Updates only the last login date, so a concurrent edit of the user is not overwritten. */
    @Modifying
    @Query("update User u set u.lastlogindate = :date where u.username = :username")
    int updateLastLoginDate(@Param("username") String username, @Param("date") String date);
}
