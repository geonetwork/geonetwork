/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.geonetwork.domain.Source;
import org.geonetwork.domain.repository.SourceRepository;
import org.junit.jupiter.api.Test;

class RssRedirectControllerTest {

    @Test
    void redirectToRssFeed_replacesMainPortalUuidPlaceholder() {
        var sourceRepository = mock(SourceRepository.class);
        var portal = Source.builder()
                .uuid("d1bd08f0-16ac-47c3-b581-2e8db715530b")
                .type("portal")
                .build();
        when(sourceRepository.findByType("portal")).thenReturn(Optional.of(portal));

        var controller =
                new RssRedirectController(sourceRepository, "ogcapi-records/collections/{{UUID}}/items?f=rss&limit=20");

        var redirect = controller.redirectToRssFeed();

        assertEquals(
                "redirect:ogcapi-records/collections/d1bd08f0-16ac-47c3-b581-2e8db715530b/items?f=rss&limit=20",
                redirect);
    }

    @Test
    void redirectToRssFeed_replacesLegacyMainCollectionPath() {
        var sourceRepository = mock(SourceRepository.class);
        var portal = Source.builder().uuid("portal-uuid").type("portal").build();
        when(sourceRepository.findByType("portal")).thenReturn(Optional.of(portal));

        var controller = new RssRedirectController(sourceRepository, "ogcapi-records/collections/main/items?f=rss");

        var redirect = controller.redirectToRssFeed();

        assertEquals("redirect:ogcapi-records/collections/portal-uuid/items?f=rss", redirect);
    }
}
