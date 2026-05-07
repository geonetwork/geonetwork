/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.configuration;

import java.util.List;
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
public class ConfigurationController {

    private final ConfigurationService configurationService;

    /**
     * Retrieve all configuration parameters stored in the database.
     *
     * @param app optional application name to filter by
     * @return list of configurations
     */
    @GetMapping("/list")
    public List<AppConfig> list(@RequestParam(required = false) String app) {
        if (app != null) {
            return configurationService.getConfigurationsByApp(app);
        }
        return configurationService.getAllConfigurations();
    }

    /**
     * Retrieve a configuration parameter value from the Spring Environment.
     *
     * @param key the configuration parameter key
     * @return the parameter value
     */
    @GetMapping
    public String get(@RequestParam String key) {
        return configurationService.getConfiguration(key);
    }

    /**
     * Update a configuration parameter in the database using PUT body.
     *
     * @param config the configuration object
     * @return success message
     */
    @PutMapping
    public String update(@RequestBody AppConfig config) {
        configurationService.updateConfiguration(
                config.getApp(),
                config.getProfile(),
                config.getLabel(),
                config.getConfigParam(),
                config.getConfigValue());
        return "Parameter updated";
    }
}
