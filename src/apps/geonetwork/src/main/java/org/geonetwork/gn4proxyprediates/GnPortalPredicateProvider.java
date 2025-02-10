/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4proxyprediates;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.cloud.gateway.server.mvc.predicate.PredicateSupplier;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GnPortalPredicateProvider implements PredicateSupplier {

    @Override
    public Collection<Method> get() {
        return Arrays.asList(GnPortalPredicate.class.getMethods());
    }
}
