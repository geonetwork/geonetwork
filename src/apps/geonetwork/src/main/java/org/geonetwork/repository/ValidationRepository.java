/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.repository;

import java.util.List;
import org.geonetwork.domain.Validation;
import org.geonetwork.domain.ValidationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationRepository extends JpaRepository<Validation, ValidationId> {
  List<Validation> findAllById_metadataid(Integer metadataId);
}
