/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.application;

public class ProxyApplicationYmlProvider extends AbstractResourceApplicationYmlProvider
        implements IConfigurationLocator {

    public ProxyApplicationYmlProvider() {}

    @Override
    public String getResourcePath() {
        return "application-proxy.yml";
    }

    @Override
    public Double getPriority() {
        return PRECONFIGURED_PRIORITY;
    }
}
