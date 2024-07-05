package org.geonetwork.repository;

import org.geonetwork.domain.Metadatacateg;
import org.geonetwork.domain.MetadatacategId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadatacategRepository extends JpaRepository<Metadatacateg, MetadatacategId> {}
