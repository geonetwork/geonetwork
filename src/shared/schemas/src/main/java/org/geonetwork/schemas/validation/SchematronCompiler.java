/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.validation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.constants.Geonet;
import org.geonetwork.schemas.SchemaPlugin;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class SchematronCompiler {
    public static final String XSL_FILE_EXTENSION = ".xsl";
    public static final String SCHEMATRON_DIR = "schematron";
    public static final String SCHEMATRON_RULE_FILE_PREFIX = "schematron-rules";
    public static final String SCH_FILE_EXTENSION = ".sch";

    public void compile(SchemaPlugin schemaPlugin, Path schemaDir) {
        Path schematronResourceDir = schemaDir.resolve(SCHEMATRON_DIR);
        Path schemaSchematronDir =
                schemaDir.resolve(schemaPlugin.getIdentifier()).resolve(SCHEMATRON_DIR);

        if (!Files.exists(schemaSchematronDir)) {
            log.debug(String.format(
                    "No schematron rules found for schema %s at %s",
                    schemaPlugin.getIdentifier(), schemaSchematronDir));
            return;
        }

        Path schematronCompilationFile = schematronResourceDir.resolve("iso_svrl_for_xslt2.xsl");
        Path schematronExpandFile = schematronResourceDir.resolve("iso_abstract_expand.xsl");

        if (log.isDebugEnabled()) {
            log.debug("     Schematron compilation for schema " + schemaPlugin.getIdentifier());
            log.debug("          - compiling with " + schematronCompilationFile);
            log.debug("          - rules location is " + schemaSchematronDir);
        }

        try (DirectoryStream<Path> paths = Files.newDirectoryStream(schemaSchematronDir, "*.sch")) {
            for (Path rule : paths) {
                if (log.isDebugEnabled()) {
                    log.debug("                - rule " + rule);
                }

                // Compile all schematron rules
                final String xslPath = rule.toAbsolutePath().toString().replace(SCH_FILE_EXTENSION, XSL_FILE_EXTENSION);
                Path schematronXslFilePath = rule.getFileSystem().getPath(xslPath);

                try {
                    Element schematronRule = Xml.loadFile(schemaSchematronDir.resolve(rule));
                    Element schematronStylesheetExpanded =
                            Xml.transformJdomElement(schematronRule, schematronExpandFile.toFile(), new HashMap<>());
                    Element schematronStylesheet = Xml.transformJdomElement(
                            schematronStylesheetExpanded, schematronCompilationFile.toFile(), new HashMap<>());
                    Files.write(
                            schematronXslFilePath,
                            Xml.getString(schematronStylesheet).getBytes(StandardCharsets.UTF_8));
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

    /** Schematron rules filename is like "schematron-rules-iso.xsl */
    @SuppressWarnings("unused")
    private static class SchematronReportRulesFilter implements DirectoryStream.Filter<Path> {
        @Override
        public boolean accept(Path entry) {
            String filename = entry.getFileName().toString();
            return filename.startsWith(SCHEMATRON_RULE_FILE_PREFIX) && filename.endsWith(XSL_FILE_EXTENSION);
        }
    }

    /**
     * Compile and register all schematron rules available for current schema. Schematron rules files are in schema
     * schematron directory and start with "schematron-rules" prefix.
     */
    public void loadSchematronRules(SchemaPlugin schemaPlugin, Path schemaDir) {
        compile(schemaPlugin, schemaDir);

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
}
