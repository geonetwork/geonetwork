/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.index.model.record;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class HashMapSerializer extends JsonSerializer<Map<String, ArrayList<Object>>> {
    @Override
    public void serialize(Map<String, ArrayList<Object>> map, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        map.keySet().forEach(key -> {
            try {
                jsonGenerator.writeFieldName(key);
                jsonGenerator.writeStartArray();
                map.get(key).forEach(value -> {
                    try {
                        jsonGenerator.writeObject(value);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                jsonGenerator.writeEndArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
