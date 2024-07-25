/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.indexing;

import static org.geonetwork.index.model.record.IndexRecordFieldNames.CommonField.DEFAULT_TEXT;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.DATE_STAMP;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.DOC_TYPE;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.MAIN_LANGUAGE;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.METADATA_IDENTIFIER;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.RESOURCE_TYPE;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.STANDARD_NAME;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.STANDARD_VERSION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.geonetwork.domain.Metadata;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.index.model.record.IndexRecords;
import org.geonetwork.repository.MetadataRepository;
import org.junit.jupiter.api.Test;
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

  @Test
  void test_indexing_dataset_compared_to_version4() {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      String xml =
          Files.readString(
              Path.of(new ClassPathResource("samples/iso19115-3_dataset.xml").getURI()));

      Metadata dbRecord =
          metadataRepository.save(
              Metadata.builder()
                  .uuid("ABC")
                  .istemplate("n")
                  .schemaid("iso19115-3.2018")
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
          indexingRecordService.collectProperties("iso19115-3.2018", List.of(dbRecord));
      IndexRecord indexRecord = indexRecords.getIndexRecord().getFirst();

      assertEquals("2020-01-01T00:00:00Z", indexRecord.getChangeDate());

      String jsonFromVersion4Index =
          Files.readString(
              Path.of(
                  new ClassPathResource("samples/iso19115-3_dataset_indexdocument_in_v4.json")
                      .getURI()));

      Map o = objectMapper.readValue(jsonFromVersion4Index, Map.class);
      String jsonFromSerialization =
          objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(indexRecord);

      assertEquals(o.get(DOC_TYPE), indexRecord.getDocType().toString());
      assertEquals(o.get(METADATA_IDENTIFIER), indexRecord.getMetadataIdentifier());
      assertEquals(
          ((Map<String, String>) o.get(STANDARD_NAME)).get(DEFAULT_TEXT),
          indexRecord.getStandardName().get(DEFAULT_TEXT));
      assertEquals(
          ((Map<String, String>) o.get(STANDARD_VERSION)).get(DEFAULT_TEXT),
          indexRecord.getStandardVersion().get(DEFAULT_TEXT));
      assertEquals(o.get(RESOURCE_TYPE), indexRecord.getResourceType());
      assertEquals(o.get(DATE_STAMP), indexRecord.getDateStamp());
      assertEquals(o.get(MAIN_LANGUAGE), indexRecord.getMainLanguage());

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}
