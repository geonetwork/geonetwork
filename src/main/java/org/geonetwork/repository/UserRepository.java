/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.repository;

import java.util.Optional;
import org.geonetwork.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findOptionalByUsername(String username);

  Optional<User> findOptionalByUsernameAndAuthtypeIsNull(String username);

  Optional<User> findOptionalByEmailAndAuthtypeIsNull(String email);

  Optional<User> findOptionalByUsernameOrEmailAndAuthtypeIsNull(String username, String email);
}
