/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.thesaurus.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geonetwork.domain.thesaurus.repository.ConceptSchemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DownloadThesaurusServiceTest {

    @Mock
    private ConceptSchemeRepository repository;

    private ObjectMapper objectMapper;

    private DownloadThesaurusService service;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        service = new DownloadThesaurusService(objectMapper, repository);
    }

    @Test
    void testDownload_withValidData() throws Exception {
        // Given
        String thesaurusName = "external.place.regions";
        String jsonResponse =
                """
        {
            "key": "external.place.regions",
            "dname": "place",
            "description": "Regions of the world",
            "filename": "regions.rdf",
            "title": "Regions",
            "multilingualTitles": [],
            "multilingualDescriptions": [],
            "url": "http://example.org/regions",
            "defaultNamespace": "http://example.org/regions#",
            "type": "external"
        }
        """;

        when(repository.findSchemeResponse(thesaurusName)).thenReturn(jsonResponse);

        // When
        ResponseEntity<Resource> result = service.download(thesaurusName);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(
                "attachment; filename=\"data.json\"",
                result.getHeaders().getContentDisposition().toString());
        assertNotNull(result.getBody());
    }

    @Test
    void testDownload_thesaurusNotFound() {
        // Given
        String thesaurusName = "nonexistent.thesaurus";
        when(repository.findSchemeResponse(thesaurusName)).thenReturn(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.download(thesaurusName);
        });
        assertEquals("Thesaurus not found --> " + thesaurusName, exception.getMessage());
    }
}
