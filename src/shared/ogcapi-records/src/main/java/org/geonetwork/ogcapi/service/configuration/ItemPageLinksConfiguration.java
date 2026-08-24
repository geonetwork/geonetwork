/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
