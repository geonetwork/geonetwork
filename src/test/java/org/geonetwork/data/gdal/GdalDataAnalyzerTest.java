/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.geonetwork.data.AttributeStatistics;
import org.geonetwork.data.DataFormat;
import org.geonetwork.data.gdal.model.GdalDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class GdalDataAnalyzerTest {

  private GdalDataAnalyzer analyzer;

  private static final boolean USE_GDAL_FROM_THE_SYSTEM = true;

  @BeforeEach
  void setUp() throws IOException {
    if (USE_GDAL_FROM_THE_SYSTEM) {
      analyzer = new GdalDataAnalyzer();
    } else {
      analyzer = new GdalDataAnalyzer();
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
    Optional<GdalDataset> layerProperties =
        analyzer.getLayerProperties(
            new ClassPathResource("data/samples/CEEUBG100kV2_1.shp").getFile().getCanonicalPath(),
            "CEEUBG100kV2_1");

    assertEquals(51738, layerProperties.get().getLayers().getFirst().getFeatureCount());
    // TODO: Add more assertions
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
    Object layerProperties =
        analyzer.getRasterProperties(
            new ClassPathResource("data/samples/ENI2018_CHA0018_00_V2020_1_AM_PILOT.tif")
                .getFile()
                .getCanonicalPath());

    assertNotNull(layerProperties);
  }
}
