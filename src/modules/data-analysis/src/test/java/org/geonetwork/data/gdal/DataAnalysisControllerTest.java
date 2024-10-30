/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// @SpringBootTest(classes = GeonetworkTestingApplication.class)
public class DataAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // @Test
    void getDataAnalysisPreview() throws Exception {

        mockMvc.perform(
                        get("api/data/analysis/preview?uuid=cf902271-e4d8-4336-871f-c3e2fe9bfa7b&datasource=WFS%3Ahttps%3A%2F%2Fgeoservices.brgm.fr%2Frisques&layer=ms%3ANEOPAL_FAILLE")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // TODO: From the data directory
        // http://localhost:8080/geonetwork/srv/api/records/da165110-88fd-11da-a88f-000d939bc5d8/attachments/basins.zip
    }
}
