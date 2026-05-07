/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.configuration;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfigurationService {

    private final Environment env;
    private final AppConfigRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${spring.application.name:GeoNetwork}")
    private String defaultApp;

    @Value("${spring.profiles.active:default}")
    private String defaultProfile;

    /** Retrieve configuration parameter from Environment */
    public String getConfiguration(String key) {
        return env.getProperty(key);
    }

    /** Retrieve all configuration parameters stored in the database. */
    public List<AppConfig> getAllConfigurations() {
        return repository.findAll();
    }

    /** Retrieve all configuration parameters for a specific application stored in the database. */
    public List<AppConfig> getConfigurationsByApp(String app) {
        return repository.findByApp(app);
    }

    /** Update an existing configuration parameter in the database and refresh context. */
    @Transactional
    public void updateConfiguration(String app, String profile, String label, String key, String value) {
        AppConfigId id = new AppConfigId(
                app != null ? app : defaultApp,
                profile != null ? profile : defaultProfile,
                label != null ? label : "master",
                key);

        if (!repository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Configuration parameter '" + key + "' does not exist for app '" + id.getApp() + "', profile '"
                            + id.getProfile() + "', label '" + id.getLabel() + "'. Update aborted.");
        }

        AppConfig config = AppConfig.builder()
                .app(id.getApp())
                .profile(id.getProfile())
                .label(id.getLabel())
                .configParam(id.getConfigParam())
                .configValue(value)
                .build();
        repository.save(config);

        // The context refresh is managed via ConfigurationUpdatedEvent
        log.info("Configuration saved to DB for {}. Publishing update event...", id);
        eventPublisher.publishEvent(new ConfigurationUpdatedEvent(id, value));
    }
}
