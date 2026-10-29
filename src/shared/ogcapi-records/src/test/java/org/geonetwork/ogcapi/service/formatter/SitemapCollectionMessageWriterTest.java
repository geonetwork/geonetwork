/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.formatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;
import java.util.Map;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.metadata.MetadataManager;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsSingleRecordResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.xmlunit.builder.DiffBuilder;

@ExtendWith(MockitoExtension.class)
class SitemapCollectionMessageWriterTest {

    @Mock
    MetadataManager metadataManager;

    @InjectMocks
    SitemapCollectionMessageWriter writer;

    @Test
    void writesSitemapForItemsResponse() throws Exception {
        var contentType = MediaType.valueOf("application/sitemap+xml");
        assertTrue(writer.canWrite(OgcApiRecordsMultiRecordResponse.class, contentType));
        assertFalse(writer.canWrite(OgcApiRecordsMultiRecordResponse.class, MediaType.APPLICATION_XML));
        when(metadataManager.getPermalinkUrlBuilder("all"))
                .thenReturn(Map.of(
                        "record-1", "https://example.org/srv/api/records/record-1?language=all&a=b",
                        "record-2", "https://example.org/srv/api/records/record-2?language=all")::get);

        var record1 = new IndexRecord();
        record1.setUuid("record-1");
        record1.setChangeDate("2025-06-23T22:21:13.857456Z");
        var record2 = new IndexRecord();
        record2.setUuid("record-2");

        var response = new OgcApiRecordsMultiRecordResponse();
        response.setCatalogId("main");
        response.setRecords(List.of(
                new OgcApiRecordsSingleRecordResponse("main", "record-1", record1, null, URI.create("https://x")),
                new OgcApiRecordsSingleRecordResponse("main", "record-2", record2, null, URI.create("https://x"))));

        var output = new MockHttpOutputMessage();
        writer.write(response, contentType, output);

        assertEquals(MediaType.APPLICATION_XML, output.getHeaders().getContentType());
        var expected =
                """
                <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                  <url>
                    <loc>https://example.org/srv/api/records/record-1?language=all&amp;a=b</loc>
                    <lastmod>2025-06-23T22:21:13Z</lastmod>
                  </url>
                  <url>
                    <loc>https://example.org/srv/api/records/record-2?language=all</loc>
                  </url>
                </urlset>
                """;

        var diff = DiffBuilder.compare(expected)
                .withTest(output.getBodyAsString())
                .ignoreWhitespace()
                .checkForSimilar()
                .build();

        assertFalse(diff.hasDifferences(), diff.toString());
        // the URL template is resolved once per response, not once per record
        verify(metadataManager, times(1)).getPermalinkUrlBuilder("all");
    }
}
