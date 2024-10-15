/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.geonetwork.data.AttributeStatistics;
import org.geonetwork.data.DataFormat;
import org.geonetwork.data.DatasetInfo;
import org.geonetwork.data.DatasetLayer;
import org.geonetwork.data.DatasetLayerField;
import org.geonetwork.data.RasterInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest(classes = {TestConfiguration.class})
class GdalDataAnalyzerTest {

  private GdalDataAnalyzer analyzer;

  private static final boolean USE_GDAL_FROM_THE_SYSTEM = false;

  @BeforeEach
  void setUp() throws IOException, InterruptedException {
    if (USE_GDAL_FROM_THE_SYSTEM) {
      analyzer = new GdalDataAnalyzer("", "", "", 60);
    } else {
      String dataFolder = new ClassPathResource("data/samples").getFile().toString();
      String mountPoint = "/data";
      analyzer =
          new GdalDataAnalyzer(
              String.format(
                  "docker run --rm -v %s:%s ghcr.io/osgeo/gdal:alpine-normal-latest ",
                  dataFolder, mountPoint),
              dataFolder,
              mountPoint,
              60);
      //      GdalDataAnalyzer gdalDataAnalyzerMock = mock(GdalDataAnalyzer.class);
      //      when(gdalDataAnalyzerMock.getName()).thenReturn("GDAL");
      //      when(gdalDataAnalyzerMock.getVersion()).thenReturn("GDAL 3.2.0, released 2020/10/26");
      //
      //      try (MockedStatic<GdalUtils> gdalUtilsMock = mockStatic(GdalUtils.class)) {
      //        gdalUtilsMock
      //            .when(() -> GdalUtils.execute(eq(GdalUtils.getVersionCommand())))
      //            .thenReturn(Optional.of("GDAL 3.2.0, released 2020/10/26"));
      //      }
    }
  }

  @Test
  void getNameReturnsCorrectName() {
    assertEquals("GDAL", analyzer.getName());
  }

  @Test
  void getVersionReturnsVersion() {
    assertTrue(analyzer.getVersion().startsWith("GDAL "));
  }

  @Test
  void checkValidVersion() {
    assertFalse(analyzer.isValidVersion("GDAL 2.2.2, rel"));
    assertTrue(analyzer.isValidVersion("GDAL 3.7.0, rel"));
    assertTrue(analyzer.isValidVersion("GDAL 4.7.0, rel"));
    assertTrue(
        analyzer.isValidVersion(
            "GDAL 3.10.0dev-d1efd4e8b238d3c76d62dd66e65673d3a15c4ee2, released 2024/08/28\n"));
  }

  @Test
  void checkFormatListParsing() {
    try {
      String ogrFormats =
          Files.readString(Path.of(new ClassPathResource("data/ogrinfo-formats.txt").getURI()));
      List<DataFormat> gdalFormats = GdalUtils.parseFormats(ogrFormats);
      assertEquals(ogrFormats.split(System.lineSeparator()).length - 1, gdalFormats.size());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void getFormats() {
    List<DataFormat> formats = analyzer.getFormats();
    assertFalse(formats.isEmpty());
  }

  @Test
  void getDatasourceLayers() throws IOException {
    List<String> datasourceLayers =
        analyzer.getDatasourceLayers(
            new ClassPathResource("data/samples/CEEUBG100kV2_1.shp").getFile().getCanonicalPath());
    assertEquals("CEEUBG100kV2_1", datasourceLayers.getFirst());
  }

  @Test
  void getLayerProperties() throws IOException {
    Optional<DatasetInfo> layerProperties =
        analyzer.getLayerProperties(
            new ClassPathResource("data/samples/CEEUBG100kV2_1.shp").getFile().getCanonicalPath(),
            "CEEUBG100kV2_1");

    assertEquals("ESRI Shapefile", layerProperties.get().getFormatDescription());

    DatasetLayer layer = layerProperties.get().getLayers().getFirst();
    assertEquals(BigDecimal.valueOf(51738), layer.getFeatureCount());
    assertEquals("CEEUBG100kV2_1", layer.getName());
    assertEquals("CESGCD", layer.getFields().getFirst().getName());
    assertEquals("LineString", layer.getGeometryFields().getFirst().getType());
    assertEquals(
        "[-61.805814108435243, 4.2138689443886506, 34.605668981354938, 65.90833200184602]",
        layer.getGeometryFields().getFirst().getExtent().toString());

    assertTrue(layer.getGeometryFields().getFirst().getCrs().contains("ID[\"EPSG\",4258]"));

    assertEquals(null, layer.getFidColumnName());
    assertEquals(
        "CESGCD,CESGLN,NURGCDV7,CEMOV1,CEMOV2,CEEVV1,CEEVV2,CEGOV2,CEDWV1,CEDWV2,CEDAV2,CEDC,COVERED",
        layer.getFields().stream()
            .map(DatasetLayerField::getName)
            .collect(Collectors.joining(",")));
    assertEquals(
        "STRING,REAL,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING",
        layer.getFields().stream().map(f -> f.getType()).collect(Collectors.joining(",")));
  }

  @Test
  void getAttributeStatistics() throws IOException {
    String attributeName = "CESGLN";
    List<AttributeStatistics> attributesStatistics =
        analyzer.getAttributesStatistics(
            new ClassPathResource("data/samples/CEEUBG100kV2_1.shp").getFile().getCanonicalPath(),
            "CEEUBG100kV2_1",
            List.of(attributeName));

    assertEquals(attributeName, attributesStatistics.getFirst().getName());
    assertEquals(
        0.0D,
        attributesStatistics
            .getFirst()
            .getStatistics()
            .get(AttributeStatistics.StatisticFields.MIN));
    assertEquals(
        1921407.14904D,
        attributesStatistics
            .getFirst()
            .getStatistics()
            .get(AttributeStatistics.StatisticFields.MAX));
  }

  @Test
  void getAttributeUniqueValues() throws IOException {

    List<Object> attributeUniqueValues =
        analyzer.getAttributeUniqueValues(
            new ClassPathResource("data/samples/CEEUBG100kV2_1.shp").getFile().getCanonicalPath(),
            "CEEUBG100kV2_1",
            "CEDWV2",
            10);

    assertEquals(Arrays.asList(null, "N", "Y"), attributeUniqueValues);
  }

  @Test
  void getRasterProperties() throws IOException {
    Optional<RasterInfo> layerProperties =
        analyzer.getRasterProperties(
            new ClassPathResource("data/samples/ENI2018_CHA0018_00_V2020_1_AM_PILOT.tif")
                .getFile()
                .getCanonicalPath());

    assertEquals("GTiff", layerProperties.get().getType());

    assertNotNull(layerProperties);
    assertTrue(layerProperties.get().getCrs().contains("\"EPSG\",3035"));
    // assertEquals(3035, layerProperties.get().getStac().getProjColonEpsg().get());
    assertEquals(
        "7146300.0,2543100.0",
        layerProperties.get().getRasterCornerCoordinates().getLowerLeft().stream()
            .map(BigDecimal::toString)
            .collect(Collectors.joining(",")));
    assertEquals(
        "7203800.0,2600700.0",
        layerProperties.get().getRasterCornerCoordinates().getUpperRight().stream()
            .map(BigDecimal::toString)
            .collect(Collectors.joining(",")));
    assertEquals(575, layerProperties.get().getSize().get(0));
    assertEquals(576, layerProperties.get().getSize().get(1));
    assertEquals(1, layerProperties.get().getBands().size());
  }

  @Test
  void getDatasourceLayersAndPropertiesFromParquet() throws IOException {
    String parquetFile =
        new ClassPathResource("data/samples/Weather.parquet").getFile().getCanonicalPath();
    List<String> datasourceLayers = analyzer.getDatasourceLayers(parquetFile);
    assertTrue(datasourceLayers.stream().anyMatch("Weather"::equals));

    DatasetInfo properties = analyzer.getLayerProperties(parquetFile, "Weather").get();
    assertEquals("Parquet", properties.getFormatDescription());
    assertEquals(22, properties.getLayers().getFirst().getFields().size());
  }

  @Test
  void getDatasourceLayersAndPropertiesFromWfs() {
    //    String wfsUrl = "WFS:https://sextant.ifremer.fr/services/wfs/environnement_marin";
    //    String wfsTypeName = "surval_lieux_actifs_ligne";
    String wfsUrl = "WFS:https://geoservices.brgm.fr/risques";
    String wfsTypeName = "ms:NEOPAL_FAILLE";
    List<String> datasourceLayers = analyzer.getDatasourceLayers(wfsUrl);
    assertTrue(datasourceLayers.stream().anyMatch(wfsTypeName::equals));

    analyzer.getLayerProperties(wfsUrl, wfsTypeName);
    DatasetInfo properties = analyzer.getLayerProperties(wfsUrl, wfsTypeName).get();
    assertEquals("WFS", properties.getFormatDescription());
    if (properties.getMetadata().get("") instanceof Map serviceProperties) {
      assertEquals("GéoServices : risques naturels et industriels", serviceProperties.get("TITLE"));
      assertEquals(
          "Ensemble des services d'accès aux données sur les risques naturels et industriels"
              + " diffusées par le BRGM",
          serviceProperties.get("ABSTRACT"));
      assertEquals("BRGM", serviceProperties.get("PROVIDER_NAME"));
    }
    if (properties.getLayers().getFirst().getMetadata().get("") instanceof Map layerProperties) {
      assertEquals("Déformations récentes et paléoséismes - Failles", layerProperties.get("TITLE"));
      assertEquals(
          "Néopal est la base de données recensant les arguments géologiques de déformation plus"
              + " récentes que deux millions d'années (indices néotectoniques) en France, publiés"
              + " dans la littérature scientifique et évalués par un comité d'experts.",
          layerProperties.get("ABSTRACT"));
      assertEquals("Risques;INSPIRE:Zones à risque naturel", layerProperties.get("KEYWORD_1"));
    }
  }
}
