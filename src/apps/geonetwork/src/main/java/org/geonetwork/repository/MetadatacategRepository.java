/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.repository;

import java.util.List;
import org.geonetwork.domain.Metadatacateg;
import org.geonetwork.domain.MetadatacategId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadatacategRepository extends JpaRepository<Metadatacateg, MetadatacategId> {
  List<Metadatacateg> findAllByIdMetadataid(Integer metadataId);
}
