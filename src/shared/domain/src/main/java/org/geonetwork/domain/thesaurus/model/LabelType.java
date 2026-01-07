/*
 * (c) 2026 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.thesaurus.model;

import jakarta.persistence.*;

@Entity
@Table(name = "label_type")
public record LabelType(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id,
        @Column(unique = true, nullable = false) String name) {}
