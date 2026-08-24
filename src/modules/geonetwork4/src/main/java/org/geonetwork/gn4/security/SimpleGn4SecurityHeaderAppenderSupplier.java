/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.gn4.security;

import org.springframework.cloud.gateway.server.mvc.filter.SimpleFilterSupplier;
import org.springframework.context.annotation.Configuration;

/** supplier class for SimpleGn4SecurityHeaderAppender2Supplier See spring documentation */
@Configuration
public class SimpleGn4SecurityHeaderAppenderSupplier extends SimpleFilterSupplier {

    public SimpleGn4SecurityHeaderAppenderSupplier() {
        super(SimpleGn4SecurityHeaderAppender.class);
    }
}
