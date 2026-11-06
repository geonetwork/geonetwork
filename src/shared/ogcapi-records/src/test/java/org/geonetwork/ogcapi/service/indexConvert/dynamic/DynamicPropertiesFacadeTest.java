/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.indexConvert.dynamic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import org.geonetwork.ogcapi.service.configuration.OgcApiConfigChangedEvent;
import org.geonetwork.ogcapi.service.configuration.OgcApiPropertyMappingService;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldMapperConfig;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldsMapperConfig;
import org.geonetwork.ogcapi.service.configuration.OverrideType;
import org.geonetwork.ogcapi.service.configuration.SimpleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

/** The facade owns the DB property mapping and pushes it into the typing system (at startup and on change). */
public class DynamicPropertiesFacadeTest {

    OgcApiPropertyMappingService configService = Mockito.mock(OgcApiPropertyMappingService.class);
    ElasticTypingSystem typingSystem = new ElasticTypingSystem(new OgcElasticFieldsMapperConfig(), "gn-records", null);
    DynamicPropertiesFacade facade = new DynamicPropertiesFacade();

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(facade, "configService", configService);
        ReflectionTestUtils.setField(facade, "elasticTypingSystem", typingSystem);
    }

    private static OgcElasticFieldsMapperConfig config(String ogcProperty, String elasticProperty) {
        var field = new OgcElasticFieldMapperConfig();
        field.setOgcProperty(ogcProperty);
        field.setElasticProperty(elasticProperty);
        field.setTypeOverride(OverrideType.LIST_STRING);
        var config = new OgcElasticFieldsMapperConfig();
        config.setFields(new ArrayList<>(List.of(field)));
        return config;
    }

    /** Properties only present in the DB property mapping (not in the YAML) must be typed at startup. */
    @Test
    public void typesFromDbConfigAtStartup() {
        Mockito.when(configService.getConfig())
                .thenReturn(config("eeaBusinessDomain", "th_eea-business-domains_tree.default"));

        facade.loadConfig();

        var typeInfo = typingSystem.getTypeInfoByOgcProperty("eeaBusinessDomain");
        assertNotNull(typeInfo);
        assertEquals(SimpleType.STRING, typeInfo.getType());
        assertNotNull(typingSystem.getTypeInfoByElasticProperty("th_eea-business-domains_tree.default"));
        assertNotNull(facade.findFieldByOgcProperty("eeaBusinessDomain"));
    }

    /** A config update at runtime is visible to the CQL field lookup and the typing system. */
    @Test
    public void configChangeIsPickedUp() {
        Mockito.when(configService.getConfig())
                .thenReturn(config("eeaBusinessDomain", "th_eea-business-domains_tree.default"))
                .thenReturn(config("newProp", "newProp.default"));

        facade.loadConfig();
        facade.onConfigChanged(new OgcApiConfigChangedEvent(this, 2L));

        assertNull(facade.findFieldByOgcProperty("eeaBusinessDomain"));
        assertEquals("newProp.default", facade.findFieldByOgcProperty("newProp").getElasticProperty());
        assertNotNull(typingSystem.getTypeInfoByOgcProperty("newProp"));
        assertNull(typingSystem.getTypeInfoByOgcProperty("eeaBusinessDomain"));
    }
}
