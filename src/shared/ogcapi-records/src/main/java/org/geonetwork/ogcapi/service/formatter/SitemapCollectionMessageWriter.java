/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.formatter;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.index.model.record.IndexRecordFieldNames;
import org.geonetwork.metadata.IMetadataManager;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsSingleRecordResponse;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

/**
 * Writes `/collections/{catalogId}/items` as a sitemap. See https://www.sitemaps.org/protocol.html
 *
 * <p>Requested as application/sitemap+xml (f=sitemap) but returned as application/xml.
 */
@Component
@RequiredArgsConstructor
public class SitemapCollectionMessageWriter implements HttpMessageConverter<OgcApiRecordsMultiRecordResponse> {

    public static final String SITEMAP_MEDIA_TYPE_VALUE = "application/sitemap+xml";
    public static final MediaType SITEMAP_MEDIA_TYPE = MediaType.valueOf(SITEMAP_MEDIA_TYPE_VALUE);

    /** max number of urls in a sitemap file. */
    public static final int MAX_URLS = 50000;

    /** index fields used by the sitemap. */
    public static final Set<String> INDEX_FIELDS = Set.of(
            IndexRecordFieldNames.UUID,
            IndexRecordFieldNames.CHANGE_DATE,
            IndexRecordFieldNames.CREATE_DATE,
            "dateStamp");

    private static final String SITEMAP_NAMESPACE = "http://www.sitemaps.org/schemas/sitemap/0.9";
    private static final DateTimeFormatter W3C_DATETIME_UTC =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneOffset.UTC);

    private final IMetadataManager metadataManager;

    @Getter
    private final List<MediaType> supportedMediaTypes = List.of(SITEMAP_MEDIA_TYPE);

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        if (!OgcApiRecordsMultiRecordResponse.class.equals(clazz) || mediaType == null) {
            return false;
        }
        return SITEMAP_MEDIA_TYPE.isCompatibleWith(mediaType);
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
        outputMessage.getHeaders().setContentType(MediaType.APPLICATION_XML);

        try {
            var writer = XMLOutputFactory.newFactory().createXMLStreamWriter(outputMessage.getBody(), "UTF-8");
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("urlset");
            writer.writeDefaultNamespace(SITEMAP_NAMESPACE);

            var permalinkUrl = metadataManager.getPermalinkUrlBuilder("all");
            List<OgcApiRecordsSingleRecordResponse> records =
                    source.getRecords() == null ? List.of() : source.getRecords();
            for (var record : records) {
                if (record == null || record.getIndexRecord() == null) {
                    continue;
                }
                var loc = permalinkUrl.apply(record.getRecordId());
                // <loc> is mandatory in a sitemap entry
                if (StringUtils.isBlank(loc)) {
                    continue;
                }
                var indexRecord = record.getIndexRecord();
                writer.writeStartElement("url");
                writeSimpleElement(writer, "loc", loc);
                var lastMod = IndexRecordDates.firstParseable(
                        indexRecord.getChangeDate(), indexRecord.getDateStamp(), indexRecord.getCreateDate());
                if (lastMod != null) {
                    writeSimpleElement(
                            writer, "lastmod", W3C_DATETIME_UTC.format(lastMod.truncatedTo(ChronoUnit.SECONDS)));
                }
                writer.writeEndElement();
            }

            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new IOException("Error writing sitemap response", e);
        }
    }

    private static void writeSimpleElement(XMLStreamWriter writer, String elementName, String value) throws Exception {
        writer.writeStartElement(elementName);
        writer.writeCharacters(value);
        writer.writeEndElement();
    }
}
