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
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import org.geonetwork.domain.Metadata;
import org.geonetwork.utility.xml.XsltUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

@SuppressWarnings("unchecked") // added to make test case easier to read (and to build)
@DisplayName("XsltFormatter Unit Tests")
class XsltFormatterTest {

    private XsltFormatter xsltFormatter;
    private Metadata testMetadata;

    @TempDir
    Path tempDir;

    private static final String TEST_SCHEMA_ID = "iso19139";
    private static final String TEST_FORMATTER_ID = "html";
    private static final String TEST_XML_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<metadata>\n"
            + "  <title>Test Title</title>\n"
            + "  <abstract>Test Abstract</abstract>\n"
            + "</metadata>";

    private static final String EXPECTED_HTML_OUTPUT = "<html>\n" + "  <body>\n"
            + "    <h1>Test Title</h1>\n"
            + "    <p>Test Abstract</p>\n"
            + "  </body>\n"
            + "</html>";

    @BeforeEach
    void setUp() {
        xsltFormatter = new XsltFormatter();

        testMetadata = new Metadata();
        testMetadata.setId(123);
        testMetadata.setUuid("test-uuid");
        testMetadata.setSchemaid(TEST_SCHEMA_ID);
        testMetadata.setData(TEST_XML_DATA);
    }

    @Test
    @DisplayName("Should format metadata using XSLT transformation")
    void testFormat_Success() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Mock the static XsltUtil method
        try (MockedStatic<XsltUtil> xsltUtilMock = Mockito.mockStatic(XsltUtil.class)) {
            xsltUtilMock
                    .when(() -> XsltUtil.transformXmlAsOutputStream(
                            eq(TEST_XML_DATA), any(ClassPathResource.class), any(Map.class), eq(outputStream)))
                    .thenAnswer(invocation -> {
                        outputStream.write(EXPECTED_HTML_OUTPUT.getBytes(StandardCharsets.UTF_8));
                        return null;
                    });

            // When
            xsltFormatter.format(testMetadata, TEST_FORMATTER_ID, outputStream);

            // Then
            String result = outputStream.toString(StandardCharsets.UTF_8);
            assertEquals(EXPECTED_HTML_OUTPUT, result);

            // Verify the correct XSLT path was used
            xsltUtilMock.verify(
                    () -> XsltUtil.transformXmlAsOutputStream(
                            eq(TEST_XML_DATA),
                            argThat(resource -> {
                                ClassPathResource cpr = (ClassPathResource) resource;
                                return cpr.getPath().equals("schemas/iso19139/formatter/html/view.xsl");
                            }),
                            any(Map.class),
                            eq(outputStream)),
                    times(1));
        }
    }

    @Test
    @DisplayName("Should throw FormatterException when XSLT transformation fails")
    void testFormat_TransformationError() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (MockedStatic<XsltUtil> xsltUtilMock = Mockito.mockStatic(XsltUtil.class)) {
            xsltUtilMock
                    .when(() -> XsltUtil.transformXmlAsOutputStream(
                            any(String.class),
                            any(ClassPathResource.class),
                            any(Map.class),
                            any(ByteArrayOutputStream.class)))
                    .thenThrow(new RuntimeException("XSLT transformation failed"));

            // When & Then
            FormatterException exception = assertThrows(FormatterException.class, () -> {
                xsltFormatter.format(testMetadata, TEST_FORMATTER_ID, outputStream);
            });

            assertTrue(exception.getMessage().contains("Formatter html not found"));
            assertTrue(exception.getMessage().contains(TEST_SCHEMA_ID));
            assertNotNull(exception.getCause());
        }
    }

    @Test
    @DisplayName("Should check if formatter is not available when XSLT file does not exist")
    void testIsFormatterAvailable_FileDoesNotExist() {
        // Given
        String formatterId = "non-existing-formatter";

        // When
        boolean result = xsltFormatter.isFormatterAvailable(testMetadata, formatterId);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Should construct correct formatter XSLT path")
    void testFormatterXsltPath() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String customFormatterId = "custom-format";

        try (MockedStatic<XsltUtil> xsltUtilMock = Mockito.mockStatic(XsltUtil.class)) {
            xsltUtilMock
                    .when(() -> XsltUtil.transformXmlAsOutputStream(
                            any(String.class),
                            any(ClassPathResource.class),
                            any(Map.class),
                            any(ByteArrayOutputStream.class)))
                    .thenAnswer(invocation -> null);

            // When
            try {
                xsltFormatter.format(testMetadata, customFormatterId, outputStream);
            } catch (FormatterException e) {
                // Expected if file doesn't exist
            }

            // Then - Verify the path construction
            xsltUtilMock.verify(
                    () -> XsltUtil.transformXmlAsOutputStream(
                            eq(TEST_XML_DATA),
                            argThat(resource -> {
                                ClassPathResource cpr = (ClassPathResource) resource;
                                String expectedPath = String.format(
                                        "schemas/%s/formatter/%s/view.xsl", TEST_SCHEMA_ID, customFormatterId);
                                return cpr.getPath().equals(expectedPath);
                            }),
                            any(Map.class),
                            eq(outputStream)),
                    times(1));
        }
    }

    @Test
    @DisplayName("Should pass empty parameters map to XSLT transformation")
    void testFormat_EmptyParametersMap() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (MockedStatic<XsltUtil> xsltUtilMock = Mockito.mockStatic(XsltUtil.class)) {
            xsltUtilMock
                    .when(() -> XsltUtil.transformXmlAsOutputStream(
                            any(String.class),
                            any(ClassPathResource.class),
                            any(Map.class),
                            any(ByteArrayOutputStream.class)))
                    .thenAnswer(invocation -> {
                        // Verify empty map is passed
                        Map<?, ?> params = invocation.getArgument(2);
                        assertTrue(params.isEmpty());
                        return null;
                    });

            // When
            try {
                xsltFormatter.format(testMetadata, TEST_FORMATTER_ID, outputStream);
            } catch (FormatterException e) {
                // Expected if transformation fails
            }

            // Then - verification happens in the answer above
        }
    }
}
