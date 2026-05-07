/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.configuration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/configuration")
@RequiredArgsConstructor
@Tag(name = "Configuration", description = "API for managing application configuration parameters")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    /**
     * Retrieve all configuration parameters stored in the database for a given app, profile and label.
     *
     * @param app optional application name to filter by
     * @param profile optional profile to filter by
     * @param label optional label to filter by
     * @return map of configurations (key, value)
     */
    @GetMapping("/list")
    @Operation(
            summary = "List configuration parameters",
            description =
                    "Retrieve a key-value map of configuration parameters for a specific context (app, profile, label). Defaults to current application context if parameters are omitted.")
    public Map<String, String> list(
            @Parameter(description = "Application name to filter by (e.g., 'GeoNetwork')")
                    @RequestParam(required = false)
                    String app,
            @Parameter(description = "Spring profile to filter by (e.g., 'default', 'prod')")
                    @RequestParam(required = false)
                    String profile,
            @Parameter(description = "Configuration label to filter by (e.g., 'master')")
                    @RequestParam(required = false)
                    String label) {
        return configurationService.getConfigurationMap(app, profile, label);
    }

    /**
     * Retrieve a configuration parameter value from the Spring Environment.
     *
     * @param key the configuration parameter key
     * @return the parameter value
     */
    @GetMapping
    @Operation(
            summary = "Get a configuration value",
            description = "Retrieve a configuration parameter value from the Spring Environment using its key.")
    public String get(
            @Parameter(description = "The configuration parameter key (e.g., 'geonetwork.url')", required = true)
                    @RequestParam
                    String key) {
        return configurationService.getConfiguration(key);
    }

    /**
     * Update a configuration parameter in the database using PUT body.
     *
     * @param config the configuration object
     * @return success message
     */
    @PutMapping
    @Operation(
            summary = "Update a configuration parameter",
            description =
                    "Update an existing configuration parameter in the database. The context (app, profile, label) is automatically determined if not explicitly provided in the request body.")
    public String update(@RequestBody AppConfig config) {
        configurationService.updateConfiguration(config);
        return "Parameter updated";
    }
}
