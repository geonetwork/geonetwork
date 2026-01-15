/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.thesaurus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "relation",
        uniqueConstraints = @UniqueConstraint(columnNames = {"from_concept_id", "to_concept_id", "type_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Relation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "from_concept_id")
    private Concept fromConcept;

    @ManyToOne(optional = false)
    @JoinColumn(name = "to_concept_id")
    private Concept toConcept;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id")
    private RelationType type;
}
