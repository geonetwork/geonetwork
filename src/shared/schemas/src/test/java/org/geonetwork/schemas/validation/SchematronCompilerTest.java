/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.validation;

import static org.geonetwork.schemas.validation.SchematronCompiler.SCHEMATRON_DIR;
import static org.geonetwork.utility.JarFileCopy.copyFolder;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.nio.file.Files;
import java.nio.file.Path;
import org.geonetwork.schemas.SchemaPlugin;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

public class SchematronCompilerTest {

    @Test
    public void compileSchematronFiles() throws Exception {
        SchemaPlugin schemaPlugin = mock(SchemaPlugin.class);
        String schemaName = "iso19115-3";
        when(schemaPlugin.getIdentifier()).thenReturn(schemaName);

        Path schemaDir = Files.createTempDirectory("gn-schematron-test");
        schemaDir.toFile().deleteOnExit();

        copyFolder(new ClassPathResource(SCHEMATRON_DIR).getURI(), SCHEMATRON_DIR, schemaDir.resolve(SCHEMATRON_DIR));
        copyFolder(new ClassPathResource("schemas").getURI(), "schemas", schemaDir);

        SchematronCompiler compiler = new SchematronCompiler();
        compiler.compile(schemaPlugin, schemaDir);

        Path compiledFile =
                schemaDir.resolve(schemaName).resolve(SCHEMATRON_DIR).resolve("schematron-rules-iso.xsl");
        assertTrue(Files.exists(compiledFile));
    }
}
