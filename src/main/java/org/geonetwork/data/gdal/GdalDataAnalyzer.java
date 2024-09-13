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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.exec.CommandLine;
import org.geonetwork.data.AttributeStatistics;
import org.geonetwork.data.DataFormat;
import org.geonetwork.data.RasterDataAnalyzer;
import org.geonetwork.data.VectorDataAnalyzer;
import org.geonetwork.data.gdal.model.GdalDataset;
import org.geonetwork.data.gdal.model.GdalGdalinfoSchema;

public class GdalDataAnalyzer implements RasterDataAnalyzer, VectorDataAnalyzer {

  // TODO: configuration from application.yml
  // docker run --rm -v ${metadataDir}:/data ghcr.io/osgeo/gdal:ubuntu-full-latest ogrinfo
  public static final String OGR_INFO_APP = "ogrinfo";
  public static final String GDAL_INFO_APP = "gdalinfo";

  @Override
  public String getName() {
    return "GDAL";
  }

  /** JSON output added in version 3.7. */
  protected boolean isValidVersion(String version) {
    String versionNumberAsString =
        version.replaceFirst("GDAL (\\d+\\.\\d+\\.\\d+),.*", "$1").trim();
    int versionNumber = Integer.parseInt(versionNumberAsString.replace(".", ""));
    return versionNumber >= 370;
  }

  @Override
  public String getVersion() {
    Optional<String> version = execute(getVersionCommand());
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
    return executeCommand(OGR_INFO_APP, "-ro", dataSource)
        .map(GdalUtils::parseLayers)
        .orElse(List.of());
  }

  @Override
  public Optional<GdalDataset> getLayerProperties(String dataSource, String layer) {
    return executeCommand(OGR_INFO_APP, "-json", "-so", "-ro", dataSource, layer)
        .map(output -> parseJson(output, GdalDataset.class));
  }

  @Override
  public Optional<GdalGdalinfoSchema> getRasterProperties(String rasterSource) {
    return executeCommand(GDAL_INFO_APP, "-json", rasterSource)
        .map(output -> parseJson(output, GdalGdalinfoSchema.class));
  }

  @Override
  public List<AttributeStatistics> getAttributesStatistics(
      String dataSource, String layer, List<String> attribute) {
    return attribute.stream()
        .map(attr -> getAttributeStatistics(dataSource, layer, attr))
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
            new CommandLine(OGR_INFO_APP)
                .addArgument("-json")
                .addArgument("-features")
                .addArgument("-ro")
                .addArgument("-sql")
                .addArgument(query, false)
                .addArgument(dataSource))
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
            new CommandLine(OGR_INFO_APP)
                .addArgument("-json")
                .addArgument("-features")
                .addArgument("-ro")
                .addArgument("-sql")
                .addArgument(query, false)
                .addArgument(dataSource))
        .map(output -> parseAttributeValues(output, "value"))
        .orElse(List.of());
  }

  @Override
  public Object getFeatures(String dataSource, String layer, int limit) {
    return null;
  }

  private <T> T parseJson(String json, Class<T> clazz) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      return objectMapper.readValue(json, clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Optional<AttributeStatistics> parseAttributeStatistics(
      String json, String attributeName) {
    try {
      GdalDataset dataset = new ObjectMapper().readValue(json, GdalDataset.class);
      if (dataset.getLayers().getFirst().getFeatures().getFirst().getProperties()
          instanceof Map properties) {
        return Optional.of(
            AttributeStatistics.builder()
                .name(attributeName)
                .statistic(AttributeStatistics.StatisticFields.MIN, (Double) properties.get("min"))
                .statistic(AttributeStatistics.StatisticFields.MAX, (Double) properties.get("max"))
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
      GdalDataset dataset = new ObjectMapper().readValue(json, GdalDataset.class);
      return dataset.getLayers().getFirst().getFeatures().stream()
          .map(
              feature -> {
                if (feature.getProperties() instanceof Map properties) {
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
}
