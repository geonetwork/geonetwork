/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** simple configuration for links in ogcapi-records. */
@Configuration
@ConfigurationProperties(prefix = "geonetwork.openapi-records.links")
@Getter
@Setter
public class OgcApiLinkConfiguration {

    /** base url to OgcApiRecords - i.e. http://localhost:7979/ogcapi-records */
    private String ogcApiRecordsBaseUrl;

    /** base URL for geonetwork */
    private String gnBaseUrl;
}
