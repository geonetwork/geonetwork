/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.application;

import java.io.File;
import java.net.URI;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/** see GenericGN5ApplicationYmlLocator */
public class OgcApiRecordsApplicationYmlLocator implements IConfigurationLocator {

    /**
     * see GenericGN5ApplicationYmlLocator
     *
     * @return location of application-ogcapi-records.yml
     * @throws Exception config? missing file?
     */
    @Override
    public List<ApplicationYmlLocation> getApplicationYmls() throws Exception {

        if (new File("./application-ogcapi-records.yml").exists()) {
            var url = new URI("file:./application-ogcapi-records.yml").toURL();
            return List.of(new ApplicationYmlLocation(url, 10.0)); // 0.0 = FIRST
        }

        if (new File("./config/application-ogcapi-records.yml").exists()) {
            var url = new URI("file:./config/application-ogcapi-records.yml").toURL();
            return List.of(new ApplicationYmlLocation(url, 10.0)); // 0.0 = FIRST
        }

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("application-ogcapi-records.yml");
        if (resources.length == 1) {
            var result = new ApplicationYmlLocation(resources[0].getURL(), 10.0); // 0.0 = FIRST
            return List.of(result);
        }

        resources = resolver.getResources("config/application-ogcapi-records.yml");
        if (resources.length == 1) {
            var result = new ApplicationYmlLocation(resources[0].getURL(), 20.0); // 0.0 = FIRST
            return List.of(result);
        }

        throw new Exception("cannnot find the  application-ogcapi-records.yml - looked in:\n"
                + " file system: ./application-ogcapi-records.yml\n"
                + " file system: ./config/application-ogcapi-records.yml\n"
                + " resource: application-ogcapi-records.yml\n"
                + " resource: config/application-ogcapi-records.yml");
    }
}
