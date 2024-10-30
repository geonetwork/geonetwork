/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.repository;

import java.util.Optional;
import org.geonetwork.domain.Metadatastatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadatastatusRepository extends JpaRepository<Metadatastatus, Integer> {

    Optional<Metadatastatus> findFirstByMetadataidAndStatusidTypeOrderByChangedateDesc(Integer metadataId, String type);
}
