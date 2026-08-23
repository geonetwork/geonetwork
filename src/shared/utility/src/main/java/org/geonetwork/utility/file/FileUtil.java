/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.utility.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import org.apache.commons.io.FilenameUtils;

public class FileUtil {

    private FileUtil() {
        // Make constructor private so class cannot be instantiated.
    }

    /**
     * Based on the file content or file extension return an appropiate mime type.
     *
     * @return The mime type or application/{{file_extension}} if none found.
     */
    public static String getFileContentType(Path file) throws IOException {
        String contentType = Files.probeContentType(file);
        if (contentType == null) {
            String ext = FilenameUtils.getExtension(file.toFile().getName()).toLowerCase(Locale.ROOT);
            switch (ext) {
                case "png":
                case "gif":
                case "bmp":
                    contentType = "image/" + ext;
                    break;
                case "tif":
                case "tiff":
                    contentType = "image/tiff";
                    break;
                case "jpg":
                case "jpeg":
                    contentType = "image/jpeg";
                    break;
                case "txt":
                    contentType = "text/plain";
                    break;
                case "htm":
                case "html":
                    contentType = "text/html";
                    break;
                default:
                    contentType = "application/" + ext;
            }
        }
        return contentType;
    }
}
