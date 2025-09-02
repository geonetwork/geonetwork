/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class MetadataSchemaAutodetectorTest {
    @Test
    @SuppressWarnings("unchecked")
    public void compileAutodetectMetadata() throws Exception {
        // ISO19139 metadata
        Resource iso19139resource = new ClassPathResource("xml/iso19139.xml");
        Element metadata = Xml.loadFile(iso19139resource.getFile().toPath());

        // ISO19139 NL metadata
        Resource iso19139NLresource = new ClassPathResource("xml/iso19139.nl.geografie.2.0.0.xml");
        Element metadataNL = Xml.loadFile(iso19139NLresource.getFile().toPath());

        // ISO19139 schema plugin configuration
        SchemaPlugin schemaPluginIso19139 = mock(SchemaPlugin.class);
        String schemaNameIso19139 = "iso19139";

        List<Namespace> namespacesIso19139 = new ArrayList<>();
        namespacesIso19139.add(metadata.getNamespace());
        namespacesIso19139.addAll((List<Namespace>) metadata.getAdditionalNamespaces());

        XSDSchemaDefinition xsdSchemaDefinitionIso19139 = mock(XSDSchemaDefinition.class);
        when(xsdSchemaDefinitionIso19139.getNamespaces()).thenReturn(namespacesIso19139);

        when(schemaPluginIso19139.getIdentifier()).thenReturn(schemaNameIso19139);
        when(schemaPluginIso19139.getXsdSchemaDefinition()).thenReturn(xsdSchemaDefinitionIso19139);

        SchemaPluginConfiguration configuration = mock(SchemaPluginConfiguration.class);
        when(configuration.getAutodetect())
                .thenReturn(
                        "(local-name(.) = 'MD_Metadata' or local-name(.) = 'CI_ResponsibleParty' or local-name(.) = 'DQ_DomainConsistency' or local-name(.) = 'MD_Format' or local-name(.) = 'EX_Extent')");
        when(schemaPluginIso19139.getConfiguration()).thenReturn(configuration);

        List<Namespace> namespacesIso19139NL = new ArrayList<>();
        namespacesIso19139NL.add(metadataNL.getNamespace());
        namespacesIso19139NL.addAll((List<Namespace>) metadataNL.getAdditionalNamespaces());

        XSDSchemaDefinition xsdSchemaDefinitionIso19139NL = mock(XSDSchemaDefinition.class);
        when(xsdSchemaDefinitionIso19139NL.getNamespaces()).thenReturn(namespacesIso19139NL);

        // ISO19139 NL schema plugin configuration
        SchemaPlugin schemaPluginIso19139NL = mock(SchemaPlugin.class);
        String schemaNameIso19139NL = "iso19139.nl.geografie.2.0.0";
        when(schemaPluginIso19139NL.getIdentifier()).thenReturn(schemaNameIso19139NL);
        when(schemaPluginIso19139NL.getXsdSchemaDefinition()).thenReturn(xsdSchemaDefinitionIso19139NL);

        SchemaPluginConfiguration configurationNL = mock(SchemaPluginConfiguration.class);
        when(configurationNL.getDepends()).thenReturn(schemaNameIso19139);
        when(configurationNL.getAutodetect())
                .thenReturn(
                        """
            count(gmd:metadataStandardVersion[gco:CharacterString = 'Nederlands metadata profiel op ISO 19115 voor geografie 2.1.0' or
                    gco:CharacterString = 'Nederlands metadata profiel op ISO 19115 voor geografie 2.0' or
                    gco:CharacterString = 'Nederlandse metadata profiel op ISO 19115 voor geografie 2.0' or
                    gco:CharacterString = 'Nederlands metadata profiel op ISO 19115 voor geografie 2.1' or
                    gco:CharacterString = 'Nederlandse metadata profiel op ISO 19115 voor geografie 2.1' or
                    gco:CharacterString = 'Nederlandse metadata profiel op ISO 19115 voor geografie 2.1.0' or
                    gco:CharacterString = 'Nederlands metadata profiel op ISO 19115 voor geografie 2.0.0']) > 0""");
        when(schemaPluginIso19139NL.getConfiguration()).thenReturn(configurationNL);

        // Checks
        MetadataSchemaAutodetector metadataSchemaAutodetector =
                new MetadataSchemaAutodetector(List.of(schemaPluginIso19139, schemaPluginIso19139NL));

        Assertions.assertEquals("iso19139", metadataSchemaAutodetector.autodetectSchema(metadata));
        Assertions.assertEquals("iso19139.nl.geografie.2.0.0", metadataSchemaAutodetector.autodetectSchema(metadataNL));
    }
}
