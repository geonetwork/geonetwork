/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.formatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.geonetwork.domain.Source;
import org.geonetwork.domain.repository.SourceRepository;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.metadata.MetadataManager;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsSingleRecordResponse;
import org.geonetwork.setting.SettingManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.ComparisonResult;

@ExtendWith(MockitoExtension.class)
class RssCollectionMessageWriterTest {

    @Mock
    SourceRepository sourceRepository;

    @Mock
    MetadataManager metadataManager;

    @Mock
    SettingManager settingManager;

    @InjectMocks
    RssCollectionMessageWriter writer;

    @Test
    void writesRssForItemsResponse() throws Exception {
        var contentType = MediaType.valueOf("application/rss+xml");
        assertTrue(writer.canWrite(OgcApiRecordsMultiRecordResponse.class, contentType));
        when(sourceRepository.findById("main"))
                .thenReturn(Optional.of(Source.builder()
                        .name("GeoNetwork OGC API Records - main")
                        .build()));
        when(metadataManager.getPermalinkUrl("record-1", "all"))
                .thenReturn("https://example.org/ogcapi-records/collections/main/items/record-1?f=geojson");

        var indexRecord = new IndexRecord();
        indexRecord.setUuid("record-1");
        indexRecord.setResourceTitle(Map.of("default", "Record <1>"));
        indexRecord.setResourceAbstract(Map.of("default", "A & B"));
        indexRecord.setChangeDate("2025-06-23T22:21:13.857456Z");

        var item = new OgcApiRecordsSingleRecordResponse(
                "main",
                "record-1",
                indexRecord,
                null,
                URI.create("https://example.org/ogcapi-records/collections/main/items/record-1?f=geojson"));

        var response = new OgcApiRecordsMultiRecordResponse();
        response.setCatalogId("main");
        response.setRecords(List.of(item));
        response.setJsonLink(URI.create("https://example.org/ogcapi-records/collections/main/items?f=json"));

        var output = new MockHttpOutputMessage();
        writer.write(response, contentType, output);

        assertEquals(contentType, output.getHeaders().getContentType());
        var body = output.getBodyAsString();
        var expected =
                """
                <rss xmlns:media="http://search.yahoo.com/mrss/" xmlns:atom="http://www.w3.org/2005/Atom" version="2.0">
                  <channel>
                    <title>GeoNetwork OGC API Records - main</title>
                    <link>https://example.org/ogcapi-records/collections/main/items?f=json</link>
                    <description>OGC API Records results for collection GeoNetwork OGC API Records - main</description>
                    <lastBuildDate>IGNORED</lastBuildDate>
                    <generator>GeoNetwork OGC API Records - main</generator>
                    <item>
                      <title>Record &lt;1&gt;</title>
                      <link>https://example.org/ogcapi-records/collections/main/items/record-1?f=geojson</link>
                      <guid isPermaLink="false">record-1</guid>
                      <description>A &amp; B</description>
                      <pubDate>IGNORED</pubDate>
                    </item>
                  </channel>
                </rss>
                """;

        var diff = DiffBuilder.compare(expected)
                .withTest(body)
                .ignoreComments()
                .ignoreWhitespace()
                .normalizeWhitespace()
                .checkForSimilar()
                .withDifferenceEvaluator((comparison, outcome) -> {
                    if (outcome == ComparisonResult.EQUAL) {
                        return outcome;
                    }
                    var xpath = comparison.getControlDetails().getXPath();
                    if ("/rss[1]/channel[1]/lastBuildDate[1]/text()[1]".equals(xpath)
                            || "/rss[1]/channel[1]/item[1]/pubDate[1]/text()[1]".equals(xpath)) {
                        return ComparisonResult.SIMILAR;
                    }
                    return outcome;
                })
                .build();

        assertFalse(diff.hasDifferences(), diff.toString());
    }
}
