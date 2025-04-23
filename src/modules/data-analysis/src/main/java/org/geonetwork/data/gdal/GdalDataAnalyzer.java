/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.data.gdal;

import static org.geonetwork.data.geom.GeomUtil.NUMBER_OF_DECIMALS_IN_WGS84;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.data.DataAnalyzerException;
import org.geonetwork.data.RasterDataAnalyzer;
import org.geonetwork.data.VectorDataAnalyzer;
import org.geonetwork.data.gdal.model.generated.GdalGdalinfoDto;
import org.geonetwork.data.gdal.model.generated.GdalGeoJSONPolygonDto;
import org.geonetwork.data.gdal.model.generated.GdalOgrinfoDatasetDto;
import org.geonetwork.data.geom.GeomUtil;
import org.geonetwork.data.model.AttributeStatistics;
import org.geonetwork.data.model.DataFormat;
import org.geonetwork.data.model.DatasetInfo;
import org.geonetwork.data.model.DatasetLayer;
import org.geonetwork.data.model.DatasetLayerField;
import org.geonetwork.data.model.DatasetLayerGeomField;
import org.geonetwork.data.model.RasterBand;
import org.geonetwork.data.model.RasterCornerCoordinates;
import org.geonetwork.data.model.RasterInfo;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GdalDataAnalyzer implements RasterDataAnalyzer, VectorDataAnalyzer {

    public static final String OGR_INFO_APP = "ogrinfo";
    public static final String GDAL_INFO_APP = "gdalinfo";
    public static final String GDAL_RASTERIZE_APP = "gdal_rasterize";
    public static final String GDAL_TRANSLATE_APP = "gdal_translate";

    private String command;
    private String baseDir;
    private String mountPoint;

    @Getter
    private int timeoutInSeconds = 60;

    /**
     * GdalDataAnalyzer constructor.
     *
     * @param command Command to execute the GDAL process.
     * @param baseDir GeoNetwork data directory.
     * @param mountPoint GeoNetwork data directory mount path (required when running GDAL in Docker).
     * @param processTimeoutInSeconds Max number of seconds to wait for the GDAL process to finish.
     */
    public GdalDataAnalyzer(
            @Value("${geonetwork.data.analyzer.gdal.command:}") String command,
            @Value("${geonetwork.directory.data:}") String baseDir,
            @Value("${geonetwork.data.analyzer.gdal.mountPoint:}") String mountPoint,
            @Value("${geonetwork.data.analyzer.gdal.processTimeoutInSeconds:60}") int processTimeoutInSeconds) {
        this.command = command;
        this.baseDir = baseDir;
        this.mountPoint = mountPoint;
        this.timeoutInSeconds = processTimeoutInSeconds;
    }

    @Override
    public String getName() {
        return "GDAL";
    }

    /** JSON output added in version 3.7. */
    protected boolean isValidVersion(String version) {
        String versionNumberAsString =
                version.replaceFirst("GDAL (\\d+\\.\\d+\\.\\d+).*", "$1").trim();
        int versionNumber = Integer.parseInt(versionNumberAsString.replace(".", ""));
        return versionNumber >= 370;
    }

    @Override
    public String getVersion() {
        Optional<String> version = GdalUtils.execute(getVersionCommand(), timeoutInSeconds);
        if (version.isEmpty()) {
            throw new DataAnalyzerException("GDAL version not found");
        }
        if (!isValidVersion(version.get())) {
            throw new DataAnalyzerException(
                    "GDAL version is not supported. JSON output is available since version 3.7");
        }
        return version.get();
    }

    @Override
    public List<DataFormat> getFormats() {
        return Stream.concat(
                        getFormatsFromUtility(OGR_INFO_APP).stream(), getFormatsFromUtility(GDAL_INFO_APP).stream())
                .toList();
    }

    @Override
    public String getStatus() {
        try {
            getVersion();
            return "GREEN";
        } catch (Exception e) {
            return "RED";
        }
    }

    @Override
    public List<String> getDatasourceLayers(String dataSource) {
        boolean datasourceHasRasterExtension = hasRasterExtension(dataSource);

        DataAnalyzerException vectorException = null;
        DataAnalyzerException rasterException = null;

        try {
            if (datasourceHasRasterExtension) {
                return getDatasourceRasterLayers(dataSource);
            } else {
                return getDatasourceVectorLayers(dataSource);
            }
        } catch (DataAnalyzerException dataAnalyzerException) {
            if (datasourceHasRasterExtension) {
                rasterException = dataAnalyzerException;
            } else {
                vectorException = dataAnalyzerException;
            }
        }

        // Fallback
        try {
            if (datasourceHasRasterExtension) {
                return getDatasourceVectorLayers(dataSource);
            } else {
                return getDatasourceRasterLayers(dataSource);
            }
        } catch (DataAnalyzerException dataAnalyzerException) {
            if (datasourceHasRasterExtension) {
                vectorException = dataAnalyzerException;
            } else {
                rasterException = dataAnalyzerException;
            }
        }

        throw new DataAnalyzerException(String.format(
                "Error while collecting layers in datasource %s.%n* Vector analysis error: %s%n* Raster analysis error: %s",
                dataSource, vectorException.getMessage(), rasterException.getMessage()));
    }

    private List<String> getDatasourceRasterLayers(String dataSource) {
        Optional<List<String>> rasterLayers = GdalUtils.executeCommand(
                        buildUtilityCommand(GDAL_INFO_APP), timeoutInSeconds, buildDataSourcePath(dataSource))
                .map(GdalUtils::parseRasterLayers);
        if (rasterLayers.isPresent() && !rasterLayers.get().isEmpty()) {
            return rasterLayers.get();
        }

        return List.of();
    }

    private List<String> getDatasourceVectorLayers(String dataSource) {
        Optional<List<String>> vectorLayers = GdalUtils.executeCommand(
                        buildUtilityCommand(OGR_INFO_APP), timeoutInSeconds, "-ro", buildDataSourcePath(dataSource))
                .map(GdalUtils::parseLayers);
        if (vectorLayers.isPresent() && !vectorLayers.get().isEmpty()) {
            return vectorLayers.get();
        }

        return List.of();
    }

    /**
     * Data analysis cache contains information about the analyzed data sources and layers.
     *
     * <p>This cache is used to avoid unnecessary calls to the GDAL utilities when user run preview and then apply the
     * analysis to a record.
     */
    @CacheEvict(value = "data-analysis", allEntries = true)
    @Scheduled(fixedRateString = "${geonetwork.data.analyzer.gdal.cacheTimeoutInMilliseconds:30000}")
    public void emptyCache() {}

    @Cacheable(cacheNames = "data-analysis")
    @Override
    public Optional<DatasetInfo> getLayerProperties(String dataSource, String layer) {
        return GdalUtils.executeCommand(
                        buildUtilityCommand(OGR_INFO_APP),
                        timeoutInSeconds,
                        "-json",
                        "-so",
                        "-ro",
                        buildDataSourcePath(dataSource),
                        layer)
                .map(this::parseDatasetInfo);
    }

    @Cacheable(cacheNames = "data-analysis")
    @Override
    public Optional<RasterInfo> getRasterProperties(String rasterSource) {
        return GdalUtils.executeCommand(
                        buildUtilityCommand(GDAL_INFO_APP),
                        timeoutInSeconds,
                        "-json",
                        buildDataSourcePath(rasterSource))
                .map(this::parseRasterInfo);
    }

    @Override
    public List<AttributeStatistics> getAttributesStatistics(String dataSource, String layer, List<String> attribute) {
        return attribute.stream()
                .map(attr -> getAttributeStatistics(buildDataSourcePath(dataSource), layer, attr))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<AttributeStatistics> getAttributeStatistics(
            String dataSource, String layer, String attributeName) {
        String query =
                String.format("SELECT MIN(%s) AS min, MAX(%s) AS max FROM %s", attributeName, attributeName, layer);
        return GdalUtils.execute(
                        new CommandLine(buildUtilityCommand(OGR_INFO_APP))
                                .addArgument("-json")
                                .addArgument("-features")
                                .addArgument("-ro")
                                .addArgument("-dialect")
                                .addArgument("SQLite")
                                .addArgument("-sql")
                                .addArgument(query, false)
                                .addArgument(buildDataSourcePath(dataSource)),
                        timeoutInSeconds)
                .flatMap(output -> parseAttributeStatistics(output, attributeName));
    }

    @Override
    public List<Object> getAttributeUniqueValues(String dataSource, String layer, String attributeName, int limit) {
        String query = String.format(
                "SELECT DISTINCT %s AS value FROM %s ORDER BY %s LIMIT %d", attributeName, layer, attributeName, limit);
        return GdalUtils.execute(
                        buildUtilityCommand(OGR_INFO_APP)
                                .addArgument("-json")
                                .addArgument("-features")
                                .addArgument("-ro")
                                .addArgument("-dialect")
                                .addArgument("SQLite")
                                .addArgument("-sql")
                                .addArgument(query, false)
                                .addArgument(buildDataSourcePath(dataSource)),
                        timeoutInSeconds)
                .map(output -> parseAttributeValues(output, "value"))
                .orElse(List.of());
    }

    public boolean isRasterLayer(String datasource) {
        try {
            getRasterProperties(datasource);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Object getFeatures(String dataSource, String layer, int limit) {
        return null;
    }

    public CommandLine getVersionCommand() {
        return buildUtilityCommand(OGR_INFO_APP).addArgument("--version");
    }

    public CommandLine getFormatCommand(String utility) {
        return buildUtilityCommand(utility).addArgument("--formats");
    }

    protected String buildDataSourcePath(String dataSource) {
        return mountPoint.isEmpty()
                        || dataSource.matches("(?i)^(http|wfs|vis|db|/vsizip//vsicurl/http|/vsicurl/http).*")
                ? dataSource
                : dataSource.replace(baseDir, mountPoint);
    }

    protected String buildDataSourceTempPath(String dataSource) {
        return mountPoint.isEmpty() ? dataSource : mountPoint + dataSource;
    }

    protected CommandLine buildUtilityCommand(String utility) {
        if (command.isEmpty()) {
            return new CommandLine(utility);
        } else {
            String[] commandArgs = StringUtils.split(command, " ");
            CommandLine commandLine = new CommandLine(commandArgs[0]);
            for (int i = 1; i < commandArgs.length; i++) {
                commandLine.addArgument(commandArgs[i]);
            }
            commandLine.addArgument(utility);
            return commandLine;
        }
    }

    protected List<DataFormat> getFormatsFromUtility(String utility) {
        return GdalUtils.execute(getFormatCommand(utility), timeoutInSeconds)
                .map(GdalUtils::parseFormats)
                .orElse(List.of());
    }

    private ObjectMapper buildObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JsonNullableModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);
        return objectMapper;
    }

    @SuppressWarnings("unused")
    private <T> T parseJson(String json, Class<T> clazz) {
        try {
            return buildObjectMapper().readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<AttributeStatistics> parseAttributeStatistics(String json, String attributeName) {
        try {
            GdalOgrinfoDatasetDto dataset = buildObjectMapper().readValue(json, GdalOgrinfoDatasetDto.class);
            JsonNullable<Object> optionalProperties =
                    dataset.getLayers().getFirst().getFeatures().getFirst().getProperties();
            if (optionalProperties.isPresent() && optionalProperties.get() instanceof Map properties) {
                return Optional.of(AttributeStatistics.builder()
                        .name(attributeName)
                        .statistic(AttributeStatistics.StatisticFields.MIN, properties.get("min"))
                        .statistic(AttributeStatistics.StatisticFields.MAX, properties.get("max"))
                        .build());
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Object> parseAttributeValues(String json, String attributeName) {
        try {
            GdalOgrinfoDatasetDto dataset = buildObjectMapper().readValue(json, GdalOgrinfoDatasetDto.class);
            return dataset.getLayers().getFirst().getFeatures().stream()
                    .map(feature -> {
                        if (feature.getProperties().isPresent()
                                && feature.getProperties().get() instanceof Map properties) {
                            return properties.get(attributeName);
                        } else {
                            return null;
                        }
                    })
                    .toList();
        } catch (IOException e) {
            //      throw new RuntimeException(e);
            return List.of();
        }
    }

    private DatasetInfo parseDatasetInfo(String json) {
        try {
            GdalOgrinfoDatasetDto dataset = buildObjectMapper().readValue(json, GdalOgrinfoDatasetDto.class);

            List<DatasetLayer> layers = dataset.getLayers().stream()
                    .map(l -> {
                        Map<String, Object> layerMetadataInfo = new HashMap<>();
                        if (l.getMetadata().getAdditionalProperties() != null) {
                            l.getMetadata().getAdditionalProperties().entrySet().stream()
                                    .forEach(e -> layerMetadataInfo.put(e.getKey(), e.getValue()));
                        }

                        return DatasetLayer.builder()
                                .name(l.getName())
                                .fidColumnName(l.getFidColumnName())
                                .featureCount(l.getFeatureCount())
                                .metadata(layerMetadataInfo)
                                .fields(l.getFields().stream()
                                        .map(f -> DatasetLayerField.builder()
                                                .name(f.getName())
                                                .defaultValue(f.getDefaultValue())
                                                .nullable(f.getNullable())
                                                .type(f.getType().name())
                                                .build())
                                        .collect(Collectors.toUnmodifiableList()))
                                .geometryFields(l.getGeometryFields().stream()
                                        .map(f -> {
                                            var crs = f.getCoordinateSystem().isPresent()
                                                            && f.getCoordinateSystem()
                                                                            .get()
                                                                    != null
                                                    ? f.getCoordinateSystem()
                                                            .get()
                                                            .getWkt()
                                                    : "";

                                            var datasetLayerGeomFieldBuilder = DatasetLayerGeomField.builder()
                                                    .name(f.getName())
                                                    .type(
                                                            f.getType().isPresent()
                                                                    ? f.getType()
                                                                            .get()
                                                                            .toString()
                                                                    : "")
                                                    .crs(crs)
                                                    .nullable(f.getNullable());

                                            if (f.getExtent() != null) {
                                                List<Double> wgs84Extent = GeomUtil.calculateWgs84Bbox(
                                                        GeomUtil.parseCrsCode(crs),
                                                        f.getExtent().stream()
                                                                .map(BigDecimal::doubleValue)
                                                                .toList());
                                                if (wgs84Extent != null) {
                                                    datasetLayerGeomFieldBuilder.extent(wgs84Extent.stream()
                                                            .map(d -> BigDecimal.valueOf(d)
                                                                    .setScale(
                                                                            NUMBER_OF_DECIMALS_IN_WGS84,
                                                                            RoundingMode.HALF_UP))
                                                            .toList());
                                                }
                                            }
                                            return datasetLayerGeomFieldBuilder.build();
                                        })
                                        .collect(Collectors.toUnmodifiableList()))
                                .build();
                    })
                    .toList();

            Map<String, Object> metadataInfo = new HashMap<>();
            if (dataset.getMetadata().getAdditionalProperties() != null) {
                dataset.getMetadata().getAdditionalProperties().entrySet().stream()
                        .forEach(e -> metadataInfo.put(e.getKey(), e.getValue().toString()));
            }

            return DatasetInfo.builder()
                    .description(dataset.getDescription())
                    .formatDescription(dataset.getDriverShortName())
                    .format(dataset.getDriverLongName())
                    .metadata(metadataInfo)
                    .layers(layers)
                    .build();
        } catch (IOException e) {
            throw new DataAnalyzerException(json);
        }
    }

    private RasterInfo parseRasterInfo(String json) {
        try {
            GdalGdalinfoDto raster = buildObjectMapper().readValue(json, GdalGdalinfoDto.class);

            Map<String, Object> metadataInfo = new HashMap<>();
            raster.getMetadata().getAdditionalProperties().entrySet().stream()
                    .forEach(e -> metadataInfo.put(e.getKey(), e.getValue().toString()));

            GdalGeoJSONPolygonDto extent = (GdalGeoJSONPolygonDto) raster.getWgs84Extent();

            RasterCornerCoordinates rasterCornerCoordinates = RasterCornerCoordinates.builder()
                    .upperLeft(raster.getCornerCoordinates().getUpperLeft())
                    .upperRight(raster.getCornerCoordinates().getUpperRight())
                    .lowerLeft(raster.getCornerCoordinates().getLowerLeft())
                    .lowerRight(raster.getCornerCoordinates().getLowerRight())
                    .center(raster.getCornerCoordinates().getCenter())
                    .build();

            return RasterInfo.builder()
                    .description(raster.getDescription())
                    .format(raster.getDriverShortName())
                    .formatDescription(raster.getDriverLongName())
                    .metadata(metadataInfo)
                    .crs(raster.getCoordinateSystem().getWkt())
                    .wgs84Extent(getWgs84Extent(extent))
                    .rasterCornerCoordinates(rasterCornerCoordinates)
                    .bands(raster.getBands().stream()
                            .map(b -> RasterBand.builder().build())
                            .toList())
                    .size(raster.getSize())
                    .geoTransform(raster.getGeoTransform())
                    .build();

        } catch (IOException e) {
            throw new DataAnalyzerException(json);
        }
    }

    private static List<Double> getWgs84Extent(GdalGeoJSONPolygonDto extent) {
        // TODO: Reproject extent to WGS84 and add polygon in shape property
        if (!extent.getBbox().isEmpty()) {
            return extent.getBbox().stream().map(Number::doubleValue).toList();
        } else if (!extent.getCoordinates().isEmpty() && extent.getCoordinates().getFirst() != null) {
            try {
                List<Coordinate> coordinates = extent.getCoordinates().getFirst().stream()
                        .map(c -> new Coordinate(
                                c.getFirst().doubleValue(), c.getLast().doubleValue()))
                        .toList();
                Polygon geometry = new GeometryFactory().createPolygon(coordinates.toArray(new Coordinate[0]));

                return List.of(
                        geometry.getEnvelopeInternal().getMinX(),
                        geometry.getEnvelopeInternal().getMinY(),
                        geometry.getEnvelopeInternal().getMaxX(),
                        geometry.getEnvelopeInternal().getMaxY());
            } catch (Exception e) {
                log.atWarn().log(e.getMessage());
                return List.of();
            }
        }
        return List.of();
    }

    private boolean hasRasterExtension(String datasource) {
        String datasourceValue = datasource.toLowerCase(Locale.ROOT);

        return (datasourceValue.endsWith(".tiff") || datasourceValue.endsWith(".tif"))
                || datasourceValue.endsWith(".jpg")
                || datasourceValue.endsWith(".jpeg");
    }
}
