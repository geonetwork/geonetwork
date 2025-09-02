/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas;

import static org.geonetwork.utility.JarFileCopy.copyFolder;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.constants.Constants;
import org.geonetwork.constants.Geonet;
import org.geonetwork.schemas.validation.SchematronCompiler;
import org.geonetwork.utility.ApplicationContextProvider;
import org.geonetwork.utility.legacy.Pair;
import org.geonetwork.utility.legacy.exceptions.NoSchemaMatchesException;
import org.geonetwork.utility.legacy.io.IO;
import org.geonetwork.utility.legacy.nio.NioPathAwareCatalogResolver;
import org.geonetwork.utility.legacy.resolver.ResolverWrapper;
import org.geonetwork.utility.legacy.resolver.SchemaPluginUrlRewrite;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Class that handles all functions relating to metadata schemas. This includes inserting/removing/updating metadata
 * schemas for use in GeoNetwork as well as determining whether a metadata record belongs to any of the metadata schemas
 * known to GeoNetwork.
 *
 * <p>The Schema class holds all information describing a schema in GeoNetwork. The SchemaManager holds a map of Schema
 * objects known to GeoNetwork.
 */
@Component
@Slf4j
public class SchemaManager {
    private static final String GEONET_SCHEMA_URI = "http://geonetwork-opensource.org/schemas/schema-ident";

    @SuppressWarnings("unused")
    private static final Namespace GEONET_SCHEMA_PREFIX_NS = Namespace.getNamespace("gns", GEONET_SCHEMA_URI);

    @SuppressWarnings("unused")
    private static final Namespace GEONET_SCHEMA_NS = Namespace.getNamespace(GEONET_SCHEMA_URI);

    private Map<String, SchemaPlugin> hmSchemas = new HashMap<>();
    private Map<String, Namespace> hmSchemasTypenames = new HashMap<>();

    @SuppressWarnings("unused")
    private String[] fnames = {"labels.xml", "codelists.xml", "strings.xml"};

    private Path schemaPluginsDir;
    private Path schemaPluginsCat;
    private boolean createOrUpdateSchemaCatalog;

    @SuppressWarnings("unused")
    @Value("${geonetwork.language.default:'eng'}")
    private String defaultLang;

    private Path basePath;

    @SuppressWarnings("unused")
    private Path resourcePath;

    private Path schemaPublicationDir;
    private int numberOfCoreSchemasAdded = 0;

    @Autowired
    List<? extends SchemaPlugin> schemaPlugins;

    @Autowired
    SchematronCompiler schematronCompiler;

    @Autowired
    MetadataSchemaAutodetector metadataSchemaAutodetector;

    public SchemaPlugin getSchemaPluginByIdentifier(String identifier) {
        for (SchemaPlugin plugin : schemaPlugins) {
            if (plugin.getIdentifier().equals(identifier)) {
                return plugin;
            }
        }
        return null;
    }

    public static Path registerXmlCatalogFiles(Path basePath, Path schemapluginUriCatalog) throws IOException {
        //    Path webInf = webappDir.resolve("WEB-INF");

        // --- Set jeeves.xml.catalog.files property
        // --- this is critical to schema support so must be set correctly
        String catalogProp = System.getProperty(Constants.XML_CATALOG_FILES);
        if (catalogProp == null) {
            catalogProp = "";
        }
        if (!catalogProp.isEmpty()) {
            log.info("Overriding " + Constants.XML_CATALOG_FILES + " property (was set to " + catalogProp + ")");
        }

        catalogProp = basePath.resolve("xml")
                .resolve("resolver")
                .resolve("oasis-catalog.xml")
                .toUri()
                .toASCIIString();
        //    catalogProp = webInf.resolve("oasis-catalog.xml") + ";" + schemapluginUriCatalog;
        System.setProperty(Constants.XML_CATALOG_FILES, catalogProp);
        log.info(Constants.XML_CATALOG_FILES + " property set to " + catalogProp);

        Path blankXSLFile = basePath.resolve("xsl").resolve("blanks.xsl");
        System.setProperty(
                Constants.XML_CATALOG_BLANKXSLFILE, blankXSLFile.toUri().toASCIIString());
        log.info(Constants.XML_CATALOG_BLANKXSLFILE + " property set to " + blankXSLFile);

        return null;
    }

    public static SchemaPlugin getSchemaPlugin(String schemaIdentifier) {
        @SuppressWarnings("unused")
        String schemaBeanIdentifier = schemaIdentifier + "SchemaPlugin";
        SchemaPlugin schemaPlugin = null;
        try {
            //      if (ApplicationContextHolder.get() != null) {
            //        schemaPlugin = (SchemaPlugin)
            // ApplicationContextHolder.get().getBean(schemaBeanIdentifier);
            //
            //        if (schemaPlugin == null &&
            // schemaIdentifier.startsWith(ISO19139SchemaPlugin.IDENTIFIER)) {
            //          // For ISO19139 profiles, get the ISO19139 bean if no custom one defined
            //          // Can't depend here on ISO19139SchemaPlugin to avoid to introduce
            //          // circular ref.
            //          schemaBeanIdentifier = ISO19139SchemaPlugin.IDENTIFIER + "SchemaPlugin";
            //          schemaPlugin =
            //              (SchemaPlugin)
            // ApplicationContextHolder.get().getBean(schemaBeanIdentifier);
            //        }
            //      }
        } catch (Exception e) {
            // No bean for this schema
            if (log.isDebugEnabled()) {
                log.debug("No bean defined for the schema plugin '" + schemaIdentifier + "'. " + e.getMessage());
            }
        }
        return schemaPlugin;
    }

    // @VisibleForTesting
    //  public void configureFrom(
    //      SchemaManager schemaManager, Path basePath, GeonetworkDataDirectory dataDir) {
    //    this.basePath = basePath;
    //    this.resourcePath = dataDir.getResourcesDir();
    //    this.schemaPublicationDir = dataDir.getSchemaPublicationDir();
    //    this.schemaPluginsDir = dataDir.getSchemaPluginsDir();
    //    this.schemaPluginsCat = schemaPluginsDir.resolve("schemaplugin-uri-catalog.xml");
    //    this.defaultLang = schemaManager.defaultLang;
    //    this.createOrUpdateSchemaCatalog = schemaManager.createOrUpdateSchemaCatalog;
    //
    //    addResolverRewriteDirectives(dataDir);
    //
    //    this.hmSchemas.clear();
    //    this.hmSchemas.putAll(schemaManager.hmSchemas);
    //
    //    fnames = new String[schemaManager.fnames.length];
    //    System.arraycopy(schemaManager.fnames, 0, fnames, 0, fnames.length);
    //    numberOfCoreSchemasAdded = schemaManager.numberOfCoreSchemasAdded;
    //  }
    //
    //  private void addResolverRewriteDirectives(GeonetworkDataDirectory dataDir) {
    //    NioPathAwareCatalogResolver.addRewriteDirective(
    //        new PrefixUrlRewrite(
    //            "sharedFormatterDir/", dataDir.getFormatterDir().toAbsolutePath().toUri() + "/"));
    //  }

    public Path getBasePath() {
        return basePath;
    }

    /** initialize and configure schema manager. should only be on startup. */
    @PostConstruct
    public void configure() throws Exception {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        hmSchemas.clear();

        // FIXME: Should be in the data directory?
        Path schemaPath = Files.createTempDirectory("gn5-schema");
        Files.createDirectories(schemaPath);
        this.basePath = schemaPath; // .resolve("usedforschematronrulecompilation");
        //    this.resourcePath = resourcePath;
        this.schemaPublicationDir = schemaPath.resolve("schemapublicationwww");

        // Copy schemas JAR resources to the schema directory
        for (SchemaPlugin plugin : schemaPlugins) {
            URI schemaResource = plugin.getClass()
                    .getResource(plugin.getSchemasResourcePath())
                    .toURI();

            copyFolder(
                    schemaResource,
                    "schemas/" + plugin.getIdentifier(),
                    schemaPath.resolve("schemaplugins").resolve(plugin.getIdentifier()));
        }

        this.schemaPluginsDir = schemaPath.resolve("schemaplugins");

        copyFolder(new ClassPathResource("/xml").getURI(), "xml", schemaPath.resolve("xml"));

        Path uriCatalogPath = schemaPath.resolve("schemaplugin-uri-catalog.xml");
        if (Files.notExists(uriCatalogPath)) {
            Files.copy(
                    new ClassPathResource("xml/resolver/schemaplugin-uri-catalog.xml").getInputStream(),
                    uriCatalogPath,
                    StandardCopyOption.REPLACE_EXISTING);
        }

        this.schemaPluginsCat = uriCatalogPath;

        this.createOrUpdateSchemaCatalog = true;

        //    final Path configDir = IO.toPath(handlerConfig.getValue(Geonet.Config.CONFIG_DIR));
        //    final Path schemapluginUriCatalog = configDir.resolve("schemaplugin-uri-catalog.xml");
        registerXmlCatalogFiles(this.basePath, null);

        Element schemaPluginCatRoot = getSchemaPluginCatalogTemplate();

        //
        // addResolverRewriteDirectives(applicationContext.getBean(GeonetworkDataDirectory.class));

        // -- check the plugin directory and add any schemas already in there
        try (DirectoryStream<Path> saSchemas = Files.newDirectoryStream(this.schemaPluginsDir)) {
            for (Path schemaDir : saSchemas) {
                if (!schemaDir.getFileName().startsWith(".")) {
                    if (Files.isDirectory(schemaDir)) {
                        log.info("Loading schema " + schemaDir.getFileName() + "...");
                        processSchema(applicationContext, schemaDir, schemaPluginCatRoot);
                    }
                }
            }

            checkAppSupported(schemaPluginCatRoot);

            checkDependencies(schemaPluginCatRoot);
        }

        writeSchemaPluginCatalog(schemaPluginCatRoot);
    }

    // --------------------------------------------------------------------------
    // ---
    // --- API methods
    // ---
    // --------------------------------------------------------------------------

    /** Ensures singleton-ness by preventing cloning. */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Return the MetadataSchema objects
     *
     * @param name the metadata schema we want the MetadataSchema for
     */
    public SchemaPlugin getSchema(String name) {
        SchemaPlugin schema = hmSchemas.get(name);

        if (schema == null) {
            throw new IllegalArgumentException("Schema not registered : " + name);
        }

        return schema;
    }

    /**
     * Return the list of dependent schemas
     *
     * @param name the metadata schema we want the list of dependencies for
     */
    public Set<String> getDependencies(String name) {
        Set<String> dependencies = new HashSet<>();

        SchemaPlugin schema = hmSchemas.get(name);
        if (schema != null) { // if it is null then that is a config error
            dependencies.add(schema.getConfiguration().getDepends());
        }
        return dependencies;
    }

    /**
     * Return the Id and Version of the schema
     *
     * @param name the metadata schema we want the MetadataSchema for
     * @return Pair with schema Id and Version
     */
    public Pair<String, String> getIdVersion(String name) {
        SchemaPlugin schema = hmSchemas.get(name);

        if (schema == null) throw new IllegalArgumentException("Schema not registered : " + name);

        return Pair.read(
                schema.getConfiguration().getId(), schema.getConfiguration().getVersion());
    }

    /**
     * Adds a plugin schema to the list of schemas registered here.
     *
     * @param name the metadata schema we want to add
     * @param zipFs A filesystem (probably a ZipFileSystem) to copy files from.
     */
    public void addPluginSchema(ApplicationContext applicationContext, String name, FileSystem zipFs) throws Exception {
        //
        //    beforeWrite();
        //    try {
        //      realAddPluginSchema(applicationContext, name, zipFs);
        //    } finally {
        //      afterWrite();
        //    }
    }

    /**
     * Updates a plugin schema in the list of schemas registered here.
     *
     * @param name the metadata schema we want to update
     * @param zipFs A filesystem (probably a ZipFileSystem) to copy files from.
     */
    public void updatePluginSchema(ApplicationContext applicationContext, String name, FileSystem zipFs)
            throws Exception {

        //    beforeWrite();
        //    try {
        //      // -- delete schema, trap any exception here as we need to say
        //      // -- why the update failed
        //      try {
        //        boolean doDependencies = false;
        //        realDeletePluginSchema(name, doDependencies);
        //      } catch (Exception e) {
        //        String errStr =
        //            "Could not update schema "
        //                + name
        //                + ", remove of outdated schema failed. Exception message if any is "
        //                + e.getMessage();
        //        log.error(errStr, e);
        //        throw new RuntimeException(errStr, e);
        //      }
        //
        //      // -- add the new one
        //      realAddPluginSchema(applicationContext, name, zipFs);
        //    } finally {
        //      afterWrite();
        //    }
    }

    /**
     * Returns the schema directory.
     *
     * @param name the metadata schema we want the directory for
     */
    public Path getSchemaDir(String name) {
        SchemaPlugin schema = hmSchemas.get(name);

        if (schema == null) throw new IllegalArgumentException("Schema not registered : " + name);

        return this.schemaPluginsDir.resolve(schema.getIdentifier());
    }

    /**
     * Returns the schema location as a JDOM attribute - this can be either an xsi:schemaLocation or
     * xsi:noNamespaceSchemaLocation depending on the schema.
     *
     * @param name the metadata schema we want the schemaLocation for
     */
    public Attribute getSchemaLocation(String name) {
        Attribute out = null;
        SchemaPlugin schema = hmSchemas.get(name);

        if (schema == null) throw new IllegalArgumentException("Schema not registered : " + name);

        String nsUri = schema.getConfiguration().getPrimeNamespace();
        String schemaLoc = schema.getConfiguration().getSchemaLocation();
        Path schemaFile = basePath.resolve(schema.getIdentifier()).resolve("schema.xsd");

        if (schemaLoc.isEmpty()) {
            if (Files.exists(schemaFile)) { // build one
                String schemaUrl = getSchemaUrl(name);
                if (nsUri == null || nsUri.isEmpty()) {
                    out = new Attribute("noNamespaceSchemaLocation", schemaUrl, Geonet.Namespaces.XSI);
                } else {
                    schemaLoc = nsUri + " " + schemaUrl;
                    out = new Attribute("schemaLocation", schemaLoc, Geonet.Namespaces.XSI);
                }
            } // else return null - no schema xsd exists - could be dtd
        } else {
            if (nsUri == null || nsUri.isEmpty()) {
                out = new Attribute("noNamespaceSchemaLocation", schemaLoc, Geonet.Namespaces.XSI);
            } else {
                out = new Attribute("schemaLocation", schemaLoc, Geonet.Namespaces.XSI);
            }
        }
        return out;
    }

    /**
     * Returns the schema templatesdirectory.
     *
     * @param name the metadata schema we want the templates directory for
     */
    public Path getSchemaTemplatesDir(String name) {
        Path dir = getSchemaDir(name);

        dir = dir.resolve("templates");
        if (!Files.exists(dir)) {
            return null;
        }
        return dir;
    }

    /**
     * Returns the schema sample data directory.
     *
     * @param name the metadata schema we want the sample data directory for
     */
    public Path getSchemaSampleDataDir(String name) {
        Path dir = getSchemaDir(name);

        dir = dir.resolve("sample-data");
        if (!Files.exists(dir)) {
            return null;
        }
        return dir;
    }

    /**
     * Returns the schema csw presentation directory.
     *
     * @param name the metadata schema we want the csw present info directory
     */
    public Path getSchemaCSWPresentDir(String name) {
        Path dir = getSchemaDir(name);

        dir = dir.resolve("present").resolve("csw");

        return dir;
    }

    /**
     * Return the schema information (usually localized codelists, labels etc) XmlFile objects.
     *
     * @param name the metadata schema we want schema info for
     */
    //  public Map<String, XmlFile> getSchemaInfo(String name) {
    //
    //    beforeRead();
    //    try {
    //      Schema schema = hmSchemas.get(name);
    //
    //      if (schema == null) throw new IllegalArgumentException("Schema not registered : " +
    // name);
    //
    //      return schema.getInfo();
    //    } finally {
    //      afterRead();
    //    }
    //  }

    /** Returns the list of schema names that have been registered. */
    public Set<String> getSchemas() {
        return hmSchemas.keySet();
    }

    /**
     * Returns the schema converter elements for a schema (as a list of cloned elements).
     *
     * @param name the metadata schema we want search
     * @throws Exception if schema is not registered
     */
    public List<Element> getConversionElements(String name) throws Exception {
        throw new NotImplementedException("TODO: GN5 use file in convert/* for available conversions");
    }

    /**
     * Return the schema converter(s) that produce the specified namespace.
     *
     * @param name the metadata schema we want search
     * @param namespaceUri the namespace URI we are looking for
     * @return List of XSLTs that produce this namespace URI (full pathname)
     * @throws Exception if schema is not registered
     */
    public List<Path> existsConverter(String name, String namespaceUri) throws Exception {
        throw new NotImplementedException("TODO: GN5 use file in convert/* for available conversions");
    }

    /**
     * Whether the schema named in the parameter exist.
     *
     * @param name the metadata schema we want to check existence of
     */
    public boolean existsSchema(String name) {
        return hmSchemas.containsKey(name);
    }

    /**
     * Gets the SchemaSuggestions class for the supplied schema name.
     *
     * @param name the metadata schema whose suggestions class we want
     */
    public SchemaSuggestions getSchemaSuggestions(String name) {
        SchemaPlugin schema = hmSchemas.get(name);

        return new SchemaSuggestions(schema.getConfiguration().getSuggestions());
    }

    /**
     * Gets the namespace URI from the schema information (XSD) for the supplied prefix.
     *
     * @param name the metadata schema whose namespaces we are searching
     * @param prefix the namespace prefix we want the URI for
     */
    public String getNamespaceURI(String name, String prefix) {
        SchemaPlugin schema = hmSchemas.get(name);

        return schema.getAllNamespaces().stream()
                .filter(ns -> ns.getPrefix().equals(prefix))
                .map(Namespace::getURI)
                .findFirst()
                .orElse(null);
    }

    public String autodetectSchema(Element md) throws NoSchemaMatchesException, JDOMException {
        return metadataSchemaAutodetector.autodetectSchema(md);
    }

    // --------------------------------------------------------------------------
    // -- Private methods
    // --------------------------------------------------------------------------

    /**
     * Check that schema is present and that the record can be assigned to it - namespace of metadata schema is compared
     * with prime namespace of metadata record.
     *
     * @param md the metadata record being checked for prime namespace equality
     * @param schema the name of the metadata schema we want to test
     */
    private String checkNamespace(Element md, String schema) {
        String result = null;

        try {

            SchemaPlugin mds = hmSchemas.get(schema);
            if (mds != null) {
                String primeNs = mds.getConfiguration().getPrimeNamespace();
                if (log.isDebugEnabled()) {
                    log.debug("  primeNs " + primeNs + " for schema " + schema);
                }
                if (md.getNamespace().getURI().equals(primeNs)) {
                    result = schema;
                } else {
                    // Check if the metadata could match a schema dependency
                    // (If preferredSchema is an ISO profil a fragment or subtemplate
                    // may match ISO core schema and should not be rejected).
                    SchemaPlugin sch = hmSchemas.get(schema);
                    String depends = sch.getConfiguration().getDepends();
                    return checkNamespace(md, depends);
                }
            }
        } catch (Exception e) {
            log.warn("Schema " + schema + " not registered?");
        }

        return result;
    }

    /**
     * Really add a plugin schema to the list of schemas registered here.
     *
     * <p>name the metadata schema we want to add zipFs A filesystem (probably a ZipFileSystem) to copy files from.
     */
    @SuppressWarnings("unused")
    private void realAddPluginSchema() throws Exception {

        throw new UnsupportedOperationException("Not implemented");
        //    Element schemaPluginCatRoot = getSchemaPluginCatalog();
        //
        //    // -- create schema directory
        //    Path schemaDir = buildSchemaFolderPath(name);
        //    Files.createDirectories(schemaDir);
        //
        //    try {
        //      ZipUtil.extract(zipFs, schemaDir);
        //
        //      // -- add schema using the addSchema method
        //      processSchema(applicationContext, schemaDir, schemaPluginCatRoot);
        //
        //      // -- check that dependent schemas are already loaded
        //      Schema schema = hmSchemas.get(name);
        //      checkDepends(name, schema.getDependElements());
        //
        //      writeSchemaPluginCatalog(schemaPluginCatRoot);
        //    } catch (Exception e) {
        //      log.error(e.getMessage(), e);
        //      hmSchemas.remove(name);
        //      IO.deleteFileOrDirectory(schemaDir);
        //      throw new OperationAbortedEx("Failed to add schema " + name + " : " +
        // e.getMessage(),
        // e);
        //    }
    }

    /**
     * Loads the metadata schema from disk and adds it to the pool.
     *
     * @param xmlSchemaFile name of XML schema file (usually schema.xsd)
     * @param xmlIdFile name of XML file that identifies the schema
     * @param oasisCatFile name of XML OASIS catalog file
     */
    @SuppressWarnings("unused")
    private void addSchema(
            ApplicationContext applicationContext,
            Path schemaDir,
            Element schemaPluginCatRoot,
            Path xmlSchemaFile,
            Path xmlIdFile,
            Path oasisCatFile)
            throws Exception {
        // -- add any oasis catalog files to Jeeves.XML_CATALOG_FILES system
        // -- property for resolver to pick up
        if (Files.exists(oasisCatFile)) {
            String catalogProp = System.getProperty(Constants.XML_CATALOG_FILES);
            if (catalogProp == null) catalogProp = ""; // shouldn't happen
            if (catalogProp.isEmpty()) {
                catalogProp = oasisCatFile.toString();
            } else {
                catalogProp = catalogProp + ";" + oasisCatFile;
            }
            System.setProperty(Constants.XML_CATALOG_FILES, catalogProp);
            Xml.resetResolver();
        }

        //    SchematronRepository schemaRepo =
        // applicationContext.getBean(SchematronRepository.class);
        //    SchematronCriteriaGroupRepository criteriaGroupRepository =
        //        applicationContext.getBean(SchematronCriteriaGroupRepository.class);
        String schemaName = schemaDir.getFileName().toString();
        SchemaPlugin schemaPlugin = this.schemaPlugins.stream()
                .filter(plugin -> plugin.getIdentifier().equals(schemaName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Schema plugin not found for " + schemaName));

        XSDSchemaDefinition mds = new SchemaParser().load(xmlSchemaFile, schemaPlugin.getConfiguration());

        schemaPlugin.setXsdSchemaDefinition(mds);
        schemaPlugin.setDirectory(schemaDir);
        schematronCompiler.loadSchematronRules(schemaPlugin, basePath);

        if (schemaPlugin != null && schemaPlugin.getCswTypeNames() != null) {
            hmSchemasTypenames.putAll(schemaPlugin.getCswTypeNames());
        }

        // -- add cached xml files (schema codelists and label files)
        // -- as Jeeves XmlFile objects (they need not exist)

        //    Path locBase = schemaDir.resolve("loc");
        //    //    Map<String, XmlFile> xfMap = new HashMap<>();
        //
        //    for (String fname : fnames) {
        //      Path filePath = path.resolve("loc").resolve(defaultLang).resolve(fname);
        //      if (log.isDebugEnabled()) {
        //        log.debug("Searching for " + filePath);
        //      }
        //      if (Files.exists(filePath)) {
        //        Element config = new Element("xml");
        //        config.setAttribute("name", schemaName);
        //        config.setAttribute("base", locBase.toUri().toString());
        //        config.setAttribute("file", fname);
        //        if (log.isDebugEnabled()) {
        //          log.debug("Adding XmlFile " + Xml.getString(config));
        //        }
        //        //        XmlFile xf = new XmlFile(config, defaultLang, true);
        //        //        xfMap.put(fname, xf);
        //      } else {
        //        log.warn("Unable to load loc file: " + filePath);
        //      }
        //    }

        putSchemaInfo(schemaPlugin.getIdentifier(), schemaPlugin);

        if (log.isDebugEnabled()) {
            log.debug("Property "
                    + Constants.XML_CATALOG_FILES
                    + " is "
                    + System.getProperty(Constants.XML_CATALOG_FILES));
        }

        // -- Add entry for presentation xslt to schemaPlugins catalog
        // -- if this schema is a plugin schema
        int baseNrInt = getHighestSchemaPluginCatalogId(schemaPlugin.getIdentifier(), schemaPluginCatRoot);
        if (baseNrInt == 0) baseNrInt = numberOfCoreSchemasAdded;
        if (baseNrInt != -1) {
            createUriEntryInSchemaPluginCatalog(schemaPlugin.getIdentifier(), baseNrInt, schemaPluginCatRoot);
        }

        // -- copy schema.xsd and schema directory from schema to
        // -- <web_app_dir>/xml/schemas/<schema_name>
        copySchemaXSDsToWebApp(schemaPlugin.getIdentifier(), schemaDir);
    }

    /**
     * Reload a schema.
     *
     * <p>Compile validation rules (conversion from SCH to XSL).
     *
     * @param schemaIdentifier The schema identifier.
     */
    public void reloadSchema(String schemaIdentifier) {
        // TODO: GN5 move to SchemaPlugin
        //        MetadataSchema metadataSchema = this.getSchema(schemaIdentifier);
        //        metadataSchema.loadSchematronRules(basePath);
    }

    /** Read the elements from the schema plugins catalog for use by other methods. */
    @SuppressWarnings("UnusedMethod")
    private Element getSchemaPluginCatalog() throws Exception {
        // -- open schemaPlugins catalog, get children named uri
        return Xml.loadFile(schemaPluginsCat);
    }

    /** Read the empty template for the schema plugins oasis catalog. */
    private Element getSchemaPluginCatalogTemplate() throws Exception {
        return Xml.loadFile(basePath.resolve(Geonet.File.SCHEMA_PLUGINS_CATALOG));
    }

    /**
     * Build a path to the schema plugin folder
     *
     * @param name the name of the schema to use
     */
    private Path buildSchemaFolderPath(String name) {
        return schemaPluginsDir.resolve(name);
    }

    /**
     * Deletes the presentation xslt from the schemaplugin oasis catalog.
     *
     * @param root the list of elements from the schemaplugin-uri-catalog
     * @param name the name of the schema to use
     */
    private Element deleteSchemaFromPluginCatalog(String name, Element root) throws Exception {
        @SuppressWarnings(value = "unchecked")
        List<Content> contents = root.getContent();

        Path ourUri = buildSchemaFolderPath(name);

        int index = -1;
        for (Content content : contents) {
            Element uri;

            if (content instanceof Element) uri = (Element) content;
            else continue; // skip this

            if (!uri.getName().equals("uri") || !uri.getNamespace().equals(Geonet.Namespaces.OASIS_CATALOG)) {
                if (log.isDebugEnabled()) {
                    log.debug("Skipping element " + uri.getQualifiedName() + ":" + uri.getNamespace());
                }
                continue;
            }

            // -- if already mapped then exit
            if (uri.getAttributeValue("uri").equals(ourUri.toString())) index = root.indexOf(uri);
        }

        if (index != -1) root.removeContent(index);
        return root;
    }

    /**
     * Gets the next available blank number that can be used to map the presentation xslt used by the schema (see
     * metadata-utils.xsl and Geonet.File.METADATA_MAX_BLANKS). If the presentation xslt is already mapped then we exit
     * early with return value -1.
     *
     * @param root the list of elements from the schemaplugin-uri-catalog
     * @param name the name of the schema to use
     */
    private int getHighestSchemaPluginCatalogId(String name, Element root) throws Exception {
        @SuppressWarnings("unchecked")
        List<Content> contents = root.getContent();

        String baseBlank = Geonet.File.METADATA_BASEBLANK;
        Path ourUri = buildSchemaFolderPath(name);

        for (Content content : contents) {
            Element uri = null;

            if (content instanceof Element) uri = (Element) content;
            else continue; // skip this

            if (!uri.getName().equals("rewriteURI") || !uri.getNamespace().equals(Geonet.Namespaces.OASIS_CATALOG)) {
                if (log.isDebugEnabled()) {
                    log.debug("Skipping element " + uri.getQualifiedName() + ":" + uri.getNamespace());
                }
                continue;
            }

            // -- if already mapped then exit
            if (uri.getAttributeValue("rewritePrefix").equals(ourUri.toString())) return -1;

            String nameAttr = uri.getAttributeValue("uriStartString");
            if (nameAttr.startsWith(Geonet.File.METADATA_BLANK) && nameAttr.compareTo(baseBlank) > 0) {
                baseBlank = nameAttr;
            }
        }

        // -- get highest appropriate number
        String baseNr = baseBlank.replace(Geonet.File.METADATA_BLANK, "");
        int baseNrInt = 0;
        try {
            baseNrInt = Integer.parseInt(baseNr);
        } catch (NumberFormatException nfe) {
            log.error("Cannot decode blank number from " + baseBlank, nfe);
            throw new IllegalArgumentException("Cannot decode blank number from " + baseBlank);
        }
        return baseNrInt;
    }

    /**
     * Creates a uri remap entry in the schema plugins catalog for the presentation xslt used by the schema.
     *
     * @param name the name of the schema to use
     * @param baseNrInt the number of the plugin schema to map the presentation xslt to
     * @param root the list of elements from the schemaplugin-uri-catalog
     */
    private void createUriEntryInSchemaPluginCatalog(String name, int baseNrInt, Element root) throws Exception {

        baseNrInt = baseNrInt + 1;

        Element newBlank = new Element("rewriteURI", Geonet.Namespaces.OASIS_CATALOG);
        // Element newBlank = new Element("uri", Geonet.OASIS_CATALOG_NAMESPACE);
        if (baseNrInt <= Geonet.File.METADATA_MAX_BLANKS) {
            String zero = "";
            if (baseNrInt < 10) zero = "0";
            newBlank.setAttribute("uriStartString", Geonet.File.METADATA_BLANK + zero + baseNrInt);
            final Path schemaFolderPath = buildSchemaFolderPath(name);
            try {
                newBlank.setAttribute("rewritePrefix", schemaFolderPath.toFile().toString());
            } catch (UnsupportedOperationException e) {
                newBlank.setAttribute("rewritePrefix", schemaFolderPath.toUri().toString());
            }
        } else {
            throw new IllegalArgumentException(
                    "Exceeded maximum number of plugin schemas " + Geonet.File.METADATA_MAX_BLANKS);
        }

        // -- write out new schemaPlugins catalog and re-init the resolvers that
        // -- use this catalog

        root.addContent(newBlank);
    }

    /**
     * Writes the schema plugin catalog out.
     *
     * @param root the list of elements from the schemaplugin-uri-catalog
     */
    private void writeSchemaPluginCatalog(Element root) throws Exception {
        if (createOrUpdateSchemaCatalog) {
            NioPathAwareCatalogResolver.addRewriteDirective(new SchemaPluginUrlRewrite(root));
            try (OutputStream out = Files.newOutputStream(schemaPluginsCat)) {
                Xml.writeResponse(new Document((Element) root.detach()), out);
                Xml.resetResolver();
                //        Xml.clearTransformerFactoryStylesheetCache();
            }
        }
    }

    /**
     * Puts information into the schema information hashtables.
     *
     * @param name schema name
     * @param schemaPlugin schemaPlugin object with details of XML schema info
     */
    private void putSchemaInfo(String name, SchemaPlugin schemaPlugin) {
        hmSchemas.put(name, schemaPlugin);
    }

    /**
     * Processes schemas in either web/xml/schemas or schema plugin directory.
     *
     * @param schemasDir path name of directory containing schemas
     */
    private void processSchema(ApplicationContext applicationContext, Path schemasDir, Element schemaPluginCatRoot)
            throws RuntimeException {

        Path schemaFile = schemasDir.resolve(Geonet.File.SCHEMA);
        Path idFile = schemasDir.resolve(Geonet.File.SCHEMA_ID);
        Path oasisCatFile = schemasDir.resolve(Geonet.File.SCHEMA_OASIS);

        if (!Files.exists(idFile)) {
            log.error("    Skipping : " + schemasDir.getFileName() + " as it doesn't have " + Geonet.File.SCHEMA_ID);
            return;
        }

        log.info("    Adding xml schema : " + schemasDir.getFileName());

        String stage = "";
        try {
            final String schemaName = schemasDir.getFileName().toString();
            if (hmSchemas.containsKey(schemaName)) { // exists so ignore it
                log.error("Schema " + schemaName + " already exists - cannot add!");
            } else {
                stage = "adding the schema information";
                addSchema(applicationContext, schemasDir, schemaPluginCatRoot, schemaFile, idFile, oasisCatFile);
                ResolverWrapper.createResolverForSchema(schemasDir.getFileName().toString(), oasisCatFile);
            }
        } catch (Exception e) {
            String errStr = "Failed whilst " + stage + ". Exception message if any is " + e.getMessage();
            log.error(errStr, e);
            throw new RuntimeException(errStr, e);
        }
    }

    /** Check dependencies for all schemas - remove those that fail. */
    private void checkDependencies(Element schemaPluginCatRoot) throws Exception {
        List<String> removes = new ArrayList<>();

        // process each schema to see whether its dependencies are present
        for (Map.Entry<String, SchemaPlugin> schemaInfo : hmSchemas.entrySet()) {
            SchemaPlugin schema = schemaInfo.getValue();
            try {
                checkDepends(schemaInfo.getKey(), schema.getConfiguration().getDepends());
            } catch (Exception e) {
                log.error("check dependencies failed: " + e.getMessage());
                // add the schema to list for removal
                removes.add(schemaInfo.getKey());
            }
        }

        // now remove any that failed the dependency test
        for (String removeSchema : removes) {
            hmSchemas.remove(removeSchema);
            deleteSchemaFromPluginCatalog(removeSchema, schemaPluginCatRoot);
        }
    }

    private void checkAppSupported(Element schemaPluginCatRoot) throws Exception {
        List<String> removes = new ArrayList<>();

        //    final SystemInfo systemInfo =
        // ApplicationContextHolder.get().getBean(SystemInfo.class);
        //
        //    String version = systemInfo.getVersion();
        //        String version = "5.0.0";
        //        Version appVersion = Version.parseVersionNumber(version);

        // process each schema to see whether its dependencies are present
        // TODO: GN5 / Do we need minor/major version checks?
        //        for (Map.Entry<String, Schema> schemaInfo : hmSchemas.entrySet()) {
        //            Schema schema = schemaInfo.getValue();
        //            String minorAppVersionSupported = schema.getMetadataSchema().getAppMinorVersionSupported();
        //
        //            Version schemaMinorAppVersion = Version.parseVersionNumber(minorAppVersionSupported);
        //
        //            if (appVersion.compareTo(schemaMinorAppVersion) < 0) {
        //                log.error("Schema "
        //                        + schemaInfo.getKey()
        //                        + " requires min Geonetwork version: "
        //                        + minorAppVersionSupported
        //                        + ", current is: "
        //                        + version
        //                        + ". Skip load schema.");
        //                removes.add(schemaInfo.getKey());
        //                continue;
        //            }
        //
        //            String majorAppVersionSupported = schema.getMetadataSchema().getAppMajorVersionSupported();
        //            if (StringUtils.isNotEmpty(majorAppVersionSupported)) {
        //                Version schemaMajorAppVersion = Version.parseVersionNumber(majorAppVersionSupported);
        //
        //                if (appVersion.compareTo(schemaMajorAppVersion) > 0) {
        //                    log.error("Schema "
        //                            + schemaInfo.getKey()
        //                            + " requires max Geonetwork version: "
        //                            + majorAppVersionSupported
        //                            + ", current is: "
        //                            + version
        //                            + ". Skip load schema.");
        //                    removes.add(schemaInfo.getKey());
        //                }
        //            }
        //        }

        // now remove any that failed the app version test
        for (String removeSchema : removes) {
            hmSchemas.remove(removeSchema);
            deleteSchemaFromPluginCatalog(removeSchema, schemaPluginCatRoot);
        }
    }

    /**
     * Get list of schemas that depend on supplied schema name.
     *
     * @param schemaName Schema being checked
     * @return List of schemas that depend on schemaName.
     */
    public List<String> getSchemasThatDependOnMe(String schemaName) {

        List<String> myDepends = new ArrayList<>();

        // process each schema to see whether its dependencies are present
        for (Map.Entry<String, SchemaPlugin> schemaInfoToTest : hmSchemas.entrySet()) {
            if (schemaInfoToTest.getKey().equals(schemaName)) continue;

            SchemaPlugin schema = schemaInfoToTest.getValue();
            String depends = schema.getConfiguration().getDepends();
            if (depends.equals(schemaName)) {
                myDepends.add(schemaInfoToTest.getKey());
            }
        }

        return myDepends;
    }

    /**
     * Check schema dependencies (depend elements).
     *
     * @param thisSchema Schema being checked
     * @param dependency Schema dependency to check
     */
    private void checkDepends(String thisSchema, String dependency) {
        // process each dependency to see whether it is present

        if (StringUtils.isNotBlank(dependency)) {
            if (!hmSchemas.containsKey(dependency)) {
                throw new IllegalArgumentException(
                        "Schema " + thisSchema + " depends on " + dependency + ", but that schema is not loaded");
            }
        }
    }

    /**
     * Create a URL that can be used to point to a schema XSD delivered by GeoNetwork.
     *
     * @param schemaName the name of the schema
     */
    private String getSchemaUrl(String schemaName) {
        //    SettingInfo si = context.getBean(SettingInfo.class);

        String relativePath = Geonet.Path.SCHEMAS + schemaName + "/schema.xsd";
        return "TODO/" + relativePath;
        //    return si.getSiteUrl() + context.getBaseUrl() + "/" + relativePath;
    }

    /**
     * Copy the schema.xsd file and the schema directory from the schema plugin directory to the webapp.
     *
     * @param name the name of the schema
     * @param schemaPluginDir the directory containing the schema plugin
     */
    private void copySchemaXSDsToWebApp(String name, Path schemaPluginDir) throws Exception {
        Path schemasDir = schemaPublicationDir.resolve(Geonet.Path.SCHEMAS);
        Files.createDirectories(schemasDir);

        Path webAppDirSchemaXSD = schemasDir.resolve(name);
        IO.deleteFileOrDirectory(webAppDirSchemaXSD, true);
        if (!Files.exists(webAppDirSchemaXSD)) {
            Files.createDirectories(webAppDirSchemaXSD);
        }

        // copy all XSDs from schema plugin dir to webapp schema dir

        DirectoryStream.Filter<? super Path> xsdFilter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path entry) throws IOException {
                return entry.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".xsd");
            }
        };
        try (DirectoryStream<Path> schemaplugins = Files.newDirectoryStream(schemaPluginDir, xsdFilter)) {
            boolean missingXsdFiles = true;
            for (Path schemaplugin : schemaplugins) {
                IO.copyDirectoryOrFile(schemaplugin, webAppDirSchemaXSD.resolve(schemaplugin.getFileName()), false);
                missingXsdFiles = false;
            }

            if (missingXsdFiles) {
                log.error("Schema " + name + " does not have any XSD files!");
            }
        }

        Path fileSchemaDir = schemaPluginDir.resolve("schema");
        if (Files.exists(fileSchemaDir)) {
            IO.copyDirectoryOrFile(fileSchemaDir, webAppDirSchemaXSD.resolve("schema"), false);
        }
    }

    /** Return the list of typenames declared in all schema plugins. */
    public Map<String, Namespace> getHmSchemasTypenames() {
        return hmSchemasTypenames;
    }

    /** Return the list of namespace URI of all typenames declared in all schema plugins. */
    public List<String> getListOfOutputSchemaURI() {
        Iterator<String> iterator = hmSchemasTypenames.keySet().iterator();
        List<String> listOfSchemaURI = new ArrayList<>();
        while (iterator.hasNext()) {
            String typeLocalName = iterator.next();
            Namespace ns = hmSchemasTypenames.get(typeLocalName);
            listOfSchemaURI.add(ns.getURI());
        }
        return listOfSchemaURI;
    }

    /** Return the list of typenames (with prefix) declared in all schema plugin. */
    public List<String> getListOfTypeNames() {
        Iterator<String> iterator = hmSchemasTypenames.keySet().iterator();
        List<String> listOfTypenames = new ArrayList<>();
        while (iterator.hasNext()) {
            String typeName = iterator.next();
            listOfTypenames.add(typeName);
        }
        return listOfTypenames;
    }
}
