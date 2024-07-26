/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.indexing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.domain.Metadata;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.index.model.record.IndexRecords;
import org.geonetwork.repository.MetadataRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = {"prod", "test"})
class IndexingServiceTest {

  @Autowired IndexingService indexingService;

  @Autowired IndexingRecordService indexingRecordService;

  @Autowired MetadataRepository metadataRepository;

  @ParameterizedTest
  @ValueSource(
      strings = {
         "iso19115-3.2018_dataset",
         "iso19115-3.2018_datamodel",
         "iso19115-3.2018_service",
         "iso19139_dataset",
        "dublin-core_dataset",
         "iso19110_datamodel"
      })
  void test_indexing_dataset(String file) {
    try {
      String schema = StringUtils.split(file, "_")[0];
      String fileBaseName = String.format("samples/%s", file);
      String xml = Files.readString(Path.of(new ClassPathResource(fileBaseName + ".xml").getURI()));
      String expectedIndexDocument =
          Files.readString(Path.of(new ClassPathResource(fileBaseName + ".json").getURI()));
      Metadata dbRecord =
          metadataRepository.save(
              Metadata.builder()
                  .uuid(fileBaseName)
                  .istemplate("n")
                  .schemaid(schema)
                  .changedate("2020-01-01T00:00:00Z")
                  .createdate("2020-01-01T00:00:00Z")
                  .isharvested("n")
                  .source("null")
                  .popularity(0)
                  .rating(0)
                  .owner(1)
                  .groupowner(null)
                  .data(xml)
                  .build());

      IndexRecords indexRecords =
          indexingRecordService.collectProperties(schema, List.of(dbRecord));
      IndexRecord indexRecord = indexRecords.getIndexRecord().getFirst();

      indexRecord.setIndexingDate("");
      indexRecord.setId(null);
      expectedIndexDocument =
          expectedIndexDocument.replaceAll(" +\"indexingDate\" : \".*\",\n", "").trim();
      expectedIndexDocument =
          expectedIndexDocument.replaceAll(" +\"id\" : \"[0-9]+\",\n", "").trim();

      assertEquals(
          expectedIndexDocument,
          new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(indexRecord),
          indexRecord.getUuid());

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}
