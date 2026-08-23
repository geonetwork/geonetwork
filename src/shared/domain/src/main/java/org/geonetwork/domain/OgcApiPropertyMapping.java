/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
@Table(name = "ogcapi_property_mapping")
public class OgcApiPropertyMapping {

    @Id
    @Column(name = "id", nullable = false, length = 255)
    private String id;

    @Column(name = "default_bucket_count", nullable = false)
    @Builder.Default
    private int defaultBucketCount = 10;

    @Builder.Default
    @Column(name = "update_sequence", nullable = false)
    private Long updateSequence = 1L;

    @Builder.Default
    @OneToMany(mappedBy = "config", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC")
    private List<OgcApiFieldMapping> fields = new ArrayList<>();
}
