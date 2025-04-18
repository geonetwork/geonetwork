/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.proxy;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.geonetwork.testing.GeoNetwork4BasedIntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Disabled
class GeoNetworkProxyTest extends GeoNetwork4BasedIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_can_access_testcontainer_directly() throws Exception {
        mockMvc.perform(get(getGeoNetwork4Url() + "/srv/api/me").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_can_access_geonetwork4_thourgh_gateway() throws Exception {
        mockMvc.perform(get("/geonetwork/srv/api/me").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/geonetwork/").accept(MediaType.TEXT_HTML)).andExpect(status().is3xxRedirection());
    }
}
