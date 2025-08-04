/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.geonetwork.domain.Metadata;
import org.geonetwork.metadata.IMetadataAccessManager;
import org.geonetwork.metadata.IMetadataManager;
import org.geonetwork.schemas.MetadataSchemaConfiguration;
import org.geonetwork.schemas.SchemaManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

class FormatterApiTest {

    private IMetadataManager metadataManager;
    private IMetadataAccessManager metadataAccessManager;
    private FormatterFactory formatterFactory;
    private SchemaManager schemaManager;
    private FormatterApi formatterApi;

    @BeforeEach
    void setUp() {
        metadataManager = mock(IMetadataManager.class);
        metadataAccessManager = mock(IMetadataAccessManager.class);
        formatterFactory = mock(FormatterFactory.class);
        schemaManager = mock(SchemaManager.class);

        formatterApi = new FormatterApi(metadataManager, metadataAccessManager, formatterFactory, schemaManager);
    }

    @Test
    void getRecordFormattersForMetadata_shouldReturnFormatters() throws Exception {
        // Given
        String uuid = "test-uuid";
        Metadata metadata = new Metadata();
        metadata.setId(123);
        metadata.setUuid(uuid);

        MetadataSchemaConfiguration.Formatter formatter = mock(MetadataSchemaConfiguration.Formatter.class);
        when(formatter.getName()).thenReturn("test-formatter");
        List<MetadataSchemaConfiguration.Formatter> formatters = Arrays.asList(formatter);

        when(metadataManager.findMetadataByUuid(uuid, true)).thenReturn(metadata);
        when(metadataAccessManager.canView(123)).thenReturn(true);
        when(formatterFactory.getAvailableFormatters(metadata)).thenReturn(formatters);

        // When
        List<MetadataSchemaConfiguration.Formatter> result = formatterApi.getRecordFormattersForMetadata(uuid);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test-formatter", result.get(0).getName());
    }

    @Test
    void getRecordFormattersForMetadata_shouldThrowAccessDenied() throws Exception {
        // Given
        String uuid = "test-uuid";
        Metadata metadata = new Metadata();
        metadata.setId(123);

        when(metadataManager.findMetadataByUuid(uuid, true)).thenReturn(metadata);
        when(metadataAccessManager.canView(123)).thenReturn(false);

        // When & Then
        assertThrows(AccessDeniedException.class, () -> formatterApi.getRecordFormattersForMetadata(uuid));
    }

    @Test
    void getAvailableFormattersForSchema_shouldReturnFormatters() {
        // Given
        String schemaId = "iso19139";
        MetadataSchemaConfiguration.Formatter formatter = mock(MetadataSchemaConfiguration.Formatter.class);
        List<MetadataSchemaConfiguration.Formatter> formatters = Arrays.asList(formatter);

        when(formatterFactory.getAvailableFormattersForSchema(schemaId)).thenReturn(formatters);

        // When
        List<MetadataSchemaConfiguration.Formatter> result = formatterApi.getAvailableFormattersForSchema(schemaId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
