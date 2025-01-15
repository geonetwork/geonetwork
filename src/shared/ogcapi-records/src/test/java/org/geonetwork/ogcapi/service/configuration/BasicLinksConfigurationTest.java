/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;

public class BasicLinksConfigurationTest {

    @Test
    public void testBasicLinksConfiguration() {
        BasicLinksConfiguration config = new BasicLinksConfiguration();
        ContentNegotiationManager manager = configureContentNegotiation();
        config.contentNegotiationManager = manager;

        config.setFormats(Arrays.asList("json", "xml", "geojson"));

        assertEquals(3, config.getMimeFormats().size());
        assertEquals(MediaType.APPLICATION_JSON, config.getMimeFormats().get("json"));
        assertEquals(MediaType.APPLICATION_XML, config.getMimeFormats().get("xml"));
        assertEquals(
                MediaType.valueOf("application/geo+json"),
                config.getMimeFormats().get("geojson"));
    }

    public static ContentNegotiationManager configureContentNegotiation() {
        var configurer = new ContentNegotiationConfigurer(null) {
            public ContentNegotiationManager build() {
                return this.buildContentNegotiationManager();
            }
        };

        configurer
                // .favorPathExtension(false)
                .favorParameter(true)
                .parameterName("f")
                .ignoreAcceptHeader(true)

                //      defaultContentType(MediaType.APPLICATION_JSON).
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("html", MediaType.TEXT_HTML)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("geojson", MediaType.valueOf("application/geo+json"))
                .defaultContentType(MediaType.TEXT_HTML);

        return configurer.build();
    }
}
