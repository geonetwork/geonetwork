/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.cql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.geonetwork.ogcapi.controllerexceptions.InvalidParameterException;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldMapperConfig;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.DynamicPropertiesFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

/** The CQL `filter` field mapping must follow the current (DB) property mapping, not only the YAML. */
public class OgcElasticFieldMapperTest {

    CqlToElasticSearch cqlToElasticSearch = new CqlToElasticSearch();

    @BeforeEach
    public void setup() {
        var field = new OgcElasticFieldMapperConfig();
        field.setOgcProperty("eeaBusinessDomain");
        field.setElasticProperty("th_eea-business-domains_tree.default");

        var facade = Mockito.mock(DynamicPropertiesFacade.class);
        Mockito.when(facade.findFieldByOgcProperty("eeaBusinessDomain")).thenReturn(field);

        var mapper = new OgcElasticFieldMapper();
        ReflectionTestUtils.setField(mapper, "dynamicPropertiesFacade", facade);
        ReflectionTestUtils.setField(cqlToElasticSearch, "ogcElasticFieldMapper", mapper);
    }

    @Test
    public void filterOnPropertyFromCurrentConfig() throws Exception {
        var query = cqlToElasticSearch.create("eeaBusinessDomain = 'Water'");
        assertEquals("th_eea-business-domains_tree.default", query.term().field());
    }

    @Test
    public void filterOnUnknownPropertyIsInvalidParameter() {
        assertThrows(InvalidParameterException.class, () -> cqlToElasticSearch.create("themes = 'Water'"));
    }
}
