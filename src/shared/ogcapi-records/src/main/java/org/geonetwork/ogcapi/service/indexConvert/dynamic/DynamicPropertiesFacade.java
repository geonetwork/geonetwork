package org.geonetwork.ogcapi.service.indexConvert.dynamic;

import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldsMapperConfig;
import org.geonetwork.ogcapi.service.cql.OgcElasticFieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This gives a higher level api to the Dynamic properties.
 */
@Component
public class DynamicPropertiesFacade {

  @Autowired
  private OgcElasticFieldsMapperConfig config;

  @Autowired
  ElasticTypingSystem elasticTypingSystem;

  @Autowired
  ExtraElasticPropertiesService extraElasticPropertiesService;

  @Autowired
  OgcElasticFieldMapper ogcElasticFieldMapper;


  public List<ElasticTypeInfo> getAllFields() {
    return  new ArrayList<>(this.elasticTypingSystem.getFinalElasticTypes().values());
  }

  public List<ElasticTypeInfo> getSortables() {
    return this.elasticTypingSystem.getFinalElasticTypes()
      .values().stream()
      .filter(e -> e.getConfig().getIsSortable() != null && e.getConfig().getIsSortable())
      .toList();
  }



}
