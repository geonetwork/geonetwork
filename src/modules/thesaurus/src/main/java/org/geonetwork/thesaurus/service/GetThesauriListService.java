/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
/*
 * (c) 2026 Open Source Geospatial Foundation - all rights reserved
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
import org.geonetwork.domain.thesaurus.repository.ConceptSchemeRepository;
import org.geonetwork.thesaurus.model.GetThesauriListResponse;
import org.geonetwork.thesaurus.model.Thesaurus;
import org.geonetwork.thesaurus.util.LanguageCodeConverter;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetThesauriListService {

    private ObjectMapper objectMapper;
    private final ConceptSchemeRepository repository;

    public GetThesauriListResponse getList(String uiLang) throws JsonProcessingException {
        var langCode = LanguageCodeConverter.toTwoLetterCode(uiLang);
        String schemeResponse = repository.findSchemeResponse(langCode);
        var jsonResponse = schemeResponse != null ? objectMapper.readTree(schemeResponse) : null;
        return convertJsonToResponse(jsonResponse);
    }

    private GetThesauriListResponse convertJsonToResponse(JsonNode jsonResponse) {
        GetThesauriListResponse response = new GetThesauriListResponse();

        if (jsonResponse != null && jsonResponse.isArray()) {
            List<Thesaurus> thesaurusList = new ArrayList<>();

            for (JsonNode node : jsonResponse) {
                Thesaurus thesaurus = objectMapper.convertValue(node, Thesaurus.class);
                thesaurusList.add(thesaurus);
            }

            response.setData(thesaurusList);
        }

        return response;
    }
}
