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
@Table(name = "sources")
public class Source {
    @Id
    @Size(max = 255)
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 30)
    @Column(name = "creationdate", length = 30)
    private String creationdate;

    @Size(max = 255)
    @Column(name = "filter")
    private String filter;

    @Column(name = "groupowner")
    private Integer groupowner;

    @Size(max = 255)
    @Column(name = "logo")
    private String logo;

    @Size(max = 255)
    @Column(name = "type")
    private String type;

    @Size(max = 255)
    @Column(name = "uiconfig")
    private String uiconfig;

    @Size(max = 255)
    @Column(name = "servicerecord")
    private String servicerecord;

    @NotNull
    @Column(name = "islistableinheaderselector", nullable = false)
    @Convert(converter = BooleanToYN.class)
    private Boolean islistableinheaderselector;
}
