/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.records;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.geonetwork.ogcapi.service.configuration.OgcApiPropertyMappingService;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldsMapperConfig;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Admin endpoints for managing the OGC API Records facets and property-mapping configuration. */
@RestController
@RequestMapping("/api/ogcapi/configuration")
@RequiredArgsConstructor
@Tag(name = "OGC API Configuration", description = "Manage OGC API Records facets and property-mapping config")
public class OgcApiConfigController {

    private final OgcApiPropertyMappingService configService;

    @Operation(summary = "Get the current OGC API property-mapping configuration")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrator')")
    public OgcElasticFieldsMapperConfig getConfiguration() {
        return configService.getConfig();
    }

    @Operation(summary = "Replace the OGC API property-mapping configuration")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrator')")
    public OgcElasticFieldsMapperConfig updateConfiguration(@RequestBody OgcElasticFieldsMapperConfig newConfig) {
        return configService.updateConfig(newConfig);
    }
}
