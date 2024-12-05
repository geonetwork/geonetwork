/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork; /*
                         * (c) 2003 Open Source Geospatial Foundation - all rights reserved
                         * This code is licensed under the GPL 2.0 license,
                         * available at the root application directory.
                         */

import static org.geonetwork.data.gdal.GdalUtils.GDAL_DEFAULT_RASTER_LAYER;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.geonetwork.data.MetadataBuilder;
import org.geonetwork.data.gdal.GdalDataAnalyzer;
import org.geonetwork.data.model.AttributeStatistics;
import org.geonetwork.data.model.BaseDataInfo;
import org.geonetwork.data.model.DataFormat;
import org.geonetwork.data.model.DatasetInfo;
import org.geonetwork.data.model.RasterInfo;
import org.geonetwork.domain.Metadata;
import org.geonetwork.editing.BatchEditMode;
import org.geonetwork.metadata.MetadataManager;
import org.geonetwork.metadata.MetadataNotFoundException;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data/analysis")
@AllArgsConstructor
public class DataAnalysisController {

    GdalDataAnalyzer analyzer;

    MetadataBuilder metadataBuilder;

    MetadataManager metadataManager;

    @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public String status() {
        return analyzer.getStatus();
    }

    @GetMapping(path = "/formats", produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('Editor')")
    public List<DataFormat> formats() {
        return analyzer.getFormats();
    }

    @GetMapping(path = "/attribute/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('Editor')")
    public List<AttributeStatistics> attributeStatistics(
            @RequestParam String datasource, @RequestParam String layer, @RequestParam String attribute) {

        return analyzer.getAttributesStatistics(datasource, layer, List.of(attribute));
    }

    @GetMapping(path = "/attribute/codelist", produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('Editor')")
    public List<Object> attributeCodelist(
            @RequestParam String datasource,
            @RequestParam String layer,
            @RequestParam String attribute,
            @RequestParam(defaultValue = "10") @Max(value = 100) int limit) {

        return analyzer.getAttributeUniqueValues(datasource, layer, attribute, limit);
    }

    @GetMapping(path = "/execute", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Analysis executed successfully",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(oneOf = {DatasetInfo.class, RasterInfo.class}))
                        }),
            })
    //@PreAuthorize("hasRole('Editor')")
    public ResponseEntity<BaseDataInfo> analysisSynch(@RequestParam String datasource, @RequestParam String layer) {
        try {
            Optional<DatasetInfo> layerProperties = analyzer.getLayerProperties(datasource, layer);
            if (layerProperties.isPresent()) {
                return new ResponseEntity<>(layerProperties.get(), HttpStatus.OK);
            }
        } catch (Exception vectorException) {
            try {
                Optional<RasterInfo> rasterProperties = analyzer.getRasterProperties(datasource);
                if (rasterProperties.isPresent()) {
                    return new ResponseEntity<>(rasterProperties.get(), HttpStatus.OK);
                }
            } catch (Exception rasterException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/layers", produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('Editor')")
    public ResponseEntity<List<String>> layers(@RequestParam String datasource) {
        return new ResponseEntity<>(analyzer.getDatasourceLayers(datasource), HttpStatus.OK);
    }

    @GetMapping(path = "/preview", produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('Editor')")
    public ResponseEntity<String> previewDataAnalysisOnRecord(
            @RequestParam String uuid, @RequestParam String datasource, @RequestParam String layer)
            throws MetadataNotFoundException {
        Metadata record = metadataManager.findMetadataByUuid(uuid, false);
        BatchEditMode editMode = BatchEditMode.PREVIEW;
        ResponseEntity<String> builtMetadata = applyDataAnalysisOnRecord(uuid, datasource, layer, record, editMode);
        if (builtMetadata != null) return builtMetadata;
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('Editor')")
    public ResponseEntity<String> applyDataAnalysisOnRecord(
            @RequestParam String uuid, @RequestParam String datasource, @RequestParam String layer)
            throws MetadataNotFoundException {
        Metadata record = metadataManager.findMetadataByUuid(uuid, false);
        BatchEditMode editMode = BatchEditMode.SAVE;
        ResponseEntity<String> builtMetadata = applyDataAnalysisOnRecord(uuid, datasource, layer, record, editMode);
        if (builtMetadata != null) return builtMetadata;
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private @Nullable ResponseEntity<String> applyDataAnalysisOnRecord(
            String uuid, String datasource, String layer, Metadata record, BatchEditMode editMode) {
        if (layer.equals(GDAL_DEFAULT_RASTER_LAYER)) {
            Optional<RasterInfo> layerProperties = analyzer.getRasterProperties(datasource);

            if (layerProperties.isPresent()) {
                String builtMetadata =
                        metadataBuilder.buildMetadata(uuid, record.getSchemaid(), layerProperties.get(), editMode);
                return new ResponseEntity<>(builtMetadata, HttpStatus.OK);
            }
        } else {
            Optional<DatasetInfo> layerProperties = analyzer.getLayerProperties(datasource, layer);

            if (layerProperties.isPresent()) {
                String builtMetadata =
                        metadataBuilder.buildMetadata(uuid, record.getSchemaid(), layerProperties.get(), editMode);
                return new ResponseEntity<>(builtMetadata, HttpStatus.OK);
            }
        }
        return null;
    }
}
