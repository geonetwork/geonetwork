/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.schemas;

import org.geonetwork.schemas.model.schemaident.Filter;

public class MetadataSchemaOperationFilter {
    private String xpath;
    private String jsonpath;
    private String ifNotOperation;
    private Filter.KeepMarkedElement markedElement;

    public MetadataSchemaOperationFilter(String xpath, String jsonpath, String ifNotOperation) {
        this(xpath, jsonpath, ifNotOperation, null);
    }

    public MetadataSchemaOperationFilter(
            String xpath, String jsonpath, String ifNotOperation, Filter.KeepMarkedElement markedElement) {
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

    public Filter.KeepMarkedElement getMarkedElement() {
        return markedElement;
    }
}
