/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.metadatastore;

public interface MetadataResourceContainer {

    String getId();

    String getUrl();

    String getContainterName();

    boolean isApproved();

    int getMetadataId();

    String getMetadataUuid();

    MetadataResourceExternalManagementProperties getMetadataResourceExternalManagementProperties();
}
