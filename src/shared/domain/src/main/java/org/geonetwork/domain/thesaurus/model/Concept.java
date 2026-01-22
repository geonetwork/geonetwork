/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.thesaurus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "concept")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Concept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "concept_scheme_id")
    private ConceptScheme conceptScheme;

    @Column(columnDefinition = "TEXT", unique = true)
    private String uri;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id")
    private ConceptStatus status;

    private Integer position;

    @Column(name = "icon_url", columnDefinition = "TEXT")
    private String iconUrl;
}
