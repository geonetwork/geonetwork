/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.thesaurus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "concept_scheme_note")
@Getter
@Setter
@NoArgsConstructor
public class ConceptSchemeNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concept_scheme_id", nullable = false)
    private ConceptScheme conceptScheme;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String text;

    private String language = "en";
}
