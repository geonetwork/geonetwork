package org.geonetwork.repository;

import org.geonetwork.domain.MetadataDraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadataDraftRepository extends JpaRepository<MetadataDraft, Integer> {
}
