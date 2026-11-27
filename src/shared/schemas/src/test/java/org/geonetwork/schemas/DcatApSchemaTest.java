/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.schemas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

/** Checks that the DCAT-AP schema plugin XSD can be loaded the same way SchemaManager does on startup. */
class DcatApSchemaTest {

    @Test
    void dcatApXsd_shouldLoad() throws Exception {
        Path schemaDir = new ClassPathResource("schemas/dcat-ap").getFile().toPath();

        MetadataSchema schema =
                new SchemaLoader().load(schemaDir.resolve("schema.xsd"), schemaDir.resolve("schema-substitutes.xml"));

        assertNotNull(schema);
        assertEquals("http://www.w3.org/1999/02/22-rdf-syntax-ns#", schema.getNS("rdf"));
        assertEquals("http://www.w3.org/ns/dcat#", schema.getNS("dcat"));
        assertNotNull(schema.getElementType("dcat:Dataset", "dcat:dataset"));
        assertTrue(schema.getNS("dct") != null || schema.getNS("dcterms") != null);
    }
}
