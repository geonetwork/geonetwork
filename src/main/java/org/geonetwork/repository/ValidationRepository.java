package org.geonetwork.repository;

import java.util.List;
import org.geonetwork.domain.Validation;
import org.geonetwork.domain.ValidationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationRepository extends JpaRepository<Validation, ValidationId> {
  List<Validation> findAllById_metadataid(Integer metadataId);
}
