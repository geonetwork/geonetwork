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
    @Column(name = "islistableinheaderselector", nullable = false, length = Integer.MAX_VALUE)
    private String islistableinheaderselector;
}
