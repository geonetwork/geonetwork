/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "validation")
public class Validation {
    @SequenceGenerator(name = "validation_id_gen", sequenceName = "user_search_id_seq", allocationSize = 1)
    @EmbeddedId
    private ValidationId id;

    @Column(name = "failed")
    private Integer failed;

    @Column(name = "tested")
    private Integer tested;

    @Column(name = "required")
    private Boolean required;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @Size(max = 30)
    @Column(name = "valdate", length = 30)
    private String valdate;

    @Column(name = "reportcontent", length = Integer.MAX_VALUE)
    private String reportcontent;

    @Size(max = 255)
    @Column(name = "reporturl")
    private String reporturl;
}
