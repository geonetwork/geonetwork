/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.application;

import java.util.List;

/**
 * This allows applications/modules to "plug-in" extra `application.yml`-like configuration files to the main
 * application.
 *
 * <p>Each module/application should have an implementation of this that points to `*.yml` file/resource for
 * configuration.
 *
 * <p>Use `ApplicationYmlLocation.priority` to control the order of the yamls. Yamls are applied in priority order
 * (lower priority (i.e. `0`) = first). Latter Yamls can override values in previous Yamls.
 *
 * <p>NOTES: <br>
 * 1. Implementations MUST BE in the "org.geonetwork.application" package (or nested inside this package). This makes
 * the scanner more efficient for finding implementations. <br>
 * 2. Set the priority for each of them to order how they are includes. 0=first priority <br>
 * 3. These classes need to have a zero-argument constructor<br>
 * 4. These classes will be created, and used, before spring is initialized. Thing like @Autowire and constructor
 * injection will not be available!<br>
 * 5. the location can point to any `.yml` file.
 */
public interface IConfigurationLocator {

    List<ApplicationYmlLocation> getApplicationYmls() throws Exception;
}
