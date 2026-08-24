/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.utility;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.stream.Stream;

public class JarFileCopy {
    public static void copyFolder(URI uri, String folderName, Path targetDir) throws IOException {
        boolean isJarFile = uri.toString().contains(".jar!");
        if (isJarFile) {
            try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                Path jarPath = fileSystem.getPath(folderName);
                copyDirectory(jarPath, targetDir);
            }
        } else {
            copyDirectory(Path.of(uri), targetDir);
        }
    }

    private static void copyDirectory(Path sourceDir, Path targetDir) throws IOException {
        try (Stream<Path> paths = Files.walk(sourceDir)) {
            paths.forEach(source -> {
                try {
                    Path destination =
                            targetDir.resolve(sourceDir.relativize(source).toString());
                    if (Files.isDirectory(source)) {
                        Files.createDirectories(destination);
                    } else {
                        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error copying file", e);
                }
            });
        }
    }
}
