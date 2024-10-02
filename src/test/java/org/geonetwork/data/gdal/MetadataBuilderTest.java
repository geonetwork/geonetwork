/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal;

import lombok.AllArgsConstructor;
import org.geonetwork.ApplicationContextProvider;
import org.geonetwork.data.DataIngesterConfiguration;
import org.geonetwork.editing.BatchEditsService;
import org.geonetwork.index.IndexClient;
import org.geonetwork.indexing.IndexingRecordService;
import org.geonetwork.indexing.IndexingService;
import org.geonetwork.schemas.CodeListTranslator;
import org.geonetwork.schemas.SchemaManager;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.geonetwork.data.DatasetInfo;
import org.geonetwork.data.MetadataBuilder;
import org.geonetwork.domain.Metadata;
import org.geonetwork.repository.MetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@DataJpaTest(showSql = false)
@ExtendWith(SpringExtension.class)
@Import(
  value = {
    MetadataBuilder.class,
    DataIngesterConfiguration.class,
    BatchEditsService.class,
    SchemaManager.class
  })
@ActiveProfiles(value = {"prod", "test"})
class MetadataBuilderTest {

  private GdalDataAnalyzer analyzer;
  @Autowired private MetadataBuilder metadataBuilder;

  @MockBean private MetadataRepository metadataRepository;

  private static final boolean USE_GDAL_FROM_THE_SYSTEM = false;


  @BeforeEach
  void setUp() throws IOException, InterruptedException {
    if (USE_GDAL_FROM_THE_SYSTEM) {
      analyzer = new GdalDataAnalyzer("", "", "", 60);
    } else {
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
  }

  @Test
  void getLayerProperties() throws IOException {
    Optional<DatasetInfo> layerProperties =
        analyzer.getLayerProperties(
            new ClassPathResource("data/samples/CEEUBG100kV2_1.shp").getFile().getCanonicalPath(),
            "CEEUBG100kV2_1");

    String uuid = "uuid1";
    Metadata metadata = new Metadata();
    metadata.setUuid(uuid);
    metadata.setSchemaid("iso19115-3.2018");
    metadata.setData(
        "<mdb:MD_Metadata xmlns:mdb=\"http://standards.iso.org/iso/19115/-3/mdb/2.0\"/>");
    when(metadataRepository.findAllByUuidIn(List.of("uuid1"))).thenReturn(List.of(metadata));

    String builtMetadata = metadataBuilder.buildMetadata(uuid, layerProperties.get());
    System.out.println(builtMetadata);
  }
}
