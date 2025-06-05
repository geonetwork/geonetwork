/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import co.elastic.clients.json.JsonpUtils;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.geonetwork.domain.Metadata;
import org.geonetwork.formatting.processor.IndexFormatterProcessor;
import org.geonetwork.formatting.processor.IndexFormatterProcessorFactory;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.search.SearchController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@DisplayName("IndexFormatter Unit Tests")
class IndexFormatterTest {

    private IndexFormatter indexFormatter;

    @Mock
    private SearchController searchController;

    @Mock
    private IndexFormatterProcessorFactory indexFormatterProcessorFactory;

    @Mock
    private IndexFormatterProcessor indexFormatterProcessor;

    @Mock
    private IndexRecord indexRecord;

    private Metadata testMetadata;

    private static final String TEST_UUID = "test-uuid-123";
    private static final String TEST_FORMATTER_ID = "json";
    private static final String TEST_JSON_OUTPUT =
            "{\"uuid\":\"test-uuid-123\",\"title\":\"Test Title\",\"abstract\":\"Test Abstract\"}";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        indexFormatter = new IndexFormatter(searchController, indexFormatterProcessorFactory);

        testMetadata = new Metadata();
        testMetadata.setId(123);
        testMetadata.setUuid(TEST_UUID);
        testMetadata.setSchemaid("iso19139");
        testMetadata.setData("<metadata><title>Test Title</title></metadata>");
    }

    @Test
    @DisplayName("Should format metadata using custom processor when available")
    void testFormat_WithCustomProcessor() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(searchController.getIndexDocument(TEST_UUID)).thenReturn(indexRecord);
        when(indexFormatterProcessorFactory.getFormatterProcessor(TEST_FORMATTER_ID))
                .thenReturn(indexFormatterProcessor);

        doAnswer(invocation -> {
                    ByteArrayOutputStream os = invocation.getArgument(1);
                    os.write(TEST_JSON_OUTPUT.getBytes(StandardCharsets.UTF_8));
                    return null;
                })
                .when(indexFormatterProcessor)
                .process(eq(indexRecord), eq(outputStream));

        // When
        indexFormatter.format(testMetadata, TEST_FORMATTER_ID, outputStream);

        // Then
        String result = outputStream.toString(StandardCharsets.UTF_8);
        assertEquals(TEST_JSON_OUTPUT, result);

        verify(searchController).getIndexDocument(TEST_UUID);
        verify(indexFormatterProcessorFactory).getFormatterProcessor(TEST_FORMATTER_ID);
        verify(indexFormatterProcessor).process(indexRecord, outputStream);
    }

    @Test
    @DisplayName("Should format metadata using default JSON serialization when no processor")
    void testFormat_WithoutCustomProcessor() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String expectedJson = "{\"test\":\"data\"}";

        when(searchController.getIndexDocument(TEST_UUID)).thenReturn(indexRecord);
        when(indexFormatterProcessorFactory.getFormatterProcessor(TEST_FORMATTER_ID))
                .thenReturn(null);

        // Mock the static JsonpUtils method
        try (MockedStatic<JsonpUtils> jsonpUtilsMock = Mockito.mockStatic(JsonpUtils.class)) {
            jsonpUtilsMock
                    .when(() -> JsonpUtils.toJsonString(eq(indexRecord), any(JacksonJsonpMapper.class)))
                    .thenReturn(expectedJson);

            // When
            indexFormatter.format(testMetadata, TEST_FORMATTER_ID, outputStream);

            // Then
            String result = outputStream.toString(StandardCharsets.UTF_8);
            assertEquals(expectedJson, result);

            verify(searchController).getIndexDocument(TEST_UUID);
            verify(indexFormatterProcessorFactory).getFormatterProcessor(TEST_FORMATTER_ID);
            jsonpUtilsMock.verify(() -> JsonpUtils.toJsonString(eq(indexRecord), any(JacksonJsonpMapper.class)));
        }
    }

    @Test
    @DisplayName("Should throw FormatterException when search controller fails")
    void testFormat_SearchControllerError() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(searchController.getIndexDocument(TEST_UUID)).thenThrow(new RuntimeException("Index document not found"));

        // When & Then
        FormatterException exception = assertThrows(FormatterException.class, () -> {
            indexFormatter.format(testMetadata, TEST_FORMATTER_ID, outputStream);
        });

        assertTrue(exception.getMessage().contains("Error occur while formatting record"));
        assertTrue(exception.getMessage().contains(TEST_UUID));
        assertNotNull(exception.getCause());
    }

    @Test
    @DisplayName("Should throw FormatterException when processor fails")
    void testFormat_ProcessorError() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(searchController.getIndexDocument(TEST_UUID)).thenReturn(indexRecord);
        when(indexFormatterProcessorFactory.getFormatterProcessor(TEST_FORMATTER_ID))
                .thenReturn(indexFormatterProcessor);

        doThrow(new IOException("Processing failed"))
                .when(indexFormatterProcessor)
                .process(any(), any());

        // When & Then
        FormatterException exception = assertThrows(FormatterException.class, () -> {
            indexFormatter.format(testMetadata, TEST_FORMATTER_ID, outputStream);
        });

        assertTrue(exception.getMessage().contains("Error occur while formatting record"));
        assertNotNull(exception.getCause());
    }

    @Test
    @DisplayName("Should check formatter availability when processor exists")
    void testIsFormatterAvailable_ProcessorExists() {
        // Given
        when(indexFormatterProcessorFactory.getFormatterProcessor(TEST_FORMATTER_ID))
                .thenReturn(indexFormatterProcessor);

        // When
        boolean result = indexFormatter.isFormatterAvailable(testMetadata, TEST_FORMATTER_ID);

        // Then
        assertTrue(result);
        verify(indexFormatterProcessorFactory).getFormatterProcessor(TEST_FORMATTER_ID);
    }

    @Test
    @DisplayName("Should check formatter availability when processor does not exist")
    void testIsFormatterAvailable_ProcessorDoesNotExist() {
        // Given
        when(indexFormatterProcessorFactory.getFormatterProcessor("non-existent"))
                .thenReturn(null);

        // When
        boolean result = indexFormatter.isFormatterAvailable(testMetadata, "non-existent");

        // Then
        assertFalse(result);
        verify(indexFormatterProcessorFactory).getFormatterProcessor("non-existent");
    }

    @Test
    @DisplayName("Should handle different formatter IDs correctly")
    void testFormat_DifferentFormatterIds() throws Exception {
        // Given
        Map<String, IndexFormatterProcessor> processors = new HashMap<>();
        processors.put("json", mock(IndexFormatterProcessor.class));
        processors.put("xml", mock(IndexFormatterProcessor.class));
        processors.put("csv", mock(IndexFormatterProcessor.class));

        when(searchController.getIndexDocument(TEST_UUID)).thenReturn(indexRecord);

        for (Map.Entry<String, IndexFormatterProcessor> entry : processors.entrySet()) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            String formatterId = entry.getKey();
            IndexFormatterProcessor processor = entry.getValue();

            when(indexFormatterProcessorFactory.getFormatterProcessor(formatterId))
                    .thenReturn(processor);

            // When
            indexFormatter.format(testMetadata, formatterId, outputStream);

            // Then
            verify(processor).process(indexRecord, outputStream);
        }
    }

    @Test
    @DisplayName("Should handle null index document gracefully")
    void testFormat_NullIndexDocument() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(searchController.getIndexDocument(TEST_UUID)).thenReturn(null);
        when(indexFormatterProcessorFactory.getFormatterProcessor(TEST_FORMATTER_ID))
                .thenReturn(null);

        // Mock JsonpUtils to handle null
        try (MockedStatic<JsonpUtils> jsonpUtilsMock = Mockito.mockStatic(JsonpUtils.class)) {
            jsonpUtilsMock
                    .when(() -> JsonpUtils.toJsonString(isNull(), any(JacksonJsonpMapper.class)))
                    .thenReturn("null");

            // When
            indexFormatter.format(testMetadata, TEST_FORMATTER_ID, outputStream);

            // Then
            String result = outputStream.toString(StandardCharsets.UTF_8);
            assertEquals("null", result);
        }
    }
}
