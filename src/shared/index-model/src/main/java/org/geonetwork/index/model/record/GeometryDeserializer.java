/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Handles new (object) and old (string) serializations of geom */
public class GeometryDeserializer extends JsonDeserializer<List<Map>> {

    @Override
    public List<Map> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec oc = p.getCodec();
        JsonNode node = oc.readTree(p);
        List<Map> geometries = new ArrayList<>();

        if (node.isArray()) {
            node.elements().forEachRemaining(c -> {
                geometries.add(deserializeGeom(c));
            });
        } else {
            geometries.add(deserializeGeom(node));
        }
        return geometries;
    }

    public Map deserializeGeom(JsonNode node) {
        ObjectMapper mapper = new ObjectMapper();
        if (node.isTextual()) {
            try {
                return mapper.readValue(node.asText(), Map.class);
            } catch (JsonProcessingException e) {
                return null;
            }
        }
        Map result = mapper.convertValue(node, Map.class);
        return result;
    }
}
