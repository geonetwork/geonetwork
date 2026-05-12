/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.thesaurus.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.geonetwork.domain.thesaurus.repository.ConceptSchemeRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DownloadThesaurusService {
    private ObjectMapper objectMapper;
    private final ConceptSchemeRepository repository;

    public ResponseEntity<Resource> download(String thesaurus) throws Exception {

        String responseString = repository.findSchemeResponse(thesaurus);
        JsonNode jsonResponse = responseString != null ? objectMapper.readTree(responseString) : null;

        if (jsonResponse == null) throw new IllegalArgumentException("Thesaurus not found --> " + thesaurus);

        return jsonToResource(jsonResponse);
    }

    private ResponseEntity<Resource> jsonToResource(JsonNode jsonNode) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        byte[] jsonBytes = mapper.writeValueAsBytes(jsonNode);
        Resource resource = new ByteArrayResource(jsonBytes);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.json")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(jsonBytes.length)
                .body(resource);
    }
}
