/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.configuration;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.OgcApiFacetConfig;
import org.geonetwork.domain.OgcApiFieldMapping;
import org.geonetwork.domain.OgcApiFilterFacet;
import org.geonetwork.domain.OgcApiPropertyMapping;
import org.geonetwork.domain.repository.OgcApiPropertyMappingRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manages the OGC API property-mapping configuration in the database.
 *
 * <p>On first startup, seeds the DB from the YAML-bound {@link OgcElasticFieldsMapperConfig}. Subsequent reads come
 * from the DB. Updates increment the {@code update_sequence} counter and publish an {@link OgcApiConfigChangedEvent} so
 * in-memory caches (e.g. {@code DynamicPropertiesFacade}) can reload.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OgcApiPropertyMappingService {

    static final String CONFIG_ID = "property-mapping";

    private final OgcApiPropertyMappingRepository repository;
    private final OgcElasticFieldsMapperConfig yamlConfig;
    private final ApplicationEventPublisher eventPublisher;

    @PostConstruct
    void seedFromYamlIfAbsent() {
        if (repository.existsById(CONFIG_ID)) {
            return;
        }
        OgcApiPropertyMapping entity = buildEntityFromYaml(yamlConfig);
        repository.save(entity);
        log.info("Seeded OGC API property-mapping config from YAML into DB.");
    }

    /** Returns the current configuration, loaded from the DB. */
    @Transactional(readOnly = true)
    public OgcElasticFieldsMapperConfig getConfig() {
        OgcApiPropertyMapping entity = repository
                .findById(CONFIG_ID)
                .orElseThrow(
                        () -> new IllegalStateException("OGC API config row missing from DB (id=" + CONFIG_ID + ")"));
        return toConfig(entity);
    }

    /**
     * Persists a new configuration, increments the update sequence, and fires {@link OgcApiConfigChangedEvent} so
     * in-JVM listeners can reload their caches.
     */
    @Transactional
    public OgcElasticFieldsMapperConfig updateConfig(OgcElasticFieldsMapperConfig newConfig) {
        OgcApiPropertyMapping entity = repository.findById(CONFIG_ID).orElseGet(() -> OgcApiPropertyMapping.builder()
                .id(CONFIG_ID)
                .updateSequence(0L)
                .build());

        entity.getFields().clear();
        int fieldOrder = 0;
        for (OgcElasticFieldMapperConfig fieldConfig : newConfig.getFields()) {
            OgcApiFieldMapping fieldEntity = toFieldEntity(fieldConfig, entity, fieldOrder++);
            entity.getFields().add(fieldEntity);
        }
        entity.setDefaultBucketCount(newConfig.getDefaultBucketCount());
        entity.setUpdateSequence(entity.getUpdateSequence() + 1);
        repository.save(entity);

        eventPublisher.publishEvent(new OgcApiConfigChangedEvent(this, entity.getUpdateSequence()));
        log.info("OGC API property-mapping config updated (updateSequence={}).", entity.getUpdateSequence());
        return newConfig;
    }

    // --- Entity → POJO mapping ---

    private OgcElasticFieldsMapperConfig toConfig(OgcApiPropertyMapping entity) {
        OgcElasticFieldsMapperConfig config = new OgcElasticFieldsMapperConfig();
        config.setDefaultBucketCount(entity.getDefaultBucketCount());
        List<OgcElasticFieldMapperConfig> fields = new ArrayList<>();
        for (OgcApiFieldMapping fieldEntity : entity.getFields()) {
            fields.add(toFieldConfig(fieldEntity));
        }
        config.setFields(fields);
        return config;
    }

    private OgcElasticFieldMapperConfig toFieldConfig(OgcApiFieldMapping fieldEntity) {
        OgcElasticFieldMapperConfig field = new OgcElasticFieldMapperConfig();
        field.setOgcProperty(fieldEntity.getOgcProperty());
        field.setElasticProperty(fieldEntity.getElasticProperty());
        field.setIndexRecordProperty(fieldEntity.getIndexRecordProperty());
        field.setTypeOverride(
                fieldEntity.getTypeOverride() != null ? OverrideType.valueOf(fieldEntity.getTypeOverride()) : null);
        field.setSortFieldSuffix(fieldEntity.getSortFieldSuffix());
        field.setIsSortable(fieldEntity.getIsSortable());
        field.setIsQueryable(fieldEntity.getIsQueryable());
        field.setTitle(fieldEntity.getTitle());
        field.setDescription(fieldEntity.getDescription());
        field.setAddPropertyToOutput(fieldEntity.getAddPropertyToOutput());
        List<OgcFacetConfig> facets = new ArrayList<>();
        for (OgcApiFacetConfig facetEntity : fieldEntity.getFacets()) {
            facets.add(toFacetConfig(facetEntity));
        }
        field.setFacetsConfig(facets);
        return field;
    }

    private OgcFacetConfig toFacetConfig(OgcApiFacetConfig facetEntity) {
        OgcFacetConfig facet = new OgcFacetConfig();
        facet.setFacetName(facetEntity.getFacetName());
        facet.setFacetType(facetEntity.getFacetType() != null ? FacetType.valueOf(facetEntity.getFacetType()) : null);
        facet.setBucketSorting(
                facetEntity.getBucketSorting() != null ? BucketSorting.valueOf(facetEntity.getBucketSorting()) : null);
        facet.setBucketSortingDirection(
                facetEntity.getBucketSortingDirection() != null
                        ? BucketSortingDirection.valueOf(facetEntity.getBucketSortingDirection())
                        : null);
        facet.setBucketCount(facetEntity.getBucketCount());
        facet.setMinimumDocumentCount(facetEntity.getMinimumDocumentCount());
        facet.setNumberBucketInterval(facetEntity.getNumberBucketInterval());
        facet.setCalendarIntervalUnit(
                facetEntity.getCalendarIntervalUnit() != null
                        ? CalendarIntervalUnit.valueOf(facetEntity.getCalendarIntervalUnit())
                        : null);
        List<FilterFacetInfo> filters = new ArrayList<>();
        for (OgcApiFilterFacet filterEntity : facetEntity.getFilters()) {
            filters.add(toFilterInfo(filterEntity));
        }
        facet.setFilters(filters);
        return facet;
    }

    private FilterFacetInfo toFilterInfo(OgcApiFilterFacet filterEntity) {
        FilterFacetInfo filter = new FilterFacetInfo();
        filter.setFilterName(filterEntity.getFilterName());
        filter.setFilterEquationCql(filterEntity.getFilterEquationCql());
        return filter;
    }

    // --- POJO → Entity mapping ---

    private OgcApiPropertyMapping buildEntityFromYaml(OgcElasticFieldsMapperConfig yaml) {
        OgcApiPropertyMapping entity = OgcApiPropertyMapping.builder()
                .id(CONFIG_ID)
                .defaultBucketCount(yaml.getDefaultBucketCount())
                .updateSequence(1L)
                .build();
        int fieldOrder = 0;
        for (OgcElasticFieldMapperConfig fieldConfig : yaml.getFields()) {
            OgcApiFieldMapping fieldEntity = toFieldEntity(fieldConfig, entity, fieldOrder++);
            entity.getFields().add(fieldEntity);
        }
        return entity;
    }

    private OgcApiFieldMapping toFieldEntity(
            OgcElasticFieldMapperConfig fieldConfig, OgcApiPropertyMapping parent, int order) {
        OgcApiFieldMapping fieldEntity = OgcApiFieldMapping.builder()
                .config(parent)
                .sortOrder(order)
                .ogcProperty(fieldConfig.getOgcProperty())
                .elasticProperty(fieldConfig.getElasticProperty())
                .indexRecordProperty(fieldConfig.getIndexRecordProperty())
                .typeOverride(
                        fieldConfig.getTypeOverride() != null
                                ? fieldConfig.getTypeOverride().name()
                                : null)
                .sortFieldSuffix(fieldConfig.getSortFieldSuffix())
                .isSortable(fieldConfig.getIsSortable())
                .isQueryable(fieldConfig.getIsQueryable())
                .title(fieldConfig.getTitle())
                .description(fieldConfig.getDescription())
                .addPropertyToOutput(fieldConfig.getAddPropertyToOutput())
                .build();
        int facetOrder = 0;
        if (fieldConfig.getFacetsConfig() != null) {
            for (OgcFacetConfig facetConfig : fieldConfig.getFacetsConfig()) {
                OgcApiFacetConfig facetEntity = toFacetEntity(facetConfig, fieldEntity, facetOrder++);
                fieldEntity.getFacets().add(facetEntity);
            }
        }
        return fieldEntity;
    }

    private OgcApiFacetConfig toFacetEntity(OgcFacetConfig facetConfig, OgcApiFieldMapping parent, int order) {
        OgcApiFacetConfig facetEntity = OgcApiFacetConfig.builder()
                .fieldMapping(parent)
                .sortOrder(order)
                .facetName(facetConfig.getFacetName())
                .facetType(
                        facetConfig.getFacetType() != null
                                ? facetConfig.getFacetType().name()
                                : null)
                .bucketSorting(
                        facetConfig.getBucketSorting() != null
                                ? facetConfig.getBucketSorting().name()
                                : null)
                .bucketSortingDirection(
                        facetConfig.getBucketSortingDirection() != null
                                ? facetConfig.getBucketSortingDirection().name()
                                : null)
                .bucketCount(facetConfig.getBucketCount())
                .minimumDocumentCount(facetConfig.getMinimumDocumentCount())
                .numberBucketInterval(facetConfig.getNumberBucketInterval())
                .calendarIntervalUnit(
                        facetConfig.getCalendarIntervalUnit() != null
                                ? facetConfig.getCalendarIntervalUnit().name()
                                : null)
                .build();
        int filterOrder = 0;
        if (facetConfig.getFilters() != null) {
            for (FilterFacetInfo filterInfo : facetConfig.getFilters()) {
                OgcApiFilterFacet filterEntity = toFilterEntity(filterInfo, facetEntity, filterOrder++);
                facetEntity.getFilters().add(filterEntity);
            }
        }
        return facetEntity;
    }

    private OgcApiFilterFacet toFilterEntity(FilterFacetInfo filterInfo, OgcApiFacetConfig parent, int order) {
        return OgcApiFilterFacet.builder()
                .facetConfig(parent)
                .sortOrder(order)
                .filterName(filterInfo.getFilterName())
                .filterEquationCql(filterInfo.getFilterEquationCql())
                .build();
    }
}
