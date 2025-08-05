/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas;

import static org.geonetwork.schemas.SchemaManager.SCHEMATRON_DIR;
import static org.geonetwork.schemas.SchemaManager.SCHEMATRON_RULE_FILE_PREFIX;
import static org.geonetwork.schemas.SchemaManager.SCH_FILE_EXTENSION;
import static org.geonetwork.schemas.SchemaManager.XSL_FILE_EXTENSION;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.constants.Geonet;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.jdom.Namespace;

/** Class that holds the schema definition for a given schema. */
@Slf4j
public class XSDSchemaDefinition {
    private Map<String, List<String>> hmElements = new HashMap<>();
    private Map<String, List<List<String>>> hmRestric = new HashMap<>();
    private Map<String, XSDTypeDefinition> hmTypes = new HashMap<>();
    private Map<String, List<String>> hmSubs = new HashMap<>();
    private Map<String, String> hmSubsLink = new HashMap<>();
    private Map<String, Namespace> hmNameSpaces = new HashMap<>();
    private Map<String, Namespace> hmPrefixes = new HashMap<>();

    private String[] schematronRules;

    XSDSchemaDefinition() {}

    public XSDTypeDefinition getTypeInfo(String type) {
        if (hmTypes.get(type) == null) return new XSDTypeDefinition();
        else return hmTypes.get(type);
    }

    // ---------------------------------------------------------------------------

    public String getElementType(String elem, String parent) throws Exception {
        // two cases here - if we have just one element (or a substitute) with
        // this name then return its type

        List<String> childType = hmElements.get(elem);
        if (childType == null) {
            // Check and see whether we can substitute another element from the
            // substitution link
            String oldelem = elem;
            elem = hmSubsLink.get(elem);
            childType = hmElements.get(elem);
            if (childType == null) {
                log.warn("ERROR: Mismatch between schema and xml: No type for 'element' : "
                        + oldelem
                        + " with parent "
                        + parent
                        + ". Returning xs:string");
                return "xs:string";
            }
        }
        if (childType.size() == 1) return childType.get(0);

        // OTHERWISE get the type by examining the parent:
        // for each parent with that name parent
        // 1. retrieve its mdt
        List<String> exType = hmElements.get(parent);
        if (exType == null) return "xs:string";
        for (String type : exType) {
            // 2. search that mdt for the element names elem
            XSDTypeDefinition mdt = getTypeInfo(type);
            for (int k = 0; k < mdt.getElementCount(); k++) {
                String elemTest = mdt.getElementAt(k);
                // 3. return the type name of that element
                if (elem.equals(elemTest)) {
                    return mdt.getElementTypeAt(k);
                }
            }
        }

        return null;
    }

    /** A simple type is a type that has no children and no attributes (but can have restrictions on its value) */
    public boolean isSimpleElement(String elem, String parent) throws Exception {
        String type = getElementType(elem, parent);
        return type != null && !hmTypes.containsKey(type);
    }

    public List<String> getElementValues(String elem, String parent) throws Exception {

        String type = getElementType(elem, parent);
        String restricName = elem;
        if (type != null) restricName = restricName + "+" + type;

        // two cases here - if we have just one element with this name
        // then return its values
        List<List<String>> childValues = hmRestric.get(restricName);
        if (childValues == null) return null;
        if (childValues.size() == 1) return childValues.get(0);

        // OTHERWISE we don't know what to do so return the first one anyway! This
        // should not happen....
        return childValues.get(0);
    }

    void addElement(String name, String type, List<String> alValues, List<String> alSubs, String subLink) {
        // first just add the subs - because these are for global elements we
        // never have a clash because global elements are all in the same scope
        // and are thus unique
        if (alSubs != null && !alSubs.isEmpty()) hmSubs.put(name, alSubs);
        if (subLink != null && StringUtils.isNotBlank(subLink)) hmSubsLink.put(name, subLink);

        List<String> exType = hmElements.get(name);

        // it's already there but the type has been added already
        if (exType != null && exType.contains(type)) return;

        // it's already there but doesn't have this type
        if (exType != null && !exType.contains(type)) {

            // it's not there so add a new list
        } else {
            exType = new ArrayList<>();
            hmElements.put(name, exType);
        }
        exType.add(type);

        String restricName = name;
        if (type != null) restricName = name + "+" + type;

        // it's already there
        List<List<String>> exValues = hmRestric.get(restricName);
        if (exValues != null) {
            // it's not there so add a new list of lists
        } else {
            exValues = new ArrayList<>();
            hmRestric.put(restricName, exValues);
        }
        exValues.add(alValues);
    }

    public void addType(String name, XSDTypeDefinition mdt) {
        mdt.setName(name);
        hmTypes.put(name, mdt);
    }

    public void addNS(String targetNSPrefix, String targetNSUri) {

        Namespace ns = Namespace.getNamespace(targetNSPrefix, targetNSUri);
        hmNameSpaces.put(targetNSPrefix, ns);
        hmPrefixes.put(targetNSUri, ns);
    }

    // ---------------------------------------------------------------------------
    @JsonIgnore
    public String getNS(String targetNSPrefix) {
        Namespace ns = hmNameSpaces.get(targetNSPrefix);
        if (ns != null) {
            return ns.getURI();
        } else {
            return null;
        }
    }

    /** Return the list of namespaces for the schema. */
    @JsonIgnore
    public List<Namespace> getNamespaces() {
        List<Namespace> list = new ArrayList<>(hmNameSpaces.size());
        for (Namespace ns : hmNameSpaces.values()) {
            list.add(ns);
        }
        return list;
    }

    public String getPrefix(String theNSUri) {
        Namespace ns = hmPrefixes.get(theNSUri);
        if (ns != null) {
            return ns.getPrefix();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public List<Namespace> getSchemaNS() {
        return new ArrayList<>(hmPrefixes.values());
    }

    @JsonProperty(value = "namespaces")
    public Map<String, String> getSchemaNSWithPrefix() {
        Map<String, String> mapNs = new HashMap<>();
        List<Namespace> schemaNsList = getSchemaNS();

        for (Namespace ns : schemaNsList) {
            mapNs.put(ns.getPrefix(), ns.getURI());
        }
        return mapNs;
    }

    // TODO: GN5 Move to dedicated class
    @SuppressWarnings("unused")
    public void buildchematronRules(Path basePath, Path schemaDirectory) {
        Path schematronResourceDir =
                basePath.resolve("WEB-INF").resolve("classes").resolve(SCHEMATRON_DIR);
        Path schemaSchematronDir = schemaDirectory.resolve(SCHEMATRON_DIR);
        Path schematronCompilationFile = schematronResourceDir.resolve("iso_svrl_for_xslt2.xsl");
        @SuppressWarnings("unused")
        Path schematronExpandFile = schematronResourceDir.resolve("iso_abstract_expand.xsl");

        if (log.isDebugEnabled()) {
            // log.debug("     Schematron compilation for schema " + schemaPlugin.getIdentifier());
            log.debug("          - compiling with " + schematronCompilationFile);
            log.debug("          - rules location is " + schemaSchematronDir);
        }

        if (Files.exists(schemaSchematronDir)) {
            try (DirectoryStream<Path> paths = Files.newDirectoryStream(schemaSchematronDir, "*.sch")) {
                for (Path rule : paths) {
                    if (log.isDebugEnabled()) {
                        log.debug("                - rule " + rule);
                    }

                    // Compile all schematron rules
                    final String xslPath =
                            rule.toAbsolutePath().toString().replace(SCH_FILE_EXTENSION, XSL_FILE_EXTENSION);
                    Path schematronXslFilePath = rule.getFileSystem().getPath(xslPath);

                    try (OutputStream schematronXsl = Files.newOutputStream(schematronXslFilePath)) {
                        Element schematronRule = Xml.loadFile(schemaSchematronDir.resolve(rule));
                        // Expand schematron abstract rules
                        // TODO           Element schematronExpandXml =
                        // Xml.transform(schematronRule,
                        // schematronExpandFile);
                        //            Xml.transform(schematronExpandXml, schematronCompilationFile,
                        // schematronXsl);
                    } catch (FileNotFoundException e) {
                        log.error(
                                Geonet.SCHEMA_MANAGER,
                                "     Schematron rule file not found "
                                        + schematronXslFilePath
                                        + ". Error is "
                                        + e.getMessage());
                    } catch (Exception e) {
                        log.error(
                                Geonet.SCHEMA_MANAGER,
                                "     Schematron rule compilation failed for "
                                        + schematronXslFilePath
                                        + ". Error is "
                                        + e.getMessage());
                    }
                }
            } catch (IOException e) {
                log.error(
                        Geonet.SCHEMA_MANAGER,
                        "     Schematron rule file not found " + schemaSchematronDir + ". Error is " + e.getMessage());
            }
        }
    }

    /**
     * Compile and register all schematron rules available for current schema. Schematron rules files are in schema
     * schematron directory and start with "schematron-rules" prefix.
     */
    public void loadSchematronRules(Path basePath, Path schemaDirectory) {
        // Compile schema schematron rules
        buildchematronRules(basePath, schemaDirectory);

        setSchematronRules(new String[] {});
        //    List<String> saSchemas = new ArrayList();

        //    final Path schematronDir = schemaDir.resolve(SCHEMATRON_DIR);
        //    if (Files.exists(schematronDir)) {

        //      Map<String, Schematron> existing = Maps.newHashMap();
        //
        //      for (Schematron schematron : schemaRepo.findAllBySchemaName(schemaName)) {
        //        existing.put(schematron.getRuleName(), schematron);
        //      }

        //      try (DirectoryStream<Path> schematronFiles =
        //          Files.newDirectoryStream(schematronDir, new SchematronReportRulesFilter())) {
        //        for (Path schematronFile : schematronFiles) {
        //          final String schematronFileName = schematronFile.getFileName().toString();
        //          saSchemas.add(schematronFileName);
        //
        //          Schematron schematron = new Schematron();
        //          schematron.setSchemaName(schemaName);
        //          schematron.setFile(schematronFileName);
        //          schematron.setDisplayPriority(0);
        //
        //          // if schematron not already exists
        //          if (existing.containsKey(schematron.getRuleName())) {
        //            if (!Files.exists(schematronDir.resolve(schematron.getFile()))) {
        //              schematron.setFile(schematronFileName);
        //              schemaRepo.saveAndFlush(schematron);
        //            }
        //          } else {
        //            schematron
        //                .getLabelTranslations()
        //                .put(Geonet.DEFAULT_LANGUAGE, schematron.getRuleName());
        //            schemaRepo.saveAndFlush(schematron);
        //
        //            final SchematronCriteriaGroup schematronCriteriaGroup = new
        // SchematronCriteriaGroup();
        //            schematronCriteriaGroup.setId(
        //                new SchematronCriteriaGroupId("*Generated*", schematron.getId()));
        //            schematronCriteriaGroup.setRequirement(schematron.getDefaultRequirement());
        //
        //            SchematronCriteria criteria = new SchematronCriteria();
        //            criteria.setValue("_ignored_");
        //            criteria.setType(SchematronCriteriaType.ALWAYS_ACCEPT);
        //
        //            schematronCriteriaGroup.addCriteria(criteria);
        //
        //            criteriaGroupRepository.saveAndFlush(schematronCriteriaGroup);
        //          }
        //        }
        //      } catch (IOException e) {
        //        throw new RuntimeException(e);
        //      }

        //      setSchematronPriorities();
        //    }
        //
        //    setSchematronRules(saSchemas.toArray(new String[saSchemas.size()]));
    }

    @SuppressWarnings("unused")
    private void setSchematronPriorities() {
        //    List<Schematron> schematronList = schemaRepo.findAllBySchemaName(schemaName);
        //
        //    Collections.sort(schematronList, Schematron.DISPLAY_PRIORITY_COMPARATOR);
        //
        //    List<Schematron> updated = Lists.newArrayList();
        //    for (int i = 0; i < schematronList.size(); i++) {
        //      Schematron schematron = schematronList.get(i);
        //      if (schematron.getDisplayPriority() != i) {
        //        schematron.setDisplayPriority(i);
        //        updated.add(schematron);
        //      }
        //    }
        //    this.schemaRepo.saveAll(updated);
    }

    /** Return the list of schematron rules to applied for this schema */
    public String[] getSchematronRules() {
        if (schematronRules != null) {
            return this.schematronRules.clone();
        } else {
            return new String[] {};
        }
    }

    private void setSchematronRules(String[] schematronRules) {
        if (schematronRules != null) {
            this.schematronRules = schematronRules.clone();
        }
    }

    /** Schematron rules filename is like "schematron-rules-iso.xsl */
    @SuppressWarnings("unused")
    private static class SchematronReportRulesFilter implements DirectoryStream.Filter<Path> {
        @Override
        public boolean accept(Path entry) {
            String filename = entry.getFileName().toString();
            return filename.startsWith(SCHEMATRON_RULE_FILE_PREFIX) && filename.endsWith(XSL_FILE_EXTENSION);
        }
    }
}
