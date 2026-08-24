/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.thesaurus.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.domain.thesaurus.model.ConceptScheme;
import org.geonetwork.domain.thesaurus.repository.ConceptRepository;
import org.geonetwork.domain.thesaurus.repository.ConceptSchemeRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class GetKeyworkByIdService {
    private final String SEPARATOR = ",";
    private final ConceptSchemeRepository conceptSchemeRepository;
    private final ConceptRepository conceptRepository;
    private final ObjectMapper mapper;

    public Object getKeyword(
            String uri,
            String sThesaurusName,
            Map<String, String> allRequestParams, // to be used for XML only
            HttpServletRequest request)
            throws Exception {

        String acceptHeader = StringUtils.isBlank(request.getHeader(HttpHeaders.ACCEPT))
                ? MediaType.APPLICATION_XML_VALUE
                : request.getHeader(HttpHeaders.ACCEPT);
        if (MediaType.APPLICATION_JSON_VALUE.equals(acceptHeader)) {

            // Fetch the concept scheme related details . First by full match ad then by partial match
            Long conceptSchemeId;
            Optional<ConceptScheme> conceptScheme = conceptSchemeRepository.findByUri(sThesaurusName);
            if (conceptScheme.isEmpty()) {
                List<ConceptScheme> conceptSchemes = conceptSchemeRepository.findByInternalIdentifier(sThesaurusName);
                if (conceptSchemes.isEmpty()) {
                    throw new IllegalArgumentException(String.format("Thesaurus '%s' not found.", sThesaurusName));
                } else {
                    conceptSchemeId = conceptSchemes.getFirst().getId();
                }
            } else {
                conceptSchemeId = conceptScheme.get().getId();
            }

            Map<String, Map<String, Object>> jsonResponse = new HashMap<>();

            uri = URLDecoder.decode(uri, StandardCharsets.UTF_8);
            if (uri != null) {
                String[] url;
                if (!uri.contains(SEPARATOR)) {
                    url = new String[] {uri};
                } else {
                    url = uri.split(SEPARATOR);
                }

                List<String> uris = Arrays.asList(url);

                String keywordsJson = conceptRepository.getKeywordsByUris(uris, conceptSchemeId);

                if (keywordsJson != null) {
                    JsonNode rootNode = mapper.readTree(keywordsJson);
                    if (rootNode.isArray()) {
                        for (JsonNode node : rootNode) {
                            String nodeUri = node.get("uri").asText();
                            JsonNode valuesMap = node.get("values");
                            JsonNode definitionsMap = node.get("definitions");

                            Map<String, Object> keywordInfo = new HashMap<>();
                            keywordInfo.put("values", valuesMap);
                            keywordInfo.put("definitions", definitionsMap);
                            jsonResponse.put(nodeUri, keywordInfo);
                        }
                    }
                }
            }
            return jsonResponse;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Only JSON format is supported");
        }
    }
}
