/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.schemas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Stream;
import org.geonetwork.setting.SettingManager;
import org.geonetwork.utility.ApplicationContextProvider;
import org.geonetwork.utility.schemas.CodeListTranslator;
import org.geonetwork.utility.xml.XsltUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

class FormatterTransformationTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @BeforeAll
    static void setUpApplicationContextForXsltExtensions() {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.registerBean(CodeListTranslator.class, CodeListTranslator::new);
        applicationContext.registerBean(SettingManager.class, () -> new SettingManager(null) {
            @Override
            public String getServerURL() {
                return "http://localhost:8080/srv";
            }

            @Override
            public String getValue(String name) {
                if ("nodeUrl".equals(name)) {
                    return "http://localhost:8080/srv";
                }
                if ("system/site/name".equals(name)) {
                    return "My GeoNetwork catalogue";
                }
                return null;
            }
        });
        applicationContext.refresh();
        new ApplicationContextProvider().setApplicationContext(applicationContext);
    }

    @ParameterizedTest
    @MethodSource("formatterCases")
    void formatter_shouldMatchExpectedOutput(
            String schemaId, String formatterId, String inputResource, String expectedResource) throws Exception {
        String input = loadResourceAsString(inputResource);
        String expected = loadResourceAsString(expectedResource);

        String output = XsltUtil.transformXmlAsString(
                input,
                new ClassPathResource(String.format("schemas/%s/formatter/%s/view.xsl", schemaId, formatterId))
                        .getURL(),
                Map.of());

        if ("schema.org".equals(formatterId)) {
            JsonNode expectedJson = OBJECT_MAPPER.readTree(expected);
            JsonNode outputJson = OBJECT_MAPPER.readTree(output);
            assertEquals(expectedJson, outputJson);
            return;
        }

        Diff diff = DiffBuilder.compare(expected)
                .withTest(output)
                .ignoreComments()
                .ignoreWhitespace()
                .normalizeWhitespace()
                .checkForSimilar()
                .build();

        assertFalse(diff.hasDifferences(), diff.toString());
    }

    private static Stream<Arguments> formatterCases() {
        return Stream.of(
                Arguments.of(
                        "iso19115-3.2018",
                        "datacite",
                        "iso19115-3.2018-datacite.xml",
                        "iso19115-3.2018-datacite-out.xml"),
                Arguments.of(
                        "iso19115-3.2018",
                        "schema.org",
                        "iso19115-3.2018-schemaorg.xml",
                        "iso19115-3.2018-schemaorg.json"),
                Arguments.of(
                        "iso19115-3.2018",
                        "dcat",
                        "iso19115-3.2018-dcat-dataset.xml",
                        "iso19115-3.2018-dcat-dataset-core.rdf"),
                Arguments.of(
                        "iso19115-3.2018",
                        "eu-dcat-ap",
                        "iso19115-3.2018-dcat-dataset.xml",
                        "iso19115-3.2018-eu-dcat-ap-dataset-core.rdf"),
                Arguments.of(
                        "iso19115-3.2018",
                        "eu-dcat-ap-hvd",
                        "iso19115-3.2018-dcat-dataset.xml",
                        "iso19115-3.2018-eu-dcat-ap-hvd-dataset-core.rdf"),
                Arguments.of(
                        "iso19115-3.2018",
                        "eu-dcat-ap-mobility",
                        "iso19115-3.2018-dcat-dataset.xml",
                        "iso19115-3.2018-eu-dcat-ap-mobility-dataset-core.rdf"),
                Arguments.of(
                        "iso19115-3.2018",
                        "eu-geodcat-ap",
                        "iso19115-3.2018-dcat-dataset.xml",
                        "iso19115-3.2018-eu-geodcat-ap-dataset-core.rdf"),
                Arguments.of(
                        "iso19115-3.2018",
                        "dcat",
                        "iso19115-3.2018-dcat-service.xml",
                        "iso19115-3.2018-dcat-service-core.rdf"),
                Arguments.of("iso19139", "dcat", "iso19139-dcat-dataset.xml", "iso19139-dcat-dataset-core.rdf"));
    }

    private static String loadResourceAsString(String resourcePath) throws Exception {
        ClassPathResource resource = new ClassPathResource("formatters/" + resourcePath);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
