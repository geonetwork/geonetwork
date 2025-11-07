/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** simple configuration for links in ogcapi-records = /collections/{collectionid} */
@Configuration
@ConfigurationProperties(prefix = "geonetwork.openapi-records.links.collection")
public class CollectionPageLinksConfiguration extends BasicLinksConfiguration {}
