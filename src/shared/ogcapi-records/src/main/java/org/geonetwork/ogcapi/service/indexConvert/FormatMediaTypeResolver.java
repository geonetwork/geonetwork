/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.indexConvert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.ogcapi.service.configuration.FormatMediaTypeEntry;
import org.geonetwork.ogcapi.service.configuration.OgcApiRecordsOutputConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Maps a metadata format name (e.g. "SHP", "GeoTIFF 1.1", "TIFF (.tif, .tiff)") to a media type.
 *
 * <p>Uses the built-in vocabulary ({@value #VOCABULARY_RESOURCE}) plus the configured {@code formatMediaTypes}, which
 * win over it. Matching is case-insensitive and tries: the full name, the name without the part in brackets, the name
 * without a trailing version, then the extensions in brackets. A name that is already a media type is returned as is.
 */
@Component
@Slf4j
public class FormatMediaTypeResolver {

    static final String VOCABULARY_RESOURCE = "ogcapi/format-media-types.json";

    private static final Pattern PARENTHESES = Pattern.compile("\\([^)]*\\)");
    private static final Pattern TRAILING_VERSION = Pattern.compile("\\s+(v|version\\s*)?\\d+(\\.\\d+)*$");
    private static final Pattern MEDIA_TYPE =
            Pattern.compile("^[a-z0-9][a-z0-9!#$&^_.+-]*/[a-z0-9][a-z0-9!#$&^_.+-]*(;.*)?$");

    /** normalized alias -> media type */
    private final Map<String, String> aliasToMediaType = new HashMap<>();

    @Autowired
    public FormatMediaTypeResolver(OgcApiRecordsOutputConfig config) {
        this(config.getFormatMediaTypes());
    }

    /** @param configuredEntries extra entries, they override the built-in ones (can be null) */
    public FormatMediaTypeResolver(List<FormatMediaTypeEntry> configuredEntries) {
        register(loadBuiltInVocabulary());
        if (configuredEntries != null) {
            register(configuredEntries); // last, so they override
        }
    }

    /**
     * @param formatName format name from the metadata
     * @return media type, or null if unknown
     */
    public String resolve(String formatName) {
        if (formatName == null || formatName.isBlank()) {
            return null;
        }
        for (var candidate : candidates(formatName)) {
            var mediaType = aliasToMediaType.get(candidate);
            if (mediaType != null) {
                return mediaType;
            }
        }
        var normalized = normalize(formatName);
        return MEDIA_TYPE.matcher(normalized).matches() ? formatName.trim() : null;
    }

    /** names to look up, in order */
    static Set<String> candidates(String formatName) {
        var result = new LinkedHashSet<String>();
        var full = normalize(formatName);
        result.add(full);

        var withoutParentheses = normalize(PARENTHESES.matcher(full).replaceAll(" "));
        result.add(withoutParentheses);
        result.add(TRAILING_VERSION.matcher(withoutParentheses).replaceFirst(""));

        var matcher = PARENTHESES.matcher(full);
        while (matcher.find()) {
            var content = matcher.group();
            for (var token : content.substring(1, content.length() - 1).split("[,;/\\s]+")) {
                result.add(stripLeadingDot(token));
            }
        }
        result.add(stripLeadingDot(full));
        result.remove("");
        return result;
    }

    private void register(List<FormatMediaTypeEntry> entries) {
        for (var entry : entries) {
            if (entry == null
                    || entry.getMediaType() == null
                    || entry.getMediaType().isBlank()) {
                log.warn("Ignoring format vocabulary entry without a media type");
                continue;
            }
            var mediaType = entry.getMediaType().trim();
            // also match the media type itself
            aliasToMediaType.put(normalize(mediaType), mediaType);
            if (entry.getAliases() != null) {
                for (var alias : entry.getAliases()) {
                    if (alias != null && !alias.isBlank()) {
                        aliasToMediaType.put(normalize(alias), mediaType);
                    }
                }
            }
        }
    }

    private static List<FormatMediaTypeEntry> loadBuiltInVocabulary() {
        try (InputStream in = new ClassPathResource(VOCABULARY_RESOURCE).getInputStream()) {
            return new ObjectMapper().readValue(in, new TypeReference<List<FormatMediaTypeEntry>>() {});
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read the format vocabulary " + VOCABULARY_RESOURCE, e);
        }
    }

    /** lower case, single spaces, no spaces around ";" and "=" */
    static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ").replaceAll("\\s*([;=])\\s*", "$1");
    }

    private static String stripLeadingDot(String value) {
        var trimmed = value.trim();
        return trimmed.startsWith(".") ? trimmed.substring(1) : trimmed;
    }
}
