/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain;

import jakarta.persistence.Column;
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
    @Column(name = "internal", nullable = false, length = Integer.MAX_VALUE)
    private String internal;

    @NotNull
    @Column(name = "\"position\"", nullable = false)
    private Integer position;

    @Column(name = "\"value\"", length = Integer.MAX_VALUE)
    private String value;

    @NotNull
    @Column(name = "editable", nullable = false, length = Integer.MAX_VALUE)
    private String editable;

    @NotNull
    @Column(name = "encrypted", nullable = false, length = Integer.MAX_VALUE)
    private String encrypted;
}
