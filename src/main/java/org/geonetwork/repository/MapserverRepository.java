package org.geonetwork.repository;

import org.geonetwork.domain.Mapserver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapserverRepository extends JpaRepository<Mapserver, Integer> {
}
