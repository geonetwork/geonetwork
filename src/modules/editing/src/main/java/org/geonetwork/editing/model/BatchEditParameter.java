/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.editing.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

@XmlRootElement(name = "edit")
public class BatchEditParameter implements Serializable {
  private String xpath;
  private String value;
  private String condition;

  public BatchEditParameter() {}

  public BatchEditParameter(String xpath, String value, String condition) {
    if (StringUtils.isEmpty(xpath)) {
      throw new IllegalArgumentException(
          "Parameter xpath is not set. It should be not empty and define the XPath of the element"
              + " to update.");
    }
    this.xpath = xpath;
    this.value = value;
    this.condition = condition;
  }

  public BatchEditParameter(String xpath, String value) {
    this(xpath, value, null);
  }

  @XmlElement(required = true)
  public String getXpath() {
    return xpath;
  }

  public void setXpath(String xpath) {
    this.xpath = xpath;
  }

  @XmlElement(required = true)
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @XmlElement(required = false)
  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Editing xpath ");
    sb.append(this.xpath);
    if (StringUtils.isNotEmpty(this.value)) {
      sb.append(", searching for ");
      sb.append(this.value);
    }
    sb.append(".");
    sb.append(" Check expression: ");
    sb.append(this.condition);
    return sb.toString();
  }
}
