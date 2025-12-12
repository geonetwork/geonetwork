/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.gn4proxypredicates;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.cloud.gateway.server.mvc.predicate.PredicateSupplier;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeoNetwork4PredicateProvider implements PredicateSupplier {

    @Override
    public Collection<Method> get() {
        return Arrays.asList(GeoNetwork4Predicate.class.getMethods());
    }
}
