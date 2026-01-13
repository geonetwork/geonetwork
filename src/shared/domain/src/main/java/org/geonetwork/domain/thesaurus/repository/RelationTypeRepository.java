/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
/*
 * (c) 2026 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.thesaurus.repository;

import org.geonetwork.domain.thesaurus.model.RelationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationTypeRepository extends JpaRepository<RelationType, Long> {}
