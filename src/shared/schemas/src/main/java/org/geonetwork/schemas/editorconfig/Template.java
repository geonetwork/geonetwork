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
 *         &lt;element ref="{}values" minOccurs="0"/>
 *         &lt;element ref="{}snippet"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"values", "snippet"})
@XmlRootElement(name = "template")
public class Template {

    protected Values values;

    @XmlElement(required = true)
    protected Snippet snippet;

    /**
     * The list of values to match from the template.
     *
     * @return possible object is {@link Values }
     */
    public Values getValues() {
        return values;
    }

    /**
     * Sets the value of the values property.
     *
     * @param value allowed object is {@link Values }
     */
    public void setValues(Values value) {
        this.values = value;
    }

    /**
     * Gets the value of the snippet property.
     *
     * @return possible object is {@link Snippet }
     */
    public Snippet getSnippet() {
        return snippet;
    }

    /**
     * Sets the value of the snippet property.
     *
     * @param value allowed object is {@link Snippet }
     */
    public void setSnippet(Snippet value) {
        this.snippet = value;
    }
}
