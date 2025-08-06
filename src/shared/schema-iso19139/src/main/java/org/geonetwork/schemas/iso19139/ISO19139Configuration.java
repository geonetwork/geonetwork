/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.iso19139;

import org.geonetwork.schemas.SchemaPluginConfiguration;
import org.geonetwork.utility.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:/schemas/iso19139/configuration.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties("geonetwork.schemas.iso19139")
public class ISO19139Configuration extends SchemaPluginConfiguration {}
