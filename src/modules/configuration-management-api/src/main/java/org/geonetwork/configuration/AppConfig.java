/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.configuration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
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
@Table(name = "APP_CONFIGS")
@IdClass(AppConfigId.class)
public class AppConfig {
    @Id
    @Column(name = "APP")
    private String app;

    @Id
    @Column(name = "PROFILE")
    private String profile;

    @Id
    @Column(name = "LABEL")
    private String label;

    @Id
    @Column(name = "CONFIG_PARAM")
    private String configParam;

    @Column(name = "CONFIG_VALUE")
    private String configValue;
}
