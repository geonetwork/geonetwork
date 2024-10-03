/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.data.gdal;

import static org.geonetwork.data.gdal.GdalUtils.*;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.data.AttributeStatistics;
import org.geonetwork.data.DataAnalyzerException;
import org.geonetwork.data.DataFormat;
import org.geonetwork.data.DatasetInfo;
import org.geonetwork.data.DatasetLayer;
import org.geonetwork.data.DatasetLayerField;
import org.geonetwork.data.DatasetLayerGeomField;
import org.geonetwork.data.RasterBand;
import org.geonetwork.data.RasterCornerCoordinates;
import org.geonetwork.data.RasterDataAnalyzer;
import org.geonetwork.data.RasterInfo;
import org.geonetwork.data.VectorDataAnalyzer;
import org.geonetwork.data.gdal.model.generated.GdalGdalinfoDto;
import org.geonetwork.data.gdal.model.generated.GdalGeoJSONPolygonDto;
import org.geonetwork.data.gdal.model.generated.GdalOgrinfoDatasetDto;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "org.geonetwork.tasks.data.analysis")
public class GdalDataAnalyzer implements RasterDataAnalyzer, VectorDataAnalyzer {

  public static final String OGR_INFO_APP = "ogrinfo";
  public static final String GDAL_INFO_APP = "gdalinfo";
  private String command;
  private String baseDir;
  private String mountPoint;
  private int timeoutInSeconds = 60;

  public GdalDataAnalyzer(
      @Value("${geonetwork.data.analyzer.gdal.command:}") String command,
      @Value("${geonetwork.data.analyzer.gdal.baseDir:}") String baseDir,
      @Value("${geonetwork.data.analyzer.gdal.mountPoint:}") String mountPoint,
      @Value("${geonetwork.data.analyzer.gdal.processTimeoutInSeconds:60}")
          int processTimeoutInSeconds) {
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
    String versionNumberAsString = version.replaceFirst("GDAL (\\d+\\.\\d+\\.\\d+).*", "$1").trim();
    int versionNumber = Integer.parseInt(versionNumberAsString.replace(".", ""));
    return versionNumber >= 370;
  }

  @Override
  public String getVersion() {
    Optional<String> version = execute(getVersionCommand(), timeoutInSeconds);
    if (version.isPresent()) {
      if (!isValidVersion(version.get())) {
        throw new RuntimeException(
            "GDAL version is not supported. JSON output is available since version 3.7");
      }
      return version.get();
    } else {
      throw new RuntimeException("GDAL version not found");
    }
  }

  @Override
  public List<DataFormat> getFormats() {
    return Stream.concat(
            getFormatsFromUtility(OGR_INFO_APP).stream(),
            getFormatsFromUtility(GDAL_INFO_APP).stream())
        .toList();
  }

  @Override
  public String getStatus() {
    return "";
  }

  @Override
  public List<String> getDatasourceLayers(String dataSource) {
    return executeCommand(
            buildUtilityCommand(OGR_INFO_APP),
            timeoutInSeconds,
            "-ro",
            buildDataSourcePath(dataSource))
        .map(GdalUtils::parseLayers)
        .orElse(List.of());
  }

  @Override
  public Optional<DatasetInfo> getLayerProperties(String dataSource, String layer) {
    return executeCommand(
            buildUtilityCommand(OGR_INFO_APP),
            timeoutInSeconds,
            "-json",
            "-so",
            "-ro",
            buildDataSourcePath(dataSource),
            layer)
        .map(this::parseDatasetInfo);
  }

  @Override
  public Optional<RasterInfo> getRasterProperties(String rasterSource) {
    return executeCommand(
            buildUtilityCommand(GDAL_INFO_APP),
            timeoutInSeconds,
            "-json",
            buildDataSourcePath(rasterSource))
        .map(output -> parseRasterInfo(output));
  }

  @Override
  public List<AttributeStatistics> getAttributesStatistics(
      String dataSource, String layer, List<String> attribute) {
    return attribute.stream()
        .map(attr -> getAttributeStatistics(buildDataSourcePath(dataSource), layer, attr))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private Optional<AttributeStatistics> getAttributeStatistics(
      String dataSource, String layer, String attributeName) {
    String query =
        String.format(
            "SELECT MIN(%s) AS min, MAX(%s) AS max FROM %s", attributeName, attributeName, layer);
    return execute(
            new CommandLine(buildUtilityCommand(OGR_INFO_APP))
                .addArgument("-json")
                .addArgument("-features")
                .addArgument("-ro")
                .addArgument("-sql")
                .addArgument(query, false)
                .addArgument(buildDataSourcePath(dataSource)),
            timeoutInSeconds)
        .flatMap(output -> parseAttributeStatistics(output, attributeName));
  }

  @Override
  public List<Object> getAttributeUniqueValues(
      String dataSource, String layer, String attributeName, int limit) {
    String query =
        String.format(
            "SELECT DISTINCT %s AS value FROM %s ORDER BY %s LIMIT %d",
            attributeName, layer, attributeName, limit);
    return execute(
            buildUtilityCommand(OGR_INFO_APP)
                .addArgument("-json")
                .addArgument("-features")
                .addArgument("-ro")
                .addArgument("-sql")
                .addArgument(query, false)
                .addArgument(buildDataSourcePath(dataSource)),
            timeoutInSeconds)
        .map(output -> parseAttributeValues(output, "value"))
        .orElse(List.of());
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

  private String buildDataSourcePath(String dataSource) {
    // TODO: Only applies to file system data sources
    return mountPoint.isEmpty() ? dataSource : dataSource.replace(baseDir, mountPoint);
  }

  private CommandLine buildUtilityCommand(String utility) {
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
    return execute(getFormatCommand(utility), timeoutInSeconds)
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

  private Optional<AttributeStatistics> parseAttributeStatistics(
      String json, String attributeName) {
    try {
      GdalOgrinfoDatasetDto dataset =
          buildObjectMapper().readValue(json, GdalOgrinfoDatasetDto.class);
      JsonNullable<Object> optionalProperties =
          dataset.getLayers().getFirst().getFeatures().getFirst().getProperties();
      if (optionalProperties.isPresent() && optionalProperties.get() instanceof Map properties) {
        return Optional.of(
            AttributeStatistics.builder()
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
      GdalOgrinfoDatasetDto dataset =
          buildObjectMapper().readValue(json, GdalOgrinfoDatasetDto.class);
      return dataset.getLayers().getFirst().getFeatures().stream()
          .map(
              feature -> {
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
      GdalOgrinfoDatasetDto dataset =
          buildObjectMapper().readValue(json, GdalOgrinfoDatasetDto.class);

      List<DatasetLayer> layers =
          dataset.getLayers().stream()
              .map(
                  l -> {
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
                        .fields(
                            l.getFields().stream()
                                .map(
                                    f ->
                                        DatasetLayerField.builder()
                                            .name(f.getName())
                                            .defaultValue(f.getDefaultValue())
                                            .nullable(f.getNullable())
                                            .type(f.getType().name())
                                            .build())
                                .collect(Collectors.toUnmodifiableList()))
                        .geometryFields(
                            l.getGeometryFields().stream()
                                .map(
                                    f ->
                                        DatasetLayerGeomField.builder()
                                            .name(f.getName())
                                            .type(
                                                f.getType().isPresent()
                                                    ? f.getType().get().toString()
                                                    : "")
                                            .crs(
                                                f.getCoordinateSystem().isPresent()
                                                    ? f.getCoordinateSystem().get().getWkt()
                                                    : "")
                                            .nullable(f.getNullable())
                                            .extent(f.getExtent())
                                            .build())
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

      RasterCornerCoordinates rasterCornerCoordinates =
          RasterCornerCoordinates.builder()
              .upperLeft(raster.getCornerCoordinates().getUpperLeft())
              .upperRight(raster.getCornerCoordinates().getUpperRight())
              .lowerLeft(raster.getCornerCoordinates().getLowerLeft())
              .lowerRight(raster.getCornerCoordinates().getLowerRight())
              .center(raster.getCornerCoordinates().getCenter())
              .build();

      return RasterInfo.builder()
          .description(raster.getDescription())
          .type(raster.getDriverShortName())
          .metadata(metadataInfo)
          .crs(raster.getCoordinateSystem().getWkt())
          .wgs84Extent(extent.getBbox().stream().map(Number::doubleValue).toList())
          .rasterCornerCoordinates(rasterCornerCoordinates)
          .bands(raster.getBands().stream().map(b -> RasterBand.builder().build()).toList())
          .size(raster.getSize())
          .build();

    } catch (IOException e) {
      throw new DataAnalyzerException(json);
    }
  }
}
