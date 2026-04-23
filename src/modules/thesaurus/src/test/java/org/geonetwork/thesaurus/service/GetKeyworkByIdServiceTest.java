/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.thesaurus.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.geonetwork.domain.thesaurus.model.ConceptScheme;
import org.geonetwork.domain.thesaurus.repository.ConceptRepository;
import org.geonetwork.domain.thesaurus.repository.ConceptSchemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class GetKeyworkByIdServiceTest {

    @Mock
    private ConceptSchemeRepository conceptSchemeRepository;

    @Mock
    private ConceptRepository conceptRepository;

    @Mock
    private HttpServletRequest request;

    private GetKeyworkByIdService service;

    @BeforeEach
    void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        service = new GetKeyworkByIdService(conceptSchemeRepository, conceptRepository, objectMapper);
    }

    @Test
    void testGetKeyword_withValidData() throws Exception {
        // Given
        String uri = "http://example.org/address";
        String thesaurusName = "external.place.regions";
        Map<String, String> allRequestParams = Collections.emptyMap();

        ConceptScheme conceptScheme = new ConceptScheme();
        conceptScheme.setId(1L);
        when(conceptSchemeRepository.findByUri(thesaurusName)).thenReturn(Optional.of(conceptScheme));

        String keywordsJson =
                """
        [
            {
                "uri": "http://example.org/address",
                "values": {
                    "eng": "Addresses",
                    "fre": "Adresses"
                },
                "definitions": {
                    "eng": "Address definition",
                    "fre": "Définition d'adresse"
                }
            }
        ]
        """;
        when(conceptRepository.getKeywordsByUris(anyList(), any(Long.class))).thenReturn(keywordsJson);
        when(request.getHeader(HttpHeaders.ACCEPT)).thenReturn(MediaType.APPLICATION_JSON_VALUE);

        // When
        Object result = service.getKeyword(uri, thesaurusName, allRequestParams, request);

        // Then
        assertNotNull(result);
        assertInstanceOf(Map.class, result);
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> resultMap = (Map<String, Map<String, Object>>) result;
        assertEquals(1, resultMap.size());
        assertTrue(resultMap.containsKey("http://example.org/address"));
        Map<String, Object> keywordInfo = resultMap.get("http://example.org/address");
        assertNotNull(keywordInfo.get("values"));
        assertNotNull(keywordInfo.get("definitions"));
    }

    @Test
    void testGetKeyword_withMultipleUris() throws Exception {
        // Given
        String uri = "http://example.org/address,http://example.org/degree";
        String thesaurusName = "external.place.regions";
        Map<String, String> allRequestParams = Collections.emptyMap();

        ConceptScheme conceptScheme = new ConceptScheme();
        conceptScheme.setId(1L);
        when(conceptSchemeRepository.findByUri(thesaurusName)).thenReturn(Optional.of(conceptScheme));

        String keywordsJson =
                """
        [
            {
                "uri": "http://example.org/address",
                "values": { "eng": "Addresses" },
                "definitions": { "eng": "Address definition" }
            },
            {
                "uri": "http://example.org/degree",
                "values": { "eng": "Degree" },
                "definitions": { "eng": "Degree definition" }
            }
        ]
        """;
        when(conceptRepository.getKeywordsByUris(anyList(), any(Long.class))).thenReturn(keywordsJson);
        when(request.getHeader(HttpHeaders.ACCEPT)).thenReturn(MediaType.APPLICATION_JSON_VALUE);

        // When
        Object result = service.getKeyword(uri, thesaurusName, allRequestParams, request);

        // Then
        assertNotNull(result);
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> resultMap = (Map<String, Map<String, Object>>) result;
        assertEquals(2, resultMap.size());
        assertTrue(resultMap.containsKey("http://example.org/address"));
        assertTrue(resultMap.containsKey("http://example.org/degree"));
    }

    @Test
    void testGetKeyword_thesaurusNotFound() throws Exception {
        // Given
        String uri = "http://example.org/address";
        String thesaurusName = "nonexistent.thesaurus";
        Map<String, String> allRequestParams = Collections.emptyMap();

        when(conceptSchemeRepository.findByUri(thesaurusName)).thenReturn(Optional.empty());
        when(conceptSchemeRepository.findByInternalIdentifier(thesaurusName)).thenReturn(Collections.emptyList());
        when(request.getHeader(HttpHeaders.ACCEPT)).thenReturn(MediaType.APPLICATION_JSON_VALUE);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            service.getKeyword(uri, thesaurusName, allRequestParams, request);
        });
    }

    @Test
    void testGetKeyword_keywordNotFound() throws Exception {
        // Given
        String uri = "http://example.org/nonexistent";
        String thesaurusName = "external.place.regions";
        Map<String, String> allRequestParams = Collections.emptyMap();

        ConceptScheme conceptScheme = new ConceptScheme();
        conceptScheme.setId(1L);
        when(conceptSchemeRepository.findByUri(thesaurusName)).thenReturn(Optional.of(conceptScheme));

        when(conceptRepository.getKeywordsByUris(anyList(), any(Long.class))).thenReturn("[]");
        when(request.getHeader(HttpHeaders.ACCEPT)).thenReturn(MediaType.APPLICATION_JSON_VALUE);

        // When
        Object result = service.getKeyword(uri, thesaurusName, allRequestParams, request);

        // Then
        assertNotNull(result);
    }

    @Test
    void testGetKeyword_invalidAcceptHeader() {
        // Given
        String uri = "http://example.org/address";
        String thesaurusName = "external.place.regions";
        Map<String, String> allRequestParams = Collections.emptyMap();

        when(request.getHeader(HttpHeaders.ACCEPT)).thenReturn(MediaType.APPLICATION_XML_VALUE);

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.getKeyword(uri, thesaurusName, allRequestParams, request);
        });
        assertEquals(HttpStatus.NOT_IMPLEMENTED, exception.getStatusCode());
    }
}
