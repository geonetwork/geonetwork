/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.formatting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import org.geonetwork.domain.Metadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class XsltFormatterTest {

    private XsltFormatter xsltFormatter;

    @BeforeEach
    void setUp() {
        xsltFormatter = new XsltFormatter();
    }

    @Test
    void format_shouldThrowFormatterException_whenXsltFails() {
        // Given
        Metadata metadata = new Metadata();
        metadata.setSchemaid("iso19139");
        metadata.setData("<metadata>test</metadata>");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When & Then
        assertThrows(
                FormatterException.class, () -> xsltFormatter.format(metadata, "test-formatter", outputStream, null));
    }

    @Test
    void isFormatterAvailable_shouldReturnFalse_whenXsltDoesNotExist() {
        // Given
        Metadata metadata = new Metadata();
        metadata.setSchemaid("iso19139");

        // When
        boolean result = xsltFormatter.isFormatterAvailable(metadata, "non-existing");

        // Then
        assertFalse(result);
    }
}
