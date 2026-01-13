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
        name = "relation",
        uniqueConstraints = @UniqueConstraint(columnNames = {"from_concept_id", "to_concept_id", "type_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Relation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_concept_id", nullable = false)
    private Concept from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_concept_id", nullable = false)
    private Concept to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private RelationType type;
}
