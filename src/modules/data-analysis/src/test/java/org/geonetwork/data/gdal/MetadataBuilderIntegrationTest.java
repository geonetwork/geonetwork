/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.geonetwork.data.DataIngesterConfiguration;
import org.geonetwork.data.MetadataBuilder;
import org.geonetwork.data.model.DatasetInfo;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.User;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.domain.repository.OperationRepository;
import org.geonetwork.domain.repository.OperationallowedRepository;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.domain.repository.UsergroupRepository;
import org.geonetwork.editing.BatchEditMode;
import org.geonetwork.editing.BatchEditsService;
import org.geonetwork.editing.SchemaConfiguration;
import org.geonetwork.metadata.MetadataAccessManager;
import org.geonetwork.metadata.MetadataManager;
import org.geonetwork.schemas.SchemaManager;
import org.geonetwork.security.AuthenticationFacade;
import org.geonetwork.security.user.UserManager;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

@ExtendWith(SpringExtension.class)
@Import({
    MetadataBuilder.class,
    MetadataManager.class,
    BatchEditsService.class,
    MetadataManager.class,
    MetadataAccessManager.class,
    UserManager.class,
    AuthenticationFacade.class,
    SchemaManager.class
})
@EnableConfigurationProperties({DataIngesterConfiguration.class, SchemaConfiguration.class})
@ActiveProfiles({"prod", "test"})
@SpringBootTest(classes = {TestConfiguration.class})
@WithMockUser(username = "mock_test_admin")
class MetadataBuilderIntegrationTest {

    @Autowired
    private MetadataBuilder metadataBuilder;

    @MockBean
    private MetadataRepository metadataRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UsergroupRepository usergroupRepository;

    @MockBean
    private OperationRepository operationRepository;

    @MockBean
    private OperationallowedRepository operationallowedRepository;

    private GdalDataAnalyzer analyzer;

    @BeforeEach
    void setUp() throws IOException {
        String dataFolder = new ClassPathResource("data/samples").getFile().toString();
        String mountPoint = "/data";
        analyzer = new GdalDataAnalyzer(
                String.format(
                        "docker run --rm -v %s:%s ghcr.io/osgeo/gdal:alpine-normal-latest ", dataFolder, mountPoint),
                dataFolder,
                mountPoint,
                60);
    }

    @Test
    void dataAnalysisFromShapefileInjectedInTemplate() throws IOException {
        String layerFile = "CEEUBG100kV2_1.shp";
        Optional<DatasetInfo> layerProperties = analyzer.getLayerProperties(
                new ClassPathResource("data/samples/" + layerFile).getFile().getCanonicalPath(), "CEEUBG100kV2_1");

        String uuid = "uuid1";
        String template = IOUtils.toString(
                new ClassPathResource("schemas/iso19115-3.2018/templates/geodata.xml").getInputStream(),
                StandardCharsets.UTF_8);

        Metadata metadata = new Metadata();
        metadata.setUuid(uuid);
        metadata.setId(1);
        metadata.setSchemaid("iso19115-3.2018");
        metadata.setData(template);
        when(metadataRepository.findAllByUuidIn(List.of("uuid1"))).thenReturn(List.of(metadata));
        when(metadataRepository.findByUuid("uuid1")).thenReturn(Optional.of(metadata));
        when(userRepository.findOptionalByUsername("mock_test_admin"))
                .thenReturn(Optional.of(User.builder()
                        .name("mock_test_admin")
                        .profile(Profile.Administrator)
                        .build()));

        String builtMetadata = metadataBuilder.buildMetadata(
                uuid, metadata.getSchemaid(), layerProperties.get(), BatchEditMode.PREVIEW);
        String expected = Files.readString(
                Path.of(new ClassPathResource("data/samples/test_data_analysis_injected_in_template.xml").getURI()),
                StandardCharsets.UTF_8);

        Diff diff = DiffBuilder.compare(Input.fromString(expected))
                .withTest(Input.fromString(builtMetadata))
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
                .normalizeWhitespace()
                .ignoreComments()
                .checkForSimilar()
                .build();
        assertFalse(diff.hasDifferences(), String.format("%s. Differences: %s", layerFile, diff));
    }

    @ParameterizedTest
    @CsvSource({
        "CEEUBG100kV2_1.shp,iso19115-3.2018,<mdb:MD_Metadata xmlns:mdb=\"http://standards.iso.org/iso/19115/-3/mdb/2.0\"/>",
        "ENI2018_CHA0018_00_V2020_1_AM_PILOT.tif,iso19115-3.2018,<mdb:MD_Metadata xmlns:mdb=\"http://standards.iso.org/iso/19115/-3/mdb/2.0\"/>"
    })
    void dataAnalysisFromRasterFromEmptyRecord(String layerFile, String schema, String xml)
            throws IOException, JDOMException {
        String filePath = new ClassPathResource(String.format("data/samples/%s", layerFile))
                .getFile()
                .getCanonicalPath();
        String layerName = FilenameUtils.removeExtension(layerFile);

        String uuid = "uuid1";
        Metadata metadata = new Metadata();
        metadata.setUuid(uuid);
        metadata.setId(1);
        metadata.setSchemaid(schema);
        metadata.setData(xml);
        when(metadataRepository.findAllByUuidIn(List.of("uuid1"))).thenReturn(List.of(metadata));
        when(metadataRepository.findByUuid("uuid1")).thenReturn(Optional.of(metadata));
        when(userRepository.findOptionalByUsername("mock_test_admin"))
                .thenReturn(Optional.of(User.builder()
                        .name("mock_test_admin")
                        .profile(Profile.Administrator)
                        .build()));

        String builtMetadata = metadataBuilder.buildMetadata(
                uuid,
                metadata.getSchemaid(),
                layerFile.endsWith(".shp")
                        ? analyzer.getLayerProperties(filePath, layerName).get()
                        : analyzer.getRasterProperties(filePath).get(),
                BatchEditMode.PREVIEW);
        String expected = IOUtils.toString(
                new ClassPathResource(String.format("data/samples/%s-%s.xml", layerName, schema)).getInputStream(),
                StandardCharsets.UTF_8);

        Diff diff = DiffBuilder.compare(Input.fromString(expected))
                .withTest(Input.fromString(builtMetadata))
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
                .normalizeWhitespace()
                .ignoreComments()
                .checkForSimilar()
                .build();
        assertFalse(diff.hasDifferences(), String.format("%s. Differences: %s", layerFile, diff));

        metadata.setData(Xml.getString(
                (Element) Xml.loadString(builtMetadata, false).getChildren().getFirst()));
        String metadataAfterReapplyingAnalysis = metadataBuilder.buildMetadata(
                uuid,
                metadata.getSchemaid(),
                layerFile.endsWith(".shp")
                        ? analyzer.getLayerProperties(filePath, layerName).get()
                        : analyzer.getRasterProperties(filePath).get(),
                BatchEditMode.PREVIEW);

        diff = DiffBuilder.compare(Input.fromString(expected))
                .withTest(Input.fromString(metadataAfterReapplyingAnalysis))
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
                .normalizeWhitespace()
                .ignoreComments()
                .checkForSimilar()
                .build();
        assertFalse(diff.hasDifferences(), String.format("%s. Differences: %s", layerFile, diff));
    }
}
