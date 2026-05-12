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
    @Column(name = "APP", nullable = false)
    private String app;

    @Id
    @Column(name = "PROFILE", nullable = false)
    private String profile;

    @Id
    @Column(name = "LABEL", nullable = false)
    private String label;

    @Id
    @Column(name = "CONFIG_PARAM", nullable = false)
    private String configParam;

    @Column(name = "CONFIG_VALUE", columnDefinition = "TEXT", nullable = false)
    private String configValue;

    @Column(name = "INTERNAL", nullable = false)
    @Builder.Default
    private boolean internal = true;
}
