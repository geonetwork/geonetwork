/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.indexConvert.dynamic;

import static org.junit.jupiter.api.Assertions.*;

import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.stream.JsonParser;
import java.io.InputStream;
import java.util.ArrayList;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldMapperConfig;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldsMapperConfig;
import org.geonetwork.ogcapi.service.configuration.SimpleType;
import org.geonetwork.ogcapi.service.queryables.QueryablesService;
import org.junit.jupiter.api.Test;

public class ElasticTypingSystemTest {

    private GetIndexResponse loadIndexDefinition() throws Exception {
        try (InputStream is = QueryablesService.class
                .getClassLoader()
                .getResourceAsStream("indexrecord/gn-records-getindexresponse.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper(objectMapper);
            JsonParser parser = jacksonJsonpMapper.jsonProvider().createParser(is);
            return GetIndexResponse._DESERIALIZER.deserialize(parser, jacksonJsonpMapper);
        }
    }

    @Test
    public void testNestedPropertyTyping() throws Exception {
        var indexDef = loadIndexDefinition();
        var indexState = indexDef.get("gn-records");

        var config = new OgcElasticFieldsMapperConfig();
        var fields = new ArrayList<OgcElasticFieldMapperConfig>();

        // Nested property path: resourceDate.date
        var updatedField = new OgcElasticFieldMapperConfig();
        updatedField.setOgcProperty("updated");
        updatedField.setElasticProperty("resourceDate.date");
        fields.add(updatedField);

        // Object property path: resourceTitleObject.default
        var titleField = new OgcElasticFieldMapperConfig();
        titleField.setOgcProperty("title");
        titleField.setElasticProperty("resourceTitleObject.default");
        fields.add(titleField);

        // Direct primitive property: createDate
        var createDateField = new OgcElasticFieldMapperConfig();
        createDateField.setOgcProperty("created");
        createDateField.setElasticProperty("createDate");
        fields.add(createDateField);

        // Direct keyword property: uuid
        var idField = new OgcElasticFieldMapperConfig();
        idField.setOgcProperty("id");
        idField.setElasticProperty("uuid");
        fields.add(idField);

        config.setFields(fields);

        var typingSystem = new ElasticTypingSystem(config, "gn-records", indexState);

        var updatedTypeInfo = typingSystem.getTypeInfoByOgcProperty("updated");
        assertNotNull(updatedTypeInfo);
        assertEquals(SimpleType.DATE, updatedTypeInfo.getType());

        var titleTypeInfo = typingSystem.getTypeInfoByOgcProperty("title");
        assertNotNull(titleTypeInfo);
        assertEquals(SimpleType.STRING, titleTypeInfo.getType());

        var createdTypeInfo = typingSystem.getTypeInfoByOgcProperty("created");
        assertNotNull(createdTypeInfo);
        assertEquals(SimpleType.DATE, createdTypeInfo.getType());

        var idTypeInfo = typingSystem.getTypeInfoByOgcProperty("id");
        assertNotNull(idTypeInfo);
        assertEquals(SimpleType.STRING, idTypeInfo.getType());
    }

    @Test
    public void testMultiFieldPropertyTyping() throws Exception {
        var indexDef = loadIndexDefinition();
        var indexState = indexDef.get("gn-records");

        var config = new OgcElasticFieldsMapperConfig();
        var fields = new ArrayList<OgcElasticFieldMapperConfig>();

        // Text field with keyword multi-field: resourceEdition.keyword
        var editionKeywordField = new OgcElasticFieldMapperConfig();
        editionKeywordField.setOgcProperty("editionKeyword");
        editionKeywordField.setElasticProperty("resourceEdition.keyword");
        fields.add(editionKeywordField);

        // Non-existent subfield on a text field: resourceEdition.nonExistent
        var invalidSubField = new OgcElasticFieldMapperConfig();
        invalidSubField.setOgcProperty("invalidSubField");
        invalidSubField.setElasticProperty("resourceEdition.nonExistent");
        fields.add(invalidSubField);

        config.setFields(fields);

        var typingSystem = new ElasticTypingSystem(config, "gn-records", indexState);

        var editionKeywordTypeInfo = typingSystem.getTypeInfoByOgcProperty("editionKeyword");
        assertNotNull(editionKeywordTypeInfo);
        assertEquals(SimpleType.STRING, editionKeywordTypeInfo.getType());

        assertNull(typingSystem.getTypeInfoByOgcProperty("invalidSubField"));
    }

    @Test
    public void testNonExistentPropertyDoesNotThrow() throws Exception {
        var indexDef = loadIndexDefinition();
        var indexState = indexDef.get("gn-records");

        var config = new OgcElasticFieldsMapperConfig();
        var fields = new ArrayList<OgcElasticFieldMapperConfig>();

        var unknownField = new OgcElasticFieldMapperConfig();
        unknownField.setOgcProperty("unknown");
        unknownField.setElasticProperty("nonExistentField.nestedProperty.foo");
        fields.add(unknownField);

        config.setFields(fields);

        var typingSystem = new ElasticTypingSystem(config, "gn-records", indexState);
        assertNull(typingSystem.getTypeInfoByOgcProperty("unknown"));
    }
}
