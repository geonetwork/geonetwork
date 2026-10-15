/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.metadata;

import static org.geonetwork.setting.Settings.METADATA_URL_DYNAMICAPPLINKURL;
import static org.geonetwork.setting.Settings.METADATA_URL_SITEMAPLINKURL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.domain.repository.OperationRepository;
import org.geonetwork.domain.repository.OperationallowedRepository;
import org.geonetwork.setting.SettingManager;
import org.junit.jupiter.api.Test;

class MetadataManagerTest {
    String baseUrl = "https://catalog.example";
    String contextPath = "/geonetwork";
    String urlWithContext = baseUrl + contextPath;

    @Test
    void getPermalinkUrl_appliesTemplateTokens_caseInsensitive() {
        var settingManager = mock(SettingManager.class);
        when(settingManager.getServerURL()).thenReturn(urlWithContext);
        when(settingManager.getValue(METADATA_URL_SITEMAPLINKURL))
                .thenReturn("https://public.example/{{uUiD}}?lang={{LaNg}}");

        var metadataManager = new MetadataManager(
                mock(MetadataRepository.class),
                mock(OperationRepository.class),
                mock(OperationallowedRepository.class),
                settingManager);

        var url = metadataManager.getPermalinkUrl("abc-123", "eng");

        assertEquals("https://public.example/abc-123?lang=eng", url);
    }

    @Test
    void getWebClientUrl_usesDefaultWhenTemplateSettingMissing() {
        var settingManager = mock(SettingManager.class);
        when(settingManager.getBaseUrlWithContextPath()).thenReturn(urlWithContext);
        when(settingManager.getValue(METADATA_URL_DYNAMICAPPLINKURL)).thenReturn(null);

        var metadataManager = new MetadataManager(
                mock(MetadataRepository.class),
                mock(OperationRepository.class),
                mock(OperationallowedRepository.class),
                settingManager);

        var url = metadataManager.getWebClientUrl("abc-123", "eng");

        assertEquals("https://catalog.example/geonetwork/srv/api/records/abc-123?language=all", url);
    }

    @Test
    void getPermalinkUrl_replacesMissingLanguageWithEmptyValue() {
        var settingManager = mock(SettingManager.class);
        when(settingManager.getServerURL()).thenReturn("https://catalog.example/geonetwork/");
        when(settingManager.getValue(METADATA_URL_SITEMAPLINKURL))
                .thenReturn("https://public.example/{{UUID}}?lang={{LANG}}");

        var metadataManager = new MetadataManager(
                mock(MetadataRepository.class),
                mock(OperationRepository.class),
                mock(OperationallowedRepository.class),
                settingManager);

        var url = metadataManager.getPermalinkUrl("abc-123", "");

        assertEquals("https://public.example/abc-123?lang=", url);
    }
}
