/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.metadata;

import static org.geonetwork.setting.Settings.METADATA_URL_DYNAMICAPPLINKURL;
import static org.geonetwork.setting.Settings.METADATA_URL_SITEMAPLINKURL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
        when(settingManager.getValue(METADATA_URL_DYNAMICAPPLINKURL)).thenReturn(null);

        var metadataManager = new MetadataManager(
                mock(MetadataRepository.class),
                mock(OperationRepository.class),
                mock(OperationallowedRepository.class),
                settingManager);
        metadataManager.defaultPermalinkUrl = urlWithContext + "/api/records/{{UUID}}";

        var url = metadataManager.getWebClientUrl("abc-123", "eng");

        assertEquals("https://catalog.example/geonetwork/api/records/abc-123", url);
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

    @Test
    void getPermalinkUrlBuilder_readsSettingOnce() {
        var settingManager = mock(SettingManager.class);
        when(settingManager.getValue(METADATA_URL_SITEMAPLINKURL))
                .thenReturn("https://public.example/{{UUID}}?lang={{LANG}}");

        var metadataManager = new MetadataManager(
                mock(MetadataRepository.class),
                mock(OperationRepository.class),
                mock(OperationallowedRepository.class),
                settingManager);

        var permalink = metadataManager.getPermalinkUrlBuilder("all");

        assertEquals("https://public.example/abc-123?lang=all", permalink.apply("abc-123"));
        assertEquals("https://public.example/def-456?lang=all", permalink.apply("def-456"));
        verify(settingManager, times(1)).getValue(METADATA_URL_SITEMAPLINKURL);
    }

    @Test
    void getPermalinkUrl_keepsSpecialCharactersInUuidLiteral() {
        var settingManager = mock(SettingManager.class);
        when(settingManager.getValue(METADATA_URL_SITEMAPLINKURL))
                .thenReturn("https://public.example/{{UUID}}?lang={{LANG}}");

        var metadataManager = new MetadataManager(
                mock(MetadataRepository.class),
                mock(OperationRepository.class),
                mock(OperationallowedRepository.class),
                settingManager);

        var permalink = metadataManager.getPermalinkUrlBuilder("all");

        // '$' and '\' are not group references, and placeholders inside the uuid are not replaced
        assertEquals("https://public.example/a$1\\b?lang=all", permalink.apply("a$1\\b"));
        assertEquals("https://public.example/x{{LANG}}?lang=all", permalink.apply("x{{LANG}}"));
    }
}
