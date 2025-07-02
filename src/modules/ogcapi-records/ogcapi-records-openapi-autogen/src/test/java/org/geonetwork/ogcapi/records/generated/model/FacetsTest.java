/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.records.generated.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FacetsTest {

    String example = "{\n" + "  \"id\": \"wis2-discovery-metadata\",\n"
            + "  \"title\": \"WIS2 Discovery Metadata\",\n"
            + "  \"defaultBucketCount\": 10,\n"
            + "  \"facets\": {\n"
            + "    \"date\": {\n"
            + "      \"type\": \"histogram\",\n"
            + "      \"property\": \"updated\",\n"
            + "      \"bucketType\": \"fixedInterval\"\n"
            + "    },\n"
            + "    \"organization\": {\n"
            + "      \"type\": \"term\",\n"
            + "      \"property\": \"contacts.organization\",\n"
            + "      \"sortedBy\": \"count\",\n"
            + "      \"minOccurs\": 50\n"
            + "    },\n"
            + "    \"created\": {\n"
            + "      \"type\": \"histogram\",\n"
            + "      \"property\": \"created\",\n"
            + "      \"bucketType\": \"fixedBucketCount\"\n"
            + "    },\n"
            + "    \"format\": {\n"
            + "      \"type\": \"term\",\n"
            + "      \"property\": \"links.format\",\n"
            + "      \"sortedBy\": \"value\",\n"
            + "      \"minOccurs\": 10\n"
            + "    },\n"
            + "    \"usage\": {\n"
            + "      \"type\": \"filter\",\n"
            + "      \"filters\": {\n"
            + "        \"view\": \"links.type = 'wms'\",\n"
            + "        \"download\": \"links.type = 'wfs'\"\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "}";

    @Test
    public void testSpecExampleFacets() throws JsonProcessingException {
        var json = example;
        var obj = mapper.readValue(json, OgcApiRecordsFacetsDto.class);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        var json2 = mapper.writeValueAsString(obj);
        json2 = json2.replace("\" :","\":");
        assertEquals(json, json2);
    }

    ObjectMapper mapper;

    @BeforeEach
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, true);
        mapper.findAndRegisterModules();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }
}
