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

    /**
     * base priority for application.yml files. Recall that priority orders all the application.yml. The lower number
     * for Priority means that it is applied first. This means that higher-number priority will override lower-number
     * priority configuration (since later application.yml will "override" previous ones).
     *
     * <p>suggested priorities: <br>
     * 0 -> base application priority (everything overrides it) <br>
     * 1-10 -> pre-configured configuration (overrides the base application) <br>
     * 11+ -> user configuration (highest priority) <br>
     *
     * <p>Use more specific numbers to control what application.yml files override other configuration files.
     */
    public static double BASE_PRIORITY = 0.0;

    public static double PRECONFIGURED_PRIORITY = 10.0;
    public static double USER_PRIORITY = 20.0;

    public List<ApplicationYmlLocation> getApplicationYmls() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(getResourcePath());
        if (resources.length == 1) {
            var result = new ApplicationYmlLocation(resources[0].getURL(), getPriority());
            return List.of(result);
        }
        throw new Exception("Resource not found - " + getResourcePath());
    }

    public abstract String getResourcePath();

    public Double getPriority() {
        return PRECONFIGURED_PRIORITY;
    }
}
