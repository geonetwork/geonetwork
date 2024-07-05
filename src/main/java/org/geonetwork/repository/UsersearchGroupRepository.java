package org.geonetwork.repository;

import org.geonetwork.domain.UsersearchGroup;
import org.geonetwork.domain.UsersearchGroupId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersearchGroupRepository
    extends JpaRepository<UsersearchGroup, UsersearchGroupId> {}
