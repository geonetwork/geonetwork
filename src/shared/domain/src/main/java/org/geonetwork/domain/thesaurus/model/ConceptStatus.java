/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.thesaurus.model;

import jakarta.persistence.*;

@Entity
@Table(name = "concept_status")
public record ConceptStatus(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id,
        @Column(unique = true, nullable = false) String name) {
    // Note: Records as JPA entities may not be portable across all JPA providers.
    // Prefer mutable entity classes in production, or use records only for read-only DTOs.
}
