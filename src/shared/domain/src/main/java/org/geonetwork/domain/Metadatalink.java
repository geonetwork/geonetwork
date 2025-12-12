/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
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
@Table(name = "metadatalink")
public class Metadatalink {
    @SequenceGenerator(name = "metadatalink_id_gen", sequenceName = "harvester_setting_id_seq", allocationSize = 1)
    @EmbeddedId
    private MetadatalinkId id;

    @MapsId("linkid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "linkid", nullable = false)
    private Link linkid;

    @Size(max = 255)
    @Column(name = "metadatauuid")
    private String metadatauuid;
}
