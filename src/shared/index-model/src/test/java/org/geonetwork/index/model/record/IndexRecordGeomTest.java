/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.index.model.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Tests serialization/deserialization of Geoms. Should accept a string (old method) or object (new method). */
public class IndexRecordGeomTest {

    @Test
    public void testObject() throws JsonProcessingException {
        String json = "{    \"geom\": [\n"
                + "      {\n"
                + "        \"type\": \"Polygon\",\n"
                + "        \"coordinates\": [\n"
                + "          [\n"
                + "            [\n"
                + "              112,\n"
                + "              -44\n"
                + "            ],\n"
                + "            [\n"
                + "              154,\n"
                + "              -44\n"
                + "            ],\n"
                + "            [\n"
                + "              154,\n"
                + "              -9\n"
                + "            ],\n"
                + "            [\n"
                + "              112,\n"
                + "              -9\n"
                + "            ],\n"
                + "            [\n"
                + "              112,\n"
                + "              -44\n"
                + "            ]\n"
                + "          ]\n"
                + "        ]\n"
                + "      }\n"
                + "    ]\n"
                + "}";

        ObjectMapper mapper = new ObjectMapper();
        var record = mapper.readValue(json, IndexRecord.class);

        assertEquals(1, record.getGeometries().size());
        assertEquals(2, record.getGeometries().get(0).size());
        assertEquals("Polygon", record.getGeometries().get(0).get("type"));
        assertTrue(record.getGeometries().get(0).get("coordinates") instanceof List);
        List coords = (List) record.getGeometries().get(0).get("coordinates");
        assertEquals(1, coords.size());

        List coords1 = (List) coords.get(0);
        assertEquals(5, coords1.size());

        List c = (List) coords1.get(0);
        assertEquals(2, c.size());
        assertEquals(112, c.get(0));
        assertEquals(-44, c.get(1));

        c = (List) coords1.get(1);
        assertEquals(2, c.size());
        assertEquals(154, c.get(0));
        assertEquals(-44, c.get(1));

        c = (List) coords1.get(2);
        assertEquals(2, c.size());
        assertEquals(154, c.get(0));
        assertEquals(-9, c.get(1));

        c = (List) coords1.get(3);
        assertEquals(2, c.size());
        assertEquals(112, c.get(0));
        assertEquals(-9, c.get(1));

        c = (List) coords1.get(4);
        assertEquals(2, c.size());
        assertEquals(112, c.get(0));
        assertEquals(-44, c.get(1));
    }

    @Test
    public void testString() throws JsonProcessingException {
        String json = "{\n"
                + "  \"geom\": \"{  \\\"type\\\": \\\"Polygon\\\",\\\"coordinates\\\": [[[112,-44],[154,-44],[154,-9],[112,-9],[112,-44]]]}\"\n"
                + "}";
        ObjectMapper mapper = new ObjectMapper();
        var record = mapper.readValue(json, IndexRecord.class);

        assertEquals(1, record.getGeometries().size());
        assertEquals(2, record.getGeometries().get(0).size());
        assertEquals("Polygon", record.getGeometries().get(0).get("type"));
        assertTrue(record.getGeometries().get(0).get("coordinates") instanceof List);
        List coords = (List) record.getGeometries().get(0).get("coordinates");
        assertEquals(1, coords.size());

        List coords1 = (List) coords.get(0);
        assertEquals(5, coords1.size());

        List c = (List) coords1.get(0);
        assertEquals(2, c.size());
        assertEquals(112, c.get(0));
        assertEquals(-44, c.get(1));

        c = (List) coords1.get(1);
        assertEquals(2, c.size());
        assertEquals(154, c.get(0));
        assertEquals(-44, c.get(1));

        c = (List) coords1.get(2);
        assertEquals(2, c.size());
        assertEquals(154, c.get(0));
        assertEquals(-9, c.get(1));

        c = (List) coords1.get(3);
        assertEquals(2, c.size());
        assertEquals(112, c.get(0));
        assertEquals(-9, c.get(1));

        c = (List) coords1.get(4);
        assertEquals(2, c.size());
        assertEquals(112, c.get(0));
        assertEquals(-44, c.get(1));
    }
}
