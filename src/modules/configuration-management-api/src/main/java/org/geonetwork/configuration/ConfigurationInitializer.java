/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** Initializes the APP_CONFIGS table with default values if it is empty. */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test") // Avoid running during unit tests unless specifically needed
public class ConfigurationInitializer implements CommandLineRunner {

    private final AppConfigRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            log.info("Initializing APP_CONFIGS table with default values...");

            // Example default configuration
            saveDefault("GeoNetwork", "default", "master", "geonetwork.api.configuration.enabled", "true");
            saveDefault("GeoNetwork", "default", "master", "geonetwork.api.configuration.refresh-on-write", "true");

            log.info("Configuration initialization complete.");
        }
    }

    private void saveDefault(String app, String profile, String label, String key, String value) {
        repository.save(AppConfig.builder()
                .app(app)
                .profile(profile)
                .label(label)
                .configParam(key)
                .configValue(value)
                .build());
    }
}
