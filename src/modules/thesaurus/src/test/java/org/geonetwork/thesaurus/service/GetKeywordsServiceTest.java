/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.thesaurus.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geonetwork.domain.thesaurus.repository.ConceptRepository;
import org.geonetwork.thesaurus.model.GetKeywordsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetKeywordsServiceTest {

    @Mock
    private ConceptRepository repository;

    private ObjectMapper objectMapper;

    private GetKeywordsService service;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        service = new GetKeywordsService(objectMapper, repository);
    }

    @Test
    void testGetKeywords_withValidData() throws Exception {
        // Given
        String uiLang = "eng";
        String thesaurusName = "external.place.regions";
        String jsonResponse =
                """
        [
            {
                "values": {
                    "eng": "Addresses",
                    "fre": "Adresses"
                },
                "definitions": {
                    "eng": "Address definition",
                    "fre": "Définition d'adresse"
                },
                "coordEast": "180",
                "coordWest": "-180",
                "coordSouth": "-90",
                "coordNorth": "90",
                "thesaurusKey": "local.theme.codelist_unit",
                "value": "Addresses",
                "definition": "Address definition",
                "uri": "http://example.org/address"
            },
            {
                "values": {
                    "eng": "Degree",
                    "fre": "Degré"
                },
                "definitions": {},
                "coordEast": null,
                "coordWest": null,
                "coordSouth": null,
                "coordNorth": null,
                "thesaurusKey": "local.theme.codelist_unit_distance",
                "value": "Degree",
                "definition": null,
                "uri": "http://example.org/degree"
            }
        ]
        """;

        when(repository.getKeywords("place", "en", 50)).thenReturn(jsonResponse);

        // When
        GetKeywordsResponse result = service.getKeywords(uiLang, thesaurusName, 50);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        assertEquals("Addresses", result.getData().get(0).getValue());
        assertEquals("local.theme.codelist_unit", result.getData().get(0).getThesaurusKey());
    }

    @Test
    void testGetKeywords_withEmptyArray() throws Exception {
        // Given
        String uiLang = "eng";
        String thesaurusName = "external.place.regions";
        String jsonResponse = "[]";

        when(repository.getKeywords("place", "en", 50)).thenReturn(jsonResponse);

        // When
        GetKeywordsResponse result = service.getKeywords(uiLang, thesaurusName, 50);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(0, result.getData().size());
    }

    @Test
    void testGetKeywords_withNullResponse() throws Exception {
        // Given
        String uiLang = "eng";
        String thesaurusName = "external.place.regions";

        when(repository.getKeywords("place", "en", 50)).thenReturn(null);

        // When
        GetKeywordsResponse result = service.getKeywords(uiLang, thesaurusName, 50);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(0, result.getData().size());
    }
}
