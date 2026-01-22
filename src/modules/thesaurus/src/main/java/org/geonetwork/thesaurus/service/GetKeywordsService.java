/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.thesaurus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.geonetwork.domain.thesaurus.repository.ConceptRepository;
import org.geonetwork.thesaurus.model.*;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetKeywordsService {
    private ObjectMapper objectMapper;
    private final ConceptRepository repository;

    public GetKeywordsResponse getKeywords(String uiLang, GetKeywordsRequest requestBody)
            throws JsonProcessingException {

        // extract scheme
        String keywordsString = repository.getKeywords(null, uiLang);

        var jsonResponse = keywordsString != null ? objectMapper.readTree(keywordsString) : null;
        return convertJsonToResponse(jsonResponse);
    }

    private GetKeywordsResponse convertJsonToResponse(JsonNode jsonResponse) {
        GetKeywordsResponse response = new GetKeywordsResponse();

        if (jsonResponse != null && jsonResponse.isArray()) {
            List<GetKeywordsResponseItem> keywords = new ArrayList<>();

            for (JsonNode node : jsonResponse) {
                GetKeywordsResponseItem thesaurus = objectMapper.convertValue(node, GetKeywordsResponseItem.class);
                keywords.add(thesaurus);
            }
            response.setData(keywords);
        }

        return response;
    }
}
