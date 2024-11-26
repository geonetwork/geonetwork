/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.org.geonetwork.ogcapi.records.generated.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFormatDto;
import org.junit.jupiter.api.Test;

/** This is a trival test case that tests the JSON input/output. */
public class FormatTest {

    String formatJson = "{\"name\":\"format\",\"mediaType\":\"media/type\"}";

    @Test
    public void t1() throws JsonProcessingException {
        OgcApiRecordsFormatDto format = new OgcApiRecordsFormatDto();
        format.name("format");
        format.mediaType("media/type");

        assertEquals("format", format.getName());
        assertEquals("media/type", format.getMediaType());

        // write as json
        ObjectMapper mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(format);
        assertEquals(formatJson, json);

        // read as Json
        var format2 = mapper.readValue(formatJson, OgcApiRecordsFormatDto.class);
        assertEquals("format", format2.getName());
        assertEquals("media/type", format2.getMediaType());
        assertEquals(format, format2);

        // read & write as json

        var format3 = mapper.readValue(formatJson, OgcApiRecordsFormatDto.class);
        var json3 = mapper.writeValueAsString(format3);
        assertEquals(formatJson, json3);
    }
}
