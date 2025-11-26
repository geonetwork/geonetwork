/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.queryables;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonSchemaDto;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.DynamicPropertiesFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Basic Service to handle Queryables according to the OGCAPI spec.
 *
 * <p>Queryables are broken into two main types: <br>
 * 1. "difficult" and "built-in" ones that are defined in the queryables.json file <br>
 * a. "built-in" ones are the already-existing properties (defined by ogc) <br>
 * b. "difficult" ones require querying multiple elastic json index properties 2. "easy" ones that are from the dynamic
 * properties config <br>
 */
@Component
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class QueryablesService {

    DynamicPropertiesFacade dynamicPropertiesFacade;

    @Autowired
    public QueryablesService(DynamicPropertiesFacade dynamicPropertiesFacade) {
        this.dynamicPropertiesFacade = dynamicPropertiesFacade;
        this.fullJsonSchema = createQueryablesJsonSchema();
        this.truncatedJsonSchema = truncatedJsonSchema();
    }

    /** full schema from queryables.json */
    public final OgcApiRecordsJsonSchemaDto fullJsonSchema;

    /** full schema from queryables.json with elastic info removed */
    public final OgcApiRecordsJsonSchemaDto truncatedJsonSchema;

    /**
     * gets the queryables with the "extra" elastic info removed (for the /collection/<collectionid>/queryables
     * endpoint).
     *
     * @return json schema for the queryables
     */
    public OgcApiRecordsJsonSchemaDto truncatedJsonSchema() {
        var result = createQueryablesJsonSchema();
        if (result != null && result.getProperties() != null) {
            for (var prop : result.getProperties().values()) {
                prop.setxGnElastic(null);
            }
        }
        return result;
    }

    /**
     * This includes the defined in the queryables.json file as well as the dynamic ones. This includes everything as
     * well as the elastic info.
     *
     * @return json schema object for the queryables.
     */
    public OgcApiRecordsJsonSchemaDto createQueryablesJsonSchema() {
        var result = readQueryablesJsonSchema();

        // for simple test cases
        if (dynamicPropertiesFacade != null) {
            dynamicPropertiesFacade.addDynamicQueryablesSchema(result);
        }

        return result;
    }

    /**
     * helper method to read the "queryables/queryables.json" resource into a JavaSchema.
     *
     * @return contents of "queryables/queryables.json"
     */
    public OgcApiRecordsJsonSchemaDto readQueryablesJsonSchema() {
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

    /**
     * is this a queryable property?
     *
     * @param propertyName name of the queryable property (i.e. "contacts")
     * @param collectionID which collection (null is usually ok)
     * @return if this is defined as a queryable
     */
    public boolean isQueryable(String propertyName, String collectionID) {
        return getFullQueryables(collectionID).getProperties().containsKey(propertyName);
    }

    /**
     * get all the queryables property names.
     *
     * @param collectionID which collection (null is usually ok)
     * @return all the properties (i.e. ["id","contacts"] )
     */
    public List<String> queryableProperties(String collectionID) {
        return new ArrayList<>(getFullQueryables(collectionID).getProperties().keySet());
    }
}
