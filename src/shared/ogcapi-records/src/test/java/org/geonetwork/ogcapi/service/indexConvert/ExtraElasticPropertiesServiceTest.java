/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert;

import static org.junit.jupiter.api.Assertions.assertEquals;

import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.stream.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldMapperConfig;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.ExtraElasticPropertiesService;
import org.geonetwork.ogcapi.service.queryables.QueryablesService;
import org.junit.jupiter.api.Test;

public class ExtraElasticPropertiesServiceTest {

    @Test
    public void testTypeConversion() throws Exception {
        //        var indexDef = indexDefinition();
        //        var service = new ExtraElasticPropertiesService(null, null, indexDef.get("gn-records"));
        //        var object = createObject();
    }

    /** Tests some simple paths for the IndexRecord traversal code. */
    @Test
    public void testPaths() throws Exception {
        var service = new ExtraElasticPropertiesService(null, null);

        var object = createObject();

        // single value
        var path = "codelists.[\"cl_spatialRepresentationType\"].[0].properties.[\"key\"]";
        var config = new OgcElasticFieldMapperConfig();
        config.setIndexRecordProperty(path);

        var result = service.getFromElasticIndexRecord(config, object);
        assertEquals("grid", result);

        // multi-value
        path = "resourceAltTitle.[0].[\"default\"]";
        config = new OgcElasticFieldMapperConfig();
        config.setIndexRecordProperty(path);
        result = service.getFromElasticIndexRecord(config, object);
        assertEquals("WAL_OCS__2020", result);

        path = "resourceAltTitle.[1].[\"default\"]";
        config = new OgcElasticFieldMapperConfig();
        config.setIndexRecordProperty(path);
        result = service.getFromElasticIndexRecord(config, object);
        assertEquals("WAL_OCS_IA__2020", result);

        path = "resourceAltTitle.[*].[\"default\"]";
        config = new OgcElasticFieldMapperConfig();
        config.setIndexRecordProperty(path);
        result = service.getFromElasticIndexRecord(config, object);
        assertEquals(2, ((List) result).size());
        assertEquals("WAL_OCS__2020", ((List) result).get(0));
        assertEquals("WAL_OCS_IA__2020", ((List) result).get(1));

        // multi-value, but single value
        path = "codelists.[\"cl_spatialRepresentationType\"].[*].properties.[\"key\"]";
        config = new OgcElasticFieldMapperConfig();
        config.setIndexRecordProperty(path);
        result = service.getFromElasticIndexRecord(config, object);
        assertEquals(1, ((List) result).size());
        assertEquals("grid", ((List) result).get(0));
    }

    /** creates an IndexRecord from a JSON. This json is taken from the indexing module. */
    public IndexRecord createObject() throws IOException {
        InputStream is = QueryablesService.class
                .getClassLoader()
                .getResourceAsStream("indexrecord/iso19115-3.2018_dataset.json");

        String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        var objectMapper = new ObjectMapper();
        var result = objectMapper.readValue(text, IndexRecord.class);
        return result;
    }

    /** Get the definition of the GN Elastic json index */
    public GetIndexResponse indexDefinition() throws Exception {
        InputStream is = QueryablesService.class
                .getClassLoader()
                .getResourceAsStream("indexrecord/gn-records-getindexresponse.json");

        ObjectMapper objectMapper = new ObjectMapper();
        JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper(objectMapper);

        JsonParser parser = jacksonJsonpMapper.jsonProvider().createParser(is);

        var response = co.elastic.clients.elasticsearch.indices.GetIndexResponse._DESERIALIZER.deserialize(
                parser, jacksonJsonpMapper);

        return response;
    }
}
