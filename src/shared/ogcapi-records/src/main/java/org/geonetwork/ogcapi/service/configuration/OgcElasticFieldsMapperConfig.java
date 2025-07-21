package org.geonetwork.ogcapi.service.configuration;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "geonetwork.openapi-records.ogcapi-property-mapping")
@Getter
@Setter
public class OgcElasticFieldsMapperConfig {

  public List<OgcElasticFieldMapperConfig> fields = new ArrayList<>();

  public OgcElasticFieldMapperConfig findByOgc(String ogcFieldName) {
    for (OgcElasticFieldMapperConfig field : fields) {
      if (field.ogcProperty.equals(ogcFieldName)) {
        return field;
      }
    }
    return null;
  }

  @Getter
  @Setter
  public static class OgcElasticFieldMapperConfig {
      public String ogcProperty;
      public String elasticProperty;
  }
}
