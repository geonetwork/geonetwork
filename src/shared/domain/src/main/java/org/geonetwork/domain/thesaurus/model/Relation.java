/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
