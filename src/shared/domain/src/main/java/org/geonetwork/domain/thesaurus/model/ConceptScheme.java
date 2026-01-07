/*
 * (c) 2026 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.thesaurus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "concept_scheme")
@Getter
@Setter
@NoArgsConstructor
public class ConceptScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "namespace_uri", unique = true)
    private String namespaceUri;

    @Column(name = "internal_identifier")
    private String internalIdentifier;

    private String type;

    private String origin;
}
