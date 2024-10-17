package org.geonetwork.utility;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

public class JarFileCopy {
  public static void copyFolder(URI uri, String folderName, Path targetDir)
      throws IOException, URISyntaxException {
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
    Files.walk(sourceDir).forEach(source -> {
      try {
        Path destination = targetDir.resolve(sourceDir.relativize(source).toString());
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