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
package org.geonetwork.domain.thesaurus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "concept_scheme_label",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"concept_scheme_id", "type_id", "language"})})
@Getter
@Setter
@NoArgsConstructor
public class ConceptSchemeLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concept_scheme_id", nullable = false)
    private ConceptScheme conceptScheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private LabelType type;

    @Column(nullable = false)
    private String text;

    private String language = "en";
}
