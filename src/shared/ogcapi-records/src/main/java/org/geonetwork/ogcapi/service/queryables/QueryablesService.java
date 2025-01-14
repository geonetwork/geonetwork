/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.queryables;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonSchemaDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Basic Service to handle Queryables according to the OGCAPI spec.
 */
@Service
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class QueryablesService {

  /**
   * full schema from queryables.json
   */
  public static final OgcApiRecordsJsonSchemaDto fullJsonSchema;

  /**
   * full schema from queryables.json with elastic removed
   */
  public static final OgcApiRecordsJsonSchemaDto truncatedJsonSchema;

  static {
    fullJsonSchema = readQueryablesJsonSchema();

    var js = readQueryablesJsonSchema();
    if (js != null && js.getProperties() != null) {
      for (var prop : js.getProperties().values()) {
        prop.setxGnElastic(null);
      }
    }
    truncatedJsonSchema = js;
  }

  /**
   * helper method to read the "queryables/queryables.json" resource into a JavaSchema.
   *
   * @return contents of "queryables/queryables.json"
   */
  public static OgcApiRecordsJsonSchemaDto readQueryablesJsonSchema() {
    InputStream is = QueryablesService.class.getClassLoader().getResourceAsStream("queryables/queryables.json");

    try {
      String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      text = text.replaceAll("(?m)^//.*", "");

      var objectMapper = new ObjectMapper();
      var result = objectMapper.readValue(text, OgcApiRecordsJsonSchemaDto.class);
      return result;
    } catch (IOException e) {
      log.debug("problem reading in Queryables - is it mal-formed?", e);
    }

    return null;
  }

  /**
   * build a schema based on collection. It will be based on the underlying elastic index json.
   *
   * <p>NOTE: these are hard coded at the moment.
   *
   * @param collectionId which collection
   * @return schema based on collection (without elastic details)
   */
  public OgcApiRecordsJsonSchemaDto buildQueryables(String collectionId) {
    return truncatedJsonSchema;
  }

  /**
   * Full version of the queryables - including the elastic details.
   *
   * @param collectionId which collection
   * @return schema based on collection (WITH elastic details)
   */
  public OgcApiRecordsJsonSchemaDto getFullQueryables(String collectionId) {
    return fullJsonSchema;
  }
}
