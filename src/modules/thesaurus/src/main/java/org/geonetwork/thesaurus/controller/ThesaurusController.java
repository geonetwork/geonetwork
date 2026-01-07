/*
 * (c) 2026 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.thesaurus.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.geonetwork.thesaurus.model.GetThesauriListResponse;
import org.geonetwork.thesaurus.service.*;
import org.geonetwork.utility.legacy.Pair;
import org.jdom.Element;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

@RestController
@RequestMapping("/api/thesaurus")
@RequiredArgsConstructor
public class ThesaurusController {

    private final AddElementService addElementService;
    private final AddRegisterService addRegisterService;
    private final CategoryListService categoryListService;
    private final ClearService clearService;
    private final EditElementService editElementService;
    private final GetKeywordsService getKeywordsService;
    private final GetKeywordbyIdAsConceptService getKeywordbyIdAsConceptService;
    private final GetKeyworkByIdService getKeyworkByIdService;
    private final GetNarrowerBroaderService getNarrowerBroaderService;
    private final GetSelectedKeyordsService getSelectedKeyordsService;
    private final GetTopConceptService getTopConceptService;
    private final SelectKeywordsService selectKeywordsService;
    private final UpdateElementService updateElementService;
    private final GetThesauriListService getThesauriListService;

    public static Map<Pair<String, String>, String> addElement(Element params) {
        return null;
    }

    public Element addRegister(Element params) {
        // TODO implementation
        return null;
    }

    public Element clear(Element params) throws Exception {
        // TODO implementation
        return null;
    }

    public Element deleteElement(Element params) throws Exception {
        // TODO implementation
        return null;
    }

    public Element editElement(Element params) throws Exception {
        // TODO implementation
        return null;
    }

    // deprecated ??
    public Element getKeyworkById(Element params) throws Exception {
        // TODO implementation
        return null;
    }

    public Element getKeywordbyIdAsConcept(Element params) throws Exception {
        // TODO implementation
        return null;
    }

    @RequestMapping(value = "/{uiLang}/keywords")
    @ResponseBody
    public HttpEntity<byte[]> getKeywords(@PathVariable String uiLang, NativeWebRequest webRequest) throws Exception {
        // TODO implementation
        return null;
    }

    @GetMapping(path = "/{uiLang}/thesaurus", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetThesauriListResponse getThesauriList(@PathVariable String uiLang) throws Exception {
        return getThesauriListService.getList(uiLang);
    }

    public Element getNarrowerBroader(Element params) throws Exception {
        // TODO implementation
        return null;
    }

    public Element getSelectedKeyords(Element params) throws Exception {
        // TODO implementation
        return null;
    }

    public Element getTopConcept(Element params) throws Exception {
        // TODO implementation
        return null;
    }

    public Element categoryList(Element params) throws Exception {
        // TODO implementation
        return null;
    }

    public Element selectKeywords(Element params) throws Exception {
        // TODO implementation
        return null;
    }

    public Element updateElement(Element params) throws Exception {
        // TODO implementation
        return null;
    }
}
