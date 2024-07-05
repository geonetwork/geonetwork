package org.geonetwork.repository;

import org.geonetwork.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/** Address repository. */
public interface AddressRepository extends JpaRepository<Address, Integer> {}
