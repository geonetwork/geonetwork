/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.metadatastore.filesystem;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.geonetwork.metadatastore.MetadataResource;
import org.geonetwork.metadatastore.MetadataResourceExternalManagementProperties;
import org.geonetwork.metadatastore.MetadataResourceVisibility;
import org.springframework.web.util.UriUtils;

/** Metadata resource stored in the file system. */
public class FilesystemStoreResource implements MetadataResource {
    private final String url;
    private final MetadataResourceVisibility metadataResourceVisibility;
    private final long size;
    private final Date lastModification;
    private final int metadataId;
    private final String metadataUuid;
    private final String filename;
    private final String version;
    private final MetadataResourceExternalManagementProperties metadataResourceExternalManagementProperties;
    private final boolean approved;

    public FilesystemStoreResource(
            String metadataUuid,
            int metadataId,
            String filename,
            String baseUrl,
            MetadataResourceVisibility metadataResourceVisibility,
            long size,
            Date lastModification,
            String version,
            MetadataResourceExternalManagementProperties metadataResourceExternalManagementProperties,
            boolean approved) {
        this.metadataUuid = metadataUuid;
        this.metadataId = metadataId;
        this.approved = approved;
        this.filename = filename;
        this.url = baseUrl + getId();
        this.metadataResourceVisibility = metadataResourceVisibility;
        this.size = size;
        this.lastModification = lastModification;
        this.version = version;
        this.metadataResourceExternalManagementProperties = metadataResourceExternalManagementProperties;
    }

    public FilesystemStoreResource(
            String metadataUuid,
            int metadataId,
            String filename,
            String baseUrl,
            MetadataResourceVisibility metadataResourceVisibility,
            long size,
            Date lastModification,
            boolean approved) {
        this(
                metadataUuid,
                metadataId,
                filename,
                baseUrl,
                metadataResourceVisibility,
                size,
                lastModification,
                null,
                null,
                approved);
    }

    @Override
    public String getId() {
        return UriUtils.encodePath(metadataUuid, StandardCharsets.UTF_8) + "/attachments/"
                + UriUtils.encodePath(filename, StandardCharsets.UTF_8);
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public MetadataResourceVisibility getVisibility() {
        return metadataResourceVisibility;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public Date getLastModification() {
        return Date.from(lastModification.toInstant());
    }

    @Override
    public String getFilename() {
        return filename;
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
    public String getVersion() {
        return version;
    }

    @Override
    public MetadataResourceExternalManagementProperties getMetadataResourceExternalManagementProperties() {
        return metadataResourceExternalManagementProperties;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append("\n");
        sb.append("Metadata: ").append(metadataUuid).append("\n");
        sb.append("Filename: ").append(filename).append("\n");
        sb.append("URL: ").append(url).append("\n");
        sb.append("Type: ").append(metadataResourceVisibility).append("\n");
        sb.append("Size: ").append(size).append("\n");
        sb.append("Last modification: ").append(lastModification).append("\n");
        sb.append("Approved: ").append(approved).append("\n");
        sb.append("Version: ").append(version).append("\n");
        sb.append("metadataResourceExternalManagementProperties.url: ")
                .append(
                        (metadataResourceExternalManagementProperties == null
                                ? ""
                                : metadataResourceExternalManagementProperties.getUrl()))
                .append("\n");
        return sb.toString();
    }
}
