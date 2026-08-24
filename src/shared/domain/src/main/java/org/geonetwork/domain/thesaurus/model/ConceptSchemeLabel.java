/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.domain.thesaurus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "concept_scheme_label",
        uniqueConstraints = @UniqueConstraint(columnNames = {"concept_scheme_id", "type_id", "language"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConceptSchemeLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "concept_scheme_id")
    private ConceptScheme conceptScheme;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id")
    private LabelType type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(length = 10)
    private String language = "en";
}
