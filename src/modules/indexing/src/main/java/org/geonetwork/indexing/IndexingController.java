/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.indexing;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.geonetwork.index.client.IndexClient;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Indexing controller. */
@RestController
@RequestMapping("/api/indexing")
@RequiredArgsConstructor
public class IndexingController {

    private final IndexingService indexingService;
    private final IndexClient indexClient;

    /** Create index. */
    @GetMapping(path = "/setup", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrator')")
    public String setupIndex(@RequestParam(defaultValue = "true") boolean dropIfExists) {
        indexClient.setupIndex(dropIfExists);
        return "Index created.";
    }

    /** Index all records. */
    @GetMapping(path = "/index", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrator')")
    public String indexRecords(@RequestParam(defaultValue = "", required = false) String[] uuid) {

        indexingService.index(uuid.length > 0 ? List.of(uuid) : null);
        return "Indexing task started.";
    }
}
