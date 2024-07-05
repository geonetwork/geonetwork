package org.geonetwork.repository;

import org.geonetwork.domain.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadataRepository extends JpaRepository<Metadata, Integer> {
}
