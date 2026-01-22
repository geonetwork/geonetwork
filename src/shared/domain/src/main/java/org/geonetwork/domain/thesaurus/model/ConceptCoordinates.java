/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.thesaurus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "concept_coordinates", uniqueConstraints = @UniqueConstraint(columnNames = "concept_id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConceptCoordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "concept_id")
    private Concept concept;

    @Column(name = "coord_east", nullable = false)
    private Double coordEast;

    @Column(name = "coord_west", nullable = false)
    private Double coordWest;

    @Column(name = "coord_south", nullable = false)
    private Double coordSouth;

    @Column(name = "coord_north", nullable = false)
    private Double coordNorth;
}
