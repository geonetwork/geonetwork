/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.thesaurus.controller;

import lombok.RequiredArgsConstructor;
import org.geonetwork.thesaurus.model.GetKeywordsRequest;
import org.geonetwork.thesaurus.model.GetKeywordsResponse;
import org.geonetwork.thesaurus.model.GetThesauriListResponse;
import org.geonetwork.thesaurus.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/thesaurus")
@RequiredArgsConstructor
public class ThesaurusController {

    private final GetThesauriListService getThesauriListService;
    private final GetKeywordsService getKeywordsService;

    @GetMapping(path = "/{uiLang}/thesaurus", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetThesauriListResponse getThesauriList(@PathVariable String uiLang) throws Exception {
        return getThesauriListService.getList(uiLang);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Search keywords")
    @GetMapping(
            path = "/{uiLang}/search",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    // @PostMapping("api/registries/vocabularies/search")
    public GetKeywordsResponse searchKeywords(@PathVariable String uiLang, @RequestBody GetKeywordsRequest requestBody)
            throws Exception {
        return getKeywordsService.getKeywords(uiLang, requestBody);
    }
}
