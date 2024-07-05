package org.geonetwork.repository;

import org.geonetwork.domain.Usergroup;
import org.geonetwork.domain.UsergroupId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsergroupRepository extends JpaRepository<Usergroup, UsergroupId> {}
