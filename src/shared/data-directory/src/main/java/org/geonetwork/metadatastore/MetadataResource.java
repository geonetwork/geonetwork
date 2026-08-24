/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
