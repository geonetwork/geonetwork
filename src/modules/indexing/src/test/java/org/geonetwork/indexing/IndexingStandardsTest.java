/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.indexing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.GeonetworkTestingApplication;
import org.geonetwork.domain.Metadata;
import org.geonetwork.index.model.record.Codelist;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.index.model.record.IndexRecords;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

@Disabled
@SpringBootTest(classes = {GeonetworkTestingApplication.class})
@ActiveProfiles(value = {"test"})
class IndexingStandardsTest {

    @Autowired
    IndexingRecordService indexingRecordService;

    @BeforeAll
    static void setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("CET"));
    }

    @Test
    void test_otherProperties() throws JsonProcessingException {
        HashMap<String, List<Object>> extraprops = new HashMap<>();
        extraprops.put("objectCount", List.of(4505));
        extraprops.put("objectLicence", List.of("licence"));
        IndexRecord record = IndexRecord.builder()
                .codelist(
                        "dada",
                        List.of(Codelist.builder().property("key", "www").build()))
                .otherProperties(extraprops)
                .build();

        String jsonString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(record);
        JsonNode jsonNode = new ObjectMapper().readTree(jsonString);
        assertEquals(4505, jsonNode.get("objectCount").get(0).intValue());
        assertEquals("licence", jsonNode.get("objectLicence").get(0).textValue());
    }

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
    void test_indexing_dataset(String file) throws Exception {
        String schema = StringUtils.split(file, "_")[0];
        String fileBaseName = String.format("samples/%s", file);
        // String xml = Files.readString(Path.of(new ClassPathResource(fileBaseName +
        // ".xml").getURI()));

        String xml = IOUtils.toString(new ClassPathResource(fileBaseName + ".xml").getInputStream());

        //    String expectedIndexDocument =
        //        Files.readString(Path.of(new ClassPathResource(fileBaseName + ".json").getURI()));

        String expectedIndexDocument = IOUtils.toString(new ClassPathResource(fileBaseName + ".json").getInputStream());

        Metadata dbRecord = Metadata.builder()
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
                .build();

        IndexRecords indexRecords = indexingRecordService.collectProperties(schema, List.of(dbRecord));
        IndexRecord indexRecord = indexRecords.getIndexRecord().getFirst();

        indexRecord.setIndexingDate("");
        indexRecord.setId(null);
        expectedIndexDocument = expectedIndexDocument
                .replaceAll(" +\"indexingDate\" : \".*\",\n", "")
                .trim();
        expectedIndexDocument =
                expectedIndexDocument.replaceAll(" +\"id\" : \"[0-9]+\",\n", "").trim();

        assertEquals(
                expectedIndexDocument,
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(indexRecord),
                indexRecord.getUuid());
    }
}
