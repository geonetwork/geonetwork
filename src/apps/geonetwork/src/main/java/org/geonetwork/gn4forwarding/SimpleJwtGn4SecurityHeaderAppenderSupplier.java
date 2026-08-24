/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4forwarding;

import org.springframework.cloud.gateway.server.mvc.filter.SimpleFilterSupplier;
import org.springframework.context.annotation.Configuration;

/** supplier class for SimpleGn4SecurityHeaderAppender2Supplier See spring documentation */
@Configuration
public class SimpleJwtGn4SecurityHeaderAppenderSupplier extends SimpleFilterSupplier {

    public SimpleJwtGn4SecurityHeaderAppenderSupplier() {
        super(SimpleJwtGn4SecurityHeaderAppender.class);
    }
}
