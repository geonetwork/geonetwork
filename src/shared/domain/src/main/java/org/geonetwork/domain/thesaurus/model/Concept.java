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
@Table(name = "concept")
@Getter
@Setter
@NoArgsConstructor
public class Concept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concept_scheme_id", nullable = false)
    private ConceptScheme conceptScheme;

    @Column(unique = true)
    private String uri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private ConceptStatus status;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "position")
    private Integer position;
}
