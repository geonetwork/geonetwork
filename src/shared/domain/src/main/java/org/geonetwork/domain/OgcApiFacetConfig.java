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
@Table(name = "ogcapi_facet_config")
public class OgcApiFacetConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_mapping_id")
    private OgcApiFieldMapping fieldMapping;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "facet_name", length = 255)
    private String facetName;

    @Column(name = "facet_type", length = 50)
    private String facetType;

    @Column(name = "bucket_sorting", length = 50)
    private String bucketSorting;

    @Column(name = "bucket_sorting_direction", length = 50)
    private String bucketSortingDirection;

    @Column(name = "bucket_count")
    private Integer bucketCount;

    @Builder.Default
    @Column(name = "minimum_document_count", nullable = false)
    private int minimumDocumentCount = 1;

    @Column(name = "number_bucket_interval")
    private Double numberBucketInterval;

    @Column(name = "calendar_interval_unit", length = 50)
    private String calendarIntervalUnit;

    @Builder.Default
    @OneToMany(mappedBy = "facetConfig", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC")
    private List<OgcApiFilterFacet> filters = new ArrayList<>();
}
