/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import java.util.Optional;
import org.geonetwork.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findOptionalByUsername(String username);

    Optional<User> findOptionalByUsernameAndAuthtypeIsNull(String username);

    Optional<User> findOptionalByEmailAndAuthtypeIsNull(String email);

    Optional<User> findOptionalByEmail(String email);

    Optional<User> findOptionalByUsernameOrEmailAndAuthtypeIsNull(String username, String email);
}
