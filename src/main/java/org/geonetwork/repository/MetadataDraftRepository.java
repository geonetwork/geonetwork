package org.geonetwork.repository;

import org.geonetwork.domain.MetadataDraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadataDraftRepository extends JpaRepository<MetadataDraft, Integer> {
  MetadataDraft findByApprovedversion_Id(Integer id);

  boolean existsByApprovedversion_Id(Integer id);
}
