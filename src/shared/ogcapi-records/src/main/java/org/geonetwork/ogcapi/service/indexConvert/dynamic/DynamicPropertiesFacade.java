/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert.dynamic;

import java.util.ArrayList;
import java.util.List;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldMapperConfig;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldsMapperConfig;
import org.geonetwork.ogcapi.service.configuration.OgcFacetConfig;
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
}
