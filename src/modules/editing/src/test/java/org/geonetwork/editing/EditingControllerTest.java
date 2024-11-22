/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.editing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.constants.Geonet;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.indexing.IndexingMode;
import org.geonetwork.schemas.SchemaManager;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest(
        classes = {
            TestConfiguration.class,
            EditUtils.class,
            SchemaManager.class,
            MetadataManager.class,
            MetadataXmlSerializer.class,
            EditLib.class
        })
class EditingControllerTest {

    @MockBean
    private MetadataRepository metadataRepository;

    @Autowired
    EditUtils editUtils;

    @Autowired
    private SchemaManager schemaManager;

    public final String XPATH_ISO191153_TITLE_TEXT =
            "mdb:identificationInfo/*/mri:citation/*/cit:title/gco:CharacterString";
    public final String XPATH_ISO191153_IDENTIFICATION_INSTANCE = "mdb:identificationInfo/*";
    public final String XPATH_ISO191153_ABSTRACT = "mdb:identificationInfo/*/mri:abstract";
    public final String XPATH_ISO191153_ABSTRACT_TEXT = "mdb:identificationInfo/*/mri:abstract/gco:CharacterString";

    @Test
    public void testEditing() throws Exception {
        String file = "iso19115-3.2018_dataset";
        String schema = StringUtils.split(file, "_")[0];
        String fileBaseName = String.format("samples/%s", file);
        String xml = IOUtils.toString(new ClassPathResource(fileBaseName + ".xml").getInputStream());

        Metadata metadata = new Metadata();
        metadata.setUuid("uuid1");
        metadata.setId(1);
        metadata.setSchemaid(schema);
        metadata.setData(xml);
        when(metadataRepository.findAllByUuidIn(List.of(metadata.getUuid()))).thenReturn(List.of(metadata));
        when(metadataRepository.findById(metadata.getId())).thenReturn(Optional.of(metadata));

        Element metaDocument = editUtils.getMetadataEmbedded(String.valueOf(metadata.getId()), false);
        assertNotNull(metaDocument);

        Object o = Xml.selectSingle(
                metaDocument,
                XPATH_ISO191153_TITLE_TEXT,
                schemaManager.getSchema(schema).getSchemaNS());
        if (o instanceof Element title) {
            Element geonetElement = title.getChild("element", Geonet.Namespaces.GEONET);

            // gco:CharacterString is a mandatory element
            testMetadocumentCardinalityForMandatoryElement(geonetElement);

            metaDocument = testUpdateElement(
                    XPATH_ISO191153_TITLE_TEXT,
                    geonetElement,
                    "Updated title",
                    metadata,
                    schema,
                    getMetadocumentVersion(metaDocument));
        }

        o = Xml.selectSingle(
                metaDocument,
                XPATH_ISO191153_ABSTRACT_TEXT,
                schemaManager.getSchema(schema).getSchemaNS());
        if (o instanceof Element description) {
            Element geonetElement = description.getChild("element", Geonet.Namespaces.GEONET);
            metaDocument = testUpdateElement(
                    XPATH_ISO191153_ABSTRACT_TEXT,
                    geonetElement,
                    "Updated abstract",
                    metadata,
                    schema,
                    getMetadocumentVersion(metaDocument));
        }

        // Delete abstract
        o = Xml.selectSingle(
                metaDocument,
                XPATH_ISO191153_ABSTRACT,
                schemaManager.getSchema(schema).getSchemaNS());
        if (o instanceof Element description) {
            Element geonetElement = description.getChild("element", Geonet.Namespaces.GEONET);
            editUtils.deleteElementEmbedded(
                    String.valueOf(metadata.getId()),
                    geonetElement.getAttributeValue("ref"),
                    description.getParentElement().getName());
            // Removed in session, not in database yet
            editUtils.updateContent(
                    buildDefaultRequestParameter(metadata.getId(), getMetadocumentVersion(metaDocument)),
                    false,
                    true,
                    IndexingMode.none);
            Optional<Metadata> updateRecord = metadataRepository.findById(metadata.getId());
            assertEquals(
                    0,
                    Xml.selectNodes(
                                    Xml.loadString(updateRecord.get().getData(), false),
                                    XPATH_ISO191153_ABSTRACT,
                                    schemaManager.getSchema(schema).getSchemaNS())
                            .size());
        }

        // Add abstract
        o = Xml.selectSingle(
                metaDocument,
                XPATH_ISO191153_IDENTIFICATION_INSTANCE,
                schemaManager.getSchema(schema).getSchemaNS());
        if (o instanceof Element identification) {
            Element geonetElement = identification.getChild("element", Geonet.Namespaces.GEONET);
            String metadocumentElementRef = geonetElement.getAttributeValue("ref");
            Element metadocumentAfterAdd = editUtils.addElementEmbedded(
                    String.valueOf(metadata.getId()), metadocumentElementRef, "mri:abstract", null);

            // Done in session, not in database yet
            editUtils.updateContent(
                    buildDefaultRequestParameter(metadata.getId(), getMetadocumentVersion(metaDocument)),
                    false,
                    true,
                    IndexingMode.none);
            Optional<Metadata> updateRecord = metadataRepository.findById(metadata.getId());
            List<?> descriptionNodes = Xml.selectNodes(
                    Xml.loadString(updateRecord.get().getData(), false),
                    XPATH_ISO191153_ABSTRACT,
                    schemaManager.getSchema(schema).getSchemaNS());
            assertEquals(1, descriptionNodes.size());
        }
    }

    private Element testUpdateElement(
            String xpath,
            Element geonetElement,
            String newElementValue,
            Metadata metadata,
            String schema,
            String version)
            throws Exception {
        Object o;
        String metadocumentElementRef = geonetElement.getAttributeValue("ref");
        assertNotNull(metadocumentElementRef);

        var allRequestParams = buildDefaultRequestParameter(metadata.getId(), version);

        allRequestParams.put("_" + metadocumentElementRef, newElementValue);

        editUtils.updateContent(allRequestParams, false, true, IndexingMode.none);
        Element updateMetadata = editUtils.getMetadataEmbedded(String.valueOf(metadata.getId()), false);
        o = Xml.selectSingle(
                updateMetadata, xpath, schemaManager.getSchema(schema).getSchemaNS());
        assertEquals(newElementValue, ((Element) o).getText());

        return updateMetadata;
    }

    private static HashMap<String, String> buildDefaultRequestParameter(int id, String version) {
        var allRequestParams = new HashMap<String, String>();
        allRequestParams.put(EditingController.ID, String.valueOf(id));
        allRequestParams.put(EditingController.VERSION, version);
        return allRequestParams;
    }

    private static String getMetadocumentVersion(Element metaDocument) {
        return metaDocument
                .getChild("info", Geonet.Namespaces.GEONET)
                .getChild("version")
                .getText();
    }

    private static void testMetadocumentCardinalityForMandatoryElement(Element geonetElement) {
        assertEquals("1", geonetElement.getAttributeValue("min"));
        assertEquals("1", geonetElement.getAttributeValue("max"));
    }

    private static void testMetadocumentCardinalityForOptionalElement(Element geonetElement) {
        assertEquals("0", geonetElement.getAttributeValue("min"));
        assertEquals("1", geonetElement.getAttributeValue("max"));
    }
}
