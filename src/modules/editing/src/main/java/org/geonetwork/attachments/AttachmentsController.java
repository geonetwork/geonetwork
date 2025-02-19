/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.attachments;

import static org.geonetwork.constants.ApiParams.API_CLASS_RECORD_OPS;
import static org.geonetwork.constants.ApiParams.API_CLASS_RECORD_TAG;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.geonetwork.domain.Metadata;
import org.geonetwork.metadata.IMetadataAccessManager;
import org.geonetwork.metadata.IMetadataManager;
import org.geonetwork.metadatastore.MetadataResource;
import org.geonetwork.metadatastore.MetadataResourceVisibility;
import org.geonetwork.metadatastore.MetadataResourceVisibilityConverter;
import org.geonetwork.metadatastore.Sort;
import org.geonetwork.metadatastore.SortConverter;
import org.geonetwork.metadatastore.Store;
import org.geonetwork.metadatastore.filesystem.FilesystemStore;
import org.geonetwork.utility.file.FileUtil;
import org.geonetwork.utility.file.ImageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(value = {"/api/records/{metadataUuid}/attachments"})
@Tag(name = API_CLASS_RECORD_TAG, description = API_CLASS_RECORD_OPS)
@RestController
@RequiredArgsConstructor
public class AttachmentsController {
    private static final Integer MIN_IMAGE_SIZE = 1;
    private static final Integer MAX_IMAGE_SIZE = 2048;

    private final Store store;
    private final IMetadataManager metadataManager;
    private final IMetadataAccessManager metadataAccessManager;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(MetadataResourceVisibility.class, new MetadataResourceVisibilityConverter());
        binder.registerCustomEditor(Sort.class, new SortConverter());
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Get a metadata resource")
    @GetMapping(value = "/{resourceId:.+}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Record attachment.",
                        content = @Content(schema = @Schema(type = "string", format = "binary"))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Operation not allowed. " + "User needs to be able to download the resource.")
            })
    public void getResource(
            @Parameter(
                            description = "The metadata UUID or internal identifier",
                            required = true,
                            example = "43d7c186-2187-4bcd-8843-41e575a5ef56")
                    @PathVariable
                    String metadataUuid,
            @Parameter(description = "The resource identifier (ie. filename)", required = true) @PathVariable
                    String resourceId,
            @Parameter(description = "Use approved version or not", example = "true")
                    @RequestParam(required = false, defaultValue = "true")
                    Boolean approved,
            @Parameter(description = "Size (only applies to images). From 1px to 2048px.", example = "200")
                    @RequestParam(required = false)
                    Integer size,
            @Parameter(hidden = true) HttpServletResponse response)
            throws Exception {
        try (Store.ResourceHolder file = store.getResource(metadataUuid, resourceId, approved)) {

            Metadata metadata = metadataManager.findMetadataByUuidOrId(metadataUuid, approved);

            if (!metadataAccessManager.canView(metadata.getId())) {
                throw new AccessDeniedException("User is not permitted to access this resource");
            }

            response.setHeader(
                    "Content-Disposition",
                    "inline; filename=\"" + file.getMetadata().getFilename() + "\"");
            response.setHeader("Cache-Control", "no-cache");
            String contentType = FileUtil.getFileContentType(file.getPath());
            response.setHeader("Content-Type", contentType);

            if (contentType.startsWith("image/") && size != null) {
                if (size >= MIN_IMAGE_SIZE && size <= MAX_IMAGE_SIZE) {
                    BufferedImage image = ImageIO.read(file.getPath().toFile());
                    BufferedImage resized = ImageUtil.resize(image, size);
                    ImageIO.write(resized, "png", response.getOutputStream());
                } else {
                    throw new IllegalArgumentException(String.format(
                            "Image can only be resized from %d to %d. You requested %d.",
                            MIN_IMAGE_SIZE, MAX_IMAGE_SIZE, size));
                }
            } else {
                try (InputStream is = Files.newInputStream(file.getPath())) {
                    StreamUtils.copy(is, response.getOutputStream());
                }
            }
        }
    }

    @io.swagger.v3.oas.annotations.Operation(
            summary = "List all metadata attachments",
            description =
                    "<a href='https://docs.geonetwork-opensource.org/latest/user-guide/associating-resources/using-filestore/'>More info</a>")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Return the record attachments."),
                @ApiResponse(responseCode = "403", description = "ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_VIEW")
            })
    public List<MetadataResource> getAllResources(
            @Parameter(
                            description = "The metadata UUID or internal identifier",
                            required = true,
                            example = "43d7c186-2187-4bcd-8843-41e575a5ef56")
                    @PathVariable
                    String metadataUuid,
            @Parameter(description = "Sort by", example = "type") @RequestParam(required = false, defaultValue = "name")
                    Sort sort,
            @Parameter(description = "Use approved version or not", example = "true")
                    @RequestParam(required = false, defaultValue = "true")
                    Boolean approved,
            @RequestParam(required = false, defaultValue = FilesystemStore.DEFAULT_FILTER) String filter)
            throws Exception {

        return store.getResources(metadataUuid, sort, filter, approved);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Delete a metadata resource")
    @PreAuthorize("hasRole('Editor')")
    @DeleteMapping(value = "/{resourceId:.+}")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "204",
                        description = "Attachment visibility removed.",
                        content = {@Content(schema = @Schema(hidden = true))}),
                @ApiResponse(responseCode = "403", description = "ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_EDIT")
            })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delResource(
            @Parameter(
                            description = "The metadata UUID or internal identifier",
                            required = true,
                            example = "43d7c186-2187-4bcd-8843-41e575a5ef56")
                    @PathVariable
                    String metadataUuid,
            @Parameter(description = "The resource identifier (ie. filename)", required = true) @PathVariable
                    String resourceId,
            @Parameter(description = "Use approved version or not", example = "true")
                    @RequestParam(required = false, defaultValue = "false")
                    Boolean approved)
            throws Exception {
        store.delResource(metadataUuid, resourceId, approved);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Delete all uploaded metadata resources")
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Editor')")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "204",
                        description = "Attachment added.",
                        content = {@Content(schema = @Schema(hidden = true))}),
                @ApiResponse(responseCode = "403", description = "ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_EDIT")
            })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delResources(
            @Parameter(
                            description = "The metadata UUID or internal identifier",
                            required = true,
                            example = "43d7c186-2187-4bcd-8843-41e575a5ef56")
                    @PathVariable
                    String metadataUuid,
            @Parameter(description = "Use approved version or not", example = "true")
                    @RequestParam(required = false, defaultValue = "false")
                    Boolean approved)
            throws Exception {

        Metadata metadata = metadataManager.findMetadataByUuidOrId(metadataUuid, approved);
        store.delResources(metadata.getId());
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Create a new resource for a given metadata")
    @PreAuthorize("hasRole('Editor')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Attachment uploaded."),
                @ApiResponse(responseCode = "403", description = "ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_EDIT")
            })
    public MetadataResource putResource(
            @Parameter(
                            description = "The metadata UUID or internal identifier",
                            required = true,
                            example = "43d7c186-2187-4bcd-8843-41e575a5ef56")
                    @PathVariable
                    String metadataUuid,
            @Parameter(description = "The sharing policy", example = "public")
                    @RequestParam(required = false, defaultValue = "public")
                    MetadataResourceVisibility visibility,
            @Parameter(description = "The file to upload") @RequestParam("file") MultipartFile file,
            @Parameter(description = "Use approved version or not", example = "true")
                    @RequestParam(required = false, defaultValue = "false")
                    Boolean approved)
            throws Exception {

        return store.putResource(metadataUuid, file, visibility, approved);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Create a new resource from a URL for a given metadata")
    @PreAuthorize("('Editor')")
    @PutMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Attachment added."),
                @ApiResponse(responseCode = "403", description = "ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_EDIT")
            })
    public MetadataResource putResourceFromURL(
            @Parameter(
                            description = "The metadata UUID or internal identifier",
                            required = true,
                            example = "43d7c186-2187-4bcd-8843-41e575a5ef56")
                    @PathVariable
                    String metadataUuid,
            @Parameter(description = "The sharing policy", example = "public")
                    @RequestParam(required = false, defaultValue = "public")
                    MetadataResourceVisibility visibility,
            @Parameter(description = "The URL to load in the store") @RequestParam("url") URL url,
            @Parameter(description = "Use approved version or not", example = "true")
                    @RequestParam(required = false, defaultValue = "false")
                    Boolean approved)
            throws Exception {

        return store.putResource(metadataUuid, url, visibility, approved);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Update the metadata resource visibility")
    @PreAuthorize("hasRole('Editor')")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Attachment visibility updated."),
                @ApiResponse(responseCode = "403", description = "ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_EDIT")
            })
    @PatchMapping(value = "/{resourceId:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public MetadataResource patchResource(
            @Parameter(
                            description = "The metadata UUID or internal identifier",
                            required = true,
                            example = "43d7c186-2187-4bcd-8843-41e575a5ef56")
                    @PathVariable
                    String metadataUuid,
            @Parameter(description = "The resource identifier (ie. filename)", required = true) @PathVariable
                    String resourceId,
            @Parameter(description = "The visibility", required = true, example = "public")
                    @RequestParam(required = true)
                    MetadataResourceVisibility visibility,
            @Parameter(description = "Use approved version or not", example = "true")
                    @RequestParam(required = false, defaultValue = "false")
                    Boolean approved)
            throws Exception {
        return store.patchResourceStatus(metadataUuid, resourceId, visibility, approved);
    }
}
