/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork;

import java.util.ArrayList;
import java.util.Comparator;

import lombok.extern.slf4j.Slf4j;
import org.geonetwork.application.ApplicationYmlLocation;
import org.geonetwork.application.IConfigurationLocator;
import org.geonetwork.application.ResolveYmlConfigs;
import org.geonetwork.configuration.BaseApplication;
import org.reflections.Reflections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/** The main class of the application. */
@EnableCaching
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"org.geonetwork"})
@Slf4j
public class GeonetworkGenericApplication extends BaseApplication {
    public static void main(String[] args) throws Exception {

        setupYmlConfigurationFiles();
        log.warn("spring.config.location = " + System.getProperty("spring.config.location"));

        SpringApplication.run(GeonetworkGenericApplication.class, args);
    }

  private static void setupYmlConfigurationFiles() throws Exception {
      var resolver = new ResolveYmlConfigs();
      var locs = resolver.resolveYamlLocations();
      resolver.setResolveYamlLocations(locs);
  }
}
