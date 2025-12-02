package org.geonetwork.application;

public class ProxyApplicationYmlProvider extends AbstractResourceApplicationYmlProvider
        implements IConfigurationLocator {

    public ProxyApplicationYmlProvider() {}

    @Override
    public String getResourcePath() {
        return "application-proxy.yml";
    }
}
