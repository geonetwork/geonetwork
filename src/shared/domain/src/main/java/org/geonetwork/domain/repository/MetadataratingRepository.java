/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.repository;

import org.geonetwork.domain.Metadatarating;
import org.geonetwork.domain.MetadataratingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadataratingRepository extends JpaRepository<Metadatarating, MetadataratingId> {}
