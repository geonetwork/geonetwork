/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.formatting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.geonetwork.domain.Metadata;
import org.geonetwork.formatting.processor.IndexFormatterProcessor;
import org.geonetwork.formatting.processor.IndexFormatterProcessorFactory;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.search.SearchController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IndexFormatterTest {

    private SearchController searchController;
    private IndexFormatterProcessorFactory indexFormatterProcessorFactory;
    private IndexFormatter indexFormatter;

    @BeforeEach
    void setUp() {
        searchController = mock(SearchController.class);
        indexFormatterProcessorFactory = mock(IndexFormatterProcessorFactory.class);
        indexFormatter = new IndexFormatter(searchController, indexFormatterProcessorFactory);
    }

    @Test
    void format_shouldUseCustomProcessor() throws Exception {
        // Given
        Metadata metadata = new Metadata();
        metadata.setUuid("test-uuid");

        IndexRecord indexRecord = mock(IndexRecord.class);
        IndexFormatterProcessor processor = mock(IndexFormatterProcessor.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Map<String, Object> config = new HashMap<>();

        when(searchController.getIndexDocument("test-uuid")).thenReturn(indexRecord);
        when(indexFormatterProcessorFactory.getFormatterProcessor("custom-formatter"))
                .thenReturn(processor);

        // When
        indexFormatter.format(metadata, "custom-formatter", outputStream, config);

        // Then
        verify(processor).process(indexRecord, outputStream, config);
    }

    @Test
    void format_shouldThrowExceptionWhenError() throws Exception {
        // Given
        Metadata metadata = new Metadata();
        metadata.setUuid("test-uuid");

        when(searchController.getIndexDocument("test-uuid")).thenThrow(new RuntimeException("Search error"));

        // When & Then
        assertThrows(
                FormatterException.class,
                () -> indexFormatter.format(metadata, "formatter-id", new ByteArrayOutputStream(), null));
    }

    @Test
    void isFormatterAvailable_shouldReturnTrueWhenProcessorExists() {
        // Given
        IndexFormatterProcessor processor = mock(IndexFormatterProcessor.class);
        when(indexFormatterProcessorFactory.getFormatterProcessor("existing-formatter"))
                .thenReturn(processor);

        // When
        boolean result = indexFormatter.isFormatterAvailable(null, "existing-formatter");

        // Then
        assertTrue(result);
    }

    @Test
    void isFormatterAvailable_shouldReturnFalseWhenProcessorNotExists() {
        // Given
        when(indexFormatterProcessorFactory.getFormatterProcessor("non-existing"))
                .thenReturn(null);

        // When
        boolean result = indexFormatter.isFormatterAvailable(null, "non-existing");

        // Then
        assertFalse(result);
    }
}
