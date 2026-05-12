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
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "APP_CONFIGS")
@IdClass(AppConfigId.class)
public class AppConfig {
    @Id
    @Column(name = "APP", nullable = false)
    @Size(max = 255)
    private String app;

    @Id
    @Column(name = "PROFILE", nullable = false)
    @Size(max = 255)
    private String profile;

    @Id
    @Column(name = "LABEL", nullable = false)
    @Size(max = 255)
    private String label;

    @Id
    @Column(name = "CONFIG_PARAM", nullable = false)
    @NotBlank
    @Size(max = 255)
    private String configParam;

    @Column(name = "CONFIG_VALUE", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String configValue;

    @Column(name = "INTERNAL", nullable = false)
    @Builder.Default
    private boolean internal = true;
}
