/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.configuration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.geonetwork.application.ctrlreturntypes.MimeAndProfilesForResponseType;
import org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfile;
import org.geonetwork.application.formatters.MessageWriterUtil;
import org.geonetwork.application.profile.ProfileDefaultsConfiguration;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiCollectionResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiCollectionResponseFormatter;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponseGeoJsonFormatter;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponseJsonFormatter;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.geonetwork.ogcapi.service.configuration.OgcApiLinkConfiguration;
import org.geonetwork.ogcapi.service.links.BasicLinks;
import org.geonetwork.ogcapi.service.links.CollectionPageLinks;
import org.geonetwork.ogcapi.service.links.ItemsPageLinks;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

/** Checks that pages link to their HTML version, which only the generic HTML writer produces. */
class HtmlLinksTest {

    @Test
    void collectionPageHasHtmlItemsLink() throws Exception {
        var links = links(new CollectionPageLinks());
        var page = new OgcApiRecordsCatalogDto();

        links.addLinks(request(MediaType.APPLICATION_JSON, OgcApiCollectionResponse.class), page, "c1");

        assertHasLink(page.getLinks(), "items", "text/html");
    }

    @Test
    void itemsPageRequestedAsHtmlHasHtmlSelfLink() throws Exception {
        var links = links(new ItemsPageLinks());
        var page = new OgcApiRecordsGetRecords200ResponseDto();

        links.addLinks(
                request(MediaType.TEXT_HTML, OgcApiRecordsMultiRecordResponse.class),
                page,
                "collections/c1/items",
                OgcApiRecordsMultiRecordResponse.class);

        assertHasLink(page.getLinks(), "self", "text/html");
        assertHasLink(page.getLinks(), "alternate", "application/json");
    }

    private RequestMediaTypeAndProfile request(MediaType mediaType, Class<?> responseClass) {
        return new RequestMediaTypeAndProfile(mediaType, null, null, responseClass);
    }

    private void assertHasLink(List<OgcApiRecordsLinkDto> links, String rel, String type) {
        assertTrue(
                links.stream().anyMatch(x -> rel.equals(x.getRel()) && type.equals(x.getType())),
                "missing rel=" + rel + " type=" + type + " in "
                        + links.stream()
                                .map(x -> x.getRel() + ":" + x.getType())
                                .toList());
    }

    private <T extends BasicLinks> T links(T links) throws Exception {
        var util = new MessageWriterUtil();
        util.setAllMessageConverters(List.<HttpMessageConverter<?>>of(
                new OgcApiRecordsHtmlMessageWriter(null, null, null, null),
                new OgcApiCollectionResponseFormatter(null, null, null),
                new OgcApiRecordsMultiRecordResponseJsonFormatter(),
                new OgcApiRecordsMultiRecordResponseGeoJsonFormatter()));

        var lookup = new MimeAndProfilesForResponseType();
        setField(lookup, "messageWriterUtil", util);
        setField(lookup, "profileDefaultsConfiguration", new ProfileDefaultsConfiguration());

        var linkConfiguration = new OgcApiLinkConfiguration();
        linkConfiguration.setOgcApiRecordsBaseUrl("http://localhost/ogcapi-records/");

        setField(links, "mimeAndProfilesForResponseType", lookup);
        setField(links, "linkConfiguration", linkConfiguration);
        return links;
    }

    private void setField(Object target, String name, Object value) throws Exception {
        for (Class<?> c = target.getClass(); c != null; c = c.getSuperclass()) {
            try {
                var field = c.getDeclaredField(name);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException ignored) {
                // look in the superclass
            }
        }
        throw new NoSuchFieldException(name);
    }
}
