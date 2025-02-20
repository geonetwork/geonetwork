/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
