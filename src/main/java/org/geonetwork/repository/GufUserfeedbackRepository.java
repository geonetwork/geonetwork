package org.geonetwork.repository;

import org.geonetwork.domain.GufUserfeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GufUserfeedbackRepository extends JpaRepository<GufUserfeedback, String> {
  long countByMetadataUuid_Uuid(String metadataUuid);
}
