/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.iso19115_3;

import org.geonetwork.schemas.MetadataSchemaConfiguration;
import org.geonetwork.utility.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
    @PropertySource(
            value = "classpath:/schemas/iso19115-3.2018/configuration.yml",
            factory = YamlPropertySourceFactory.class)
})
@ConfigurationProperties("geonetwork.schemas.iso19115-3")
public class ISO19115_3Configuration extends MetadataSchemaConfiguration {}
