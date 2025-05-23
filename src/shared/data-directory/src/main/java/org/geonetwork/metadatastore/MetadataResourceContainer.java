/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
