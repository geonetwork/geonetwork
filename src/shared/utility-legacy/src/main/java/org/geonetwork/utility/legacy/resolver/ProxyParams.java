/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.utility.legacy.resolver;

public class ProxyParams {
  String username, password, proxyHost;
  int proxyPort;
  boolean useProxy = false;
  boolean useProxyAuth = false;
}
