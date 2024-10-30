/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.editorconfig;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

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
 *         &lt;element ref="{}key" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="readonlyIf" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"key"})
@XmlRootElement(name = "values")
public class Values {

    @XmlElement(required = true)
    protected List<Key> key;

    @XmlAttribute(name = "readonlyIf")
    @XmlSchemaType(name = "anySimpleType")
    protected String readonlyIf;

    /**
     * Gets the value of the key property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why
     * there is not a <CODE>set</CODE> method for the key property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     *    getKey().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link Key }
     */
    public List<Key> getKey() {
        if (key == null) {
            key = new ArrayList<Key>();
        }
        return this.key;
    }

    /**
     * Gets the value of the readonlyIf property.
     *
     * @return possible object is {@link String }
     */
    public String getReadonlyIf() {
        return readonlyIf;
    }

    /**
     * Sets the value of the readonlyIf property.
     *
     * @param value allowed object is {@link String }
     */
    public void setReadonlyIf(String value) {
        this.readonlyIf = value;
    }
}
