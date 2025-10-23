/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

/**
 * This will:
 *
 * <p>1. scan for IConfigurationLocator implementation (must be in, or below, package `org.geonetwork.application`).
 * <br>
 * 2. use the zero-arg constructor to create the object. <br>
 * 3. Get the configured Application.yml locations. <br>
 * 4. sort them by priority (0=FIRST). <br>
 *
 * <p>It can also use the above information to set spring's spring.config.location config to the application.yml
 * locations.
 *
 * <p>NOTE: the location can point to any `.yml` file.
 */
@Component
public class ResolveYmlConfigs {

    /**
     * Scan through package (and below) "org.geonetwork.application" for implementations of IConfigurationLocator. These
     * are then constructed (zero-arg constructor) and the defined locations (#getApplicationYmls()) are retrieved.
     * These are then sorted by priorty (0 = FIRST).
     *
     * @return sorted list of Yaml locations
     * @throws Exception bad configuration
     */
    public List<ApplicationYmlLocation> resolveYamlLocations() throws Exception {
        Reflections reflections = new Reflections("org.geonetwork.application");
        var implementations = reflections.getSubTypesOf(IConfigurationLocator.class);

        var locations = new ArrayList<ApplicationYmlLocation>();
        for (var implClass : implementations) {
            IConfigurationLocator instance = implClass.getDeclaredConstructor().newInstance();
            locations.addAll(instance.getApplicationYmls());
        }
        locations.sort(Comparator.comparing(ApplicationYmlLocation::getPriority));
        return locations;
    }

    /**
     * Give a sorted list of `application.yml` locations, set the "spring.config.location" to these locations.
     *
     * @param locations see #resolveYamlLocations()
     */
    public void setResolveYamlLocations(List<ApplicationYmlLocation> locations) {
        var spring_config_locations =
                locations.stream().map(x -> x.getUrl().toString()).toList();
        var spring_config_location = String.join(",", spring_config_locations);
        System.setProperty("spring.config.location", spring_config_location);
    }
}
