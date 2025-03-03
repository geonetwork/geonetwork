/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.utility.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static boolean isValidJsonObject(String json) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            return jsonNode.isObject();
        } catch (Exception e) {
            return false;
        }
    }
}
