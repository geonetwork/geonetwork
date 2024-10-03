/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/** The main class of the application. */
@SpringBootApplication
@EnableCaching
public class GeonetworkApplication {

  public static void main(String[] args) {
    SpringApplication.run(GeonetworkApplication.class, args);
  }
}
