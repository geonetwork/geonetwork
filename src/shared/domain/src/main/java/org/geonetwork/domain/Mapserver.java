/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "mapservers")
public class Mapserver {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mapservers_id_gen")
    @SequenceGenerator(name = "mapservers_id_gen", sequenceName = "mapserver_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "configurl", nullable = false)
    private String configurl;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 32)
    @NotNull
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Size(max = 255)
    @Column(name = "namespace")
    private String namespace;

    @Size(max = 255)
    @Column(name = "namespaceprefix")
    private String namespaceprefix;

    @Size(max = 128)
    @Column(name = "password", length = 128)
    private String password;

    @Column(name = "pushstyleinworkspace", length = Integer.MAX_VALUE)
    private String pushstyleinworkspace;

    @Size(max = 255)
    @Column(name = "stylerurl")
    private String stylerurl;

    @Size(max = 128)
    @Column(name = "username", length = 128)
    private String username;

    @Size(max = 255)
    @Column(name = "wcsurl")
    private String wcsurl;

    @Size(max = 255)
    @Column(name = "wfsurl")
    private String wfsurl;

    @Size(max = 255)
    @Column(name = "wmsurl")
    private String wmsurl;
}
