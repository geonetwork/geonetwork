/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.config;

import static org.geonetwork.schemas.constant.Geonet.Path.IMPORT_STYLESHEETS_SCHEMA_PREFIX;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.schemas.SchemaManager;
import org.geonetwork.schemas.constant.Geonet;
import org.geonetwork.utility.io.FilePathChecker;
import org.geonetwork.utility.io.IO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * The GeoNetwork data directory is the location on the file system where GeoNetwork stores all of
 * its custom configuration. This configuration defines such things as: What thesaurus is used by
 * GeoNetwork? What schema is plugged in GeoNetwork?. The data directory also contains a number of
 * support files used by GeoNetwork for various purposes (eg. Lucene index, spatial index, logos).
 */
@Slf4j(topic = "org.geonetwork.datadirectory")
@Component
public class GeonetworkDataDirectory {
  /**
   * A suffix of the keys used to look up paths in system.properties or system.env or in Servlet
   * context.
   */
  public static final String KEY_SUFFIX = ".dir";

  /** The full key of the geonetwork data directory. */
  public static final String GEONETWORK_DIR_KEY = "geonetwork.dir";

  /** The id used when registering this object in a spring application context. */
  public static final String GEONETWORK_BEAN_KEY = "GeonetworkDataDirectory";

  @Autowired SchemaManager schemaManager;

  private Path webappDir;
  private Path systemDataDir;
  private Path indexConfigDir;
  private Path configDir;
  private Path thesauriDir;
  private Path schemaPluginsDir;
  private Path metadataDataDir;
  private Path backupDir;
  private Path metadataRevisionDir;
  private Path resourcesDir;
  private Path htmlCacheDir;
  private Path schemaPublicationDir;
  private Path uploadDir;
  private Path formatterDir;
  private Path nodeLessFiles;

  /**
   * Check and create if needed GeoNetwork data directory.
   *
   * <p>The data directory is the only mandatory value. If not set, the default location is {@link
   * #getDefaultDataDir(Path)}.
   *
   * <p>All properties are set using :
   *
   * <ul>
   *   <ol>
   *     Java environment variable
   *   </ol>
   *   <ol>
   *     Servlet context parameter
   *   </ol>
   *   <ol>
   *     System environment variable
   *   </ol>
   * </ul>
   */
  //  public void init(
  //      final String webappName,
  //      final Path webappDir,)
  //      throws IOException {
  //    if (log.isDebugEnabled()) {
  //      log.debug(Geonet.DATA_DIRECTORY, "Check and create if needed GeoNetwork data directory");
  //    }
  //    this.webappDir = webappDir;
  //    final ConfigurableApplicationContext applicationContext = ApplicationContextHolder.get();
  //
  ////    setDataDirectory(jeevesServlet, webappName, handlerConfig);
  //
  //    // might be null during tests
  //    if (applicationContext != null) {
  //      applicationContext.publishEvent(
  //          new GeonetworkDataDirectoryInitializedEvent(applicationContext, this));
  //    }
  //  }
  //
  //  public void init(
  //      final String webappName,
  //      final Path webappDir,
  //      Path systemDataDir)
  //      throws IOException {
  //    this.systemDataDir = systemDataDir;
  //    this.init(webappName, webappDir, handlerConfig, jeevesServlet);
  //  }

  /**
   * Logfile location as determined from appender, or system property, or default.
   *
   * <p>Note this code is duplicated with the deprecated {@code LogConfig}.
   *
   * @return logfile location, or {@code null} if unable to determine
   */
  public static File getLogfile() {
    //    return log.getLogfile();
    return null;
  }

  /**
   * Determines the location of a property based on the following lookup mechanism:
   *
   * <p>1) Java environment variable 2) Servlet context variable 3) Config.xml appHandler parameter
   * 4) System variable
   *
   * <p>For each of these, the methods checks that 1) The path exists 2) Is a directory 3) Is
   * writable
   *
   * <p>Inspired by GeoServer mechanism.
   *
   * @return String The absolute path to the data directory, or <code>null</code> if it could not be
   *     found.
   */
  private Path lookupProperty(String key) {

    final String[] typeStrs = {
      "Java environment variable ",
      "Servlet context parameter ",
      "Config.xml appHandler parameter",
      "System environment variable "
    };

    String dataDirStr = null;

    if (log.isDebugEnabled()) {
      log.debug(Geonet.DATA_DIRECTORY, "lookupProperty " + key);
    }

    // Loop over variable access methods
    for (int j = 0; j < typeStrs.length && dataDirStr == null; j++) {
      String value = null;
      String typeStr = typeStrs[j];

      String keyToUse = key;
      // Lookup section
      switch (j) {
        case 0:
          value = System.getProperty(keyToUse);
          break;
          //        case 1:
          //          if (jeevesServlet != null) {
          //            value = jeevesServlet.getInitParameter(keyToUse);
          //            if ((value == null) && (jeevesServlet.getServletContext() != null)) {
          //              value = jeevesServlet.getServletContext().getInitParameter(keyToUse);
          //            }
          //          }
          //          break;
          //        case 2:
          //          value = handlerConfig.getValue(keyToUse);
          //          break;
          //        case 3:
          //          //				Environment variable names used by the utilities in the Shell and
          // Utilities
          //          //				volume of IEEE Std 1003.1-2001 consist solely of uppercase letters,
          // digits, and the
          //          // '_'
          //          //				Instead of looking for geonetwork.dir, get geonetwork_dir
          //          value = System.getenv(keyToUse.replace('.', '_'));
          //          break;
        default:
          throw new IllegalArgumentException("Did not expect value: " + j);
      }

      if (log.isDebugEnabled()) {
        log.debug(
            Geonet.DATA_DIRECTORY,
            " Found " + typeStr + "for " + keyToUse + " with value " + value);
      }

      dataDirStr = value;
    }

    return dataDirStr == null ? null : IO.toPath(dataDirStr);
  }

  private Path setDataDirectory(String webappName) throws IOException {

    if (this.systemDataDir == null) {
      // System property defined according to webapp name
      this.systemDataDir = lookupProperty(webappName + KEY_SUFFIX);
    }
    // GEONETWORK.dir is default
    if (this.systemDataDir == null) {
      this.systemDataDir = lookupProperty(GEONETWORK_DIR_KEY);
    }

    boolean useDefaultDataDir = false;
    log.info(Geonet.DATA_DIRECTORY, "   - Data directory initialization: " + this.systemDataDir);

    if (this.systemDataDir == null) {
      log.warn(
          Geonet.DATA_DIRECTORY,
          "    - Data directory properties is not set. Use "
              + webappName
              + KEY_SUFFIX
              + " or "
              + GEONETWORK_DIR_KEY
              + " properties.");
      useDefaultDataDir = true;
    } else {
      try {
        Files.createDirectories(this.systemDataDir);
      } catch (IOException e) {
        log.error(
            Geonet.DATA_DIRECTORY, "Error creating system data directory: " + this.systemDataDir);
        useDefaultDataDir = true;
      }

      if (!Files.exists(this.systemDataDir)) {
        log.warn(Geonet.DATA_DIRECTORY, "    - Data directory does not exist. Create it first.");
        useDefaultDataDir = true;
      }

      try {
        final Path testFile = this.systemDataDir.resolve("testDD.txt");
        IO.touch(testFile);
        Files.delete(testFile);
      } catch (IOException e) {
        log.warn(
            Geonet.DATA_DIRECTORY,
            "    - Data directory is not writable. Set read/write privileges to user starting the"
                + " catalogue (ie. "
                + System.getProperty("user.name")
                + ").");
        useDefaultDataDir = true;
      }

      if (!this.systemDataDir.isAbsolute()) {
        log.warn(
            Geonet.DATA_DIRECTORY,
            "    - Data directory is not an absolute path. Relative path is not recommended.\n"
                + "Update "
                + webappName
                + KEY_SUFFIX
                + " or geonetwork.dir environment variable.");
      }
    }

    if (useDefaultDataDir) {
      systemDataDir = getDefaultDataDir(webappDir);
      log.warn(
          Geonet.DATA_DIRECTORY,
          "    - Data directory provided could not be used. Using default location: "
              + systemDataDir);
    }

    try {
      this.systemDataDir = this.systemDataDir.toRealPath();
      if (!Files.exists(this.systemDataDir)) {
        log.error(Geonet.DATA_DIRECTORY, "System Data Directory does not exist");
      }
    } catch (IOException e) {
      log.warn(Geonet.DATA_DIRECTORY, "Unable to make a canonical path from: " + systemDataDir);
    }
    log.info(Geonet.DATA_DIRECTORY, "   - Data directory is: " + systemDataDir);

    // Set subfolder data directory
    indexConfigDir =
        setDir(
            webappName,
            indexConfigDir,
            ".indexConfig" + KEY_SUFFIX,
            Geonet.Config.INDEX_CONFIG_DIR,
            "config",
            "index");

    configDir =
        setDir(webappName, configDir, ".config" + KEY_SUFFIX, Geonet.Config.CONFIG_DIR, "config");
    thesauriDir =
        setDir(
            webappName,
            thesauriDir,
            ".codeList" + KEY_SUFFIX,
            Geonet.Config.CODELIST_DIR,
            "config",
            "codelist");
    schemaPluginsDir =
        setDir(
            webappName,
            schemaPluginsDir,
            ".schema" + KEY_SUFFIX,
            Geonet.Config.SCHEMAPLUGINS_DIR,
            "config",
            "schema_plugins");
    metadataDataDir =
        setDir(
            webappName,
            metadataDataDir,
            ".data" + KEY_SUFFIX,
            Geonet.Config.DATA_DIR,
            "data",
            "metadata_data");
    metadataRevisionDir =
        setDir(
            webappName,
            metadataRevisionDir,
            ".svn" + KEY_SUFFIX,
            Geonet.Config.SUBVERSION_PATH,
            "data",
            "metadata_subversion");
    resourcesDir =
        setDir(
            webappName,
            resourcesDir,
            ".resources" + KEY_SUFFIX,
            Geonet.Config.RESOURCES_DIR,
            "data",
            "resources");
    uploadDir =
        setDir(
            webappName,
            uploadDir,
            ".upload" + KEY_SUFFIX,
            Geonet.Config.UPLOAD_DIR,
            "data",
            "upload");
    formatterDir =
        setDir(
            webappName,
            formatterDir,
            ".formatter" + KEY_SUFFIX,
            Geonet.Config.FORMATTER_PATH,
            "data",
            "formatter");
    htmlCacheDir =
        setDir(
            webappName,
            htmlCacheDir,
            ".htmlcache" + KEY_SUFFIX,
            Geonet.Config.HTMLCACHE_DIR,
            null,
            "htmlcache");
    schemaPublicationDir =
        setDir(
            webappName,
            schemaPublicationDir,
            ".schemapublication" + KEY_SUFFIX,
            Geonet.Config.SCHEMAPUBLICATION_DIR,
            null,
            "schemapublication");
    backupDir =
        setDir(
            webappName,
            backupDir,
            ".backup" + KEY_SUFFIX,
            Geonet.Config.BACKUP_DIR,
            "data",
            "backup");
    nodeLessFiles =
        setDir(
            webappName,
            nodeLessFiles,
            ".node_less_files" + KEY_SUFFIX,
            Geonet.Config.NODE_LESS_DIR,
            "data",
            "node_less_files");

    //    handlerConfig.setValue(Geonet.Config.SYSTEM_DATA_DIR, this.systemDataDir.toString());

    initDataDirectory();

    return this.systemDataDir;
  }

  /** Checks if data directory is empty or not. If empty, add mandatory elements (ie. codelist). */
  private void initDataDirectory() throws IOException {
    log.info(Geonet.DATA_DIRECTORY, "   - Data directory initialization ...");

    if (!Files.exists(this.thesauriDir) || IO.isEmptyDir(this.thesauriDir)) {
      log.info(Geonet.DATA_DIRECTORY, "     - Copying codelists directory ..." + thesauriDir);
      try {
        final Path srcThesauri = getDefaultDataDir(webappDir).resolve("config").resolve("codelist");
        IO.copyDirectoryOrFile(srcThesauri, this.thesauriDir, false);
      } catch (IOException e) {
        log.error(Geonet.DATA_DIRECTORY, "     - Thesaurus copy failed: " + e.getMessage(), e);
      }
    }

    // Copy config-viewer-XXX.xml files
    Path mapDir = this.resourcesDir.resolve("map");
    if (!Files.exists(mapDir) || IO.isEmptyDir(mapDir)) {
      log.info(Geonet.DATA_DIRECTORY, "     - Copying config-viewer-XXX.xml files ...");

      try {
        final Path srcMap =
            webappDir
                .resolve("WEB-INF")
                .resolve("data")
                .resolve("data")
                .resolve("resources")
                .resolve("map");

        if (Files.exists(srcMap)) {
          try (DirectoryStream<Path> paths = Files.newDirectoryStream(srcMap)) {
            for (Path path : paths) {
              final Path relativePath = srcMap.relativize(path);
              final Path dest = mapDir.resolve(relativePath.toString());
              if (!Files.exists(dest)) {
                IO.copyDirectoryOrFile(path, dest, false);
              }
            }
          }
        }

      } catch (IOException e) {
        log.error(
            Geonet.DATA_DIRECTORY,
            "     - config-viewer-XXX.xml copy failed: " + e.getMessage(),
            e);
      }
    }

    // Copy default logo to the images and harvesting folder
    Path imagesDir = this.resourcesDir.resolve("images");
    if (!Files.exists(imagesDir)) {
      try {
        final Path srcFile =
            getDefaultDataDir(webappDir).resolve("data").resolve("resources").resolve("images");
        IO.copyDirectoryOrFile(srcFile, imagesDir, false);
      } catch (IOException e) {
        log.info(Geonet.DATA_DIRECTORY, "      - Error copying images folder: " + e.getMessage());
      }
    }

    Path logoDir = this.resourcesDir.resolve("images").resolve("logos");
    if (!Files.exists(logoDir)) {
      try {
        Files.createDirectories(logoDir);
      } catch (IOException e) {
        log.info(
            Geonet.DATA_DIRECTORY, "      - Error creating images/logos folder: " + e.getMessage());
      }
    }

    Path resourcesConfigDir = this.resourcesDir.resolve("config");
    if (!Files.exists(resourcesConfigDir) || IO.isEmptyDir(resourcesConfigDir)) {
      log.info(Geonet.DATA_DIRECTORY, "     - Copying config ...");
      try {
        Files.createDirectories(resourcesConfigDir);
        final Path fromDir =
            getDefaultDataDir(webappDir).resolve("data").resolve("resources").resolve("config");

        if (Files.exists(fromDir)) {
          try (DirectoryStream<Path> paths = Files.newDirectoryStream(fromDir)) {
            for (Path path : paths) {
              final Path relativePath = fromDir.relativize(path);
              final Path dest = resourcesConfigDir.resolve(relativePath.toString());
              if (!Files.exists(dest)) {
                IO.copyDirectoryOrFile(path, dest, false);
              }
            }
          }
        }
      } catch (IOException e) {
        log.error(Geonet.DATA_DIRECTORY, "     - Config copy failed: " + e.getMessage(), e);
      }
    }

    logoDir = this.resourcesDir.resolve("images").resolve("harvesting");
    if (!Files.exists(logoDir) || IO.isEmptyDir(logoDir)) {
      log.info(Geonet.DATA_DIRECTORY, "     - Copying logos ...");
      try {
        Files.createDirectories(logoDir);
        final Path srcLogo =
            getDefaultDataDir(webappDir)
                .resolve("data")
                .resolve("resources")
                .resolve("images")
                .resolve("harvesting");

        if (Files.exists(srcLogo)) {
          try (DirectoryStream<Path> paths = Files.newDirectoryStream(srcLogo)) {
            for (Path path : paths) {
              final Path relativePath = srcLogo.relativize(path);
              final Path dest = logoDir.resolve(relativePath.toString());
              if (!Files.exists(dest)) {
                IO.copyDirectoryOrFile(path, dest, false);
              }
            }
          }
        }
      } catch (IOException e) {
        log.error(Geonet.DATA_DIRECTORY, "     - Logo copy failed: " + e.getMessage(), e);
      }
    }

    if (!Files.exists(this.indexConfigDir) || IO.isEmptyDir(this.indexConfigDir)) {
      log.info(Geonet.DATA_DIRECTORY, "     - Copying index configuration in data directory ...");
      try {
        final Path srcFile = getDefaultDataDir(webappDir).resolve("config").resolve("index");
        IO.copyDirectoryOrFile(srcFile, this.indexConfigDir, false);
      } catch (IOException e) {
        log.info(
            Geonet.DATA_DIRECTORY, "      - Error copying index configuration: " + e.getMessage());
      }
    }

    Path schemaCatFile = configDir.resolve(Geonet.File.SCHEMA_PLUGINS_CATALOG);
    if (!Files.exists(schemaCatFile)) {
      log.info(Geonet.DATA_DIRECTORY, "     - Copying schema plugin catalogue ...");
      try {
        final Path srcFile =
            webappDir.resolve("WEB-INF").resolve(Geonet.File.SCHEMA_PLUGINS_CATALOG);
        IO.copyDirectoryOrFile(srcFile, schemaCatFile, false);

        // Copy missing schema plugins
        Path srcPluginsDir =
            getDefaultDataDir(webappDir).resolve("config").resolve("schema_plugins");
        try (DirectoryStream<Path> schemaPlugins = Files.newDirectoryStream(srcPluginsDir)) {
          final Iterator<Path> pathIterator = schemaPlugins.iterator();
          while (pathIterator.hasNext()) {
            Path next = pathIterator.next();
            Path destDir = this.schemaPluginsDir.resolve(next.getFileName().toString());
            if (!Files.exists(destDir)) {
              IO.copyDirectoryOrFile(next, destDir, false);
            }
          }
        }

      } catch (IOException e) {
        log.info(
            Geonet.DATA_DIRECTORY,
            "      - Error copying schema plugin catalogue: " + e.getMessage());
      }
    }

    log.info(Geonet.DATA_DIRECTORY, "     - Copying encryptor.properties file...");
    try {
      final Path srcEncryptorFile =
          getDefaultDataDir(webappDir)
              .resolve("config")
              .resolve(Geonet.File.ENCRYPTOR_CONFIGURATION);
      final Path destEncryptorFile = this.configDir.resolve("encryptor.properties");
      // Copy encryptor.properties if doesn't exist
      if (!Files.exists(destEncryptorFile)) {
        IO.copyDirectoryOrFile(srcEncryptorFile, destEncryptorFile, true);
      }

    } catch (IOException e) {
      log.info(
          Geonet.DATA_DIRECTORY,
          "      - Error copying encryptor.propeties file: " + e.getMessage());
      throw e;
    }

    final Path locDir = webappDir.resolve("loc");
    if (!Files.exists(locDir)) {
      Files.createDirectories(locDir);
    }
  }

  private Path getDefaultDataDir(Path webappDir) {
    return webappDir.resolve("WEB-INF").resolve("data");
  }

  /**
   * Try to retrieve from system properties the variable with name <webapp.name>.key. If not set,
   * create the resource folder using <geonetwork.dir>/folder and set the system property value.
   * Create the folder if does not exist.
   */
  private Path setDir(
      String webappName,
      Path dir,
      String key,
      String handlerKey,
      String firstPathSeg,
      String... otherSegments) {
    String envKey = webappName + key;
    if (dir != null) {
      if (log.isDebugEnabled()) {
        log.debug(
            Geonet.DATA_DIRECTORY,
            "path for "
                + envKey
                + " set to "
                + dir.toString()
                + " via bean properties, not looking up");
      }
    } else {
      dir = lookupProperty(envKey);
    }
    if (dir == null) {
      envKey = Geonet.GEONETWORK + key;
      dir = lookupProperty(envKey);
    }
    if (dir == null) {
      dir = this.systemDataDir.resolve(firstPathSeg);
      for (String otherSegment : otherSegments) {
        dir = dir.resolve(otherSegment);
      }
    } else {
      if (!dir.isAbsolute()) {
        log.info(
            Geonet.DATA_DIRECTORY,
            "    - "
                + envKey
                + " for directory "
                + dir
                + " is relative path. Use absolute path instead.");
      }
    }
    if (handlerKey != null) {
      //      handlerConfig.setValue(handlerKey, dir.toString());
    }
    // Create directory if it does not exist
    try {
      Files.createDirectories(dir);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    log.info(Geonet.DATA_DIRECTORY, "    - " + envKey + " is " + dir);
    return dir;
  }

  /**
   * Get the root data dir for Geonetwork. Typically other "data" directories are subdirectories to
   * this.
   *
   * @return the root data dir for Geonetwork
   */
  public Path getSystemDataDir() {
    return systemDataDir;
  }

  /**
   * Set the root data dir for Geonetwork. Typically other "data" directories are subdirectories to
   * this.
   */
  public void setSystemDataDir(Path systemDataDir) {
    this.systemDataDir = systemDataDir;
  }

  /**
   * Get the directory to store the index configuration in.
   *
   * @return The directory to store the index configuration in.
   */
  public Path getIndexConfigDir() {
    return indexConfigDir;
  }

  /** Set the directory to store the index configuration in. */
  public void setIndexConfigDir(Path indexConfigDir) {
    this.indexConfigDir = indexConfigDir;
  }

  /**
   * Return the directory containing the configuration file.
   *
   * @return the directory containing the configuration file.
   */
  public Path getConfigDir() {
    return configDir;
  }

  /** Set the directory containing the configuration file. */
  public void setConfigDir(Path configDir) {
    this.configDir = configDir;
  }

  /**
   * Get the directory containing the thesauri.
   *
   * @return the directory containing the thesauri.
   */
  public Path getThesauriDir() {
    return thesauriDir;
  }

  /** Set the directory containing the thesauri. */
  public void setThesauriDir(Path thesauriDir) {
    this.thesauriDir = thesauriDir;
  }

  /**
   * Get the schema plugins directory.
   *
   * @return the schema plugins directory.
   */
  public Path getSchemaPluginsDir() {
    return schemaPluginsDir;
  }

  /** Set the schema plugins directory. */
  public void setSchemaPluginsDir(Path schemaPluginsDir) {
    this.schemaPluginsDir = schemaPluginsDir;
  }

  /**
   * Get the directory that contain all the resources related to metadata (thumbnails, attachments,
   * etc...).
   *
   * @return the directory that contain all the resources related to metadata (thumbnails,
   *     attachments, etc...).
   */
  public Path getMetadataDataDir() {
    return metadataDataDir;
  }

  /**
   * Set the directory that contain all the resources related to metadata (thumbnails, attachments,
   * etc...).
   */
  public void setMetadataDataDir(Path metadataDataDir) {
    this.metadataDataDir = metadataDataDir;
  }

  /**
   * Get the directory containing the metadata revision history if it is to be stored locally.
   *
   * @return the directory containing the metadata revision history.
   */
  public Path getMetadataRevisionDir() {
    return metadataRevisionDir;
  }

  /** Set the directory containing the metadata revision history if it is to be stored locally. */
  public void setMetadataRevisionDir(Path metadataRevisionDir) {
    this.metadataRevisionDir = metadataRevisionDir;
  }

  /**
   * Get the directory that will contain the resources for the system.
   *
   * @return the directory that will contain the resources for the system.
   */
  public Path getResourcesDir() {
    return resourcesDir;
  }

  /** Set the directory that will contain the resources for the system. */
  public void setResourcesDir(Path resourcesDir) {
    this.resourcesDir = resourcesDir;
  }

  /**
   * Get the directory containing the webapplication.
   *
   * @return the directory containing the webapplication.
   */
  public Path getWebappDir() {
    return webappDir;
  }

  /**
   * Get directory for caching html data.
   *
   * @return directory for caching html data.
   */
  public Path getHtmlCacheDir() {
    return htmlCacheDir;
  }

  /** Set directory for caching html data. */
  public void setHtmlCacheDir(Path htmlCacheDir) {
    this.htmlCacheDir = htmlCacheDir;
  }

  /**
   * Get directory for publishing schemas and making them publicly available.
   *
   * @return directory for publishing schemas and making them publicly available.
   */
  public Path getSchemaPublicationDir() {
    return schemaPublicationDir;
  }

  /**
   * Set directory for publishing schemas and making them publicly available.
   *
   * @param schemaPublicationDir
   */
  public void setSchemaPublicationDir(Path schemaPublicationDir) {
    this.schemaPublicationDir = schemaPublicationDir;
  }

  /**
   * Get directory for caching where uploaded files go.
   *
   * @return directory for caching html data.
   */
  public Path getUploadDir() {
    return uploadDir;
  }

  /** Set directory for caching where uploaded files go. */
  public void setUploadDir(Path uploadDir) {
    this.uploadDir = uploadDir;
  }

  public Path getFormatterDir() {
    return formatterDir;
  }

  public void setFormatterDir(Path formatterDir) {
    this.formatterDir = formatterDir;
  }

  public Path getNodeLessFiles() {
    return nodeLessFiles;
  }

  public void setNodeLessFiles(Path nodeLessFiles) {
    this.nodeLessFiles = nodeLessFiles;
  }

  public Path resolveWebResource(String resourcePath) {
    if (resourcePath.charAt(0) == '/' || resourcePath.charAt(0) == '\\') {
      resourcePath = resourcePath.substring(1);
    }
    return this.webappDir.resolve(resourcePath);
  }

  /** Get directory use to backup metadata when removed. */
  public Path getBackupDir() {
    return backupDir;
  }

  /** Set directory to use to backup metadata when removed. */
  public void setBackupDir(Path backupDir) {
    this.backupDir = backupDir;
  }

  public Path getXsltConversion(String conversionId) {
    if (conversionId.startsWith(IMPORT_STYLESHEETS_SCHEMA_PREFIX)) {
      String[] pathToken = conversionId.split(":");
      if (pathToken.length == 3) {
        String schema = pathToken[1];
        if (!schemaManager.existsSchema(schema)) {
          throw new IllegalArgumentException(
              String.format(
                  "Conversion not found. Schema '%s' is not registered in this catalog.", schema));
        }
        FilePathChecker.verify(pathToken[2]);
        return this.getSchemaPluginsDir().resolve(pathToken[1]).resolve(pathToken[2] + ".xsl");
      }
    } else {
      FilePathChecker.verify(conversionId);
      return this.getWebappDir()
          .resolve(Geonet.Path.IMPORT_STYLESHEETS)
          .resolve(conversionId + ".xsl");
    }
    return null;
  }

  /** Event is raised when GeonetworkDataDirectory has finished being initialized. */
  public static class GeonetworkDataDirectoryInitializedEvent extends ApplicationEvent {
    private final ApplicationContext applicationContext;

    public GeonetworkDataDirectoryInitializedEvent(
        ApplicationContext context, GeonetworkDataDirectory dataDirectory) {
      super(dataDirectory);
      this.applicationContext = context;
    }

    public ApplicationContext getApplicationContext() {
      return applicationContext;
    }

    @Override
    public GeonetworkDataDirectory getSource() {
      return (GeonetworkDataDirectory) super.getSource();
    }
  }
}
