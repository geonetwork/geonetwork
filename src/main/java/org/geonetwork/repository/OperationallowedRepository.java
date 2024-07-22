package org.geonetwork.repository;

import java.util.List;
import org.geonetwork.domain.Operationallowed;
import org.geonetwork.domain.OperationallowedId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationallowedRepository
    extends JpaRepository<Operationallowed, OperationallowedId> {
  List<Operationallowed> findAllByIdMetadataid(Integer metadataId);
}
