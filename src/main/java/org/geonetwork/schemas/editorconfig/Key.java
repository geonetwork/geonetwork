/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.editorconfig;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
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
 *       &lt;choice>
 *         &lt;element name="codelist" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="helper" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="context" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{}directiveAttributes" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="label" use="required" type="{http://www.w3.org/2001/XMLSchema}string"
 * />
 *       &lt;attribute name="xpath" use="required" type="{http://www.w3.org/2001/XMLSchema}string"
 * />
 *       &lt;attribute name="tooltip" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute ref="{}use"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"codelist", "helper", "directiveAttributes"})
@XmlRootElement(name = "key")
public class Key {

  protected Codelist codelist;
  protected Helper helper;
  protected DirectiveAttributes directiveAttributes;

  @XmlAttribute(name = "label", required = true)
  protected String label;

  @XmlAttribute(name = "xpath", required = true)
  protected String xpath;

  @XmlAttribute(name = "tooltip")
  protected String tooltip;

  @XmlAttribute(name = "use")
  protected String use;

  /**
   * Gets the value of the codelist property.
   *
   * @return possible object is {@link Codelist }
   */
  public Codelist getCodelist() {
    return codelist;
  }

  /**
   * Sets the value of the codelist property.
   *
   * @param value allowed object is {@link Codelist }
   */
  public void setCodelist(Codelist value) {
    this.codelist = value;
  }

  /**
   * Gets the value of the helper property.
   *
   * @return possible object is {@link Helper }
   */
  public Helper getHelper() {
    return helper;
  }

  /**
   * Sets the value of the helper property.
   *
   * @param value allowed object is {@link Helper }
   */
  public void setHelper(Helper value) {
    this.helper = value;
  }

  /**
   * Gets the value of the directiveAttributes property.
   *
   * @return possible object is {@link DirectiveAttributes }
   */
  public DirectiveAttributes getDirectiveAttributes() {
    return directiveAttributes;
  }

  /**
   * Sets the value of the directiveAttributes property.
   *
   * @param value allowed object is {@link DirectiveAttributes }
   */
  public void setDirectiveAttributes(DirectiveAttributes value) {
    this.directiveAttributes = value;
  }

  /**
   * Gets the value of the label property.
   *
   * @return possible object is {@link String }
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the value of the label property.
   *
   * @param value allowed object is {@link String }
   */
  public void setLabel(String value) {
    this.label = value;
  }

  /**
   * Gets the value of the xpath property.
   *
   * @return possible object is {@link String }
   */
  public String getXpath() {
    return xpath;
  }

  /**
   * Sets the value of the xpath property.
   *
   * @param value allowed object is {@link String }
   */
  public void setXpath(String value) {
    this.xpath = value;
  }

  /**
   * Gets the value of the tooltip property.
   *
   * @return possible object is {@link String }
   */
  public String getTooltip() {
    return tooltip;
  }

  /**
   * Sets the value of the tooltip property.
   *
   * @param value allowed object is {@link String }
   */
  public void setTooltip(String value) {
    this.tooltip = value;
  }

  /**
   * Gets the value of the use property.
   *
   * @return possible object is {@link String }
   */
  public String getUse() {
    return use;
  }

  /**
   * Sets the value of the use property.
   *
   * @param value allowed object is {@link String }
   */
  public void setUse(String value) {
    this.use = value;
  }

  /**
   * Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType>
   *   &lt;complexContent>
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
   *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "")
  public static class Codelist {

    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the name property.
     *
     * @return possible object is {@link String }
     */
    public String getName() {
      return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is {@link String }
     */
    public void setName(String value) {
      this.name = value;
    }
  }

  /**
   * Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType>
   *   &lt;complexContent>
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
   *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
   *       &lt;attribute name="context" type="{http://www.w3.org/2001/XMLSchema}string" />
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "")
  public static class Helper {

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "context")
    protected String context;

    /**
     * Gets the value of the name property.
     *
     * @return possible object is {@link String }
     */
    public String getName() {
      return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is {@link String }
     */
    public void setName(String value) {
      this.name = value;
    }

    /**
     * Gets the value of the context property.
     *
     * @return possible object is {@link String }
     */
    public String getContext() {
      return context;
    }

    /**
     * Sets the value of the context property.
     *
     * @param value allowed object is {@link String }
     */
    public void setContext(String value) {
      this.context = value;
    }
  }
}
