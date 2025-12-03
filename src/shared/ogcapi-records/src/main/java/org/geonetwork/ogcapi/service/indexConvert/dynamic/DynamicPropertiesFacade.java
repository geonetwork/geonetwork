/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert.dynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonPropertyDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonSchemaDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldMapperConfig;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldsMapperConfig;
import org.geonetwork.ogcapi.service.configuration.OgcFacetConfig;
import org.geonetwork.ogcapi.service.configuration.SimpleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This gives a higher level api to the Dynamic properties.
 *
 * <p>Most of the work is handed off to different classes.
 */
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

    /**
     * for aggregates/facets, what is the default number of buckets?
     *
     * @return default number of facet buckets
     */
    public int getDefaultFacetsBucketCount() {
        return config.getDefaultBucketCount();
    }

    /**
     * from the user config (application.yaml), get the facets configurations.
     *
     * <p>note - one field can have multiple facets associated with it
     *
     * @return facets configurations from the user config
     */
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

    /**
     * given an elastic property (dot separated), return the Type info for it.
     *
     * @param elasticProperty dot separated location of an elastic index property
     * @return info about that property (may return null)
     */
    public ElasticTypeInfo getByElasticProperty(String elasticProperty) {
        return elasticTypingSystem.getTypeInfoByElasticProperty(elasticProperty);
    }

    /**
     * given an ogc property (dot separated) from the application.yaml configuration, return the Type info for it.
     *
     * @param ogcProperty dot separated location of an ogc property
     * @return info about that property (may return null)
     */
    public ElasticTypeInfo getByOgcProperty(String ogcProperty) {
        return this.elasticTypingSystem.getTypeInfoByOgcProperty(ogcProperty);
    }

    /**
     * Returns the user config for a dynamic property: `#getByOgcProperty().getConfig()`
     *
     * @param ogcProperty dot separated location of the property
     * @return (may return null)
     */
    public OgcElasticFieldMapperConfig getUserConfigByOgcProperty(String ogcProperty) {
        var elasticInfo = getByOgcProperty(ogcProperty);
        if (elasticInfo == null) {
            return null;
        }
        return elasticInfo.getConfig();
    }

    /**
     * get type info of all the fields configured (and type info is available)
     *
     * @return list of all the fields configured
     */
    public List<ElasticTypeInfo> getAllFields() {
        return new ArrayList<>(this.elasticTypingSystem.getAllFieldInfos());
    }

    /**
     * get all the configured dynamic properties that are sortables (marked by #isSortable).
     *
     * @return all the configured dynamic properties that are sortables
     */
    public List<ElasticTypeInfo> getSortables() {
        return getAllFields().stream()
                .filter(e ->
                        e.getConfig().getIsSortable() != null && e.getConfig().getIsSortable())
                .toList();
    }

    /**
     * get all the configured dynamic properties that are queryable (marked by #isQueryable).
     *
     * @return all the configured dynamic properties that are queryable
     */
    public List<ElasticTypeInfo> getQueryables() {
        return getAllFields().stream()
                .filter(Objects::nonNull)
                .filter(e ->
                        e.getConfig().getIsQueryable() != null && e.getConfig().getIsQueryable())
                .toList();
    }

    /**
     * Given a `/queryables` endpoint object (OgcApiRecordsJsonSchemaDto) with the "standard" queryables already
     * present, add the dynamic ones to it.
     *
     * @param result to-be-updated OgcApiRecordsJsonSchemaDto (i.e. queryables)
     */
    public void addDynamicQueryablesSchema(OgcApiRecordsJsonSchemaDto result) {
        var queryableFields = this.getQueryables();
        for (var field : queryableFields) {
            addDynamicField(field, result);
        }
    }

    /**
     * given a defined queryable field, add it to the list of queryables.
     *
     * @param field info about one queryable field
     * @param result list of already existing fields (field will be added here)
     */
    private void addDynamicField(ElasticTypeInfo field, OgcApiRecordsJsonSchemaDto result) {
        var newQueryable = new OgcApiRecordsJsonPropertyDto();

        newQueryable.setTitle(field.getConfig().getTitle());
        newQueryable.setDescription(field.getConfig().getDescription());

        newQueryable.setType(SimpleType.getOgcTypeName(field.getType()));
        newQueryable.setFormat(SimpleType.getOgcTypeFormat(field.getType()));

        result.getProperties().put(field.getConfig().getOgcProperty(), newQueryable);
    }
}
