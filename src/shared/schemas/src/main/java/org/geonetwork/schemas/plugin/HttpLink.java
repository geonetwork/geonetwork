/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.plugin;

public class HttpLink {
  private String protocol;
  private String xpath;

  public void setXpath(String xpath) {
    this.xpath = xpath;
  }

  public String getXpath() {
    return xpath;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getProtocol() {
    return protocol;
  }
}
