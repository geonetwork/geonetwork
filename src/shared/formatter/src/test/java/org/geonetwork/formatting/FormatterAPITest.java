/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.geonetwork.domain.Metadata;
import org.geonetwork.metadata.IMetadataAccessManager;
import org.geonetwork.metadata.IMetadataManager;
import org.geonetwork.schemas.SchemaManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

@DisplayName("FormatterApi Unit Tests")
class FormatterApiTest {

    // System under test
    private FormatterApi formatterApi;

    // Mocked dependencies
    @Mock
    private IMetadataManager metadataManager;

    @Mock
    private IMetadataAccessManager metadataAccessManager;

    @Mock
    private FormatterFactory formatterFactory;

    @Mock
    private SchemaManager schemaManager;

    @Mock
    private Formatter formatter;

    //    @Mock
    //    private MetadataSchema metadataSchema;

    // Test data
    private static final String TEST_UUID = "test-uuid-123";
    private static final String TEST_FORMATTER_ID = "json";
    private static final String TEST_SCHEMA_ID = "iso19139";
    private static final int TEST_METADATA_ID = 123;
    private static final String TEST_XML_DATA = "<metadata><title>Test</title></metadata>";

    private Metadata testMetadata;
    private Map<String, String> availableFormatters;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create test metadata
        testMetadata = new Metadata();
        testMetadata.setId(TEST_METADATA_ID);
        testMetadata.setUuid(TEST_UUID);
        testMetadata.setSchemaid(TEST_SCHEMA_ID);
        testMetadata.setData(TEST_XML_DATA);

        // Set up available formatters
        availableFormatters = new HashMap<>();
        availableFormatters.put("json", "application/json");
        availableFormatters.put("xml", "application/xml");
        availableFormatters.put("html", "text/html");

        // Initialize FormatterApi with dependencies
        formatterApi = new FormatterApi(metadataManager, metadataAccessManager, formatterFactory, schemaManager);
    }

    @Test
    @DisplayName("Should get all formatters successfully")
    void testGetAllFormatters() throws Exception {
        // Given
        when(schemaManager.getSchemas()).thenReturn(Set.of("iso19139", "dublin-core"));

        Map<String, String> iso19139Formatters = new HashMap<>();
        iso19139Formatters.put("json", "application/json");
        iso19139Formatters.put("xml", "application/xml");

        Map<String, String> dublinCoreFormatters = new HashMap<>();
        dublinCoreFormatters.put("json", "application/json");
        dublinCoreFormatters.put("dc-xml", "application/xml");

        when(formatterFactory.getAvailableFormattersForSchema("iso19139")).thenReturn(iso19139Formatters);
        when(formatterFactory.getAvailableFormattersForSchema("dublin-core")).thenReturn(dublinCoreFormatters);

        // When
        Map<String, FormatterInfo> result = formatterApi.getAllFormatters();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());

        FormatterInfo jsonInfo = result.get("json");
        assertNotNull(jsonInfo);
        assertEquals("application/json", jsonInfo.getMimeType());
        assertTrue(jsonInfo.getSchemas().contains("iso19139"));
        assertTrue(jsonInfo.getSchemas().contains("dublin-core"));

        FormatterInfo xmlInfo = result.get("xml");
        assertNotNull(xmlInfo);
        assertEquals("application/xml", xmlInfo.getMimeType());
        assertTrue(xmlInfo.getSchemas().contains("iso19139"));

        FormatterInfo dcXmlInfo = result.get("dc-xml");
        assertNotNull(dcXmlInfo);
        assertEquals("application/xml", dcXmlInfo.getMimeType());
        assertTrue(dcXmlInfo.getSchemas().contains("dublin-core"));
    }

    @Test
    @DisplayName("Should get record formatters for metadata when user has access")
    void testGetRecordFormattersForMetadata_WithAccess() throws Exception {
        // Given
        when(metadataManager.findMetadataByUuid(TEST_UUID, true)).thenReturn(testMetadata);
        when(metadataAccessManager.canView(TEST_METADATA_ID)).thenReturn(true);
        when(formatterFactory.getAvailableFormatters(testMetadata)).thenReturn(availableFormatters);

        // When
        Map<String, String> result = formatterApi.getRecordFormattersForMetadata(TEST_UUID);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("application/json", result.get("json"));
        assertEquals("application/xml", result.get("xml"));
        assertEquals("text/html", result.get("html"));
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when user cannot view metadata")
    void testGetRecordFormattersForMetadata_AccessDenied() throws Exception {
        // Given
        when(metadataManager.findMetadataByUuid(TEST_UUID, true)).thenReturn(testMetadata);
        when(metadataAccessManager.canView(TEST_METADATA_ID)).thenReturn(false);

        // When & Then
        assertThrows(AccessDeniedException.class, () -> {
            formatterApi.getRecordFormattersForMetadata(TEST_UUID);
        });

        verify(formatterFactory, never()).getAvailableFormatters(any());
    }

    @Test
    @DisplayName("Should get available formatters for schema")
    void testGetAvailableFormattersForSchema() {
        // Given
        when(formatterFactory.getAvailableFormattersForSchema(TEST_SCHEMA_ID)).thenReturn(availableFormatters);

        // When
        Map<String, String> result = formatterApi.getAvailableFormattersForSchema(TEST_SCHEMA_ID);

        // Then
        assertNotNull(result);
        assertEquals(availableFormatters, result);
    }

    @Test
    @DisplayName("Should format record successfully when user has access")
    void testGetRecordFormattedBy_Success() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String expectedOutput = "{\"title\": \"Test\"}";

        when(metadataManager.findMetadataByUuid(TEST_UUID, true)).thenReturn(testMetadata);
        when(metadataAccessManager.canView(TEST_METADATA_ID)).thenReturn(true);
        when(formatterFactory.getAvailableFormatters(testMetadata)).thenReturn(availableFormatters);
        when(formatterFactory.getFormatter(testMetadata, TEST_FORMATTER_ID)).thenReturn(formatter);

        doAnswer(invocation -> {
                    OutputStream os = invocation.getArgument(2);
                    os.write(expectedOutput.getBytes(StandardCharsets.UTF_8));
                    return null;
                })
                .when(formatter)
                .format(eq(testMetadata), eq(TEST_FORMATTER_ID), any(OutputStream.class));

        // When
        formatterApi.getRecordFormattedBy(TEST_UUID, TEST_FORMATTER_ID, true, outputStream);

        // Then
        String result = outputStream.toString(StandardCharsets.UTF_8);
        assertEquals(expectedOutput, result);

        verify(formatter).format(eq(testMetadata), eq(TEST_FORMATTER_ID), eq(outputStream));
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when user cannot view metadata for formatting")
    void testGetRecordFormattedBy_AccessDenied() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(metadataManager.findMetadataByUuid(TEST_UUID, true)).thenReturn(testMetadata);
        when(metadataAccessManager.canView(TEST_METADATA_ID)).thenReturn(false);

        // When & Then
        assertThrows(AccessDeniedException.class, () -> {
            formatterApi.getRecordFormattedBy(TEST_UUID, TEST_FORMATTER_ID, true, outputStream);
        });

        verify(formatter, never()).format(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw FormatterException when formatter not found")
    void testGetRecordFormattedBy_FormatterNotFound() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Map<String, String> limitedFormatters = new HashMap<>();
        limitedFormatters.put("xml", "application/xml");

        when(metadataManager.findMetadataByUuid(TEST_UUID, true)).thenReturn(testMetadata);
        when(metadataAccessManager.canView(TEST_METADATA_ID)).thenReturn(true);
        when(formatterFactory.getAvailableFormatters(testMetadata)).thenReturn(limitedFormatters);

        // When & Then
        FormatterException exception = assertThrows(FormatterException.class, () -> {
            formatterApi.getRecordFormattedBy(TEST_UUID, "non-existent", true, outputStream);
        });

        assertTrue(exception.getMessage().contains("Formatter not found"));
        assertTrue(exception.getMessage().contains(TEST_SCHEMA_ID));
        verify(formatter, never()).format(any(), any(), any());
    }

    @Test
    @DisplayName("Should use approved parameter correctly")
    void testGetRecordFormattedBy_ApprovedParameter() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        boolean approved = false;

        when(metadataManager.findMetadataByUuid(TEST_UUID, approved)).thenReturn(testMetadata);
        when(metadataAccessManager.canView(TEST_METADATA_ID)).thenReturn(true);
        when(formatterFactory.getAvailableFormatters(testMetadata)).thenReturn(availableFormatters);
        when(formatterFactory.getFormatter(testMetadata, TEST_FORMATTER_ID)).thenReturn(formatter);

        // When
        formatterApi.getRecordFormattedBy(TEST_UUID, TEST_FORMATTER_ID, approved, outputStream);

        // Then
        verify(metadataManager).findMetadataByUuid(TEST_UUID, approved);
    }

    @Test
    @DisplayName("Should handle formatter exception during formatting")
    void testGetRecordFormattedBy_FormatterThrowsException() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(metadataManager.findMetadataByUuid(TEST_UUID, true)).thenReturn(testMetadata);
        when(metadataAccessManager.canView(TEST_METADATA_ID)).thenReturn(true);
        when(formatterFactory.getAvailableFormatters(testMetadata)).thenReturn(availableFormatters);
        when(formatterFactory.getFormatter(testMetadata, TEST_FORMATTER_ID)).thenReturn(formatter);

        doThrow(new RuntimeException("Formatting error")).when(formatter).format(any(), any(), any());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            formatterApi.getRecordFormattedBy(TEST_UUID, TEST_FORMATTER_ID, true, outputStream);
        });
    }

    @Test
    @DisplayName("Should handle inconsistent formatter mime types")
    void testGetAllFormatters_InconsistentMimeTypes() throws Exception {
        // Given
        when(schemaManager.getSchemas()).thenReturn(Set.of("schema1", "schema2"));

        Map<String, String> schema1Formatters = new HashMap<>();
        schema1Formatters.put("json", "application/json");

        Map<String, String> schema2Formatters = new HashMap<>();
        schema2Formatters.put("json", "text/json"); // Different mime type for same formatter

        when(formatterFactory.getAvailableFormattersForSchema("schema1")).thenReturn(schema1Formatters);
        when(formatterFactory.getAvailableFormattersForSchema("schema2")).thenReturn(schema2Formatters);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            formatterApi.getAllFormatters();
        });

        assertTrue(exception.getMessage().contains("inconsistent formatter mime type"));
    }
}
