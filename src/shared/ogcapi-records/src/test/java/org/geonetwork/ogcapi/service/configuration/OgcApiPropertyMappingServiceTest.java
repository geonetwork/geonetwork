/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.configuration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.geonetwork.domain.OgcApiFacetConfig;
import org.geonetwork.domain.OgcApiFieldMapping;
import org.geonetwork.domain.OgcApiPropertyMapping;
import org.geonetwork.domain.repository.OgcApiPropertyMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

class OgcApiPropertyMappingServiceTest {

    private OgcApiPropertyMappingRepository repository;
    private OgcElasticFieldsMapperConfig yamlConfig;
    private ApplicationEventPublisher eventPublisher;
    private OgcApiPropertyMappingService service;

    @BeforeEach
    void setUp() {
        repository = mock(OgcApiPropertyMappingRepository.class);
        yamlConfig = new OgcElasticFieldsMapperConfig();
        eventPublisher = mock(ApplicationEventPublisher.class);
        service = new OgcApiPropertyMappingService(repository, yamlConfig, eventPublisher);
    }

    @Test
    void seedFromYamlIfAbsent_insertsRowWhenMissing() {
        when(repository.existsById(OgcApiPropertyMappingService.CONFIG_ID)).thenReturn(false);
        when(repository.save(any(OgcApiPropertyMapping.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.seedFromYamlIfAbsent();

        var captor = ArgumentCaptor.forClass(OgcApiPropertyMapping.class);
        verify(repository).save(captor.capture());
        OgcApiPropertyMapping saved = captor.getValue();
        assertEquals(OgcApiPropertyMappingService.CONFIG_ID, saved.getId());
        assertEquals(1L, saved.getUpdateSequence());
    }

    @Test
    void seedFromYamlIfAbsent_doesNotInsertWhenRowExists() {
        when(repository.existsById(OgcApiPropertyMappingService.CONFIG_ID)).thenReturn(true);

        service.seedFromYamlIfAbsent();

        verify(repository, never()).save(any());
    }

    @Test
    void getConfig_deserializesFromDb() {
        OgcApiFacetConfig facetEntity = OgcApiFacetConfig.builder()
                .id(1L)
                .sortOrder(0)
                .facetName("myFacet")
                .facetType(FacetType.TERM.name())
                .bucketSorting(BucketSorting.COUNT.name())
                .bucketSortingDirection(BucketSortingDirection.DESCENDING.name())
                .minimumDocumentCount(1)
                .build();

        List<OgcApiFacetConfig> facets = new ArrayList<>();
        facets.add(facetEntity);

        OgcApiFieldMapping fieldEntity = OgcApiFieldMapping.builder()
                .id(1L)
                .sortOrder(0)
                .ogcProperty("myProp")
                .isSortable(false)
                .isQueryable(false)
                .addPropertyToOutput(true)
                .build();
        // set facets via setter since builder does not allow mutation after build
        fieldEntity.setFacets(facets);
        facetEntity.setFieldMapping(fieldEntity);

        List<OgcApiFieldMapping> fields = new ArrayList<>();
        fields.add(fieldEntity);

        OgcApiPropertyMapping entity = OgcApiPropertyMapping.builder()
                .id(OgcApiPropertyMappingService.CONFIG_ID)
                .defaultBucketCount(10)
                .updateSequence(3L)
                .build();
        entity.setFields(fields);
        fieldEntity.setConfig(entity);

        when(repository.findById(OgcApiPropertyMappingService.CONFIG_ID)).thenReturn(Optional.of(entity));

        OgcElasticFieldsMapperConfig result = service.getConfig();

        assertEquals(1, result.getFields().size());
        assertEquals("myProp", result.getFields().get(0).getOgcProperty());
        assertEquals(1, result.getFields().get(0).getFacetsConfig().size());
        assertEquals(
                "myFacet", result.getFields().get(0).getFacetsConfig().get(0).getFacetName());
        assertEquals(
                FacetType.TERM,
                result.getFields().get(0).getFacetsConfig().get(0).getFacetType());
    }

    @Test
    void getConfig_throwsWhenRowMissing() {
        when(repository.findById(OgcApiPropertyMappingService.CONFIG_ID)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> service.getConfig());
    }

    @Test
    void updateConfig_incrementsSequenceAndFiresEvent() {
        List<OgcApiFieldMapping> mutableFields = new ArrayList<>();
        OgcApiPropertyMapping existing = OgcApiPropertyMapping.builder()
                .id(OgcApiPropertyMappingService.CONFIG_ID)
                .updateSequence(4L)
                .build();
        existing.setFields(mutableFields);

        when(repository.findById(OgcApiPropertyMappingService.CONFIG_ID)).thenReturn(Optional.of(existing));
        when(repository.save(any(OgcApiPropertyMapping.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var newConfig = new OgcElasticFieldsMapperConfig();
        service.updateConfig(newConfig);

        var captor = ArgumentCaptor.forClass(OgcApiPropertyMapping.class);
        verify(repository).save(captor.capture());
        assertEquals(5L, captor.getValue().getUpdateSequence());

        var eventCaptor = ArgumentCaptor.forClass(OgcApiConfigChangedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertEquals(5L, eventCaptor.getValue().getUpdateSequence());
    }

    @Test
    void updateConfig_createsNewRowWhenMissing() {
        when(repository.findById(OgcApiPropertyMappingService.CONFIG_ID)).thenReturn(Optional.empty());
        when(repository.save(any(OgcApiPropertyMapping.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var newConfig = new OgcElasticFieldsMapperConfig();
        service.updateConfig(newConfig);

        var captor = ArgumentCaptor.forClass(OgcApiPropertyMapping.class);
        verify(repository).save(captor.capture());
        assertEquals(1L, captor.getValue().getUpdateSequence());
    }
}
