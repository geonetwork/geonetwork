/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.thesaurus.repository;

import org.geonetwork.domain.thesaurus.model.ConceptLabel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConceptLabelRepository extends JpaRepository<ConceptLabel, Long> {}
