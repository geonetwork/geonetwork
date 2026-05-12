/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.configuration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ConfigurationServiceTest {

    @Mock
    private Environment env;

    @Mock
    private AppConfigRepository repository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ConfigurationService configurationService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(configurationService, "defaultApp", "GeoNetwork");
        ReflectionTestUtils.setField(configurationService, "defaultProfile", "default");
        ReflectionTestUtils.setField(configurationService, "defaultLabel", "master");
    }

    @Test
    void updateConfiguration_whenInDb_shouldUpdate() {
        String key = "test.key";
        String value = "new-value";
        AppConfigId id = new AppConfigId("GeoNetwork", "default", "master", key);
        AppConfig existing = AppConfig.builder()
                .app("GeoNetwork")
                .profile("default")
                .label("master")
                .configParam(key)
                .configValue("old")
                .internal(true)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existing));

        configurationService.updateConfiguration(null, null, null, key, value);

        verify(repository).save(any(AppConfig.class));
        verify(eventPublisher).publishEvent(any(ConfigurationUpdatedEvent.class));
    }

    @Test
    void updateConfiguration_whenNotInDbButInEnv_shouldInsert() {
        String key = "test.key";
        String value = "new-value";
        AppConfigId id = new AppConfigId("GeoNetwork", "default", "master", key);

        when(repository.findById(id)).thenReturn(Optional.empty());
        when(env.getProperty(key)).thenReturn("something");

        configurationService.updateConfiguration(null, null, null, key, value);

        verify(repository).save(any(AppConfig.class));
        verify(eventPublisher).publishEvent(any(ConfigurationUpdatedEvent.class));
    }

    @Test
    void updateConfiguration_whenNotInDbAndNotInEnv_shouldThrowException() {
        String key = "test.key";
        String value = "new-value";
        AppConfigId id = new AppConfigId("GeoNetwork", "default", "master", key);

        when(repository.findById(id)).thenReturn(Optional.empty());
        when(env.getProperty(key)).thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> configurationService.updateConfiguration(null, null, null, key, value));

        verify(repository, never()).save(any(AppConfig.class));
        verify(eventPublisher, never()).publishEvent(any(ConfigurationUpdatedEvent.class));
    }
}
