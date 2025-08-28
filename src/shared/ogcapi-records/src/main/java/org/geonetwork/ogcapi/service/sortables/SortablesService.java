/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.sortables;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonPropertyDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonSchemaDto;
import org.geonetwork.ogcapi.service.configuration.SimpleType;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.ElasticTypingSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "org.fao.geonet.ogcapi")
public class SortablesService {

    @Autowired
    private ElasticTypingSystem elasticTypingSystem;

    public OgcApiRecordsJsonSchemaDto buildSortables(String collectionId) {
        var result = new OgcApiRecordsJsonSchemaDto();

        result.set$Schema(OgcApiRecordsJsonSchemaDto.SchemaEnum.HTTPS_JSON_SCHEMA_ORG_DRAFT_2020_12_SCHEMA);
        result.set$Id(collectionId);
        result.setType(OgcApiRecordsJsonSchemaDto.TypeEnum.OBJECT);
        result.setTitle("Sortables for collection " + collectionId);
        result.setDescription("Sortables for collection " + collectionId);
        result.setProperties(createProperties());
        return result;
    }

    public Map<String, OgcApiRecordsJsonPropertyDto> createProperties() {
        var props = new HashMap<String, OgcApiRecordsJsonPropertyDto>();

        var p = new OgcApiRecordsJsonPropertyDto();
        p.setType("string");
        p.setTitle("The unique identifier for this record.");
        p.setDescription("The unique identifier for this record.");
        props.put("id", p);

        for (var field : this.elasticTypingSystem.getFinalElasticTypes().entrySet()) {
            if (field.getValue().getConfig().getIsSortable() == null
                    || !field.getValue().getConfig().getIsSortable()) {
                continue;
            }
            p = new OgcApiRecordsJsonPropertyDto();
            p.setTitle(field.getValue().getConfig().getTitle());
            p.setDescription(field.getValue().getConfig().getDescription());

            var type = this.elasticTypingSystem
                    .getFinalElasticTypes()
                    .get(field.getValue().getConfig().getElasticProperty())
                    .getType();
            if (type == SimpleType.INTEGER || type == SimpleType.DOUBLE) {
                p.setType("number");
            } else if (type == SimpleType.STRING) {
                p.setType("string");
            } else if (type == SimpleType.DATE) {
                p.setType("string");
                p.setFormat("date");
            } else if (type == SimpleType.BOOLEAN) {
                p.setType("boolean");
            } else {
                throw new RuntimeException(
                        "don't know type - " + field.getValue().getType());
            }
            props.put(field.getValue().getConfig().getOgcProperty(), p);
        }

        return props;
    }
}
