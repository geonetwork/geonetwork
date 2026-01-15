/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.editing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.domain.repository.OperationRepository;
import org.geonetwork.domain.repository.OperationallowedRepository;
import org.geonetwork.editing.model.BatchEditParameter;
import org.geonetwork.metadata.MetadataManager;
import org.geonetwork.schemas.SchemaManager;
import org.geonetwork.schemas.iso19115_3.ISO19115_3SchemaPlugin;
import org.geonetwork.schemas.iso19139.ISO19139SchemaPlugin;
import org.geonetwork.utility.legacy.xml.Xml;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;

@SpringBootTest(
        classes = {TestConfiguration.class, BatchEditsService.class, SchemaManager.class, MetadataManager.class})
class BatchEditsControllerTest {

    @MockBean
    private MetadataRepository metadataRepository;

    @MockBean
    private OperationRepository operationRepository;

    @MockBean
    private OperationallowedRepository operationallowedRepository;

    @Autowired
    BatchEditsService batchEditService;

    @Test
    void previewBatchEdit_withValidInputs_returnsPreview() throws Exception {
        String[] uuids = {"uuid1"};

        BatchEditParameter[] edits = {
            new BatchEditParameter(
                    "mdb:identificationInfo/mri:MD_DataIdentification/mri:citation/cit:CI_Citation/cit:title/gco:CharacterString",
                    """
<gn_create>LAYER NAME</gn_create>
""",
                    null)
        };
        MockHttpServletRequest request = new MockHttpServletRequest();

        Metadata metadata = new Metadata();
        metadata.setUuid("uuid1");
        metadata.setSchemaid("iso19115-3.2018");
        metadata.setData("<mdb:MD_Metadata xmlns:mdb=\"http://standards.iso.org/iso/19115/-3/mdb/2.0\"/>");
        when(metadataRepository.findAllByUuidIn(List.of("uuid1"))).thenReturn(List.of(metadata));
        when(metadataRepository.findByUuid(uuids[0])).thenReturn(Optional.of(metadata));

        String result = Xml.getString(batchEditService
                .applyBatchEdits(uuids, null, false, edits, request, BatchEditMode.PREVIEW)
                .two());

        Processor proc = new Processor(false);
        XPathCompiler xpath = proc.newXPathCompiler();
        XdmNode xmlDocument = proc.newDocumentBuilder().build(new StreamSource(new StringReader(result)));
        String query =
                ".//mdb:MD_Metadata/mdb:identificationInfo/mri:MD_DataIdentification/mri:citation/cit:CI_Citation/cit:title/gco:CharacterString";
        ISO19139SchemaPlugin.allNamespaces.forEach((ns) -> xpath.declareNamespace(ns.getPrefix(), ns.getURI()));
        ISO19115_3SchemaPlugin.allNamespaces.forEach((ns) -> xpath.declareNamespace(ns.getPrefix(), ns.getURI()));
        XPathExecutable exe = xpath.compile(query);
        XPathSelector selector = exe.load();
        selector.setContextItem(xmlDocument);
        XdmValue nodeSet = selector.evaluate();
        assertEquals("LAYER NAME", nodeSet.itemAt(0).getStringValue());
    }
}
