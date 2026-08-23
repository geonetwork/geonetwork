/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.configuration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import org.geonetwork.security.SecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConfigurationController.class)
@WithMockUser
class ConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConfigurationService configurationService;

    @MockitoBean
    private SecurityService securityService;

    @Test
    void list_whenAdmin_shouldCallGetConfigurationMapWithTrue() throws Exception {
        when(securityService.isAdmin()).thenReturn(true);
        when(configurationService.getConfigurationMap(any(), any(), any(), anyBoolean()))
                .thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/api/configuration/list")).andExpect(status().isOk());

        verify(configurationService).getConfigurationMap(null, null, null, true);
    }

    @Test
    void list_whenNotAdmin_shouldCallGetConfigurationMapWithFalse() throws Exception {
        when(securityService.isAdmin()).thenReturn(false);
        when(configurationService.getConfigurationMap(any(), any(), any(), anyBoolean()))
                .thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/api/configuration/list")).andExpect(status().isOk());

        verify(configurationService).getConfigurationMap(null, null, null, false);
    }

    @Test
    void get_whenAdmin_shouldReturnEnvironmentValue() throws Exception {
        String key = "test.key";
        String value = "envValue";
        when(securityService.isAdmin()).thenReturn(true);
        when(configurationService.getConfiguration(key)).thenReturn(value);

        mockMvc.perform(get("/api/configuration").param("key", key))
                .andExpect(status().isOk())
                .andExpect(content().string(value));

        verify(configurationService).getConfiguration(key);
        verify(configurationService, never()).canAccess(anyString());
    }

    @Test
    void get_whenNotAdminAndAccessible_shouldReturnEnvironmentValue() throws Exception {
        String key = "test.key";
        String value = "envValue";
        when(securityService.isAdmin()).thenReturn(false);
        when(configurationService.canAccess(key)).thenReturn(true);
        when(configurationService.getConfiguration(key)).thenReturn(value);

        mockMvc.perform(get("/api/configuration").param("key", key))
                .andExpect(status().isOk())
                .andExpect(content().string(value));

        verify(configurationService).canAccess(key);
        verify(configurationService).getConfiguration(key);
    }

    @Test
    void get_whenNotAdminAndNotAccessible_shouldReturnForbidden() throws Exception {
        String key = "test.key";
        when(securityService.isAdmin()).thenReturn(false);
        when(configurationService.canAccess(key)).thenReturn(false);

        mockMvc.perform(get("/api/configuration").param("key", key)).andExpect(status().isForbidden());

        verify(configurationService).canAccess(key);
        verify(configurationService, never()).getConfiguration(anyString());
    }

    @Test
    void update_whenAdmin_shouldUpdateConfiguration() throws Exception {
        String app = "app";
        String profile = "profile";
        String label = "label";
        String key = "key";
        String value = "value";
        when(securityService.isAdmin()).thenReturn(true);

        mockMvc.perform(put("/api/configuration")
                        .with(csrf())
                        .param("app", app)
                        .param("profile", profile)
                        .param("label", label)
                        .param("key", key)
                        .param("value", value))
                .andExpect(status().isOk())
                .andExpect(content().string("Parameter updated"));

        verify(configurationService).updateConfiguration(app, profile, label, key, value);
    }
}
