/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geonetwork.domain.converter.BooleanToYN;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "settings")
public class Setting {
    @Id
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "datatype")
    private Integer datatype;

    @NotNull
    @Column(name = "internal", nullable = false)
    @Convert(converter = BooleanToYN.class)
    private Boolean internal;

    @NotNull
    @Column(name = "\"position\"", nullable = false)
    private Integer position;

    @Column(name = "\"value\"", length = Integer.MAX_VALUE)
    private String value;

    @NotNull
    @Column(name = "editable", nullable = false)
    @Convert(converter = BooleanToYN.class)
    private Boolean editable;

    @NotNull
    @Column(name = "encrypted", nullable = false)
    @Convert(converter = BooleanToYN.class)
    private Boolean encrypted;
}
