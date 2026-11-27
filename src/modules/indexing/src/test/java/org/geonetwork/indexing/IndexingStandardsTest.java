/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.indexing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.GeonetworkTestingApplication;
import org.geonetwork.domain.Metadata;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.index.model.record.IndexRecords;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {GeonetworkTestingApplication.class})
@ActiveProfiles(value = {"test"})
class IndexingStandardsTest {

    @Autowired
    IndexingRecordService indexingRecordService;

    @BeforeAll
    static void setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("CET"));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "iso19115-3.2018_dataset",
                "iso19115-3.2018_datamodel",
                "iso19115-3.2018_service",
                "iso19139_dataset",
                "iso19139_service",
                "dublin-core_dataset",
                "iso19110_datamodel"
            })
    void test_indexing_dataset(String file) throws Exception {
        String schema = StringUtils.split(file, "_")[0];
        String fileBaseName = String.format("samples/%s", file);
        // String xml = Files.readString(Path.of(new ClassPathResource(fileBaseName +
        // ".xml").getURI()));

        String xml =
                IOUtils.toString(new ClassPathResource(fileBaseName + ".xml").getInputStream(), StandardCharsets.UTF_8);

        //    String expectedIndexDocument =
        //        Files.readString(Path.of(new ClassPathResource(fileBaseName + ".json").getURI()));

        String expectedIndexDocument = IOUtils.toString(
                new ClassPathResource(fileBaseName + ".json").getInputStream(), StandardCharsets.UTF_8);

        IndexRecord indexRecord = index(schema, fileBaseName, xml);

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

    /** A temporal extent of a single date is a range covering that whole day, not an open-ended one. */
    @Test
    void test_indexing_single_date_temporal_extent() throws Exception {
        String xml = IOUtils.toString(
                        new ClassPathResource("samples/iso19115-3.2018_dataset.xml").getInputStream(),
                        StandardCharsets.UTF_8)
                .replace("2020-04-24</gml:endPosition>", "2020-04-01</gml:endPosition>");

        IndexRecord indexRecord = index("iso19115-3.2018", "single-date-extent", xml);

        var range = indexRecord.getResourceTemporalExtentDateRange().getFirst();
        assertEquals("2020-04-01T00:00:00.000Z", range.getGte());
        assertEquals("2020-04-01T23:59:59.999Z", range.getLte());
    }

    /** An end given as a year-month or a year covers up to the last day of that month or year. */
    @Test
    void test_indexing_year_month_and_year_end_temporal_extent() throws Exception {
        String xml = IOUtils.toString(
                new ClassPathResource("samples/iso19115-3.2018_dataset.xml").getInputStream(), StandardCharsets.UTF_8);

        IndexRecord yearMonth = index(
                "iso19115-3.2018",
                "year-month-end-extent",
                xml.replace("2020-04-24</gml:endPosition>", "2024-02</gml:endPosition>"));
        assertEquals(
                "2024-02-29T23:59:59.999Z",
                yearMonth.getResourceTemporalExtentDateRange().getFirst().getLte());

        IndexRecord year = index(
                "iso19115-3.2018",
                "year-end-extent",
                xml.replace("2020-04-24</gml:endPosition>", "2024</gml:endPosition>"));
        assertEquals(
                "2024-12-31T23:59:59.999Z",
                year.getResourceTemporalExtentDateRange().getFirst().getLte());
    }

    /**
     * A temporal extent without time is a period of calendar dates, indexed in UTC whatever the server time zone (CET
     * here), so it does not start the day and year before (#272).
     */
    @Test
    void test_indexing_temporal_extent_dates_in_utc() throws Exception {
        String xml = IOUtils.toString(
                new ClassPathResource("samples/iso19115-3.2018_dataset.xml").getInputStream(), StandardCharsets.UTF_8);
        String start = StringUtils.substringBetween(xml, "<gml:beginPosition>", "</gml:beginPosition>");

        IndexRecord fullYears = index(
                "iso19115-3.2018",
                "full-years-extent",
                xml.replace(start + "</gml:beginPosition>", "2020-01-01</gml:beginPosition>")
                        .replace("2020-04-24</gml:endPosition>", "2022-12-31</gml:endPosition>"));
        var range = fullYears.getResourceTemporalExtentDateRange().getFirst();
        assertEquals("2020-01-01T00:00:00.000Z", range.getGte());
        assertEquals("2022-12-31T23:59:59.999Z", range.getLte());

        IndexRecord yearStart = index(
                "iso19115-3.2018",
                "year-start-extent",
                xml.replace(start + "</gml:beginPosition>", "2020</gml:beginPosition>"));
        assertEquals(
                "2020-01-01T00:00:00.000Z",
                yearStart.getResourceTemporalExtentDateRange().getFirst().getGte());
    }

    /** A temporal extent that ends before it starts is not indexed as a range, only flagged with a warning. */
    @Test
    void test_indexing_end_before_start_temporal_extent() throws Exception {
        String xml = IOUtils.toString(
                        new ClassPathResource("samples/iso19115-3.2018_dataset.xml").getInputStream(),
                        StandardCharsets.UTF_8)
                .replace("2020-04-24</gml:endPosition>", "2019-04-24</gml:endPosition>");

        IndexRecord indexRecord = index("iso19115-3.2018", "end-before-start-extent", xml);

        var ranges = indexRecord.getResourceTemporalExtentDateRange();
        assertTrue(ranges == null || ranges.isEmpty(), "no temporal extent range: " + ranges);
        assertTrue(
                indexRecord.getIndexingErrorMsg().stream()
                        .anyMatch(m -> "indexingErrorMsg-temporalDateRangeLowerGreaterThanUpper".equals(m.getString())),
                "warning: " + indexRecord.getIndexingErrorMsg());
    }

    private IndexRecord index(String schema, String uuid, String xml) {
        Metadata dbRecord = Metadata.builder()
                .uuid(uuid)
                .istemplate("n")
                .schemaid(schema)
                .changedate("2020-01-01T00:00:00Z")
                .createdate("2020-01-01T00:00:00Z")
                .isharvested("n")
                .source("null")
                .popularity(0)
                .rating(0)
                .owner(9999)
                .groupowner(null)
                .data(xml)
                .build();

        IndexRecords indexRecords = indexingRecordService.collectProperties(schema, List.of(dbRecord));
        IndexRecord indexRecord = indexRecords.getIndexRecord().getFirst();
        return indexRecord;
    }
}
