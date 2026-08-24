/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.thesaurus.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.geonetwork.constants.ApiParams;
import org.geonetwork.thesaurus.model.GetKeywordsResponse;
import org.geonetwork.thesaurus.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/{portal}/api/registries/vocabularies"})
@RequiredArgsConstructor
public class VocabularyController {

    private final GetKeywordsService getKeywordsService;
    private final GetKeyworkByIdService getKeyworkByIdService;
    private final DownloadThesaurusService downloadThesaurusService;
    private final RemoveThesaurusService removeThesaurusService;

    @io.swagger.v3.oas.annotations.Operation(summary = "Search keywords")
    @GetMapping(
            path = "/search",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public GetKeywordsResponse searchKeywords(
            @PathVariable String uiLang,
            @RequestParam(name = "thesaurus") String thesaurus,
            @RequestParam(name = "rows", defaultValue = "50") int rows)
            throws Exception {
        return getKeywordsService.getKeywords(uiLang, thesaurus, rows);
    }

    /** Gets the keyword by id. */
    @io.swagger.v3.oas.annotations.Operation(
            summary = "Get keyword by id",
            description = "Retrieve XML representation of keyword(s) from same thesaurus"
                    + "using different transformations. "
                    + "'to-iso19139-keyword' is the default and return an ISO19139 snippet."
                    + "'to-iso19139-keyword-as-xlink' return an XLinked element. Custom transformation "
                    + "can be create on a per schema basis.")
    @GetMapping(
            path = "/keyword",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "XML snippet with requested keywords."),
            })
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object getKeywordById(
            @Parameter(
                            description = "Keyword identifier or list of keyword identifiers comma separated.",
                            required = true)
                    @RequestParam(name = "id")
                    String uri,
            @Parameter(description = "Thesaurus to look info for the keyword(s).", required = true)
                    @RequestParam(name = "thesaurus")
                    String sThesaurusName,
            @Parameter(description = "Languages.") @RequestParam(name = "lang", required = false) String[] langs,
            @Parameter(description = "Only print the keyword, no thesaurus information.")
                    @RequestParam(required = false, defaultValue = "false")
                    boolean keywordOnly,
            @Parameter(description = "XSL template to use (ISO19139 keyword by default, see convert.xsl).")
                    @RequestParam(required = false)
                    String transformation,
            @Parameter(
                            description =
                                    "langMap, that converts the values in the 'lang' parameter to how they will be actually represented in the record. {'fre':'fra'} or {'fre':'fr'}.  Missing/empty means to convert to iso 2 letter.")
                    @RequestParam(name = "langMap", required = false)
                    String langMapJson,
            @Parameter(hidden = true) @RequestParam Map<String, String> allRequestParams,
            @Parameter(hidden = true) HttpServletRequest request)
            throws Exception {
        return getKeyworkByIdService.getKeyword(uri, sThesaurusName, allRequestParams, request);
    }

    /** Gets the thesaurus */
    @io.swagger.v3.oas.annotations.Operation(
            summary = "Download a thesaurus by name",
            description = "Download the thesaurus in SKOS format.")
    @GetMapping(
            path = "/{thesaurus:.+}",
            produces = {MediaType.TEXT_XML_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Thesaurus in SKOS format.",
                        content =
                                @Content(
                                        schema =
                                                @io.swagger.v3.oas.annotations.media.Schema(
                                                        type = "string",
                                                        format = "binary"))),
                @ApiResponse(responseCode = "404", description = ApiParams.API_RESPONSE_RESOURCE_NOT_FOUND)
            })
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<org.springframework.core.io.Resource> getThesaurus(
            @PathVariable @Parameter(description = "Thesaurus to download.", required = true) String thesaurus)
            throws Exception {
        return downloadThesaurusService.download(thesaurus);
    }

    /**
     * Delete thesaurus.
     *
     * @param thesaurus the thesaurus
     * @throws Exception the exception
     */
    @io.swagger.v3.oas.annotations.Operation(
            summary = "Delete a thesaurus by name",
            description = "Delete a thesaurus.")
    @DeleteMapping(value = "/{thesaurus:.+}")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Thesaurus deleted."),
                @ApiResponse(responseCode = "403", description = ApiParams.API_RESPONSE_NOT_ALLOWED_ONLY_USER_ADMIN),
                @ApiResponse(responseCode = "404", description = ApiParams.API_RESPONSE_RESOURCE_NOT_FOUND)
            })
    @PreAuthorize("hasAuthority('UserAdmin')")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void deleteThesaurus(
            @PathVariable @Parameter(description = "Thesaurus to delete.", required = true) String thesaurus)
            throws Exception {
        removeThesaurusService.delete(thesaurus);
    }
}
