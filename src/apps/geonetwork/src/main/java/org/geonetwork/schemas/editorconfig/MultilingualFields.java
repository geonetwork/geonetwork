/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.editorconfig;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}expanded"/>
 *         &lt;element ref="{}exclude"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"expanded", "exclude"})
@XmlRootElement(name = "multilingualFields")
public class MultilingualFields {

  @XmlElement(required = true)
  protected Expanded expanded;

  @XmlElement(required = true)
  protected Exclude exclude;

  /**
   * Gets the value of the expanded property.
   *
   * @return possible object is {@link Expanded }
   */
  public Expanded getExpanded() {
    return expanded;
  }

  /**
   * Sets the value of the expanded property.
   *
   * @param value allowed object is {@link Expanded }
   */
  public void setExpanded(Expanded value) {
    this.expanded = value;
  }

  /**
   * Gets the value of the exclude property.
   *
   * @return possible object is {@link Exclude }
   */
  public Exclude getExclude() {
    return exclude;
  }

  /**
   * Sets the value of the exclude property.
   *
   * @param value allowed object is {@link Exclude }
   */
  public void setExclude(Exclude value) {
    this.exclude = value;
  }
}
