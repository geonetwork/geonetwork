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
import org.springframework.transaction.support.TransactionOperations;

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
        service = new OgcApiPropertyMappingService(
                repository, yamlConfig, eventPublisher, TransactionOperations.withoutTransaction());
    }

    private static OgcElasticFieldMapperConfig yamlField(String ogcProperty, String elasticProperty) {
        OgcElasticFieldMapperConfig field = new OgcElasticFieldMapperConfig();
        field.setOgcProperty(ogcProperty);
        field.setElasticProperty(elasticProperty);
        return field;
    }

    private static OgcApiPropertyMapping dbConfigWith(OgcApiFieldMapping... fields) {
        OgcApiPropertyMapping entity = OgcApiPropertyMapping.builder()
                .id(OgcApiPropertyMappingService.CONFIG_ID)
                .updateSequence(7L)
                .build();
        List<OgcApiFieldMapping> list = new ArrayList<>();
        for (OgcApiFieldMapping field : fields) {
            field.setConfig(entity);
            list.add(field);
        }
        entity.setFields(list);
        return entity;
    }

    @Test
    void seedFromYaml_insertsRowWhenMissing() {
        when(repository.findById(OgcApiPropertyMappingService.CONFIG_ID)).thenReturn(Optional.empty());
        when(repository.save(any(OgcApiPropertyMapping.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.seedFromYaml();

        var captor = ArgumentCaptor.forClass(OgcApiPropertyMapping.class);
        verify(repository).save(captor.capture());
        OgcApiPropertyMapping saved = captor.getValue();
        assertEquals(OgcApiPropertyMappingService.CONFIG_ID, saved.getId());
        assertEquals(1L, saved.getUpdateSequence());
    }

    @Test
    void seedFromYaml_doesNotSaveWhenAllYamlFieldsAreInDb() {
        yamlConfig.setFields(List.of(yamlField("title", "resourceTitleObject.default")));
        OgcApiPropertyMapping existing = dbConfigWith(OgcApiFieldMapping.builder()
                .sortOrder(0)
                .ogcProperty("title")
                .elasticProperty("resourceTitleObject.default")
                .build());
        when(repository.findById(OgcApiPropertyMappingService.CONFIG_ID)).thenReturn(Optional.of(existing));

        service.seedFromYaml();

        verify(repository, never()).save(any());
        assertEquals(7L, existing.getUpdateSequence());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void seedFromYaml_addsYamlFieldsMissingInDbAfterTheExistingOnes() {
        OgcFacetConfig facet = new OgcFacetConfig();
        facet.setFacetName("eeaBusinessDomain");
        facet.setFacetType(FacetType.TERM);
        facet.setBucketCount(100);
        OgcElasticFieldMapperConfig newField = yamlField("eeaBusinessDomain", "th_eea-business-domains_tree.default");
        newField.setFacetsConfig(List.of(facet));
        yamlConfig.setFields(List.of(yamlField("title", "resourceTitleObject.default"), newField));

        OgcApiPropertyMapping existing = dbConfigWith(
                OgcApiFieldMapping.builder()
                        .sortOrder(0)
                        .ogcProperty("title")
                        .elasticProperty("resourceTitleObject.default")
                        .build(),
                OgcApiFieldMapping.builder()
                        .sortOrder(113)
                        .ogcProperty("popularity")
                        .elasticProperty("popularity")
                        .build());
        when(repository.findById(OgcApiPropertyMappingService.CONFIG_ID)).thenReturn(Optional.of(existing));
        when(repository.save(any(OgcApiPropertyMapping.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.seedFromYaml();

        verify(repository).save(existing);
        assertEquals(8L, existing.getUpdateSequence());
        assertEquals(3, existing.getFields().size());
        OgcApiFieldMapping added = existing.getFields().get(2);
        assertEquals("eeaBusinessDomain", added.getOgcProperty());
        assertEquals(114, added.getSortOrder());
        assertSame(existing, added.getConfig());
        assertEquals(1, added.getFacets().size());
        assertEquals("eeaBusinessDomain", added.getFacets().get(0).getFacetName());
        assertEquals(100, added.getFacets().get(0).getBucketCount());
        // the field that is not in the YAML stays
        assertEquals("popularity", existing.getFields().get(1).getOgcProperty());
    }

    @Test
    void seedFromYaml_doesNotAddMissingFieldsWhenDisabled() {
        yamlConfig.setFields(List.of(yamlField("eeaBusinessDomain", "th_eea-business-domains_tree.default")));
        OgcApiPropertyMapping existing = dbConfigWith();
        when(repository.findById(OgcApiPropertyMappingService.CONFIG_ID)).thenReturn(Optional.of(existing));
        service.addMissingOnStartup = false;

        service.seedFromYaml();

        verify(repository, never()).save(any());
        assertTrue(existing.getFields().isEmpty());
    }

    @Test
    void seedFromYaml_keepsFieldsChangedThroughTheApi() {
        yamlConfig.setFields(List.of(yamlField("title", "resourceTitleObject.default")));
        OgcApiFieldMapping edited = OgcApiFieldMapping.builder()
                .sortOrder(0)
                .ogcProperty("title")
                .elasticProperty("resourceTitleObject.langeng")
                .isSortable(true)
                .build();
        OgcApiPropertyMapping existing = dbConfigWith(edited);
        when(repository.findById(OgcApiPropertyMappingService.CONFIG_ID)).thenReturn(Optional.of(existing));

        service.seedFromYaml();

        verify(repository, never()).save(any());
        assertEquals("resourceTitleObject.langeng", edited.getElasticProperty());
        assertTrue(edited.getIsSortable());
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
