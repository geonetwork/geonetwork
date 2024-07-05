package org.geonetwork.repository;

import org.geonetwork.domain.Useraddress;
import org.geonetwork.domain.UseraddressId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UseraddressRepository extends JpaRepository<Useraddress, UseraddressId> {}
