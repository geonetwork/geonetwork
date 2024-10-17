/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/** The main class of the application for testing. */
@SpringBootApplication(scanBasePackages = {"org.geonetwork"})
public class GeonetworkTestingApplication implements ApplicationContextInitializer {

  public static void main(String[] args) {
    SpringApplication.run(GeonetworkTestingApplication.class, args);
  }

  @Override
  public void initialize(ConfigurableApplicationContext ctx) {
    TestPropertyValues.of(
            "spring.datasource.url=jdbc:h2:mem:testdb",
            "spring.datasource.username=geonetwork",
            "spring.datasource.password=geonetwork",
            "spring.datasource.driverClassName=org.h2.Driver",
            "spring.jpa. database-platform=org.hibernate.dialect.PostgreSQLDialect")
        .applyTo(ctx.getEnvironment());
  }
}
