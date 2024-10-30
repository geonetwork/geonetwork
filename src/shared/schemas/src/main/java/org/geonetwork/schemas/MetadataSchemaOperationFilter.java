/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas;

import org.jdom.Element;

public class MetadataSchemaOperationFilter {
    private String xpath;
    private String jsonpath;
    private String ifNotOperation;
    private Element markedElement;

    public MetadataSchemaOperationFilter(String xpath, String jsonpath, String ifNotOperation) {
        this(xpath, jsonpath, ifNotOperation, null);
    }

    public MetadataSchemaOperationFilter(String xpath, String jsonpath, String ifNotOperation, Element markedElement) {
        this.xpath = xpath;
        this.jsonpath = jsonpath;
        this.ifNotOperation = ifNotOperation;
        this.markedElement = markedElement;
    }

    public String getXpath() {
        return xpath;
    }

    public String getJsonpath() {
        return jsonpath;
    }

    public String getIfNotOperation() {
        return ifNotOperation;
    }

    public Element getMarkedElement() {
        return markedElement;
    }
}
