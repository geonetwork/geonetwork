/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
