/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.thesaurus.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geonetwork.domain.thesaurus.repository.ConceptSchemeRepository;
import org.geonetwork.thesaurus.model.GetThesauriListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetThesauriListServiceTest {

    @Mock
    private ConceptSchemeRepository repository;

    private ObjectMapper objectMapper;

    private GetThesauriListService service;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        service = new GetThesauriListService(objectMapper, repository);
    }

    @Test
    void testGetList_withValidData() throws Exception {
        // Given
        String uiLang = "eng";
        String uiLang2Char = "en";
        String jsonResponse =
                """
       [
           {
        "key": "local.theme.codelist_unit",
        "dname": "theme",
        "description": "Standard units used for scientific measurements.",
        "filename": "codelist_unit.rdf",
        "title": "Units of Measurement",
        "multilingualTitles": [
          {
            "lang": "de",
            "title": "Einheiten"
          },
          {
            "lang": "en",
            "title": "Units of Measurement"
          }
        ],
        "multilingualDescriptions": [
          {
            "lang": "en",
            "description": "Standard units used for scientific measurements."
          }
        ],
        "url": "http://example.org/scheme/units",
        "defaultNamespace": "http://example.org/units/",
        "type": "local"
      },
      {
        "key": "external.theme.codelist_geo",
        "dname": "theme",
        "description": null,
        "filename": "codelist_geo.rdf",
        "title": "Geographic Features",
        "multilingualTitles": [
          {
            "lang": "en",
            "title": "Geographic Features"
          },
          {
            "lang": "fr",
            "title": "Géographie"
          }
        ],
        "multilingualDescriptions": [],
        "url": "http://example.org/scheme/geo",
        "defaultNamespace": "http://example.org/geo/",
        "type": "external"
      }
       ]
     """;

        when(repository.findSchemeResponse(uiLang2Char)).thenReturn(jsonResponse);

        // When
        GetThesauriListResponse result = service.getList(uiLang);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertFalse(result.getData().isEmpty());
    }

    @Test
    void testGetList_withEmptyArray() throws Exception {
        // Given
        String uiLang = "eng";
        String uiLang2Char = "en";
        String jsonResponse = "[]";

        when(repository.findSchemeResponse(uiLang2Char)).thenReturn(jsonResponse);

        // When
        GetThesauriListResponse result = service.getList(uiLang);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(0, result.getData().size());
    }

    @Test
    void testGetList_withNullResponse() throws Exception {
        // Given
        String uiLang = "eng";
        String uiLang2Char = "en";

        when(repository.findSchemeResponse(uiLang2Char)).thenReturn(null);

        // When
        GetThesauriListResponse result = service.getList(uiLang);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(0, result.getData().size());
    }
}
