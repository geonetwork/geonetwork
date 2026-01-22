/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.thesaurus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "concept_scheme_note")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConceptSchemeNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "concept_scheme_id")
    private ConceptScheme conceptScheme;

    @Column(length = 20, nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(length = 10)
    private String language = "en";
}
