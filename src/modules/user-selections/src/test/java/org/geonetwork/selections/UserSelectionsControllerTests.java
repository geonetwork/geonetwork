/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.selections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.geonetwork.domain.Selection;
import org.geonetwork.utility.legacy.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserSelectionsController.class)
@ContextConfiguration(classes = UserSelectionsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserSelectionsControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserSelectionsService userSelectionsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetSelectionList() throws Exception {
        Selection selection1 = Selection.builder().id(1).name("Selection 1").build();
        Selection selection2 = Selection.builder().id(2).name("Selection 2").build();
        when(userSelectionsService.retrieveAllSelections()).thenReturn(List.of(selection1, selection2));

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(selection1, selection2))));
    }

    @Test
    public void testGetUserSelectionRecords() throws Exception {
        when(userSelectionsService.retrieveUserSelectionItems(eq(1), any(), eq(2)))
                .thenReturn(List.of("uuid1", "uuid2"));

        mvc.perform(get("/1/items").param("userIdentifier", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of("uuid1", "uuid2"))));
    }

    @Test
    public void testAddUserSelectionRecords() throws Exception {
        doNothing().when(userSelectionsService).addItemsToUserSelection(eq(1), eq(2), any(), any());

        mvc.perform(put("/1/items").param("userIdentifier", "2").param("uuid", "uuid1", "uuid2"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddUserSelectionRecordsNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Not found"))
                .when(userSelectionsService)
                .addItemsToUserSelection(eq(1), eq(2), any(), any());

        mvc.perform(put("/1/items").param("userIdentifier", "2").param("uuid", "uuid1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not found"));
    }

    @Test
    public void testDeleteUserSelectionRecords() throws Exception {
        doNothing().when(userSelectionsService).deleteUserSelectionRecords(eq(1), eq(2), any(), any());

        mvc.perform(delete("/1/items").param("userIdentifier", "2").param("uuid", "uuid1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateUserSelection() throws Exception {
        Selection selection = Selection.builder().name("Updated Selection").build();
        doNothing().when(userSelectionsService).updateUserSelection(eq(1), any());

        mvc.perform(put("/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(selection)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUserSelection() throws Exception {
        doNothing().when(userSelectionsService).deleteUserSelection(1);

        mvc.perform(delete("/1")).andExpect(status().isNoContent());
    }

    @Test
    public void testCreatePersistentSelectionType() throws Exception {
        Selection selection = Selection.builder().id(1).name("New Selection").build();
        when(userSelectionsService.addUserSelection(any())).thenReturn(selection);

        mvc.perform(put("/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(selection)))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }
}
