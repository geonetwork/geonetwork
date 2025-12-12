/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.formatter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.SneakyThrows;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

/**
 * This writes the `/items` response out as a CSW-xml output.
 *
 * <p>This is for compatibility with existing CSW-parsers.
 *
 * <p>see #write(...) for details.
 */
@Component
public class CswCollectionMessageWriter implements HttpMessageConverter<OgcApiRecordsGetRecords200ResponseDto> {

    final MetadataRepository metadataRepository;

    /**
     * what media types do we support (we only support application/xml)
     *
     * @return what media types do we support
     */
    @Getter
    private final List<MediaType> supportedMediaTypes;

    @SneakyThrows
    public CswCollectionMessageWriter(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
        supportedMediaTypes = List.of(MediaType.valueOf("application/xml"));
    }

    /** we do not support read - always false */
    @Override
    public boolean canRead(@NotNull Class<?> clazz, MediaType mediaType) {
        return false;
    }

    /**
     * Can write if the class is OgcApiRecordsGetRecords200ResponseDto and we support the media type
     *
     * @param clazz What object are we outputting
     * @param mediaType what mime type do we want
     * @return if we can write this object with this media type
     */
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        if (!clazz.equals(OgcApiRecordsGetRecords200ResponseDto.class)) {
            return false;
        }
        return supportedMediaTypes.contains(mediaType);
    }

    @Override
    public OgcApiRecordsGetRecords200ResponseDto read(
            Class<? extends OgcApiRecordsGetRecords200ResponseDto> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        throw new IOException("Not supported");
    }

    /**
     * This directly writes the CSW output XML format.
     *
     * <p>&lt;csw:GetRecordsResponse xmlns:csw="http://www.opengis.net/cat/csw/2.0.2"
     * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.0.2"
     * xsi:schemaLocation="http://www.opengis.net/cat/csw/2.0.2
     * http://schemas.opengis.net/csw/2.0.2/CSW-discovery.xsd"&gt; <br>
     * &lt;csw:SearchStatus timestamp="2025-06-23T22:21:13.857456Z"/&gt; <br>
     * &lt;csw:SearchResults numberOfRecordsMatched="10" numberOfRecordsReturned="5" nextRecord="6"
     * recordSchema="http://www.isotc211.org/2005/gmd" elementSet="full"&gt; <br>
     * ... xml records from DB (table `metadata`) ...<br>
     * &lt;/csw:SearchResults&gt;<br>
     * &lt;/csw:GetRecords&gt;<br>
     *
     * <p>NOTE: nextRecord is a 1-based index (not a 0-based index).
     *
     * @param ogcApiRecordsGetRecords200ResponseDto the object to write to the output message. The type of this object
     *     must have previously been passed to the {@link #canWrite canWrite} method of this interface, which must have
     *     returned {@code true}.
     * @param contentType the content type to use when writing. May be {@code null} to indicate that the default content
     *     type of the converter must be used. If not {@code null}, this media type must have previously been passed to
     *     the {@link #canWrite canWrite} method of this interface, which must have returned {@code true}.
     * @param outputMessage the message to write to
     * @throws IOException an error occurred
     * @throws HttpMessageNotWritableException spring-boot or network issue
     */
    @Override
    public void write(
            OgcApiRecordsGetRecords200ResponseDto ogcApiRecordsGetRecords200ResponseDto,
            MediaType contentType,
            HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        if (ogcApiRecordsGetRecords200ResponseDto == null) {
            throw new IOException("No output");
        }

        var officialProfileName = "http://geonetwork.net/def/profile/csw";
        outputMessage.getHeaders().set("Link", "<" + officialProfileName + ">; rel=\"profile\"");
        outputMessage.getHeaders().setContentType(contentType);

        outputMessage.getBody().write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes(StandardCharsets.UTF_8));
        outputMessage
                .getBody()
                .write(
                        "<csw:GetRecordsResponse xmlns:csw=\"http://www.opengis.net/cat/csw/2.0.2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"2.0.2\" xsi:schemaLocation=\"http://www.opengis.net/cat/csw/2.0.2 http://schemas.opengis.net/csw/2.0.2/CSW-discovery.xsd\">\n"
                                .getBytes(StandardCharsets.UTF_8));
        outputMessage
                .getBody()
                .write(("  <csw:SearchStatus timestamp=\"" + Instant.now() + "\"/>\n")
                        .getBytes(StandardCharsets.UTF_8));

        // next record = 0 if none remaining
        // NOTE: next record is 1-based index (not normal 0-based)
        var nextRecord = 0;
        var header_offset = outputMessage.getHeaders().get("GN5.OGCAPI-RECORDS.REQUEST-OFFSET");
        if (header_offset != null && !header_offset.isEmpty()) {
            try {
                nextRecord = Integer.parseInt(header_offset.getFirst())
                        + ogcApiRecordsGetRecords200ResponseDto.getNumberReturned();
                if (nextRecord < ogcApiRecordsGetRecords200ResponseDto.getNumberMatched()) {
                    nextRecord = nextRecord + 1;
                } else {
                    nextRecord = 0;
                }
            } catch (NumberFormatException e) {
                nextRecord = 0;
            }
        }

        var searchResults = "  <csw:SearchResults numberOfRecordsMatched=\""
                + ogcApiRecordsGetRecords200ResponseDto.getNumberMatched()
                + "\" numberOfRecordsReturned=\"" + ogcApiRecordsGetRecords200ResponseDto.getNumberReturned()
                + "\" nextRecord=\"" + nextRecord
                + "\" recordSchema=\"http://www.isotc211.org/2005/gmd\" elementSet=\"full\">\n";
        outputMessage.getBody().write(searchResults.getBytes(StandardCharsets.UTF_8));

        for (var feature : ogcApiRecordsGetRecords200ResponseDto.getFeatures()) {
            var featureId = feature.getId();
            var metadata = metadataRepository.findByUuid(featureId);
            if (metadata.isEmpty()) {
                continue;
            }
            var xml = metadata.get().getData();
            outputMessage.getBody().write(xml.getBytes(StandardCharsets.UTF_8));
        }

        outputMessage.getBody().write("\n  </csw:SearchResults>\n".getBytes(StandardCharsets.UTF_8));
        outputMessage.getBody().write("</csw:GetRecordsResponse>".getBytes(StandardCharsets.UTF_8));
    }
}
