/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.configuration;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConfigurationController.class)
class ConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConfigurationService configurationService;

    @Test
    void list_withoutApp_shouldCallGetAllConfigurations() throws Exception {
        when(configurationService.getAllConfigurations()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/configuration/list")).andExpect(status().isOk());

        verify(configurationService).getAllConfigurations();
    }

    @Test
    void list_withApp_shouldCallGetConfigurationsByApp() throws Exception {
        String app = "testApp";
        when(configurationService.getConfigurationsByApp(app)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/configuration/list").param("app", app)).andExpect(status().isOk());

        verify(configurationService).getConfigurationsByApp(app);
    }
}
