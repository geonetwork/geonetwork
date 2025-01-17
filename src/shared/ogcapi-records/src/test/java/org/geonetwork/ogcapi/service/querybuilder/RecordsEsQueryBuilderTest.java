/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.querybuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import org.geonetwork.ogcapi.service.configuration.OgcApiSearchConfiguration;
import org.geonetwork.ogcapi.service.search.RecordsEsQueryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

// from gn-mircoservices
class RecordsEsQueryBuilderTest {
    @MockBean
    OgcApiSearchConfiguration configuration;

    @BeforeEach
    public void setUp() {
        String[] fields = {"resourceTitleObject"};
        this.configuration = Mockito.mock(OgcApiSearchConfiguration.class);
        given(this.configuration.getSources()).willReturn(Arrays.asList(fields));
        given(this.configuration.getQueryFilter()).willReturn("+isTemplate:n");
    }

    @Test
    void buildQuerySingleRecord() {
        RecordsEsQueryBuilder queryBuilder = new RecordsEsQueryBuilder(configuration);
        String query = queryBuilder.buildQuerySingleRecord("abc", null, null);

        assertEquals(
                "{\"from\": 0, \"size\": 1, \"query\": {\"query_string\": {\"query\": \"+_id:\\\"abc\\\"  +isTemplate:n\"}}, \"_source\": {\"includes\": [\"*\"]}}",
                query);

        query = queryBuilder.buildQuerySingleRecord("abc", "+source:uio", null);

        assertEquals(
                "{\"from\": 0, \"size\": 1, \"query\": {\"query_string\": {\"query\": \"+_id:\\\"abc\\\" +source:uio +isTemplate:n\"}}, \"_source\": {\"includes\": [\"*\"]}}",
                query);

        String[] fields = {"resourceType", "cl_status"};
        query = queryBuilder.buildQuerySingleRecord("abc", "+source:uio", Arrays.asList(fields));

        assertEquals(
                "{\"from\": 0, \"size\": 1, \"query\": {\"query_string\": {\"query\": \"+_id:\\\"abc\\\" +source:uio +isTemplate:n\"}}, \"_source\": {\"includes\": [\"resourceType\", \"cl_status\"]}}",
                query);
    }
}
