/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/** The main class of the application. */
@EnableCaching
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"org.geonetwork"})
public class GeonetworkApplication extends GeonetworkGenericApplication {}
