/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.application;

import java.io.File;
import java.net.URL;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * This is the "main" application.yml (that defines the DB, etc...). It's an actual file (not a resource) and comes from
 * the "/config/application.yml". The build system copies this over. See `/src/config/application.yml`
 */
public class GenericGN5ApplicationYmlLocator implements IConfigurationLocator {

    /**
     * The location of the "main" (from geonetwork/config/application.yml) config file is different depending on how the
     * application is being run.
     *
     * <p>Typically, from an IDE, its a real file. From something like a docker container, its in the main .jar file
     * (resource).
     *
     * <p>This will look for the file and should work in most cases.
     *
     * <p>See spring doc on where spring looks for the application.yml file.
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<ApplicationYmlLocation> getApplicationYmls() throws Exception {

        if (new File("./application.yml").exists()) {
            var url = new URL("file:./application.yml");
            return List.of(new ApplicationYmlLocation(url, 0.0)); // 0.0 = FIRST
        }

        if (new File("./config/application.yml").exists()) {
            var url = new URL("file:./config/application.yml");
            return List.of(new ApplicationYmlLocation(url, 0.0)); // 0.0 = FIRST
        }

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("application.yml");
        if (resources.length == 1) {
            var result = new ApplicationYmlLocation(resources[0].getURL(), 0.0); // 0.0 = FIRST
            return List.of(result);
        }

        resources = resolver.getResources("config/application.yml");
        if (resources.length == 1) {
            var result = new ApplicationYmlLocation(resources[0].getURL(), 0.0); // 0.0 = FIRST
            return List.of(result);
        }

        throw new Exception("cannnot find the main application.yml - looked in:\n" + " file system: ./application.yml\n"
                + " file system: ./config/application.yml\n"
                + " resource: application.yml\n"
                + " resource: config/application.yml");
    }
}
