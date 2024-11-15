/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.search;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Record controller. */
@RestController
@RequestMapping("/api/records")
public class RecordController {

    @Autowired
    public RecordController() {}

    /** Search. */
    @GetMapping(
            path = {"/{uuid:.+}", "/{uuid:.+}/formatters/{formatterId}"},
            produces = {
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.TEXT_HTML_VALUE,
                MediaType.TEXT_PLAIN_VALUE
            })
    public String getRecord(@PathVariable String uuid, @PathVariable String formatterId) throws IOException {

        return "";
    }
}
