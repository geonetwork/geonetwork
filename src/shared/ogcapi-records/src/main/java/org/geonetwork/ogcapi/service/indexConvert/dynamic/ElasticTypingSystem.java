/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert.dynamic;

import static org.geonetwork.ogcapi.service.configuration.OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.OverrideType.getJavaType;

import co.elastic.clients.elasticsearch._types.mapping.*;
import co.elastic.clients.elasticsearch.indices.IndexState;
import com.google.common.base.Splitter;
import java.io.IOException;
import java.util.*;
import lombok.*;
import org.apache.commons.compress.utils.Lists;
import org.geonetwork.index.client.IndexClient;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldsMapperConfig;
import org.geotools.util.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ElasticTypingSystem {

    /** user config* */
    private OgcElasticFieldsMapperConfig config; // user-configured fields

    /** what elastic index are we using? */
    private final String indexRecordName; // typically "gn-records"

    /** results of getting elastic index info i.e. http://localhost:9200/gn-records */
    private IndexState elasticIndexInfo;

    /** these are the types in from the GetIndexInfo elastic. key is from config.fields.elasticProperty */
    private Map<String, PropertyVariant> rawElasticTypes = new HashMap<String, PropertyVariant>();

    /** final elastic types key is from config.fields.elasticProperty */
    private Map<String, ElasticTypeInfo> finalElasticTypes = new HashMap<>();

    public ElasticTypingSystem(
            OgcElasticFieldsMapperConfig config,
            @Value("${geonetwork.index.indexRecordName:'gn-records'}") String indexRecordName,
            IndexState elasticIndexInfo) {
        this.config = config;
        this.indexRecordName = indexRecordName;
        this.elasticIndexInfo = elasticIndexInfo;
        computeRawElasticTypes();
        computeFinalElasticTypes();
    }

    private void computeFinalElasticTypes() {
        for (var field : config.getFields()) {
            if (field.getTypeOverride() != null) {
                // overridden type
                var override = field.getTypeOverride();
                this.finalElasticTypes.put(
                        field.getElasticProperty(),
                        new ElasticTypeInfo(
                                OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.OverrideType.isList(override),
                                OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.OverrideType.getSimpleType(
                                        override)));
            } else {
                // raw elastic type
                var rawElasticType = this.rawElasticTypes.get(field.getElasticProperty());
                if (rawElasticType instanceof KeywordProperty) {
                    this.finalElasticTypes.put(
                            field.getElasticProperty(),
                            new ElasticTypeInfo(
                                    true, OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.SimpleType.STRING));
                } else if (rawElasticType instanceof ShortNumberProperty
                        || rawElasticType instanceof IntegerNumberProperty
                        || rawElasticType instanceof LongNumberProperty) {
                    this.finalElasticTypes.put(
                            field.getElasticProperty(),
                            new ElasticTypeInfo(
                                    true, OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.SimpleType.INTEGER));
                } else if (rawElasticType instanceof FloatNumberProperty
                        || rawElasticType instanceof DoubleNumberProperty) {
                    this.finalElasticTypes.put(
                            field.getElasticProperty(),
                            new ElasticTypeInfo(
                                    true, OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.SimpleType.DOUBLE));
                } else if (rawElasticType instanceof TextProperty) {
                    this.finalElasticTypes.put(
                            field.getElasticProperty(),
                            new ElasticTypeInfo(
                                    false, OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.SimpleType.STRING));
                } else if (rawElasticType instanceof BooleanProperty) {
                    this.finalElasticTypes.put(
                            field.getElasticProperty(),
                            new ElasticTypeInfo(
                                    true, OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.SimpleType.BOOLEAN));
                } else if (rawElasticType instanceof DateProperty) {
                    this.finalElasticTypes.put(
                            field.getElasticProperty(),
                            new ElasticTypeInfo(
                                    true, OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.SimpleType.DATE));
                }
            }
        }
    }

    @Autowired
    public ElasticTypingSystem(
            OgcElasticFieldsMapperConfig config,
            @Value("${geonetwork.index.indexRecordName:'gn-records'}") String indexRecordName,
            IndexClient client) {
        this.config = config;
        this.indexRecordName = indexRecordName;
        getIndexInfo(client);
        computeRawElasticTypes();
        computeFinalElasticTypes();
    }

    private void getIndexInfo(IndexClient client) {
        try {
            if (client != null) {
                this.elasticIndexInfo = client.getEsClient()
                        .indices()
                        .get(x -> x.index(indexRecordName))
                        .get(indexRecordName);
                computeRawElasticTypes();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void computeRawElasticTypes() {
        if (this.elasticIndexInfo == null) {
            return; // nothing to do
        }
        for (var field : config.getFields()) {
            var elasticPath = field.getElasticProperty();
            var type = computeElasticType(elasticPath);
            this.rawElasticTypes.put(elasticPath, type);
        }
    }

    public PropertyVariant computeElasticType(String elasticPath) {
        if (this.elasticIndexInfo == null
                || this.elasticIndexInfo.mappings() == null
                || this.elasticIndexInfo.mappings().properties() == null) {
            throw new RuntimeException("Elastic Index Definition Unavailable");
        }
        var path = Lists.newArrayList(Splitter.on('.').split(elasticPath).iterator());

        // start off getting the first level property
        var pathPart = path.removeFirst();
        PropertyVariant elasticProperty = (PropertyVariant)
                this.elasticIndexInfo.mappings().properties().get(pathPart)._get();

        while (!path.isEmpty()) {
            pathPart = path.removeFirst();
            if (!(elasticProperty instanceof ObjectProperty objectProperty)) {
                throw new RuntimeException(
                        "Elastic Index Definition - couldn't find " + pathPart + " in " + elasticProperty);
            }
            elasticProperty =
                    (PropertyVariant) objectProperty.properties().get(pathPart)._get();
        }
        return elasticProperty;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @ToString
    public static class ElasticTypeInfo {

        private boolean isList;
        private OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.SimpleType type;
    }

    @SuppressWarnings("unchecked")
    public Object convert(String elasticPath, Object value) {
        if (value == null) {
            return null; // cannot convert
        }
        if (value instanceof Set set) {
            value = new ArrayList<>(set);
        }

        var typeInfo = this.finalElasticTypes.get(elasticPath);

        // handle list or not-list
        if (typeInfo.isList()) {
            // make sure value is a list
            if (!(value instanceof List)) {
                var list = new ArrayList<>();
                list.add(value);
                value = list;
            }
        } else {
            // make sure value isn't a list
            if (value instanceof List list) {
                if (list.isEmpty()) {
                    return null; // no elements
                }
                value = list.getFirst();
            }
        }

        // convert value type(s)
        if (value instanceof List list) {
            return list.stream().map(x -> convertObject(x, typeInfo)).toList();
        } else {
            return convertObject(value, typeInfo);
        }
    }

    @SuppressWarnings("unchecked")
    public Object convertObject(Object value, ElasticTypeInfo typeInfo) {
        if (value == null) {
            return null;
        }
        // trivial conversions
        if (typeInfo.getType().equals(OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.SimpleType.STRING)
                && value instanceof String) {
            return value; // no conversion
        }
        if (typeInfo.getType().equals(OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.SimpleType.DATE)
                && value instanceof String) {
            return value; // no conversion - elastic and json represent date the same (underlying elastic index is json)
        }

        return Converters.convert(value, getJavaType(typeInfo.getType()));
    }
}
