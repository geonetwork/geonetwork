/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.sortables;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonPropertyDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonSchemaDto;
import org.geonetwork.ogcapi.service.configuration.SimpleType;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.DynamicPropertiesFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "org.fao.geonet.ogcapi")
public class SortablesService {

    @Autowired
    DynamicPropertiesFacade dynamicPropertiesFacade;

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

        // todo this is the hard-coded ID sortable
        var p = new OgcApiRecordsJsonPropertyDto();
        p.setType("string");
        p.setTitle("The unique identifier for this record.");
        p.setDescription("The unique identifier for this record.");
        props.put("id", p);

        for (var field : this.dynamicPropertiesFacade.getSortables()) {
            p = new OgcApiRecordsJsonPropertyDto();
            p.setTitle(field.getConfig().getTitle());
            p.setDescription(field.getConfig().getDescription());

            SimpleType.fillInOgc(field.getType(), p);
            props.put(field.getConfig().getOgcProperty(), p);
        }
        return props;
    }
}
