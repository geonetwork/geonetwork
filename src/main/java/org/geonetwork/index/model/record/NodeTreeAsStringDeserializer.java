/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** JSON Node tree as string deserializer. */
public class NodeTreeAsStringDeserializer extends JsonDeserializer<List<String>> {

  @Override
  public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    ObjectCodec oc = p.getCodec();
    JsonNode node = oc.readTree(p);
    List<String> geometries = new ArrayList<>();
    if (node.isArray()) {
      node.elements()
          .forEachRemaining(
              c -> {
                geometries.add(c.textValue());
              });
    } else {
      geometries.add(node.textValue());
    }
    return geometries;
  }
}
