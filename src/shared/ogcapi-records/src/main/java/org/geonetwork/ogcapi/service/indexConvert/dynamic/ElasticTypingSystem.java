/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert.dynamic;

import static org.geonetwork.ogcapi.service.configuration.OverrideType.getJavaType;

import co.elastic.clients.elasticsearch._types.mapping.*;
import co.elastic.clients.elasticsearch.indices.IndexState;
import com.google.common.base.Splitter;
import java.io.IOException;
import java.util.*;
import org.apache.commons.compress.utils.Lists;
import org.geonetwork.index.client.IndexClient;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldMapperConfig;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldsMapperConfig;
import org.geonetwork.ogcapi.service.configuration.OverrideType;
import org.geonetwork.ogcapi.service.configuration.SimpleType;
import org.geotools.util.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This provides two related functionalities.
 *
 * <p>1. it computes the type info (ElasticTypeInfo) of each of the fields.<br>
 * 2. it can do conversion from elastic to actual datatypes (i.e. string -> short/date) <br>
 *
 * <p>Basic usage is to get type info about an elastic field (or ogc property) name. And to do some conversion.
 */
@Component
// @Getter
public class ElasticTypingSystem {

    //    /** user config* */
    //    private OgcElasticFieldsMapperConfig config; // user-configured fields
    //
    //    /** what elastic index are we using? */
    //    private final String indexRecordName; // typically "gn-records"

    /** results of getting elastic index info i.e. http://localhost:9200/gn-records */
    private IndexState elasticIndexInfo;

    /** these are the types in from the GetIndexInfo elastic. key is from config.fields.elasticProperty */
    //    private Map<String, PropertyVariant> rawElasticTypes = new HashMap<String, PropertyVariant>();

    /** final elastic types key is from config.fields.elasticProperty */
    private Map<String, ElasticTypeInfo> finalElasticTypes = new HashMap<>();

    /** final elastic types key is from config.fields.ogcproperty */
    private Map<String, ElasticTypeInfo> finalElasticTypesByOgc;

    public ElasticTypingSystem(
            OgcElasticFieldsMapperConfig config,
            @Value("${geonetwork.index.indexRecordName:'gn-records'}") String indexRecordName,
            IndexState elasticIndexInfo) {
        this.elasticIndexInfo = elasticIndexInfo;

        this.finalElasticTypesByOgc = new HashMap<>();
        for (var field : config.getFields()) {
            var finalType = computeElasticTypeInfo(field);
            this.finalElasticTypes.put(field.getElasticProperty(), finalType);

            var name = field.getOgcProperty();
            this.finalElasticTypesByOgc.put(name, finalType);
        }
    }

    @Autowired
    public ElasticTypingSystem(
            OgcElasticFieldsMapperConfig config,
            @Value("${geonetwork.index.indexRecordName:'gn-records'}") String indexRecordName,
            IndexClient client) {

        this(config, indexRecordName, getIndexInfo(client, indexRecordName));
    }

    /**
     * returns the type info for a property (by its elastic property location).
     *
     * @param elasticProperty where in the elastic index is this property
     * @return type info for the property
     */
    public ElasticTypeInfo getTypeInfoByElasticProperty(String elasticProperty) {
        // TODO: if this is null, we should re-get index
        return finalElasticTypes.get(elasticProperty);
    }

    /**
     * returns the type info for a property (by the ogcProperty name).
     *
     * @param ogcProperty name of the ogc property
     * @return type info for the property
     */
    public ElasticTypeInfo getTypeInfoByOgcProperty(String ogcProperty) {
        // TODO: if this is null, we should re-get index
        return finalElasticTypesByOgc.get(ogcProperty);
    }

    /**
     * Get info about ALL the fields (ones without info are removed)
     *
     * @return info about ALL the fields (ones without info are removed)
     */
    public List<ElasticTypeInfo> getAllFieldInfos() {
        return finalElasticTypesByOgc.values().stream().filter(Objects::nonNull).toList();
    }

    /**
     * Given a field (user configured in application.yaml), compute the ElasticTypeInfo for it.
     *
     * @param field info about the field
     * @return type info extracted from elastic (or user-configured in application.yaml)
     */
    private ElasticTypeInfo computeElasticTypeInfo(OgcElasticFieldMapperConfig field) {
        var elasticPath = field.getElasticProperty();

        // this is info from the actual elastic index
        // might be null
        var rawElasticType = computeRawElasticType(elasticPath);

        // this handles the user-configure (application.yaml) type info
        // might be null
        var result = computeFinalType(field, rawElasticType);
        return result;
    }

    /**
     * computes the full-processed type for a user-defined field. Typically, this will be the type from the elastic
     * index, but:
     *
     * <p>This can be overriden with the TypeOverride in the application.yaml
     *
     * @param field user config for the field (application.yaml)
     * @param rawElasticType info from the elastic index definition for that field.
     * @return combined type info (i.e. raw or overridden definition)
     */
    private ElasticTypeInfo computeFinalType(OgcElasticFieldMapperConfig field, PropertyVariant rawElasticType) {
        if (field.getTypeOverride() != null) {
            // overridden type
            var override = field.getTypeOverride();
            return new ElasticTypeInfo(field, OverrideType.isList(override), OverrideType.getSimpleType(override));
        }

        if (rawElasticType == null) {
            return null;
        }

        // raw elastic type
        if (rawElasticType instanceof KeywordProperty) {
            return new ElasticTypeInfo(field, true, SimpleType.STRING);
        } else if (rawElasticType instanceof ShortNumberProperty
                || rawElasticType instanceof IntegerNumberProperty
                || rawElasticType instanceof LongNumberProperty) {
            return new ElasticTypeInfo(field, true, SimpleType.INTEGER);
        } else if (rawElasticType instanceof FloatNumberProperty || rawElasticType instanceof DoubleNumberProperty) {
            return new ElasticTypeInfo(field, true, SimpleType.DOUBLE);
        } else if (rawElasticType instanceof TextProperty) {
            return new ElasticTypeInfo(field, false, SimpleType.STRING);
        } else if (rawElasticType instanceof BooleanProperty) {
            return new ElasticTypeInfo(field, true, SimpleType.BOOLEAN);
        } else if (rawElasticType instanceof DateProperty) {
            return new ElasticTypeInfo(field, true, SimpleType.DATE);
        }

        return null;
    }

    /**
     * queries elastic for the index definition.
     *
     * @param client how to talk to elastic
     * @param indexRecordName what index are we getting the definition for?
     * @return IndexState (i.e. http://localhost:9200/gn-records)
     */
    private static IndexState getIndexInfo(IndexClient client, String indexRecordName) {
        try {
            if (client != null) {
                return client.getEsClient()
                        .indices()
                        .get(x -> x.index(indexRecordName))
                        .get(indexRecordName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * given an elastic index location (elasticPath), get info from the index about that part of the index. ie.
     * cl_spatialRepresentationType.key -> will return that this is a keyword (Keyword PropertyVariant).
     *
     * <p>Its possible that the elasticPath doesn't actually exist, in which case return null. This can happen if the
     * index is empty.
     *
     * <p>this is the raw elastic type - it might be overriden in the config (handled in computeFinalElasticType)
     *
     * @param elasticPath ie. cl_spatialRepresentationType.key (from user config in application.yaml)
     * @return null or the PropertyVariant (i.e. KeywordProperty)
     */
    private PropertyVariant computeRawElasticType(String elasticPath) {
        if (this.elasticIndexInfo == null
                || this.elasticIndexInfo.mappings() == null
                || this.elasticIndexInfo.mappings().properties() == null) {
            throw new RuntimeException("Elastic Index Definition Unavailable");
        }
        var path = Lists.newArrayList(Splitter.on('.').split(elasticPath).iterator());

        // start off getting the first level property
        var pathPart = path.removeFirst();
        var first = this.elasticIndexInfo.mappings().properties().get(pathPart);

        if (first == null) {
            return null; // don't know about this type
        }
        PropertyVariant elasticProperty = (PropertyVariant) first._get();

        while (!path.isEmpty()) {
            pathPart = path.removeFirst();
            if (elasticProperty instanceof ObjectProperty objectProperty) {
                elasticProperty = (PropertyVariant)
                        objectProperty.properties().get(pathPart)._get();
            } else if (elasticProperty instanceof TextProperty textProperty) {
                elasticProperty = TextProperty.of(tp -> tp);
                //              elasticProperty = (PropertyVariant)  textProperty.fields().get(pathPart);
            } else {
                throw new RuntimeException(
                        "Elastic Index Definition - couldn't find " + pathPart + " in " + elasticProperty);
            }
        }
        return elasticProperty;
    }

    /**
     * converts a value from the elastic index to a "correct" value.
     *
     * <p>handles single-value VERSUS LIST values
     *
     * @param elasticPath which elastic property?
     * @param value what is the value provided by elastic for this property
     * @return converted value
     */
    @SuppressWarnings("unchecked")
    public Object convert(String elasticPath, Object value) {
        if (value == null) {
            return null; // cannot convert
        }
        if (value instanceof Set set) {
            value = new ArrayList<>(set);
        }

        var typeInfo = this.finalElasticTypes.get(elasticPath);
        if (typeInfo == null) {
            return null;
        }

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
            return list.stream().map(x -> convertPrimativeObject(x, typeInfo)).toList();
        } else {
            return convertPrimativeObject(value, typeInfo);
        }
    }

    /**
     * low level primitive conversions (i.e. string -> date)
     *
     * @param value from elastic
     * @param typeInfo info about the wanted type
     * @return converted value.
     */
    @SuppressWarnings("unchecked")
    private Object convertPrimativeObject(Object value, ElasticTypeInfo typeInfo) {
        if (value == null) {
            return null;
        }
        // trivial conversions
        if (typeInfo.getType().equals(SimpleType.STRING) && value instanceof String) {
            return value; // no conversion
        }
        if (typeInfo.getType().equals(SimpleType.DATE) && value instanceof String) {
            return value; // no conversion - elastic and json represent date the same (underlying elastic index is json)
        }

        return Converters.convert(value, getJavaType(typeInfo.getType()));
    }
}
