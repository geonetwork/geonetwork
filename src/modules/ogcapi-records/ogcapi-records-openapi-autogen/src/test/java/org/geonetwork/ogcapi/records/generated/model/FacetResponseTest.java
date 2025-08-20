/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.records.generated.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.text.SimpleDateFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FacetResponseTest {

    @Test
    public void test() throws IOException {
        String json = OgcApiRecordsSpecExamplesTest.readResource("ogcapi_records_spec_examples/facetResponse.json");
        var obj = mapper.readValue(json, OgcApiRecordsFeatureCollectionGeoJSONDto.class);

        var json2 = mapper.writeValueAsString(obj);
        assertEquals(json, json2);
    }

    ObjectMapper mapper = new ObjectMapper();
    /** setup object mapper */
    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, true);
        mapper.findAndRegisterModules();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
}
