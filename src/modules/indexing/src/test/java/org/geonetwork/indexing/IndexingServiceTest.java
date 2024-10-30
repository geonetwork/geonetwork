/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.indexing;

import static org.junit.jupiter.api.Assertions.assertTrue;

import co.elastic.clients.elasticsearch.core.ExistsRequest;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Future;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.GeonetworkTestingApplication;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.index.client.IndexClient;
import org.geonetwork.testing.ElasticsearchBasedIntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {GeonetworkTestingApplication.class})
@ActiveProfiles(value = {"test"})
class IndexingServiceTest extends ElasticsearchBasedIntegrationTest {

    @Autowired
    IndexingRecordService indexingRecordService;

    @Autowired
    IndexingService indexingService;

    @Autowired
    MetadataRepository metadataRepository;

    @Autowired
    private IndexClient indexClient;

    @BeforeAll
    static void setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("CET"));
    }

    @Test
    void test_which_requires_elasticsearch() throws Exception {
        String file = "iso19115-3.2018_dataset";
        String schema = StringUtils.split(file, "_")[0];
        String fileBaseName = String.format("samples/%s", file);
        // String xml = Files.readString(Path.of(new ClassPathResource(fileBaseName +
        // ".xml").getURI()));
        String xml = IOUtils.toString(new ClassPathResource(fileBaseName + ".xml").getInputStream());

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

        metadataRepository.save(dbRecord);

        List<Future<?>> indexTaskSubmissions = indexingService.index(List.of(dbRecord.getUuid()));
        for (Future<?> task : indexTaskSubmissions) {
            task.get();
        }

        boolean allDone = true;
        for (Future<?> future : indexTaskSubmissions) {
            allDone &= future.isDone();
        }

        if (allDone) {
            BooleanResponse exists = indexClient.getEsClient().exists(ExistsRequest.of(o -> o.index(
                            indexClient.getIndexRecordName())
                    .id(dbRecord.getUuid())));
            assertTrue(exists.value());
        }
    }
}
