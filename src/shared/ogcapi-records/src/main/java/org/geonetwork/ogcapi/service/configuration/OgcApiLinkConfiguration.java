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

    /**
     * base url for the ogcapi records service. Should end in "/" should NOT have "//" in it.
     *
     * @param ogcApiRecordsBaseUrl url to ogcapi-records in gn5
     */
    public void setOgcApiRecordsBaseUrl(String ogcApiRecordsBaseUrl) {
        if (ogcApiRecordsBaseUrl == null || ogcApiRecordsBaseUrl.isEmpty()) {
            return;
        }
        if (!ogcApiRecordsBaseUrl.endsWith("/")) {
            ogcApiRecordsBaseUrl += "/";
        }
        ogcApiRecordsBaseUrl = ogcApiRecordsBaseUrl.replace("//", "/");
        this.ogcApiRecordsBaseUrl = ogcApiRecordsBaseUrl;
    }

    /**
     * override lombok to ensure that the url ends in "/"
     *
     * @param gnBaseUrl where GN is (for building links)
     */
    public void setGnBaseUrl(String gnBaseUrl) {
        if (gnBaseUrl == null || gnBaseUrl.isEmpty()) {
            return;
        }
        if (!gnBaseUrl.endsWith("/")) {
            gnBaseUrl += "/";
        }
        gnBaseUrl = gnBaseUrl.replace("//", "/");

        this.gnBaseUrl = gnBaseUrl;
    }
}
