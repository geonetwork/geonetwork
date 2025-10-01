/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert.dynamic;

import co.elastic.clients.elasticsearch._types.mapping.*;
import java.util.ArrayList;
import java.util.List;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGnElasticDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonPropertyDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonSchemaDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldMapperConfig;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldsMapperConfig;
import org.geonetwork.ogcapi.service.configuration.OgcFacetConfig;
import org.geonetwork.ogcapi.service.configuration.SimpleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** This gives a higher level api to the Dynamic properties. */
@Component
public class DynamicPropertiesFacade {

    @Autowired
    private OgcElasticFieldsMapperConfig config;

    @Autowired
    ElasticTypingSystem elasticTypingSystem;

    @Autowired
    ExtraElasticPropertiesService extraElasticPropertiesService;

    public void injectFacetsIntoResponse(
            IndexRecord indexRecord, String iso3lang, OgcApiRecordsRecordGeoJSONDto result) {
        extraElasticPropertiesService.inject(indexRecord, iso3lang, result);
    }

    public int getDefaultFacetsBucketCount() {
        return config.getDefaultBucketCount();
    }

    public List<OgcFacetConfig> getFacetConfigs() {
        var result = new ArrayList<OgcFacetConfig>();
        for (var field : config.getFields()) {
            if (field.getFacetsConfig() == null || field.getFacetsConfig().isEmpty()) {
                continue;
            }
            for (var facetConfig : field.getFacetsConfig()) {
                facetConfig.setField(field); // parent link
                result.add(facetConfig);
            }
        }

        return result;
    }

    public ElasticTypeInfo getByElasticProperty(String elasticProperty) {
        return elasticTypingSystem.getFinalElasticTypes().get(elasticProperty);
    }

    public ElasticTypeInfo getByOgcProperty(String ogcProperty) {
        return this.elasticTypingSystem.getFinalElasticTypes().get(ogcProperty);
    }

    public OgcElasticFieldMapperConfig getUserConfigByOgcProperty(String ogcProperty) {
        return this.elasticTypingSystem
                .getFinalElasticTypesByOgc()
                .get(ogcProperty)
                .getConfig();
    }

    public List<ElasticTypeInfo> getAllFields() {
        return new ArrayList<>(this.elasticTypingSystem.getFinalElasticTypes().values());
    }

    public List<ElasticTypeInfo> getSortables() {
        return this.elasticTypingSystem.getFinalElasticTypes().values().stream()
                .filter(e ->
                        e.getConfig().getIsSortable() != null && e.getConfig().getIsSortable())
                .toList();
    }

    public List<ElasticTypeInfo> getQueryables() {
        return this.elasticTypingSystem.getFinalElasticTypes().values().stream()
                .filter(e ->
                        e.getConfig().getIsQueryable() != null && e.getConfig().getIsQueryable())
                .toList();
    }

    public void addDynamicQueryables(OgcApiRecordsJsonSchemaDto result) {
        var queryableFields = this.getQueryables();
        for (var field : queryableFields) {
            addDynamicField(field, result);
        }
    }

    private void addDynamicField(ElasticTypeInfo field, OgcApiRecordsJsonSchemaDto result) {
        var newQueryable = new OgcApiRecordsJsonPropertyDto();

        newQueryable.setTitle(field.getConfig().getTitle());
        newQueryable.setDescription(field.getConfig().getDescription());

        newQueryable.setType(SimpleType.getOgcTypeName(field.getType()));
        newQueryable.setFormat(SimpleType.getOgcTypeFormat(field.getType()));
        //
        //        var elasticInfo = new OgcApiRecordsGnElasticDto();
        //        elasticInfo.setElasticPath(field.getConfig().elasticProperty);
        //        var elasticType = elasticTypingSystem.getRawElasticTypes().get(field.getConfig().elasticProperty);
        //        elasticInfo.setElasticColumnType(simplifyElasticRawType(elasticType));
        //
        //        newQueryable.addXGnElasticItem(elasticInfo);

        result.getProperties().put(field.getConfig().getOgcProperty(), newQueryable);
    }

    public OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum simplifyElasticRawType(PropertyVariant rawElasticType) {
        if (rawElasticType instanceof KeywordProperty) {
            return OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.KEYWORD;
        } else if (rawElasticType instanceof ShortNumberProperty
                || rawElasticType instanceof IntegerNumberProperty
                || rawElasticType instanceof LongNumberProperty) {
            return OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.NUMBER;
        } else if (rawElasticType instanceof FloatNumberProperty || rawElasticType instanceof DoubleNumberProperty) {
            return OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.NUMBER;
        } else if (rawElasticType instanceof TextProperty) {
            return OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.TEXT;
        } else if (rawElasticType instanceof BooleanProperty) {
            return OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.BOOLEAN;
        } else if (rawElasticType instanceof DateProperty) {
            return OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.DATE;
        }
        throw new RuntimeException("Unsupported Elastic type " + rawElasticType);
    }
}
