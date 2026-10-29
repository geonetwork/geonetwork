/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.formatter;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.domain.Source;
import org.geonetwork.domain.repository.SourceRepository;
import org.geonetwork.index.model.record.Overview;
import org.geonetwork.metadata.IMetadataManager;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsSingleRecordResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

/** Writes `/collections/{catalogId}/items` as an RSS 2.0 feed. */
@Component
@RequiredArgsConstructor
public class RssCollectionMessageWriter implements HttpMessageConverter<OgcApiRecordsMultiRecordResponse> {

    private static final MediaType RSS_MEDIA_TYPE = MediaType.valueOf("application/rss+xml");
    private static final DateTimeFormatter RFC_1123_UTC = DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC);

    private final SourceRepository sourceRepository;
    private final IMetadataManager metadataManager;

    @Value("${geonetwork.url}")
    String baseUrl;

    @Getter
    private final List<MediaType> supportedMediaTypes = List.of(RSS_MEDIA_TYPE);

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        if (!OgcApiRecordsMultiRecordResponse.class.equals(clazz) || mediaType == null) {
            return false;
        }
        return RSS_MEDIA_TYPE.isCompatibleWith(mediaType);
    }

    @Override
    public OgcApiRecordsMultiRecordResponse read(
            Class<? extends OgcApiRecordsMultiRecordResponse> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        throw new IOException("Not supported");
    }

    @Override
    public void write(OgcApiRecordsMultiRecordResponse source, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        outputMessage.getHeaders().setContentType(RSS_MEDIA_TYPE);

        try {
            var writer = XMLOutputFactory.newFactory().createXMLStreamWriter(outputMessage.getBody(), "UTF-8");
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("rss");
            writer.writeNamespace("media", "http://search.yahoo.com/mrss/");
            writer.writeNamespace("atom", "http://www.w3.org/2005/Atom");
            writer.writeAttribute("version", "2.0");
            writer.writeStartElement("channel");

            String channelLink =
                    source.getJsonLink() == null ? "" : source.getJsonLink().toString();
            String channelHTMLLink = channelLink.replace("f=json", "f=html");
            String channelRSSLink = channelLink.replace("f=json", "f=rss");

            Optional<Source> collection = sourceRepository.findById(source.getCatalogId());
            var channelName = collection.map(Source::getName).orElse("GeoNetwork RSS");

            writeSimpleElement(writer, "title", channelName);
            writeSimpleElement(writer, "link", channelHTMLLink);
            writeSimpleElement(writer, "description", "OGC API Records results for collection " + channelName);
            writeSimpleElement(writer, "lastBuildDate", RFC_1123_UTC.format(Instant.now()));
            writeSimpleElement(writer, "generator", "GeoNetwork OpenSource");

            if (StringUtils.isNotBlank(channelLink)) {
                writeSimpleElement(
                        writer,
                        "atom:link",
                        null,
                        Map.of(
                                "href", channelRSSLink,
                                "rel", "self",
                                "type", "application/rss+xml"));
            }

            // TODO: Use Logo API https://github.com/geonetwork/core-geonetwork/pull/9322
            if (collection.isPresent() && collection.get().getLogo() != null) {
                var logoUrl = baseUrl + "/images/harvesting/" + collection.get().getLogo();
                writer.writeStartElement("image");
                writeSimpleElement(writer, "url", logoUrl);
                writeSimpleElement(writer, "title", channelName);
                writeSimpleElement(writer, "link", channelLink);
                writer.writeEndElement();

                writeSimpleElement(writer, "atom:logo", logoUrl);

                writeSimpleElement(writer, "atom:icon", logoUrl);
            }

            var permalinkUrl = metadataManager.getPermalinkUrlBuilder("all");
            List<OgcApiRecordsSingleRecordResponse> records =
                    source.getRecords() == null ? List.of() : source.getRecords();
            for (var record : records) {
                if (record == null || record.getIndexRecord() == null) {
                    continue;
                }
                var indexRecord = record.getIndexRecord();
                writer.writeStartElement("item");
                writeSimpleElement(
                        writer, "title", getLocalizedValue(indexRecord.getResourceTitle(), record.getRecordId()));
                var itemUrl = permalinkUrl.apply(record.getRecordId());
                if (StringUtils.isNotBlank(itemUrl)) {
                    writeSimpleElement(writer, "link", itemUrl);
                }
                writeSimpleElement(writer, "guid", record.getRecordId(), Map.of("isPermaLink", "false"));
                writeSimpleElement(writer, "description", getLocalizedValue(indexRecord.getResourceAbstract(), ""));
                var pubDate = toRfc1123Date(
                        indexRecord.getChangeDate(), indexRecord.getCreateDate(), indexRecord.getDateStamp());
                if (pubDate != null) {
                    writeSimpleElement(writer, "pubDate", pubDate);
                }

                List<Overview> overviewList = indexRecord.getOverview();
                if (overviewList != null
                        && !overviewList.isEmpty()
                        && overviewList.getFirst().getUrl() != null) {
                    var overviewUrl = resolveAbsoluteUrl(overviewList.getFirst().getUrl());
                    var mimeType = inferImageMimeType(overviewUrl);

                    writer.writeStartElement("enclosure");
                    writer.writeAttribute("url", overviewUrl);
                    writer.writeAttribute("length", "0");
                    writer.writeAttribute("type", mimeType);
                    writer.writeEndElement();

                    writer.writeStartElement("media:content");
                    writer.writeAttribute("url", overviewUrl);
                    writer.writeAttribute("type", mimeType);
                    writer.writeAttribute("medium", "image");
                    writer.writeEndElement();
                }
                writer.writeEndElement();
            }

            writer.writeEndElement();
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new IOException("Error writing RSS response", e);
        }
    }

    private String resolveAbsoluteUrl(String url) {
        if (url == null || url.isBlank() || url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        return url.startsWith("/") ? baseUrl + url : baseUrl + "/" + url;
    }

    private static String inferImageMimeType(String url) {
        if (url == null) return "image/png";
        var lower = url.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".svg")) return "image/svg+xml";
        if (lower.endsWith(".webp")) return "image/webp";
        return "image/png";
    }

    private static String getLocalizedValue(Map<String, String> multilingual, String fallback) {
        if (multilingual == null || multilingual.isEmpty()) {
            return fallback;
        }
        var defaultValue = multilingual.get("default");
        if (defaultValue != null && !defaultValue.isBlank()) {
            return defaultValue;
        }
        return multilingual.values().stream()
                .filter(v -> v != null && !v.isBlank())
                .findFirst()
                .orElse(fallback);
    }

    private static String toRfc1123Date(String... candidates) {
        var parsed = IndexRecordDates.firstParseable(candidates);
        return parsed == null ? null : RFC_1123_UTC.format(parsed);
    }

    private static void writeSimpleElement(XMLStreamWriter writer, String elementName, String value) throws Exception {
        if (value == null || value.isBlank()) {
            return;
        }
        writer.writeStartElement(elementName);
        writer.writeCharacters(value);
        writer.writeEndElement();
    }

    private static void writeSimpleElement(
            XMLStreamWriter writer, String elementName, String value, Map<String, String> attributes) throws Exception {
        if (StringUtils.isBlank(value) && attributes.isEmpty()) {
            return;
        }
        writer.writeStartElement(elementName);
        for (var attribute : attributes.entrySet()) {
            writer.writeAttribute(attribute.getKey(), attribute.getValue());
        }
        if (StringUtils.isNotBlank(value)) {
            writer.writeCharacters(value);
        }
        writer.writeEndElement();
    }
}
