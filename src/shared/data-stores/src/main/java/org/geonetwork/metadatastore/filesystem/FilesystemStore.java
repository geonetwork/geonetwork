/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.metadatastore.filesystem;

import jakarta.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.data.DataDirectory;
import org.geonetwork.metadata.IMetadataAccessManager;
import org.geonetwork.metadata.IMetadataManager;
import org.geonetwork.metadata.config.MetadataDirConfig;
import org.geonetwork.metadatastore.AbstractStore;
import org.geonetwork.metadatastore.MetadataResource;
import org.geonetwork.metadatastore.MetadataResourceContainer;
import org.geonetwork.metadatastore.MetadataResourceVisibility;
import org.geonetwork.metadatastore.ResourceAlreadyExistException;
import org.geonetwork.metadatastore.ResourceNotFoundException;
import org.geonetwork.utility.legacy.io.IO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * A FileSystemStore store resources files in the catalog data directory. Each metadata record as a directory in the
 * data directory containing a public and a private folder.
 *
 * <pre>
 *     datadir
 *      |-{{sequence_folder}}
 *      |    |-{{metadata_id}}
 *      |    |    |-private
 *      |    |    |-public
 *      |    |        |--doc.pdf
 * </pre>
 */
@Component
@Slf4j
public class FilesystemStore extends AbstractStore {
    public static final String DEFAULT_FILTER = "*";

    // TODO: Use settingManager.getNodeURL()?
    @Value("${geonetwork.url}")
    private String baseUrl;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    public FilesystemStore(
            final IMetadataManager metadataManager,
            final IMetadataAccessManager metadataAccessManager,
            final DataDirectory dataDirectory,
            final MetadataDirConfig metadataDirConfig) {
        super(metadataManager, metadataAccessManager, dataDirectory, metadataDirConfig);
    }

    @Override
    public List<MetadataResource> getResources(
            String metadataUuid, MetadataResourceVisibility visibility, String filter, Boolean approved)
            throws Exception {
        int metadataId = canDownload(metadataUuid, visibility, approved);

        Path metadataDir =
                this.metadataManager.getMetadataDir(dataDirectory.getMetadataDir(), visibility.toString(), metadataId);

        List<MetadataResource> resourceList = new ArrayList<>();
        if (filter == null) {
            filter = FilesystemStore.DEFAULT_FILTER;
        }
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(metadataDir, filter)) {
            for (Path path : directoryStream) {
                String filePathName = "";

                Path p = path.getFileName();
                if (p != null) {
                    filePathName = p.toString();
                }

                MetadataResource resource = new FilesystemStoreResource(
                        metadataUuid,
                        metadataId,
                        filePathName,
                        getBaseUrl(),
                        visibility,
                        Files.size(path),
                        Date.from(Instant.ofEpochMilli(
                                Files.getLastModifiedTime(path).toMillis())),
                        approved);
                resourceList.add(resource);
            }
        } catch (IOException ignored) {
            // Ignored
        }

        resourceList.sort(MetadataResourceVisibility.sortByFileName);

        return resourceList;
    }

    @Override
    public ResourceHolder getResource(
            final String metadataUuid,
            final MetadataResourceVisibility visibility,
            final String resourceId,
            Boolean approved)
            throws Exception {
        int metadataId = canDownload(metadataUuid, visibility, approved);
        checkResourceId(resourceId);

        final Path resourceFile = this.metadataManager
                .getMetadataDir(dataDirectory.getMetadataDir(), visibility.toString(), metadataId)
                .resolve(getFilename(metadataUuid, resourceId));

        if (Files.exists(resourceFile)) {
            return new ResourceHolderImpl(
                    resourceFile, getResourceDescription(metadataUuid, visibility, resourceFile, approved));
        } else {
            throw new ResourceNotFoundException(
                    String.format("Metadata resource '%s' not found for metadata '%s'", resourceId, metadataUuid));
        }
    }

    @Override
    public ResourceHolder getResourceInternal(
            final String metadataUuid,
            final MetadataResourceVisibility visibility,
            final String resourceId,
            Boolean approved)
            throws Exception {
        int metadataId = getAndCheckMetadataId(metadataUuid, approved);
        checkResourceId(resourceId);

        final Path resourceFile = this.metadataManager
                .getMetadataDir(this.dataDirectory.getMetadataDir(), visibility.toString(), metadataId)
                .resolve(getFilename(metadataUuid, resourceId));

        if (Files.exists(resourceFile)) {
            return new ResourceHolderImpl(resourceFile, null);
        } else {
            throw new ResourceNotFoundException(
                    String.format("Metadata resource '%s' not found for metadata '%s'", resourceId, metadataUuid));
        }
    }

    @Override
    public MetadataResource getResourceDescription(
            final String metadataUuid, MetadataResourceVisibility visibility, String filename, Boolean approved)
            throws Exception {
        Path path = getPath(metadataUuid, visibility, filename, approved);
        return getResourceDescription(metadataUuid, visibility, path, approved);
    }

    /**
     * Get the resource description or null if the file doesn't exist.
     *
     * @param metadataUuid the uuid of the owner metadata record.
     * @param visibility is the resource is public or not.
     * @param filePath the path to the resource.
     * @param approved if the metadata draft has been approved or not
     * @return the resource description or {@code null} if there is any problem accessing the file.
     */
    private MetadataResource getResourceDescription(
            final String metadataUuid,
            final MetadataResourceVisibility visibility,
            final Path filePath,
            Boolean approved) {
        FilesystemStoreResource result = null;

        try {
            int metadataId = getAndCheckMetadataId(metadataUuid, approved);
            long fileSize = Files.size(filePath);

            String filePathName = "";

            Path p = filePath.getFileName();
            if (p != null) {
                filePathName = p.toString();
            }

            result = new FilesystemStoreResource(
                    metadataUuid,
                    metadataId,
                    filePathName,
                    getBaseUrl(),
                    visibility,
                    fileSize,
                    Date.from(Instant.ofEpochMilli(
                            Files.getLastModifiedTime(filePath).toMillis())),
                    approved);
        } catch (IOException e) {
            log.error("Error getting size of file " + filePath + ": " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error in getResourceDescription: " + e.getMessage(), e);
        }
        return result;
    }

    private @NotNull String getBaseUrl() {
        return this.baseUrl + this.contextPath + "/api/records/";
    }

    @Override
    public MetadataResourceContainer getResourceContainerDescription(String metadataUuid, Boolean approved)
            throws Exception {

        int metadataId = getAndCheckMetadataId(metadataUuid, approved);
        final Path metadataDir = this.metadataManager.getMetadataDir(dataDirectory.getMetadataDir(), metadataId);
        if (!Files.exists(metadataDir)) {
            try {
                Files.createDirectories(metadataDir);
            } catch (Exception e) {
                throw new IOException(
                        String.format("Can't create folder '%s' for metadata '%d'.", metadataDir, metadataId));
            }
        }

        return new FilesystemStoreResourceContainer(metadataUuid, metadataId, metadataUuid, getBaseUrl(), approved);
    }

    @Override
    public MetadataResource renameResource(String metadataUuid, String resourceId, String newName, Boolean approved)
            throws Exception {
        int metadataId = getAndCheckMetadataId(metadataUuid, approved);
        checkResourceId(newName);
        try (ResourceHolder filePath = getResource(metadataUuid, resourceId, approved)) {
            Path newFilePath = getPath(metadataId, MetadataResourceVisibility.PRIVATE, newName, approved);
            Files.move(filePath.getPath(), newFilePath, StandardCopyOption.REPLACE_EXISTING);
            return getResourceDescription(metadataUuid, MetadataResourceVisibility.PRIVATE, newFilePath, approved);
        } catch (IOException e) {
            log.error(String.format(
                    "Unable to rename resource '%s' for metadata %d (%s). %s",
                    resourceId, metadataId, metadataUuid, e.getMessage()));
        }
        return null;
    }

    @Override
    public MetadataResource putResource(
            final String metadataUuid,
            final String filename,
            final InputStream is,
            @Nullable final Date changeDate,
            final MetadataResourceVisibility visibility,
            Boolean approved)
            throws Exception {
        int metadataId = canEdit(metadataUuid, approved);
        checkResourceId(filename);
        Path filePath = getPath(metadataId, visibility, filename, approved);
        Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
        if (changeDate != null) {
            IO.touch(filePath, FileTime.from(changeDate.toInstant()));
        }

        return getResourceDescription(metadataUuid, visibility, filePath, approved);
    }

    private Path getPath(String metadataUuid, MetadataResourceVisibility visibility, String fileName, Boolean approved)
            throws Exception {
        int metadataId = getAndCheckMetadataId(metadataUuid, approved);
        return getPath(metadataId, visibility, fileName, approved);
    }

    private Path getPath(int metadataId, MetadataResourceVisibility visibility, String fileName, Boolean approved)
            throws Exception {
        final Path folderPath = ensureDirectory(metadataId, fileName, visibility);
        Path filePath = folderPath.resolve(fileName);
        if (Files.exists(filePath) && Boolean.TRUE.equals(!approved)) {
            throw new ResourceAlreadyExistException(String.format(
                    "A resource with name '%s' and status '%s' already exists for metadata '%d'.",
                    fileName, visibility, metadataId));
        }
        return filePath;
    }

    @Override
    public String delResources(int metadataId) throws Exception {
        Path metadataDir = this.metadataManager.getMetadataDir(this.dataDirectory.getMetadataDir(), metadataId);
        try {
            log.info(String.format("Deleting all files from metadataId '%d'", metadataId));
            IO.deleteFileOrDirectory(metadataDir, true);
            log.info(String.format("Metadata '%d' directory removed.", metadataId));
            return String.format("Metadata '%d' directory removed.", metadataId);
        } catch (Exception e) {
            return String.format("Unable to remove metadata '%d' directory.", metadataId);
        }
    }

    @Override
    public String delResource(String metadataUuid, String resourceId, Boolean approved) throws Exception {
        int metadataId = canEdit(metadataUuid, approved);

        try (ResourceHolder filePath = getResource(metadataUuid, resourceId, approved)) {
            Files.deleteIfExists(filePath.getPath());
            log.info(
                    String.format("Resource '%s' removed for metadata %d (%s).", resourceId, metadataId, metadataUuid));
            return String.format("Metadata resource '%s' removed.", resourceId);
        } catch (IOException e) {
            log.warn(String.format(
                    "Unable to remove resource '%s' for metadata %d (%s). %s",
                    resourceId, metadataId, metadataUuid, e.getMessage()));
            return String.format("Unable to remove resource '%s'.", resourceId);
        }
    }

    @Override
    public String delResource(
            final String metadataUuid,
            final MetadataResourceVisibility visibility,
            final String resourceId,
            Boolean approved)
            throws Exception {
        int metadataId = canEdit(metadataUuid, approved);

        try (ResourceHolder filePath = getResource(metadataUuid, visibility, resourceId, approved)) {
            Files.deleteIfExists(filePath.getPath());
            log.info(
                    String.format("Resource '%s' removed for metadata %d (%s).", resourceId, metadataId, metadataUuid));
            return String.format("Metadata resource '%s' removed.", resourceId);
        } catch (IOException e) {
            log.warn(String.format(
                    "Unable to remove resource '%s' for metadata %d (%s). %s",
                    resourceId, metadataId, metadataUuid, e.getMessage()));
            return String.format("Unable to remove resource '%s'.", resourceId);
        }
    }

    @Override
    public MetadataResource patchResourceStatus(
            String metadataUuid, String resourceId, MetadataResourceVisibility visibility, Boolean approved)
            throws Exception {
        int metadataId = canEdit(metadataUuid, approved);

        ResourceHolder filePath = getResource(metadataUuid, resourceId, approved);
        if (filePath.getMetadata().getVisibility() == visibility) {
            // already the wanted visibility
            return filePath.getMetadata();
        }
        final Path newFolderPath = ensureDirectory(metadataId, resourceId, visibility);
        Path newFilePath = newFolderPath.resolve(filePath.getPath().getFileName());
        Files.move(filePath.getPath(), newFilePath);
        return getResourceDescription(metadataUuid, visibility, newFilePath, approved);
    }

    private Path ensureDirectory(
            final int metadataId, final String resourceId, final MetadataResourceVisibility visibility)
            throws Exception {
        final Path metadataDir = this.metadataManager.getMetadataDir(
                this.dataDirectory.getMetadataDir(), visibility.toString(), metadataId);
        if (!Files.exists(metadataDir)) {
            try {
                Files.createDirectories(metadataDir);
            } catch (Exception e) {
                throw new IOException(String.format(
                        "Can't create folder '%s' to store resource with name '%s' for metadata '%d'.",
                        visibility, resourceId, metadataId));
            }
        }
        return metadataDir;
    }

    private static class ResourceHolderImpl implements ResourceHolder {
        private final Path path;
        private final MetadataResource metadata;

        private ResourceHolderImpl(Path path, final MetadataResource metadata) {
            this.path = path;
            this.metadata = metadata;
        }

        @Override
        public Path getPath() {
            return path;
        }

        @Override
        public MetadataResource getMetadata() {
            return metadata;
        }

        @Override
        public void close() {
            // nothing to do
        }
    }
}
