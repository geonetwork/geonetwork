/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.metadatastore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import org.apache.commons.io.FilenameUtils;
import org.geonetwork.data.DataDirectory;
import org.geonetwork.domain.Metadata;
import org.geonetwork.metadata.IMetadataAccessManager;
import org.geonetwork.metadata.IMetadataManager;
import org.geonetwork.metadata.MetadataNotFoundException;
import org.springframework.web.multipart.MultipartFile;

// TODO: Add access manager support
public abstract class AbstractStore implements Store {
    protected static final String RESOURCE_MANAGEMENT_EXTERNAL_PROPERTIES_SEPARATOR = ":";
    protected static final String RESOURCE_MANAGEMENT_EXTERNAL_PROPERTIES_ESCAPED_SEPARATOR = "\\:";

    protected final IMetadataManager metadataManager;
    protected final IMetadataAccessManager metadataAccessManager;
    protected final DataDirectory dataDirectory;

    public AbstractStore(
            final IMetadataManager metadataManager,
            final IMetadataAccessManager metadataAccessManager,
            final DataDirectory dataDirectory) {
        this.metadataManager = metadataManager;
        this.metadataAccessManager = metadataAccessManager;
        this.dataDirectory = dataDirectory;
    }

    @Override
    public List<MetadataResource> getResources(String metadataUuid, Sort sort, String filter, Boolean approved)
            throws Exception {
        int metadataId = getAndCheckMetadataId(metadataUuid, approved);
        boolean canEdit = this.metadataAccessManager.canEdit(metadataId);

        List<MetadataResource> resourceList =
                new ArrayList<>(getResources(metadataUuid, MetadataResourceVisibility.PUBLIC, filter, approved));
        if (canEdit) {
            resourceList.addAll(getResources(metadataUuid, MetadataResourceVisibility.PRIVATE, filter, approved));
        }

        if (sort == Sort.name) {
            resourceList.sort(MetadataResourceVisibility.sortByFileName);
        }

        return resourceList;
    }

    @Override
    public final ResourceHolder getResource(String metadataUuid, String resourceId, Boolean approved) throws Exception {
        try {
            return getResource(metadataUuid, MetadataResourceVisibility.PUBLIC, resourceId, approved);
        } catch (ResourceNotFoundException ignored) {
            // Ignored
        }
        return getResource(metadataUuid, MetadataResourceVisibility.PRIVATE, resourceId, approved);
    }

    protected int getAndCheckMetadataId(String metadataUuid, Boolean approved) throws MetadataNotFoundException {
        final Metadata metadata = this.metadataManager.findMetadataByUuid(metadataUuid, approved);
        return metadata.getId();

        /*if (approved) {
            metadata = _appContext.getBean(MetadataRepository.class).findOneByUuid(metadataUuid);
        } else {
            metadata = _appContext.getBean(IMetadataUtils.class).findOneByUuid(metadataUuid);
        }*/

    }

    protected int canEdit(String metadataUuid, Boolean approved) throws Exception {
        return canEdit(metadataUuid, null, approved);
    }

    protected int canEdit(String metadataUuid, MetadataResourceVisibility visibility, Boolean approved)
            throws Exception {
        int metadataId = getAndCheckMetadataId(metadataUuid, approved);
        boolean canEdit = this.metadataAccessManager.canEdit(metadataId);

        if ((visibility == null && !canEdit) || (visibility == MetadataResourceVisibility.PRIVATE && !canEdit)) {
            throw new SecurityException(String.format(
                    "Current user does not have privileges to access '%s' resources for metadata '%s'.",
                    visibility == null ? "any" : visibility, metadataUuid));
        }
        return metadataId;
    }

    protected int canDownload(String metadataUuid, MetadataResourceVisibility visibility, Boolean approved)
            throws Exception {
        int metadataId = getAndCheckMetadataId(metadataUuid, approved);
        if (visibility == MetadataResourceVisibility.PRIVATE) {
            boolean canDownload = true; // getAccessManager(context).canDownload(String.valueOf(metadataId));
            if (!canDownload) {
                throw new SecurityException(String.format(
                        "Current user can't download resources for metadata '%s' and as such can't access the requested resource.",
                        metadataUuid));
            }
        }
        return metadataId;
    }

    protected String getFilenameFromUrl(final URL fileUrl) {
        String fileName = FilenameUtils.getName(fileUrl.getPath());
        if (fileName.contains("?")) {
            fileName = fileName.substring(0, fileName.indexOf("?"));
        }
        return fileName;
    }

    @Override
    public final MetadataResource putResource(
            final String metadataUuid,
            final MultipartFile file,
            final MetadataResourceVisibility visibility,
            Boolean approved)
            throws Exception {

        String fileName = file.getOriginalFilename();

        if ((fileName != null) && fileName.contains(";")) {
            throw new NotAllowedException(String.format(
                    "Uploaded resource '%s' contains forbidden character ; for metadata '%s'.",
                    file.getOriginalFilename(), metadataUuid));
        }
        return putResource(metadataUuid, file.getOriginalFilename(), file.getInputStream(), null, visibility, approved);
    }

    @Override
    public final MetadataResource putResource(
            final String metadataUuid, final Path file, final MetadataResourceVisibility visibility, Boolean approved)
            throws Exception {
        final InputStream is = new BufferedInputStream(Files.newInputStream(file));

        Path fileName = file.getFileName();

        // TODO: Check filename if file.getFileName() is null
        return putResource(metadataUuid, fileName != null ? fileName.toString() : "", is, null, visibility, approved);
    }

    @Override
    public final MetadataResource putResource(
            String metadataUuid, URL fileUrl, MetadataResourceVisibility visibility, Boolean approved)
            throws Exception {
        return putResource(metadataUuid, getFilenameFromUrl(fileUrl), fileUrl.openStream(), null, visibility, approved);
    }

    @Override
    public void copyResources(
            String sourceUuid,
            String targetUuid,
            MetadataResourceVisibility metadataResourceVisibility,
            boolean sourceApproved,
            boolean targetApproved)
            throws Exception {
        final List<MetadataResource> resources =
                getResources(sourceUuid, metadataResourceVisibility, null, sourceApproved);
        for (MetadataResource resource : resources) {
            try (Store.ResourceHolder holder =
                    getResource(sourceUuid, metadataResourceVisibility, resource.getFilename(), sourceApproved)) {
                putResource(targetUuid, holder.getPath(), metadataResourceVisibility, targetApproved);
            }
        }
    }

    protected String getFilename(final String metadataUuid, final String resourceId) {
        // It's not always clear when we get a resourceId or a filename
        String prefix = metadataUuid + "/attachments/";
        if (resourceId.startsWith(prefix)) {
            // It was a resourceId
            return resourceId.substring(prefix.length());
        } else {
            // It was a filename
            return resourceId;
        }
    }

    protected void checkResourceId(final String resourceId) {
        if (resourceId.contains("..") || resourceId.startsWith("/") || resourceId.startsWith("file:/")) {
            throw new SecurityException(String.format("Invalid resource identifier '%s'.", resourceId));
        }
    }

    @Override
    public ResourceManagementExternalProperties getResourceManagementExternalProperties() {
        return new ResourceManagementExternalProperties() {
            @Override
            public boolean isEnabled() {
                return false;
            }

            @Override
            public String getWindowParameters() {
                return null;
            }

            @Override
            public boolean isModal() {
                return false;
            }

            @Override
            public boolean isFolderEnabled() {
                return false;
            }

            @Override
            public String toString() {
                try {
                    return new ObjectMapper().writeValueAsString(this);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error converting ResourceManagementExternalProperties to json", e);
                }
            }
        };
    }

    private String escapeResourceManagementExternalProperties(String value) {
        return value.replace(
                RESOURCE_MANAGEMENT_EXTERNAL_PROPERTIES_SEPARATOR,
                RESOURCE_MANAGEMENT_EXTERNAL_PROPERTIES_ESCAPED_SEPARATOR);
    }

    /**
     * Create an encoded base 64 object id contains the following fields to uniquely identify the resource The fields
     * are separated by a colon ":"
     *
     * @param type to identify type of storage - document/folder
     * @param visibility of the resource public/private
     * @param metadataId internal metadata id
     * @param version identifier which can be used to directly get this version.
     * @param resourceId or filename of the resource
     * @return based 64 object id
     */
    protected String getResourceManagementExternalPropertiesObjectId(
            final String type,
            final MetadataResourceVisibility visibility,
            final Integer metadataId,
            final String version,
            final String resourceId) {
        return Base64.getEncoder()
                .encodeToString((type
                                + RESOURCE_MANAGEMENT_EXTERNAL_PROPERTIES_SEPARATOR
                                + escapeResourceManagementExternalProperties(
                                        visibility == null
                                                ? ""
                                                : visibility.toString().toLowerCase(Locale.ROOT))
                                + RESOURCE_MANAGEMENT_EXTERNAL_PROPERTIES_SEPARATOR
                                + metadataId
                                + RESOURCE_MANAGEMENT_EXTERNAL_PROPERTIES_SEPARATOR
                                + escapeResourceManagementExternalProperties(version == null ? "" : version)
                                + RESOURCE_MANAGEMENT_EXTERNAL_PROPERTIES_SEPARATOR
                                + escapeResourceManagementExternalProperties(resourceId))
                        .getBytes(Charset.defaultCharset()));
    }
}
