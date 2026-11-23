/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.indexConvert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.service.configuration.FormatMediaTypeEntry;
import org.geonetwork.ogcapi.service.configuration.OgcApiRecordsOutputConfig;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.DynamicPropertiesFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * Records in other standards than ISO (eg. dcat-ap) may have an empty metadataIdentifier in the index. The conversion
 * must rely on the record UUID.
 */
class OgcApiGeoJsonConverterDcatApTest {

    private OgcApiGeoJsonConverter converter;
    private MetadataRepository metadataRepository;
    private IndexRecord indexRecord;

    @BeforeEach
    void setUp() throws Exception {
        metadataRepository = mock(MetadataRepository.class);
        converter = new OgcApiGeoJsonConverter();
        converter.metadataRepository = metadataRepository;
        converter.ogcApiRecordsOutputConfig = new OgcApiRecordsOutputConfig();
        converter.dynamicPropertiesFacade = mock(DynamicPropertiesFacade.class);
        converter.formatMediaTypeResolver = new FormatMediaTypeResolver((List<FormatMediaTypeEntry>) null);

        try (var in = new ClassPathResource("indexrecord/dcat-ap_dataset.json").getInputStream()) {
            indexRecord = new ObjectMapper().readValue(in, IndexRecord.class);
        }
    }

    @Test
    void convert_dcatApRecordWithEmptyMetadataIdentifier() {
        assertEquals("", indexRecord.getMetadataIdentifier());

        var result = converter.convert(indexRecord, null);

        assertNotNull(result);
        assertEquals("31a73a47-5df1-4c5f-930e-87d547a6373d", result.getId());
        assertEquals("dataset", result.getProperties().getType());
        verify(metadataRepository, never()).findOneByUuid(anyString());
    }

    @Test
    void convert_includeRawXml_usesRecordUuid() {
        converter.ogcApiRecordsOutputConfig.includeRawXML = true;
        var metadata = new Metadata();
        metadata.setData("<rdf:RDF/>");
        when(metadataRepository.findOneByUuid("31a73a47-5df1-4c5f-930e-87d547a6373d"))
                .thenReturn(Optional.of(metadata));

        var result = converter.convert(indexRecord, null);

        assertEquals("<rdf:RDF/>", result.getMetadataRecordText());
    }
}
