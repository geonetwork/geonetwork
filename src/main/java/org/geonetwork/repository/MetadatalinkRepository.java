package org.geonetwork.repository;

import org.geonetwork.domain.Metadatalink;
import org.geonetwork.domain.MetadatalinkId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadatalinkRepository extends JpaRepository<Metadatalink, MetadatalinkId> {}
