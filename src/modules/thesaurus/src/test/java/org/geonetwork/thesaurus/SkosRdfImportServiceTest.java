/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.thesaurus;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
class SkosRdfImportServiceTest {

    @Mock
    private JdbcTemplate jdbc;

    private SkosRdfImportService service;

    @BeforeEach
    void setup() {
        service = new SkosRdfImportService(jdbc);
    }

    @Test
    void importRdf_readsResourceAndWrites() throws Exception {
        when(jdbc.queryForObject(anyString(), eq(Long.class), any())).thenReturn(1L);

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("codelist_unit_time.rdf")) {
            assertNotNull(in);
            service.importRdf(in);
        }

        String schemeUri = "https://en.wikipedia.org/wiki/Time";
        verify(jdbc)
                .update(
                        contains("INSERT INTO concept_scheme"),
                        eq(schemeUri),
                        eq("codelist_unit_time"),
                        eq("theme"),
                        eq("external"));
        verify(jdbc, atLeastOnce())
                .update(contains("INSERT INTO concept"), eq(1L), eq("https://en.wikipedia.org/wiki/Year"));
        verify(jdbc, atLeastOnce())
                .update(contains("concept_scheme_label"), eq(1L), eq(1L), eq("en"), eq("Units of measurements / Time"));

        verify(jdbc, atLeastOnce()).queryForObject(contains("label_type"), eq(Long.class), eq("prefLabel"));
        verify(jdbc, atLeastOnce()).queryForObject(contains("label_type"), eq(Long.class), eq("altLabel"));
        verify(jdbc, atLeastOnce()).queryForObject(contains("label_type"), eq(Long.class), eq("hiddenLabel"));

        verify(jdbc, atLeastOnce()).queryForObject(contains("relation_type"), eq(Long.class), eq("broader"));
        verify(jdbc, atLeastOnce()).queryForObject(contains("relation_type"), eq(Long.class), eq("narrower"));
        verify(jdbc, atLeastOnce()).queryForObject(contains("relation_type"), eq(Long.class), eq("related"));
        verify(jdbc, atLeastOnce()).queryForObject(contains("relation_type"), eq(Long.class), eq("topConceptOf"));
    }

    @Test
    void importRdf_withNullInputStream_throwsException() {
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> service.importRdf(null));
    }

    @Test
    void importRdf_withInvalidRdfFormat_throwsException() {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            String invalidRdf = "This is not valid RDF";
            try (InputStream in = new java.io.ByteArrayInputStream(invalidRdf.getBytes(StandardCharsets.UTF_8))) {
                service.importRdf(in);
            }
        });
    }

    @Test
    void importRdf_verifiesConceptSchemeInsertion() throws Exception {
        when(jdbc.queryForObject(anyString(), eq(Long.class), any())).thenReturn(1L);

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("codelist_unit_time.rdf")) {
            assertNotNull(in);
            service.importRdf(in);
        }

        verify(jdbc).update(contains("INSERT INTO concept_scheme"), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void importRdf_verifiesConceptInsertion() throws Exception {
        when(jdbc.queryForObject(anyString(), eq(Long.class), any())).thenReturn(1L);

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("codelist_unit_time.rdf")) {
            assertNotNull(in);
            service.importRdf(in);
        }

        verify(jdbc, atLeastOnce()).update(contains("INSERT INTO concept"), eq(1L), anyString());
    }

    @Test
    void importRdf_verifiesLabelTypeQueries() throws Exception {
        when(jdbc.queryForObject(anyString(), eq(Long.class), any())).thenReturn(1L);

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("codelist_unit_time.rdf")) {
            assertNotNull(in);
            service.importRdf(in);
        }

        verify(jdbc, atLeastOnce()).queryForObject(contains("label_type"), eq(Long.class), eq("prefLabel"));
    }

    @Test
    void importRdf_verifiesRelationTypeQueries() throws Exception {
        when(jdbc.queryForObject(anyString(), eq(Long.class), any())).thenReturn(1L);

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("codelist_unit_time.rdf")) {
            assertNotNull(in);
            service.importRdf(in);
        }

        verify(jdbc, atLeastOnce()).queryForObject(contains("relation_type"), eq(Long.class), anyString());
    }

    @Test
    void importRdf_verifiesConceptSchemeLabelInsertion() throws Exception {
        when(jdbc.queryForObject(anyString(), eq(Long.class), any())).thenReturn(1L);

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("codelist_unit_time.rdf")) {
            assertNotNull(in);
            service.importRdf(in);
        }

        verify(jdbc, atLeastOnce()).update(contains("concept_scheme_label"), eq(1L), eq(1L), anyString(), anyString());
    }

    @Test
    void importRdf_verifiesMultipleConceptsProcessed() throws Exception {
        when(jdbc.queryForObject(anyString(), eq(Long.class), any())).thenReturn(1L);

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("codelist_unit_time.rdf")) {
            assertNotNull(in);
            service.importRdf(in);
        }

        verify(jdbc, atLeastOnce()).update(contains("INSERT INTO concept"), anyLong(), anyString());
    }
}
