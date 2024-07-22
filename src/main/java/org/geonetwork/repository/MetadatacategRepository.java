package org.geonetwork.repository;

import java.util.List;
import org.geonetwork.domain.Metadatacateg;
import org.geonetwork.domain.MetadatacategId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadatacategRepository extends JpaRepository<Metadatacateg, MetadatacategId> {
  List<Metadatacateg> findAllByIdMetadataid(Integer metadataId);
}
