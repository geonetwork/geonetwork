/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.thesaurus.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.geonetwork.thesaurus.SkosRdfImportService;
import org.geonetwork.thesaurus.model.GetKeywordsResponse;
import org.geonetwork.thesaurus.model.GetThesauriListResponse;
import org.geonetwork.thesaurus.model.SkosRdfImportRequest;
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
    private final SkosRdfImportService skosRdfImportService;

    @GetMapping(path = "/{uiLang}/thesaurus", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetThesauriListResponse getThesauriList(@PathVariable String uiLang) throws Exception {
        return getThesauriListService.getList(uiLang);
    }

    @GetMapping(
            path = "/{uiLang}/search",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    // @PostMapping("api/registries/vocabularies/search")
    public GetKeywordsResponse searchKeywords(
            @PathVariable String uiLang,
            @RequestParam(name = "thesaurus") String thesaurus,
            @RequestParam(name = "rows", defaultValue = "50") int rows)
            throws Exception {
        return getKeywordsService.getKeywords(uiLang, thesaurus, rows);
    }

    @PostMapping(path = "/import", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, String> importRdf(@RequestBody SkosRdfImportRequest request) throws Exception {
        String path = request.getPath();
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("path is required");
        }

        Path rdfPath = Path.of(path);
        if (!Files.isRegularFile(rdfPath)) {
            throw new IllegalArgumentException("RDF file not found: " + rdfPath);
        }

        try (InputStream in = Files.newInputStream(rdfPath)) {
            skosRdfImportService.importRdf(in);
        }

        return Map.of("status", "ok");
    }
}
