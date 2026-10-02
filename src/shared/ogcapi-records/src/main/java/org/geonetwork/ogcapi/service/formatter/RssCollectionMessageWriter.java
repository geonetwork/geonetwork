/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.formatter;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.domain.Source;
import org.geonetwork.domain.repository.SourceRepository;
import org.geonetwork.index.model.record.Overview;
import org.geonetwork.metadata.MetadataManager;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsSingleRecordResponse;
import org.geonetwork.setting.SettingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

/** Writes `/collections/{catalogId}/items` as an RSS 2.0 feed. */
@Component
public class RssCollectionMessageWriter implements HttpMessageConverter<OgcApiRecordsMultiRecordResponse> {

    private static final MediaType RSS_MEDIA_TYPE = MediaType.valueOf("application/rss+xml");
    private static final DateTimeFormatter RFC_1123_UTC = DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC);

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private MetadataManager metadataManager;

    @Autowired
    private SettingManager settingManager;

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

            var channelLink =
                    source.getJsonLink() == null ? "" : source.getJsonLink().toString();

            Optional<Source> collection = sourceRepository.findById(source.getCatalogId());
            var channelName = collection.map(Source::getName).orElse("GeoNetwork RSS");

            writeSimpleElement(writer, "title", channelName);
            writeSimpleElement(writer, "link", channelLink);
            writeSimpleElement(writer, "description", "OGC API Records results for collection " + channelName);
            writeSimpleElement(writer, "lastBuildDate", RFC_1123_UTC.format(Instant.now()));
            writeSimpleElement(writer, "generator", channelName);
            // TODO: Use Logo API https://github.com/geonetwork/core-geonetwork/pull/9322
            if (collection.isPresent() && collection.get().getLogo() != null) {
                var logoUrl = settingManager.getBaseUrlWithContextPath() + "/images/harvesting/"
                        + collection.get().getLogo();
                writer.writeStartElement("image");
                writeSimpleElement(writer, "url", logoUrl);
                writeSimpleElement(writer, "title", channelName);
                writeSimpleElement(writer, "link", channelLink);
                writer.writeEndElement();

                writeSimpleElement(writer, "atom:logo", logoUrl);

                writeSimpleElement(writer, "atom:icon", logoUrl);
            }

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
                var itemUrl = metadataManager.getPermalinkUrl(record.getRecordId(), "all");
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
                if (overviewList != null && !overviewList.isEmpty()) {
                    writer.writeStartElement("enclosure");
                    writer.writeAttribute("url", overviewList.getFirst().getUrl());
                    writer.writeAttribute("type", "image/png");
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
        if (candidates == null) {
            return null;
        }
        for (var candidate : candidates) {
            var parsed = parseInstant(candidate);
            if (parsed != null) {
                return RFC_1123_UTC.format(parsed);
            }
        }
        return null;
    }

    private static Instant parseInstant(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Instant.parse(value);
        } catch (Exception e) {
            // Continue with other supported date shapes.
        }
        try {
            return OffsetDateTime.parse(value).toInstant();
        } catch (Exception e) {
            // Continue with other supported date shapes.
        }
        try {
            return ZonedDateTime.parse(value).toInstant();
        } catch (Exception e) {
            // Continue with other supported date shapes.
        }
        try {
            return LocalDateTime.parse(value).toInstant(ZoneOffset.UTC);
        } catch (Exception e) {
            // Continue with other supported date shapes.
        }
        try {
            return LocalDate.parse(value).atStartOfDay().toInstant(ZoneOffset.UTC);
        } catch (Exception e) {
            return null;
        }
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
