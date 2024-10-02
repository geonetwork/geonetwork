package org.geonetwork.data.gdal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.geonetwork.data.DataIngesterConfiguration;
import org.geonetwork.data.DatasetInfo;
import org.geonetwork.data.MetadataBuilder;
import org.geonetwork.domain.Metadata;
import org.geonetwork.editing.BatchEditsService;
import org.geonetwork.repository.MetadataRepository;
import org.geonetwork.schemas.SchemaManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

@DataJpaTest(showSql = false)
@ExtendWith(SpringExtension.class)
@Import({
  MetadataBuilder.class,
  DataIngesterConfiguration.class,
  BatchEditsService.class,
  SchemaManager.class
})
@ActiveProfiles({"prod", "test"})
class MetadataBuilderTest {

  @Autowired private MetadataBuilder metadataBuilder;
  @MockBean private MetadataRepository metadataRepository;
  private GdalDataAnalyzer analyzer;

  @BeforeEach
  void setUp() throws IOException {
    String dataFolder = new ClassPathResource("data/samples").getFile().toString();
    String mountPoint = "/data";
    analyzer =
        new GdalDataAnalyzer(
            String.format(
                "docker run --rm -v %s:%s ghcr.io/osgeo/gdal:alpine-normal-latest ",
                dataFolder, mountPoint),
            dataFolder,
            mountPoint,
            60);
  }

  @Test
  void dataAnalysisFromShapefileFromEmptyRecord() throws IOException {
    String layerFile = "CEEUBG100kV2_1.shp";
    Optional<DatasetInfo> layerProperties =
        analyzer.getLayerProperties(
            new ClassPathResource("data/samples/" + layerFile).getFile().getCanonicalPath(),
            "CEEUBG100kV2_1");

    String uuid = "uuid1";
    Metadata metadata = new Metadata();
    metadata.setUuid(uuid);
    metadata.setSchemaid("iso19115-3.2018");
    metadata.setData(
        "<mdb:MD_Metadata xmlns:mdb=\"http://standards.iso.org/iso/19115/-3/mdb/2.0\"/>");
    when(metadataRepository.findAllByUuidIn(List.of("uuid1"))).thenReturn(List.of(metadata));

    String builtMetadata = metadataBuilder.buildMetadata(uuid, layerProperties.get());
    String expected =
        Files.readString(
            Path.of(new ClassPathResource("data/samples/" + layerFile + ".xml").getURI()));

    Diff diff =
        DiffBuilder.compare(Input.fromString(builtMetadata))
            .withTest(Input.fromString(expected))
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
            .normalizeWhitespace()
            .ignoreComments()
            .checkForSimilar()
            .build();
    assertFalse(
        diff.hasDifferences(), String.format("%s. Differences: %s", layerFile, diff.toString()));
  }

  @Test
  void dataAnalysisFromShapefileInjectedInTemplate() throws IOException {
    String layerFile = "CEEUBG100kV2_1.shp";
    Optional<DatasetInfo> layerProperties =
        analyzer.getLayerProperties(
            new ClassPathResource("data/samples/" + layerFile).getFile().getCanonicalPath(),
            "CEEUBG100kV2_1");

    String uuid = "uuid1";
    String template =
        Files.readString(
            Path.of(
                new ClassPathResource("schemas/iso19115-3.2018/templates/geodata.xml").getURI()));

    Metadata metadata = new Metadata();
    metadata.setUuid(uuid);
    metadata.setSchemaid("iso19115-3.2018");
    metadata.setData(template);
    when(metadataRepository.findAllByUuidIn(List.of("uuid1"))).thenReturn(List.of(metadata));

    String builtMetadata = metadataBuilder.buildMetadata(uuid, layerProperties.get());
    String expected =
        Files.readString(
            Path.of(
                new ClassPathResource("data/samples/test_data_analysis_injected_in_template.xml")
                    .getURI()));

    Diff diff =
        DiffBuilder.compare(Input.fromString(builtMetadata))
            .withTest(Input.fromString(expected))
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
            .normalizeWhitespace()
            .ignoreComments()
            .checkForSimilar()
            .build();
    assertFalse(
        diff.hasDifferences(), String.format("%s. Differences: %s", layerFile, diff.toString()));
  }
}
