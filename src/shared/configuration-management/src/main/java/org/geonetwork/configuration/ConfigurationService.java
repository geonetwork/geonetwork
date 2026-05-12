/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.configuration;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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

    @Value("${spring.cloud.config.label:master}")
    private String defaultLabel;

    /** Retrieve configuration value from the Spring Environment. */
    public String getConfiguration(String key) {
        return env.getProperty(key);
    }

    /** Retrieve configuration value from the database using default context. */
    public String getConfigurationFromDb(String key) {
        AppConfigId id = new AppConfigId(defaultApp, defaultProfile, defaultLabel, key);
        return repository.findById(id).map(AppConfig::getConfigValue).orElse(null);
    }

    /** Check if a configuration is marked as internal in the database. */
    public boolean isInternal(String key) {
        AppConfigId id = new AppConfigId(defaultApp, defaultProfile, defaultLabel, key);
        return repository.findById(id).map(AppConfig::isInternal).orElse(false);
    }

    /** Check if a configuration exists in the database and is not internal. */
    public boolean canAccess(String key) {
        AppConfigId id = new AppConfigId(defaultApp, defaultProfile, defaultLabel, key);
        return repository.findById(id).map(c -> !c.isInternal()).orElse(false);
    }

    /** Retrieve all configurations from the database. */
    public List<AppConfig> getAllConfigurations() {
        return repository.findAll();
    }

    /** Retrieve all configurations for a specific application. */
    public List<AppConfig> getConfigurationsByApp(String app) {
        return repository.findByApp(app);
    }

    /** Retrieve configurations as a Map for a given context. */
    public Map<String, String> getConfigurationMap(String app, String profile, String label) {
        return getConfigurationMap(app, profile, label, true);
    }

    /** Retrieve configurations as a Map for a given context, optionally filtering internal ones. */
    public Map<String, String> getConfigurationMap(String app, String profile, String label, boolean includeInternal) {
        List<AppConfig> configs = repository.findByAppAndProfileAndLabel(
                app != null ? app : defaultApp,
                profile != null ? profile : defaultProfile,
                label != null ? label : defaultLabel);

        return configs.stream()
                .filter(c -> includeInternal || !c.isInternal())
                .collect(Collectors.toMap(AppConfig::getConfigParam, AppConfig::getConfigValue));
    }

    /** Update or insert a configuration parameter. */
    @Transactional
    public void updateConfiguration(AppConfig config) {
        updateConfiguration(
                config.getApp(),
                config.getProfile(),
                config.getLabel(),
                config.getConfigParam(),
                config.getConfigValue());
    }

    /**
     * Update an existing database entry or insert a new one if it exists in the Environment. Preserves the existing
     * 'internal' flag if present.
     */
    @Transactional
    public void updateConfiguration(String app, String profile, String label, String key, String value) {
        AppConfigId id = new AppConfigId(
                app != null ? app : defaultApp,
                profile != null ? profile : defaultProfile,
                label != null ? label : defaultLabel,
                key);

        Optional<AppConfig> existing = repository.findById(id);

        boolean internal = true;
        if (existing.isEmpty()) {
            if (env.getProperty(key) == null) {
                throw new IllegalArgumentException("Configuration parameter '" + key + "' does not exist for app '"
                        + id.getApp() + "', profile '" + id.getProfile() + "', label '" + id.getLabel()
                        + "' nor in Environment. Update aborted.");
            }
            log.info("Configuration parameter '{}' not in DB but found in Environment. Inserting...", key);
        } else {
            internal = existing.get().isInternal();
        }

        AppConfig config = AppConfig.builder()
                .app(id.getApp())
                .profile(id.getProfile())
                .label(id.getLabel())
                .configParam(id.getConfigParam())
                .configValue(value)
                .internal(internal)
                .build();
        repository.save(config);

        // The context refresh is managed via ConfigurationUpdatedEvent
        log.info("Configuration saved to DB for {}. Publishing update event...", id);
        eventPublisher.publishEvent(new ConfigurationUpdatedEvent(id, value));
    }
}
