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
@Table(name = "inspireatomfeed")
public class Inspireatomfeed {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inspireatomfeed_id_gen")
    @SequenceGenerator(name = "inspireatomfeed_id_gen", sequenceName = "inspire_atom_feed_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "atom", length = Integer.MAX_VALUE)
    private String atom;

    @Size(max = 255)
    @Column(name = "atomdatasetid")
    private String atomdatasetid;

    @Size(max = 255)
    @Column(name = "atomdatasetns")
    private String atomdatasetns;

    @Size(max = 255)
    @Column(name = "atomurl")
    private String atomurl;

    @Size(max = 255)
    @Column(name = "authoremail")
    private String authoremail;

    @Size(max = 255)
    @Column(name = "authorname")
    private String authorname;

    @Size(max = 3)
    @Column(name = "lang", length = 3)
    private String lang;

    @NotNull
    @Column(name = "metadataid", nullable = false)
    private Integer metadataid;

    @Size(max = 255)
    @Column(name = "rights")
    private String rights;

    @Size(max = 255)
    @Column(name = "subtitle")
    private String subtitle;

    @Size(max = 255)
    @Column(name = "title")
    private String title;
}
