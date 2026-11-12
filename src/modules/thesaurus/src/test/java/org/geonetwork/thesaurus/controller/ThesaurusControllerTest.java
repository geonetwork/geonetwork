/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.thesaurus.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.geonetwork.thesaurus.SkosRdfImportService;
import org.geonetwork.thesaurus.service.GetKeywordsService;
import org.geonetwork.thesaurus.service.GetThesauriListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

@WebMvcTest(ThesaurusController.class)
class ThesaurusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetThesauriListService getThesauriListService;

    @MockitoBean
    private GetKeywordsService getKeywordsService;

    @MockitoBean
    private SkosRdfImportService skosRdfImportService;

    private static MockMultipartHttpServletRequestBuilder importRequest() {
        var file = new MockMultipartFile("file", "thesaurus.rdf", "application/rdf+xml", "<rdf/>".getBytes());
        return (MockMultipartHttpServletRequestBuilder)
                multipart("/api/thesaurus/import").file(file).with(csrf());
    }

    @Test
    @WithAnonymousUser
    void import_whenAnonymous_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(importRequest()).andExpect(status().isUnauthorized());

        verify(skosRdfImportService, never()).importRdf(any());
    }

    @Test
    @WithMockUser(roles = "Editor")
    void import_whenNotAdmin_shouldReturnForbidden() throws Exception {
        mockMvc.perform(importRequest()).andExpect(status().isForbidden());

        verify(skosRdfImportService, never()).importRdf(any());
    }

    @Test
    @WithMockUser(roles = "Administrator")
    void import_whenAdmin_shouldImportUploadedFile() throws Exception {
        mockMvc.perform(importRequest()).andExpect(status().isOk());

        verify(skosRdfImportService).importRdf(any());
    }
}
