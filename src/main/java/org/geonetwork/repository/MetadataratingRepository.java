package org.geonetwork.repository;

import org.geonetwork.domain.Metadatarating;
import org.geonetwork.domain.MetadataratingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadataratingRepository extends JpaRepository<Metadatarating, MetadataratingId> {}
