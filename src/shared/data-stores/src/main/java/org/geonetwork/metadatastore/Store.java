/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.metadatastore;

import jakarta.annotation.Nullable;
import java.io.Closeable;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/** A store allows user to upload resources (eg. files) to a metadata record and retrieve them. */
public interface Store {
    /**
     * Retrieve all resources for a metadata. The list of resources depends on current user privileges.
     *
     * @param metadataUuidOrId The metadata UUID or the internal identifier
     * @param sort Sort by resource name or sharing policy {@link Sort}
     * @param filter a {@link java.nio.file.Files#newDirectoryStream(Path)} GLOB expression} to filter resources eg.
     *     *.{png|jpg}
     * @param approved Return the approved version or not
     * @return A list of resources
     */
    List<MetadataResource> getResources(String metadataUuidOrId, Sort sort, String filter, Boolean approved)
            throws Exception;

    /**
     * Retrieve all resources for a metadata having a specific sharing policy
     *
     * @param metadataUuidOrId The metadata UUID or the internal identifier
     * @param metadataResourceVisibility The type of sharing policy {@link MetadataResourceVisibility}
     * @param filter a {@link java.nio.file.Files#newDirectoryStream(Path) GLOB expression} to filter resources eg.
     *     *.{png|jpg}
     * @param approved Return the approved version or not
     * @return A list of resources
     */
    List<MetadataResource> getResources(
            String metadataUuidOrId,
            MetadataResourceVisibility metadataResourceVisibility,
            String filter,
            Boolean approved)
            throws Exception;

    /**
     * Retrieve a metadata resource path.
     *
     * @param metadataUuidOrId The metadata UUID or the internal identifier
     * @param metadataResourceVisibility The type of sharing policy {@link MetadataResourceVisibility}
     * @param resourceId The resource identifier of its filename
     * @return The resource
     */
    ResourceHolder getResource(
            String metadataUuidOrId,
            MetadataResourceVisibility metadataResourceVisibility,
            String resourceId,
            Boolean approved)
            throws Exception;

    /**
     * Retrieve a metadata resource path.
     *
     * @param metadataUuidOrId The metadata UUID or the internal identifier
     * @param resourceId The resource identifier
     * @param approved Return the approved version or not
     * @return The resource
     */
    ResourceHolder getResource(String metadataUuidOrId, String resourceId, Boolean approved) throws Exception;

    /** Retrieve a metadata resource path (for internal use eg. indexing) */
    ResourceHolder getResourceInternal(
            String metadataUuidOrId, final MetadataResourceVisibility visibility, String resourceId, Boolean approved)
            throws Exception;

    /**
     * Add a new resource from a file.
     *
     * @param metadataUuidOrId The metadata UUID or the internal identifier
     * @param file The resource file
     * @param metadataResourceVisibility The type of sharing policy {@link MetadataResourceVisibility}
     * @param approved Put the approved version or not
     * @return The resource description
     */
    MetadataResource putResource(
            String metadataUuidOrId,
            MultipartFile file,
            MetadataResourceVisibility metadataResourceVisibility,
            Boolean approved)
            throws Exception;

    /**
     * Add a new resource from a file.
     *
     * @param metadataUuidOrId The metadata UUID or the internal identifier
     * @param filename The resource filename
     * @param is The input stream
     * @param changeDate The optional change date
     * @param metadataResourceVisibility The type of sharing policy {@link MetadataResourceVisibility}
     * @param approved Put the approved version or not
     * @return The resource description
     */
    MetadataResource putResource(
            String metadataUuidOrId,
            String filename,
            InputStream is,
            @Nullable Date changeDate,
            MetadataResourceVisibility metadataResourceVisibility,
            Boolean approved)
            throws Exception;

    /**
     * Add a new resource from a local file path.
     *
     * @param metadataUuidOrId The metadata UUID or the internal identifier
     * @param filePath The resource local filepath
     * @param metadataResourceVisibility The type of sharing policy {@link MetadataResourceVisibility}
     * @param approved Return the approved version or not
     * @return The resource description
     */
    MetadataResource putResource(
            String metadataUuidOrId,
            Path filePath,
            MetadataResourceVisibility metadataResourceVisibility,
            Boolean approved)
            throws Exception;

    /**
     * Add a new resource from a URL.
     *
     * @param metadataUuidOrId The metadata UUID or the internal identifier
     * @param fileUrl The resource file URL
     * @param metadataResourceVisibility The type of sharing policy {@link MetadataResourceVisibility}
     * @param approved Return the approved version or not
     * @return The resource description
     */
    MetadataResource putResource(
            String metadataUuidOrId,
            URL fileUrl,
            MetadataResourceVisibility metadataResourceVisibility,
            Boolean approved)
            throws Exception;

    /**
     * Change the resource sharing policy
     *
     * @param metadataUuidOrId The metadata UUID or the internal identifier
     * @param resourceId The resource identifier
     * @param metadataResourceVisibility The type of sharing policy {@link MetadataResourceVisibility}
     * @param approved Return the approved version or not
     */
    MetadataResource patchResourceStatus(
            String metadataUuidOrId,
            String resourceId,
            MetadataResourceVisibility metadataResourceVisibility,
            Boolean approved)
            throws Exception;

    /**
     * Delete all resources for a metadata
     *
     * @param metadataId The metadata ID
     */
    String delResources(int metadataId) throws Exception;

    /**
     * Delete a resource from the metadata store
     *
     * @param metadataUuidOrId The metadata UUID or the internal identifier
     * @param resourceId The resource identifier
     * @param approved Return the approved version or not
     */
    String delResource(String metadataUuidOrId, String resourceId, Boolean approved) throws Exception;

    /**
     * Delete a resource from the metadata store
     *
     * @param metadataUuidOrId The metadata UUID or the internal identifier
     * @param metadataResourceVisibility The type of sharing policy {@link MetadataResourceVisibility}
     * @param resourceId The resource identifier
     * @param approved Return the approved version or not
     */
    String delResource(
            String metadataUuidOrId,
            MetadataResourceVisibility metadataResourceVisibility,
            String resourceId,
            Boolean approved)
            throws Exception;

    /**
     * Get the resource description.
     *
     * @param metadataUuidOrId The metadata UUID or the internal identifier
     * @param visibility The type of sharing policy {@link MetadataResourceVisibility}
     * @param filename The filename
     * @return The description or null if the file doesn't exist
     */
    MetadataResource getResourceDescription(
            String metadataUuidOrId, MetadataResourceVisibility visibility, String filename, Boolean approved)
            throws Exception;

    /**
     * Get the resource container description.
     *
     * @param metadataUuidOrId The metadata UUID or the internal identifier
     * @return The container description or null if The metadata UUID or the internal identifier does doesn't exist
     */
    MetadataResourceContainer getResourceContainerDescription(final String metadataUuidOrId, Boolean approved)
            throws Exception;

    /**
     * Copy all resources from none approved (draft working copy) to approved folder.
     *
     * @param sourceUuid The source metadata UUID
     * @param targetUuid The target metadata UUID
     * @param metadataResourceVisibility The type of sharing policy {@link MetadataResourceVisibility}
     * @param sourceApproved The source metadata is approved
     * @param targetApproved The target metadata is approved
     */
    void copyResources(
            String sourceUuid,
            String targetUuid,
            MetadataResourceVisibility metadataResourceVisibility,
            boolean sourceApproved,
            boolean targetApproved)
            throws Exception;

    MetadataResource renameResource(String metadataUuid, String resourceId, String newName, Boolean approved)
            throws Exception;

    interface ResourceHolder extends Closeable {
        Path getPath();

        MetadataResource getMetadata();
    }

    ResourceManagementExternalProperties getResourceManagementExternalProperties();

    interface ResourceManagementExternalProperties {
        /**
         * Get the modal setting for the resource management window.
         *
         * @return boolean to indicate is the external management window should be modal or not.
         */
        boolean isEnabled();

        /**
         * Get the resource management windows parameters based on configuration for the store
         *
         * @return the javascript windows open parameters. i.e."toolbar=0,width=600,height=600"
         */
        String getWindowParameters();

        /**
         * Get the modal setting for the resource management window.
         *
         * @return boolean to indicate is the external management window should be modal or not.
         */
        boolean isModal();

        /**
         * Get the folder settings for the resource management window.
         *
         * @return boolean to indicate is the external management window should be enabled or not for folders.
         */
        boolean isFolderEnabled();
    }
}
