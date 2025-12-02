/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.application;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public abstract class AbstractResourceApplicationYmlProvider {

    public List<ApplicationYmlLocation> getApplicationYmls() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(getResourcePath());
        if (resources.length == 1) {
            var result = new ApplicationYmlLocation(resources[0].getURL(), 0.0); // 0.0 = FIRST
            return List.of(result);
        }
        throw new Exception("Resource not found - " + getResourcePath());
    }

    public abstract String getResourcePath();
}
