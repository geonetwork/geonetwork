package org.geonetwork.repository;

import java.util.Optional;
import org.geonetwork.domain.Metadatastatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadatastatusRepository extends JpaRepository<Metadatastatus, Integer> {

  Optional<Metadatastatus> findFirstByMetadataidAndStatusidTypeOrderByChangedateDesc(
      Integer metadataId, String type);
}
