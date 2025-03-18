/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork;

import static org.geonetwork.data.gdal.GdalUtils.GDAL_DEFAULT_RASTER_LAYER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.geonetwork.data.DataAnalyzerException;
import org.geonetwork.data.MetadataBuilder;
import org.geonetwork.data.gdal.GdalDataAnalyzer;
import org.geonetwork.data.gdal.GdalOverviewBuilder;
import org.geonetwork.data.model.AttributeStatistics;
import org.geonetwork.data.model.DataFormat;
import org.geonetwork.data.model.DatasetInfo;
import org.geonetwork.data.model.RasterInfo;
import org.geonetwork.domain.Metadata;
import org.geonetwork.editing.BatchEditMode;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.metadata.MetadataManager;
import org.geonetwork.metadata.MetadataNotFoundException;
import org.geonetwork.metadatastore.MetadataResourceVisibility;
import org.geonetwork.metadatastore.MetadataResourceVisibilityConverter;
import org.geonetwork.metadatastore.ResourceNotFoundException;
import org.geonetwork.metadatastore.Store;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data/analysis")
@Tag(
        name = "Data analysis",
        description =
                """
Data analysis operations can extract information from a dataset, such as the number of features, the extent, the data model, ...
and update or compose a metadata record based on the dataset properties.

The default analyzer provided is based on [GDAL](http://gdal.org/), which supports a wide range of raster and vector formats.
""")
@AllArgsConstructor
public class DataAnalysisController {

    public static final String API_DATASOURCE_DESCRIPTION =
            """
            The datasource to analyze. It can be a file path or a datasource URL.
        """;
    GdalDataAnalyzer analyzer;

    GdalOverviewBuilder overviewBuilder;

    MetadataBuilder metadataBuilder;

    MetadataManager metadataManager;

    Store metadataStore;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(MetadataResourceVisibility.class, new MetadataResourceVisibilityConverter());
    }

    @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get the status of the data analyzer")
    public String status() {
        return analyzer.getStatus();
    }

    @GetMapping(path = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get the version of the data analyzer",
            description =
                    """
For GDAL, version 3.7.0+ is required (added support for JSON output in info commands).
""")
    public String getVersion() {
        return analyzer.getVersion();
    }

    @GetMapping(path = "/formats", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get the list of supported formats")
    @PreAuthorize("hasRole('Editor')")
    public List<DataFormat> formats() {
        return analyzer.getFormats();
    }

    @GetMapping(path = "/layers", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get the list of layers for a datasource",
            description = """
    Raster datasets have a single layer named 'RASTER_LAYER'.
""")
    @PreAuthorize("hasRole('Editor')")
    public ResponseEntity<List<String>> layers(
            @RequestParam(required = false) String metadataUuid,
            @RequestParam(required = false) MetadataResourceVisibility visibility,
            @RequestParam
                    @Parameter(
                            // TODO: Can we ruse this description for all methods?
                            description = DataAnalysisController.API_DATASOURCE_DESCRIPTION,
                            // TODO: Should we add restriction on drivers?
                            examples = {
                                @ExampleObject(name = "Local file", value = "/path/to/file.shp"),
                                @ExampleObject(
                                        name = "URL to a RASTER dataset in TIFF format",
                                        value =
                                                "https://sdi.eea.europa.eu/webdav/datastore/public/eea_r_3035_1_km_landscan-eurmed2_p_2008_v01_r00/lspop2008_laea.tif"),
                                @ExampleObject(
                                        name = "URL to a CSV file",
                                        value =
                                                "https://sdi.eea.europa.eu/webdav/datastore/public/coe_t_emerald_p_2021-2022_v05_r00/Emerald_2022_BIOREGION.csv"),
                                @ExampleObject(
                                        name = "URL to a GeoJSON file",
                                        value =
                                                "https://wfs-kbhkort.kk.dk/k101/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=k101:vejmidte_geodk_labels&outputFormat=json&SRSNAME=EPSG:4326"),
                                @ExampleObject(
                                        name = "URL to a WFS datasource",
                                        value = "WFS:https://service.pdok.nl/rvo/wetlands/wfs/v1_0"),
                                @ExampleObject(
                                        name = "URL to a zipped shapefile",
                                        value =
                                                "/vsizip//vsicurl/https://naciscdn.org/naturalearth/50m/physical/ne_50m_glaciated_areas.zip"),
                                @ExampleObject(
                                        name = "URL to a PostGIS database",
                                        value = "postgresql://www-data:www-data@localhost:5432/geo")
                            })
                    String datasource,
            @RequestParam(required = false, defaultValue = "true") boolean approved) {
        if (StringUtils.hasLength(metadataUuid)) {
            if (visibility == null) {
                throw new InvalidParameterException("Metadata visibility is required");
            }

            try {
                Store.ResourceHolder resourceHolder =
                        metadataStore.getResource(metadataUuid, visibility, datasource, approved);

                return new ResponseEntity<>(
                        analyzer.getDatasourceLayers(
                                buildLocalDatasourceUrl(resourceHolder.getPath().toString())),
                        HttpStatus.OK);
            } catch (ResourceNotFoundException ex) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception ex) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(analyzer.getDatasourceLayers(datasource), HttpStatus.OK);
        }
    }

    // TODO: Make distinction between raster and vector analysis in the following method
    @GetMapping(path = "/execute", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Execute analysis and return layer properties as an index record")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Analysis executed successfully")})
    @PreAuthorize("hasRole('Editor')")
    public ResponseEntity<IndexRecord> analysisSynchMetadataResource(
            @RequestParam(required = false) String metadataUuid,
            @RequestParam(required = false) MetadataResourceVisibility visibility,
            @RequestParam String datasource,
            @RequestParam(required = false, defaultValue = "true") boolean approved,
            @RequestParam String layer) {

        if (StringUtils.hasLength(metadataUuid)) {
            if (visibility == null) {
                throw new InvalidParameterException("Metadata visibility is required");
            }

            String datasourceToUse;
            try {
                datasourceToUse = calculateLocalDatasourcePath(metadataUuid, visibility, datasource, approved);
            } catch (ResourceNotFoundException ex) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception ex) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return internalAnalysisSynchIndexRecord(datasourceToUse, layer);
        } else {
            return internalAnalysisSynchIndexRecord(datasource, layer);
        }
    }

    @GetMapping(path = "/preview", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Preview metadata record updated with layer properties",
            description = """
        The metadata record is not saved.
""")
    @PreAuthorize("hasRole('Editor')")
    public ResponseEntity<String> previewDataAnalysisOnRecord(
            @RequestParam String metadataUuid,
            @RequestParam(required = false) MetadataResourceVisibility visibility,
            @RequestParam String datasource,
            @RequestParam(required = false, defaultValue = "false") boolean approved,
            @RequestParam String layer)
            throws MetadataNotFoundException {

        Metadata metadataRecord = metadataManager.findMetadataByUuidOrId(metadataUuid, approved);
        String datasourceToUse;

        if (isRemoteDatasource(datasource)) {
            datasourceToUse = datasource;
        } else {
            try {
                datasourceToUse = calculateLocalDatasourcePath(metadataUuid, visibility, datasource, approved);
            } catch (ResourceNotFoundException ex) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception ex) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        BatchEditMode editMode = BatchEditMode.PREVIEW;
        ResponseEntity<String> builtMetadata =
                applyDataAnalysisOnRecord(metadataRecord.getUuid(), datasourceToUse, layer, metadataRecord, editMode);
        if (builtMetadata != null) return builtMetadata;
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/apply", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Update metadata record with layer properties",
            description = """
        The metadata record is saved.
""")
    @PreAuthorize("hasRole('Editor')")
    public ResponseEntity<String> applyDataAnalysisOnRecord(
            @RequestParam String uuid,
            @RequestParam(required = false) MetadataResourceVisibility visibility,
            @RequestParam String datasource,
            @RequestParam(required = false, defaultValue = "false") boolean approved,
            @RequestParam String layer)
            throws MetadataNotFoundException {

        Metadata metadataRecord = metadataManager.findMetadataByUuidOrId(uuid, approved);
        String datasourceToUse;

        if (isRemoteDatasource(datasource)) {
            datasourceToUse = datasource;
        } else {
            if (visibility == null) {
                throw new InvalidParameterException("Metadata visibility is required");
            }

            try {
                Store.ResourceHolder resourceHolder = metadataStore.getResource(uuid, visibility, datasource, approved);

                datasourceToUse =
                        buildLocalDatasourceUrl(resourceHolder.getPath().toString());
            } catch (ResourceNotFoundException ex) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception ex) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        BatchEditMode editMode = BatchEditMode.SAVE;
        ResponseEntity<String> builtMetadata =
                applyDataAnalysisOnRecord(metadataRecord.getUuid(), datasourceToUse, layer, metadataRecord, editMode);
        if (builtMetadata != null) return builtMetadata;
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/attribute/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get statistics for an attribute",
            description = """
        Return the min and max value of the attribute.
""")
    @PreAuthorize("hasRole('Editor')")
    public List<AttributeStatistics> attributeStatistics(
            @RequestParam String datasource, @RequestParam String layer, @RequestParam String attribute) {

        return analyzer.getAttributesStatistics(datasource, layer, List.of(attribute));
    }

    @GetMapping(path = "/attribute/codelist", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get a list of unique values for an attribute",
            description = """
        Values are order alphabetically and limited to 10 by default.
""")
    @PreAuthorize("hasRole('Editor')")
    public List<Object> attributeCodelist(
            @RequestParam String datasource,
            @RequestParam String layer,
            @RequestParam String attribute,
            @RequestParam(defaultValue = "10") @Max(value = 100) int limit) {

        return analyzer.getAttributeUniqueValues(datasource, layer, attribute, limit);
    }

    private String buildLocalDatasourceUrl(String string) {
        if (string.endsWith(".zip")) {
            return "/vsizip/" + string;
        } else {
            return string;
        }
    }

    private @Nullable ResponseEntity<String> applyDataAnalysisOnRecord(
            String uuid, String datasource, String layer, Metadata metadataRecord, BatchEditMode editMode) {
        if (layer.equals(GDAL_DEFAULT_RASTER_LAYER)) {
            Optional<RasterInfo> layerProperties = analyzer.getRasterProperties(datasource);

            if (layerProperties.isPresent()) {
                String builtMetadata = metadataBuilder.buildMetadata(
                        uuid, metadataRecord.getSchemaid(), layerProperties.get(), editMode);
                return new ResponseEntity<>(builtMetadata, HttpStatus.OK);
            }
        } else {
            Optional<DatasetInfo> layerProperties = analyzer.getLayerProperties(datasource, layer);

            if (layerProperties.isPresent()) {
                String builtMetadata = metadataBuilder.buildMetadata(
                        uuid, metadataRecord.getSchemaid(), layerProperties.get(), editMode);
                return new ResponseEntity<>(builtMetadata, HttpStatus.OK);
            }
        }
        return null;
    }

    private ResponseEntity<IndexRecord> internalAnalysisSynchIndexRecord(String datasource, String layer) {
        try {
            Optional<DatasetInfo> layerProperties = analyzer.getLayerProperties(datasource, layer);
            if (layerProperties.isPresent()) {
                IndexRecord indexRecord = layerProperties.get().toIndexRecord(datasource, layer);

                return new ResponseEntity<>(indexRecord, HttpStatus.OK);
            }
        } catch (Exception vectorException) {
            try {
                Optional<RasterInfo> rasterProperties = analyzer.getRasterProperties(datasource);
                if (rasterProperties.isPresent()) {
                    IndexRecord indexRecord = rasterProperties.get().toIndexRecord(datasource, layer);

                    return new ResponseEntity<>(indexRecord, HttpStatus.OK);
                }
            } catch (Exception rasterException) {
                throw new DataAnalyzerException(String.format(
                        "Error analyzing datasource %s.%n* Vector analysis error: %s%n* Raster analysis error: %s",
                        datasource, vectorException.getMessage(), rasterException.getMessage()));
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/overview", produces = MediaType.IMAGE_PNG_VALUE)
    @Operation(
            summary = "Build an overview of a layer",
            description =
                    """
The overview is a small image representing the layer.

* For RASTER, `gdal_translate` is used to convert the layer to a PNG image.
* For VECTOR, `gdal_rasterize` is used to rasterize the layer and `gdal_translate` to convert it to a PNG image.

""")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Overview built successfully",
                        content = {@Content(mediaType = MediaType.IMAGE_PNG_VALUE)}),
            })
    @PreAuthorize("hasRole('Editor')")
    public ResponseEntity<byte[]> buildOverview(@RequestParam String datasource, @RequestParam String layer) {
        try {
            return ResponseEntity.ok().body(overviewBuilder.buildOverview(datasource, layer));
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/overviewForMetadataResource", produces = MediaType.IMAGE_PNG_VALUE)
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Overview built successfully",
                        content = {@Content(mediaType = MediaType.IMAGE_PNG_VALUE)}),
            })
    @PreAuthorize("hasRole('Editor')")
    public ResponseEntity<byte[]> buildOverview(
            @RequestParam String uuid,
            @RequestParam MetadataResourceVisibility visibility,
            @RequestParam String datasource,
            @RequestParam boolean approved,
            @RequestParam String layer)
            throws MetadataNotFoundException {
        metadataManager.findMetadataByUuidOrId(uuid, false);

        String datasourceToUse;
        try {
            datasourceToUse = calculateLocalDatasourcePath(uuid, visibility, datasource, approved);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            return ResponseEntity.ok().body(overviewBuilder.buildOverview(datasourceToUse, layer));
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String calculateLocalDatasourcePath(
            String metadataUuid, MetadataResourceVisibility visibility, String datasource, boolean approved)
            throws Exception {
        Store.ResourceHolder resourceHolder = metadataStore.getResource(metadataUuid, visibility, datasource, approved);

        return buildLocalDatasourceUrl(resourceHolder.getPath().toString());
    }

    /**
     * Checks if a datasource is a remote datasource.
     *
     * @param datasource Datasource reference.
     * @return true if the datasource is remote, otherwise false.
     */
    private boolean isRemoteDatasource(String datasource) {
        String datasourceValue = datasource.toLowerCase(Locale.ROOT);

        return datasourceValue.startsWith("http://")
                || datasourceValue.startsWith("https://")
                || datasourceValue.startsWith("wfs:")
                || datasourceValue.startsWith("postgresql:")
                || datasourceValue.startsWith("/vsizip//vsicurl");
    }
}
