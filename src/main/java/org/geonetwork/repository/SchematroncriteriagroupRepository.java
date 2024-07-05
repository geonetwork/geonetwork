package org.geonetwork.repository;

import org.geonetwork.domain.Schematroncriteriagroup;
import org.geonetwork.domain.SchematroncriteriagroupId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchematroncriteriagroupRepository
    extends JpaRepository<Schematroncriteriagroup, SchematroncriteriagroupId> {}
