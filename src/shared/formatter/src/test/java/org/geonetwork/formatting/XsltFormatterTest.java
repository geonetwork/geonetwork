/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
