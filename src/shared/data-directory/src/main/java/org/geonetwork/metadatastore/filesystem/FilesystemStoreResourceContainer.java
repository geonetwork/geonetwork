/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.metadatastore.filesystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import org.geonetwork.metadatastore.MetadataResourceContainer;
import org.geonetwork.metadatastore.MetadataResourceExternalManagementProperties;
import org.springframework.web.util.UriUtils;

public class FilesystemStoreResourceContainer implements MetadataResourceContainer {
    private final String url;
    private final int metadataId;
    private final String metadataUuid;
    private final String containerName;
    private final MetadataResourceExternalManagementProperties metadataResourceExternalManagementProperties;
    private final boolean approved;

    public FilesystemStoreResourceContainer(
            String metadataUuid,
            int metadataId,
            String containerName,
            String baseUrl,
            MetadataResourceExternalManagementProperties metadataResourceExternalManagementProperties,
            boolean approved) {
        this.metadataUuid = metadataUuid;
        this.metadataId = metadataId;
        this.approved = approved;
        this.containerName = containerName;
        this.url = baseUrl + getId();
        this.metadataResourceExternalManagementProperties = metadataResourceExternalManagementProperties;
    }

    public FilesystemStoreResourceContainer(
            String metadataUuid, int metadataId, String containerName, String baseUrl, boolean approved) {
        this(metadataUuid, metadataId, containerName, baseUrl, null, approved);
    }

    @Override
    public String getId() {
        return UriUtils.encodePath(metadataUuid, StandardCharsets.UTF_8) + "/attachments/";
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getContainterName() {
        return containerName;
    }

    @Override
    public boolean isApproved() {
        return approved;
    }

    @Override
    public int getMetadataId() {
        return metadataId;
    }

    @Override
    public String getMetadataUuid() {
        return metadataUuid;
    }

    @Override
    public MetadataResourceExternalManagementProperties getMetadataResourceExternalManagementProperties() {
        return metadataResourceExternalManagementProperties;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting FilesystemStoreResourceContainer to json", e);
        }
    }
}
