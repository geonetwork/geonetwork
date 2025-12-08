/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
