/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;

/** Serialize a list of strings as a list of JsonNode. */
public class StringAsNodeTreeSerializer extends JsonSerializer<List<String>> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void serialize(List<String> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || value.isEmpty()) {
            return;
        }
        gen.writeStartArray();
        for (String v : value) {
            JsonNode node = mapper.readTree(v);
            gen.writeTree(node);
        }
        gen.writeEndArray();
    }
}
