/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.configuration;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** simple configuration for links in ogcapi-records = /collections/{collectionId}/items/{itemid} */
@Configuration
@ConfigurationProperties(prefix = "geonetwork.openapi-records.links.item")
@Getter
@Setter
public class ItemPageLinksConfiguration {
    List<ProfileDefault> profileDefaults = new ArrayList<>();
}
