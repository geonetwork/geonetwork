/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.thesaurus.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geonetwork.thesaurus.util.KeywordSearchType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Keyword search request")
public class GetKeywordsRequest {

    /**
     * Search keywords.
     *
     * @param q the q
     * @param lang the lang
     * @param rows the rows
     * @param start the start
     * @param targetLangs the target langs
     * @param thesaurus the thesaurus
     * @param type the type
     * @param uri the uri
     * @param sort the sort
     * @param request the request
     * @param httpSession the http session
     */
    @Schema(description = "Query")
    private String q;

    @Schema(description = "Query language", defaultValue = "eng")
    @Builder.Default
    private String lang = "eng";

    @Schema(description = "Number of rows", defaultValue = "1000")
    @Builder.Default
    private int rows = 1000;

    @Schema(description = "Start offset", defaultValue = "0")
    @Builder.Default
    private int start = 0;

    @Schema(description = "Return keyword information in one or more languages")
    private List<String> targetLangs;

    @Schema(description = "Thesaurus identifiers")
    private List<String> thesaurus;

    @Schema(description = "Type of search", defaultValue = "CONTAINS")
    @Builder.Default
    private KeywordSearchType type = KeywordSearchType.CONTAINS;

    @Schema(description = "URI query")
    private String uri;

    @Schema(description = "Sort order", defaultValue = "DESC")
    @Builder.Default
    private String sort = "DESC";
}
