/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.configuration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.geonetwork.security.SecurityService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/configuration")
@RequiredArgsConstructor
@Validated
@Tag(name = "Configuration", description = "API for managing application configuration parameters")
public class ConfigurationController {

    private final ConfigurationService configurationService;
    private final SecurityService securityService;

    /** List configuration parameters. Non-admins will have internal parameters filtered out. */
    @GetMapping("/list")
    @Operation(
            summary = "List configuration parameters",
            description =
                    "Retrieve a map of configuration parameters. Non-admins will have internal parameters filtered out.")
    public Map<String, String> list(
            @Parameter(description = "Application name") @RequestParam(required = false) String app,
            @Parameter(description = "Spring profile") @RequestParam(required = false) String profile,
            @Parameter(description = "Configuration label") @RequestParam(required = false) String label) {
        boolean isAdmin = securityService.isAdmin();
        return configurationService.getConfigurationMap(app, profile, label, isAdmin);
    }

    /**
     * Get a configuration value from the Environment. Non-admins can only retrieve non-internal parameters that exist
     * in the database.
     */
    @GetMapping
    @Operation(
            summary = "Get a configuration value",
            description = "Retrieve a value from the Environment. Some parameters are visible to admin only.")
    public String get(
            @Parameter(description = "The configuration parameter key", required = true) @RequestParam String key) {
        if (securityService.isAdmin()) {
            return configurationService.getConfiguration(key);
        } else {
            if (!configurationService.canAccess(key)) {
                throw new AccessDeniedException("Access to configuration parameter '" + key + "' is denied.");
            }
            return configurationService.getConfiguration(key);
        }
    }

    /** Update or insert a configuration parameter. Only available to admins. */
    @PutMapping
    @Operation(
            summary = "Update or insert a configuration parameter",
            description = "Update an existing configuration in the DB or insert it from the Environment.")
    @PreAuthorize("@securityService.isAdmin()")
    public String update(
            @Parameter(description = "Application name") @RequestParam(required = false) @Size(max = 255) String app,
            @Parameter(description = "Spring profile") @RequestParam(required = false) @Size(max = 255) String profile,
            @Parameter(description = "Configuration label") @RequestParam(required = false) @Size(max = 255)
                    String label,
            @Parameter(description = "The configuration parameter key", required = true)
                    @RequestParam
                    @NotBlank
                    @Size(max = 255)
                    String key,
            @Parameter(description = "The configuration parameter value", required = true) @RequestParam @NotBlank
                    String value) {
        configurationService.updateConfiguration(app, profile, label, key, value);
        return "Parameter updated";
    }
}
