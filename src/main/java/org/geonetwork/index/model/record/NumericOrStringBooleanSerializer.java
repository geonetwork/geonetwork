/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import javax.annotation.Nullable;

/** Numeric or string boolean serializer. */
public class NumericOrStringBooleanSerializer extends JsonDeserializer<Boolean> {

  @Override
  @Nullable
  public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String text = p.getText();
    if ("1".equals(text) || "true".equalsIgnoreCase(text)) {
      return Boolean.TRUE;
    } else if ("0".equals(text) || "false".equalsIgnoreCase(text)) {
      return Boolean.FALSE;
    }
    return null;
  }
}
