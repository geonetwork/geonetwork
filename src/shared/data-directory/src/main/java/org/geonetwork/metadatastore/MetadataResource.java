/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.metadatastore;

import java.util.Date;

public interface MetadataResource {

    String getId();

    String getUrl();

    MetadataResourceVisibility getVisibility();

    long getSize();

    Date getLastModification();

    String getFilename();

    boolean isApproved();

    int getMetadataId();

    String getMetadataUuid();

    String getVersion();

    MetadataResourceExternalManagementProperties getMetadataResourceExternalManagementProperties();
}
