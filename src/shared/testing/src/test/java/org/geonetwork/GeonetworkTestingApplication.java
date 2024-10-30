/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** The main class of the application for testing. */
@SpringBootApplication(scanBasePackages = {"org.geonetwork"})
public class GeonetworkTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeonetworkTestingApplication.class, args);
    }
}
