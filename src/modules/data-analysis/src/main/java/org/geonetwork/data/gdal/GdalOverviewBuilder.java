/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal;

import static org.geonetwork.constants.Geonet.TMP_FOLDER_PREFIX;
import static org.geonetwork.data.gdal.GdalDataAnalyzer.GDAL_RASTERIZE_APP;
import static org.geonetwork.data.gdal.GdalDataAnalyzer.GDAL_TRANSLATE_APP;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

@Component
@Slf4j
@AllArgsConstructor
public class GdalOverviewBuilder {

    private GdalDataAnalyzer gdalDataAnalyzer;

    private static final int IMAGE_WIDTH = 1000;
    private static final int IIMAGE_HEIGHT = 1000;

    public byte[] buildOverview(String datasource, String layer) throws IOException {
        return gdalDataAnalyzer.isRasterLayer(datasource)
                ? buildRasterOverview(datasource, layer)
                : buildVectorOverview(datasource, layer);
    }

    /** gdal_translate CCM.tif CCM.png */
    private byte[] buildRasterOverview(String datasource, String layer) throws IOException {
        Path tempDirectory = Files.createTempDirectory(TMP_FOLDER_PREFIX);
        Path tempFile = Files.createTempFile(tempDirectory, "gn-overview" + layer, ".png");
        byte[] imageBytes;
        try {
            GdalUtils.execute(
                    new CommandLine(gdalDataAnalyzer.buildUtilityCommand(GDAL_TRANSLATE_APP))
                            .addArgument("-q")
                            .addArgument("-outsize")
                            .addArgument(String.format("%d", IMAGE_WIDTH))
                            .addArgument("0")
                            .addArgument(gdalDataAnalyzer.buildDataSourcePath(datasource))
                            .addArgument(gdalDataAnalyzer.buildDataSourceTempPath(
                                    tempFile.toFile().getCanonicalPath(),
                                    tempDirectory.toFile().getParent())),
                    gdalDataAnalyzer.getTimeoutInSeconds());

            imageBytes = Files.readAllBytes(tempFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            FileSystemUtils.deleteRecursively(tempDirectory);
        }
        return imageBytes;
    }

    /**
     * gdal_rasterize -burn 25 -burn 25 -burn 25 -ot Byte -ts 1000 1000 -a_nodata 0.0 -l CCM CCM.shp CCM.tif
     * gdal_translate CCM.tif CCM.png
     */
    public byte[] buildVectorOverview(String datasource, String layer) throws IOException {
        Path tempDirectory = Files.createTempDirectory(TMP_FOLDER_PREFIX);
        Path tempFile = Files.createTempFile(tempDirectory, "gn-overview" + layer, ".tif");
        String temporaryTiffFile = tempFile.toFile().getCanonicalPath();
        String temporaryPngFile = temporaryTiffFile.replace(".tif", ".png");
        byte[] imageBytes;
        int bandColorR = 25;
        int bandColorG = 25;
        int bandColorB = 25;
        try {
            GdalUtils.execute(
                    new CommandLine(gdalDataAnalyzer.buildUtilityCommand(GDAL_RASTERIZE_APP))
                            .addArgument("-burn")
                            .addArgument(String.format("%d", bandColorR))
                            .addArgument("-burn")
                            .addArgument(String.format("%d", bandColorG))
                            .addArgument("-burn")
                            .addArgument(String.format("%d", bandColorB))
                            .addArgument("-ot")
                            .addArgument("Byte")
                            .addArgument("-ts")
                            .addArgument(String.format("%d", IMAGE_WIDTH))
                            .addArgument(String.format("%d", IIMAGE_HEIGHT))
                            .addArgument("-a_nodata")
                            .addArgument("0.0")
                            .addArgument("-l")
                            .addArgument(layer)
                            .addArgument(gdalDataAnalyzer.buildDataSourcePath(datasource))
                            .addArgument(gdalDataAnalyzer.buildDataSourceTempPath(
                                    temporaryTiffFile, tempDirectory.toFile().getParent())),
                    gdalDataAnalyzer.getTimeoutInSeconds());

            GdalUtils.execute(
                    new CommandLine(gdalDataAnalyzer.buildUtilityCommand(GDAL_TRANSLATE_APP))
                            .addArgument("-q")
                            .addArgument(gdalDataAnalyzer.buildDataSourceTempPath(
                                    temporaryTiffFile, tempDirectory.toFile().getParent()))
                            .addArgument(gdalDataAnalyzer.buildDataSourceTempPath(
                                    temporaryPngFile, tempDirectory.toFile().getParent())),
                    gdalDataAnalyzer.getTimeoutInSeconds());

            imageBytes = Files.readAllBytes(Paths.get(temporaryPngFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            FileSystemUtils.deleteRecursively(tempDirectory);
        }
        return imageBytes;
    }
}
