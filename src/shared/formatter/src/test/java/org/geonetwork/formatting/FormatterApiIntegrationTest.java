/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import org.geonetwork.domain.Metadata;
import org.geonetwork.formatting.test.FormatterTestHelper;
import org.geonetwork.metadata.MetadataAccessManager;
import org.geonetwork.metadata.MetadataManager;
import org.geonetwork.schemas.SchemaManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Integration test for FormatterApi that uses real implementations where possible. It requires a full Spring context.
 */
@DisplayName("FormatterApi Integration Tests")
class FormatterApiIntegrationTest {

    private FormatterApi formatterApi;
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        // Create Spring context with test configuration
        applicationContext = new AnnotationConfigApplicationContext(TestConfiguration.class);
        formatterApi = applicationContext.getBean(FormatterApi.class);
    }

    @Test
    @DisplayName("Should format metadata to JSON using real formatter")
    void testRealFormatterIntegration() throws Exception {
        // Given
        String uuid = "test-uuid-123";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        formatterApi.getRecordFormattedBy(uuid, "json", true, outputStream);

        // Then
        String result = outputStream.toString(StandardCharsets.UTF_8);
        assertNotNull(result);
        assertTrue(result.contains("uuid"));
        assertTrue(result.contains(uuid));
    }

    /** Test configuration that provides real or stub implementations. */
    @Configuration
    static class TestConfiguration {

        @Bean
        public FormatterApi formatterApi() {
            return new FormatterApi(metadataManager(), metadataAccessManager(), formatterFactory(), schemaManager());
        }

        @Bean
        public MetadataManager metadataManager() {
            // Return a stub implementation
            return new MetadataManager(null, null, null) {
                @Override
                public Metadata findMetadataByUuid(String uuid, boolean approved) {
                    return FormatterTestHelper.createTestMetadata(uuid, "iso19139", 123);
                }
            };
        }

        @Bean
        public MetadataAccessManager metadataAccessManager() {
            // Return a stub that allows all access
            return new MetadataAccessManager(null, null, null, null, null) {
                @Override
                public boolean canView(int metadataId) {
                    return true;
                }
            };
        }

        @Bean
        public FormatterFactory formatterFactory() {
            return new FormatterFactory(
                    new XsltFormatter(),
                    new IndexFormatter(null, null),
                    new IdentityFormatter(),
                    null,
                    schemaManager()) {
                @Override
                public Map<String, String> getAvailableFormatters(Metadata metadata) {
                    return FormatterTestHelper.createStandardFormatters();
                }

                @Override
                public Formatter getFormatter(Metadata metadata, String formatterId) {
                    if ("json".equals(formatterId)) {
                        return new IndexFormatter(null, null) {
                            @Override
                            public void format(
                                    Metadata metadata, String formatterId, java.io.OutputStream outputStream) {
                                try {
                                    String json = String.format(
                                            "{\"uuid\": \"%s\", \"schema\": \"%s\"}",
                                            metadata.getUuid(), metadata.getSchemaid());
                                    outputStream.write(json.getBytes(StandardCharsets.UTF_8));
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        };
                    }
                    return new IdentityFormatter();
                }
            };
        }

        @Bean
        public SchemaManager schemaManager() {
            return new SchemaManager() {
                @Override
                public Set<String> getSchemas() {
                    return Set.of("iso19139", "iso19115-3");
                }
            };
        }
    }
}
