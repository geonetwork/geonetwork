/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.application;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * This is a fairly generic way of getting a `.yml` file from a resource.
 *
 * <p>We use spring to find the URL of the "config/test-app.yml" in the resources.
 *
 * <p>NOTE: in the IDE, this is likely a `file://...`, in an actual deployment, this is likely a `jar://...`
 */
public class TrivialAppApplicationYmlLocator implements IConfigurationLocator {

    @Override
    public List<ApplicationYmlLocation> getApplicationYmls() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Resource[] resources = resolver.getResources("config/test-app.yml");

        var result = new ApplicationYmlLocation(resources[0].getURL(), 10.0);

        return List.of(result);
    }
}
