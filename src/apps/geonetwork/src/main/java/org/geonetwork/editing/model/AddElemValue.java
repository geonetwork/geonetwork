/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.editing.model;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * A simple container class for some add methods in {@link EditLib} Created by Jesse on 12/10/13.
 */
@Slf4j(topic = "org.geonetwork.editing")
public class AddElemValue {
  private final String stringValue;
  private final Element nodeValue;

  public AddElemValue(String stringValue) throws JDOMException, IOException {
    Element finalNodeVal = null;
    String finalStringVal = stringValue.replaceAll("</?gn_(add|replace)>", "");
    if (Xml.isXMLLike(finalStringVal)) {
      try {
        finalNodeVal = Xml.loadString(stringValue, false);
        finalStringVal = null;
      } catch (JDOMException e) {
        log.debug(
            "Invalid XML fragment to insert " + stringValue + ". Error is: " + e.getMessage());
        throw e;
      } catch (IOException e) {
        log.error(
            "Error with XML fragment to insert " + stringValue + ". Error is: " + e.getMessage());
        throw e;
      }
    }
    this.nodeValue = finalNodeVal;
    this.stringValue = finalStringVal;
  }

  public AddElemValue(Element nodeValue) {
    this.nodeValue = nodeValue;
    this.stringValue = null;
  }

  public boolean isXml() {
    return nodeValue != null;
  }

  public String getStringValue() {
    return stringValue;
  }

  public Element getNodeValue() {
    return nodeValue;
  }
}
