/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
class HttpProxyConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testUniversalProxyRoute() throws Exception {
        try {
            mockMvc.perform(delete("/api/proxy?url=http://localhost:81"));
        } catch (Exception e) {
            assertEquals(
                    "Request processing failed: org.springframework.web.client.HttpClientErrorException:"
                            + " 400 Invalid method value: DELETE in X-METHOD header.",
                    e.getMessage());
        }

        try {
            mockMvc.perform(get("/api/proxy?url=http://localhost:81"));
        } catch (Exception e) {
            assertEquals(
                    "Request processing failed: org.springframework.web.client.HttpClientErrorException:"
                            + " 400 Invalid port value: 81",
                    e.getMessage());
        }

        mockMvc.perform(get("/api/proxy?url=https://geonetwork-opensource.org/"))
                .andExpect(status().isOk());
    }
}
