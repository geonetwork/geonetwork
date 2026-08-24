/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
