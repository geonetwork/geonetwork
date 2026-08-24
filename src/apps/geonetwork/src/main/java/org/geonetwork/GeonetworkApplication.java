/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/** The main class of the application. */
@EnableCaching
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"org.geonetwork"})
@Slf4j
public class GeonetworkApplication extends GeonetworkGenericApplication {
    public static void main(String[] args) throws Exception {

        setupYmlConfigurationFiles();
        log.warn("spring.config.location = " + System.getProperty("spring.config.location"));

        SpringApplication.run(GeonetworkApplication.class, args);
    }
}
