/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.schemas.plugin;

public class AssociatedResource {
    private String uuid;
    private String title;
    private String url;
    private String initiativeType;
    private String associationType;

    public AssociatedResource(String uuid, String initiativeType, String associationType) {
        this.uuid = uuid;
        this.initiativeType = initiativeType;
        this.associationType = associationType;
    }

    public AssociatedResource(String uuid, String initiativeType, String associationType, String url, String title) {
        this.uuid = uuid;
        this.url = url;
        this.title = title;
        this.initiativeType = initiativeType;
        this.associationType = associationType;
    }

    public String getUuid() {
        return uuid;
    }

    public AssociatedResource setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getInitiativeType() {
        if (initiativeType == null) {
            return "";
        } else {
            return initiativeType;
        }
    }

    public AssociatedResource setInitiativeType(String initiativeType) {
        this.initiativeType = initiativeType;
        return this;
    }

    public String getAssociationType() {
        if (associationType == null) {
            return "";
        } else {
            return associationType;
        }
    }

    public AssociatedResource setAssociationType(String associationType) {
        this.associationType = associationType;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
