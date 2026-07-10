/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ogcapi_field_mapping")
public class OgcApiFieldMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_id")
    private OgcApiPropertyMapping config;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "ogc_property", length = 255)
    private String ogcProperty;

    @Column(name = "elastic_property", length = 255)
    private String elasticProperty;

    @Column(name = "index_record_property", length = 255)
    private String indexRecordProperty;

    @Column(name = "type_override", length = 50)
    private String typeOverride;

    @Column(name = "sort_field_suffix", length = 255)
    private String sortFieldSuffix;

    @Builder.Default
    @Column(name = "is_sortable")
    private Boolean isSortable = false;

    @Builder.Default
    @Column(name = "is_queryable")
    private Boolean isQueryable = false;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    @Column(name = "add_property_to_output")
    private Boolean addPropertyToOutput = true;

    @Builder.Default
    @OneToMany(mappedBy = "fieldMapping", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC")
    private List<OgcApiFacetConfig> facets = new ArrayList<>();
}
