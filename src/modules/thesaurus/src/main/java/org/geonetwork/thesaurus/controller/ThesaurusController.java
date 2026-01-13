/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.thesaurus.controller;

import lombok.RequiredArgsConstructor;
import org.geonetwork.thesaurus.model.GetThesauriListResponse;
import org.geonetwork.thesaurus.service.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/thesaurus")
@RequiredArgsConstructor
public class ThesaurusController {

    private final GetThesauriListService getThesauriListService;

    @GetMapping(path = "/{uiLang}/thesaurus", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetThesauriListResponse getThesauriList(@PathVariable String uiLang) throws Exception {
        return getThesauriListService.getList(uiLang);
    }
}
