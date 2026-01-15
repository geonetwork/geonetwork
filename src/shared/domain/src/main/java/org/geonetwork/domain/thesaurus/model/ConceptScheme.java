/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.thesaurus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "concept_scheme")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConceptScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT", unique = true)
    private String uri;

    @Column(name = "namespace_uri", columnDefinition = "TEXT", unique = true)
    private String namespaceUri;

    @Column(name = "internal_identifier", columnDefinition = "TEXT")
    private String internalIdentifier;

    @Column(name = "thesaurus_type", columnDefinition = "TEXT")
    private String thesaurusType;

    @Column(name = "origin_type", columnDefinition = "TEXT")
    private String originType;
}
