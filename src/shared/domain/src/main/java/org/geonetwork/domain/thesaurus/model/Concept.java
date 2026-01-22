/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
