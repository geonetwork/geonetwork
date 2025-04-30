/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/** Serialize a list of geometries (map) as a list of strings. */
public class GeometrySerializer extends JsonSerializer<List<Map>> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void serialize(List<Map> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || value.isEmpty()) {
            return;
        }
        gen.writeStartArray();
        for (Map v : value) {
            var val = mapper.writeValueAsString(v);
            gen.writeString(val);
        }
        gen.writeEndArray();
    }
}
