/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
