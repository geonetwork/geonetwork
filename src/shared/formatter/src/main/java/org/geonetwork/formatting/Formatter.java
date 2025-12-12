/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.formatting;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.geonetwork.domain.Metadata;

/**
 * Interface for formatting metadata records into various output formats.
 *
 * <p>You must implement this interface to create custom formatters like IndexFormatter, XsltFormatter, etc.
 *
 * <p>Implementations of this interface are responsible for transforming metadata records into specific output formats
 * (e.g., XML, JSON, HTML, PDF) based on the formatter identifier and configuration parameters.
 *
 * <p>Formatters should be stateless and thread-safe, as they may be used concurrently by multiple threads.
 */
public interface Formatter {

    /**
     * Formats a metadata record and writes the output to the provided stream.
     *
     * <p>The output stream is not closed by this method; the caller is responsible for properly managing the stream
     * lifecycle.
     *
     * @param metadata the metadata record to format
     * @param formatterId the identifier of the formatter to use
     * @param outputStream the stream to write the formatted output to
     * @param config additional configuration parameters for the formatter (may be null or empty).
     * @throws IOException if an I/O error occurs while writing to the output stream
     * @throws FormatterException if the formatting process fails for any reason, including invalid metadata,
     *     unsupported formatter or configuration errors
     */
    void format(Metadata metadata, String formatterId, OutputStream outputStream, Map<String, Object> config)
            throws IOException;

    /**
     * Checks whether a specific formatter is available for the given metadata.
     *
     * @param metadata the metadata record to check formatter availability for
     * @param formatterId the identifier of the formatter to check
     * @return {@code true} if the formatter is available and can be used to format the given metadata, {@code false}
     *     otherwise. Returns {@code false} if either parameter is null or if the formatter cannot be used.
     */
    boolean isFormatterAvailable(Metadata metadata, String formatterId);
}
