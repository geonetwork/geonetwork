/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.configuration;

import org.geonetwork.domain.Source;
import org.geonetwork.domain.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Redirects /rss to a configurable default OGC API Records RSS query. */
@Controller
public class RssRedirectController {
    private final SourceRepository sourceRepository;
    private final String rssRedirectTemplate;

    public RssRedirectController(
            SourceRepository sourceRepository,
            @Value(
                            "${geonetwork.rss.redirect:${geonetwork.openapi-records.links.base-path:/ogcapi-records}/collections/{{UUID}}/items?f=rss}")
                    String rssRedirectTemplate) {
        this.sourceRepository = sourceRepository;
        this.rssRedirectTemplate = rssRedirectTemplate;
    }

    @GetMapping("/rss")
    public String redirectToRssFeed() {
        var mainPortalUuid =
                sourceRepository.findByType("portal").map(Source::getUuid).orElse("main");
        var resolvedTarget =
                rssRedirectTemplate.replace("{{UUID}}", mainPortalUuid).replace("{{uuid}}", mainPortalUuid);
        resolvedTarget = resolvedTarget.replace("/collections/main/", "/collections/" + mainPortalUuid + "/");
        resolvedTarget = resolvedTarget.replace("/collections/main?", "/collections/" + mainPortalUuid + "?");
        return "redirect:" + resolvedTarget;
    }
}
