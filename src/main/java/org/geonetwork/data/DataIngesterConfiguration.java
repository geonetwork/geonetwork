/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties("gn.data-ingester")
@PropertySource("classpath:gn.data-ingester.yml")
@Data
public class DataIngesterConfiguration {
  List<Resource> resources;

  private class Resource {
    private enum Type {
      dataset,
      service
    }

    Type type;
    List<Property> properties;

    private class Property {
      private enum Context {
        DatasetLayer,
        DatasetInfo
      }

      String name;
      Context context;
    }
  }
}
