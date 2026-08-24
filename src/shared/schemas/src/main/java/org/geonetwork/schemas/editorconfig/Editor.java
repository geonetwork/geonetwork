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
 *         &lt;element ref="{}fields" minOccurs="0"/>
 *         &lt;element ref="{}fieldsWithFieldset" minOccurs="0"/>
 *         &lt;element ref="{}multilingualFields" minOccurs="0"/>
 *         &lt;element ref="{}views"/>
 *         &lt;element ref="{}batchEditing" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"fields", "fieldsWithFieldset", "multilingualFields", "views", "batchEditing"})
@XmlRootElement(name = "editor")
public class Editor {

    protected Fields fields;
    protected FieldsWithFieldset fieldsWithFieldset;
    protected MultilingualFields multilingualFields;

    @XmlElement(required = true)
    protected Views views;

    protected BatchEditing batchEditing;

    /**
     * Gets the value of the fields property.
     *
     * @return possible object is {@link Fields }
     */
    public Fields getFields() {
        return fields;
    }

    /**
     * Sets the value of the fields property.
     *
     * @param value allowed object is {@link Fields }
     */
    public void setFields(Fields value) {
        this.fields = value;
    }

    /**
     * Gets the value of the fieldsWithFieldset property.
     *
     * @return possible object is {@link FieldsWithFieldset }
     */
    public FieldsWithFieldset getFieldsWithFieldset() {
        return fieldsWithFieldset;
    }

    /**
     * Sets the value of the fieldsWithFieldset property.
     *
     * @param value allowed object is {@link FieldsWithFieldset }
     */
    public void setFieldsWithFieldset(FieldsWithFieldset value) {
        this.fieldsWithFieldset = value;
    }

    /**
     * Gets the value of the multilingualFields property.
     *
     * @return possible object is {@link MultilingualFields }
     */
    public MultilingualFields getMultilingualFields() {
        return multilingualFields;
    }

    /**
     * Sets the value of the multilingualFields property.
     *
     * @param value allowed object is {@link MultilingualFields }
     */
    public void setMultilingualFields(MultilingualFields value) {
        this.multilingualFields = value;
    }

    /**
     * Gets the value of the views property.
     *
     * @return possible object is {@link Views }
     */
    public Views getViews() {
        return views;
    }

    /**
     * Sets the value of the views property.
     *
     * @param value allowed object is {@link Views }
     */
    public void setViews(Views value) {
        this.views = value;
    }

    /**
     * Gets the value of the batchEditing property.
     *
     * @return possible object is {@link BatchEditing }
     */
    public BatchEditing getBatchEditing() {
        return batchEditing;
    }

    /**
     * Sets the value of the batchEditing property.
     *
     * @param value allowed object is {@link BatchEditing }
     */
    public void setBatchEditing(BatchEditing value) {
        this.batchEditing = value;
    }
}
