/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert;

import static org.geonetwork.ogcapi.service.indexConvert.ElasticIndex2Catalog.getLangString;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.records.generated.model.*;
import org.geonetwork.ogcapi.service.configuration.OgcApiRecordsOutputConfig;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.ExtraElasticPropertiesService;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Converts an elastic JSON Index Record into an OGCAPI compliant GeoJSON record. */
@Component
public class OgcApiGeoJsonConverter {

    /** access to the DB metadata table */
    @Autowired
    MetadataRepository metadataRepository;

    @Autowired
    OgcApiRecordsOutputConfig ogcApiRecordsOutputConfig;

    @Autowired
    ExtraElasticPropertiesService extraElasticPropertiesService;

    /**
     * builder to construct a polygon from a bounding box.
     *
     * @param bbox bounding box - xmin, ymin, xmax, ymax
     * @return GeoJsonPolygon with a rectangular polygon representing the bounding box.
     */
    public static OgcApiRecordsPolygonDto fromBBox(List<List<BigDecimal>> bbox) {
        var result = new OgcApiRecordsPolygonDto();

        List<List<List<BigDecimal>>> coords = new ArrayList<List<List<BigDecimal>>>();
        var coords_1 = new ArrayList<List<BigDecimal>>();
        coords.add(coords_1);
        var coords_2 = new ArrayList<BigDecimal>();
        // xmin, ymin
        coords_2.add(bbox.get(0).get(0));
        coords_2.add(bbox.get(0).get(1));
        coords_1.add(coords_2);

        // xmax, ymin
        coords_2 = new ArrayList<BigDecimal>();
        coords_2.add(bbox.get(0).get(2));
        coords_2.add(bbox.get(0).get(1));
        coords_1.add(coords_2);

        // xmax, ymax
        coords_2 = new ArrayList<BigDecimal>();
        coords_2.add(bbox.get(0).get(2));
        coords_2.add(bbox.get(0).get(3));
        coords_1.add(coords_2);

        // xmin, ymax
        coords_2 = new ArrayList<BigDecimal>();
        coords_2.add(bbox.get(0).get(0));
        coords_2.add(bbox.get(0).get(3));
        coords_1.add(coords_2);

        // xmin, ymin
        coords_2 = new ArrayList<BigDecimal>();
        coords_2.add(bbox.get(0).get(0));
        coords_2.add(bbox.get(0).get(1));
        coords_1.add(coords_2);

        result.setCoordinates(coords);
        return result;
    }

    /**
     * converts an IndexRecord (elastic Index Json Record) to a OgcApiGeoJsonRecord.
     *
     * @param elasticIndexJsonRecord IndexRecord from Elastic
     * @param iso3lang language (From request) - 3 letter iso lang value (i.e. 'eng')
     * @return converted to OgcApiGeoJsonRecord.
     */
    public OgcApiRecordsRecordGeoJSONDto convert(IndexRecord elasticIndexJsonRecord, String iso3lang) {

        var result = convertElasticIndexToGeoJson(elasticIndexJsonRecord, iso3lang);

        result.setConformsTo(new ArrayList<>());
        result.getConformsTo().add("http://www.opengis.net/spec/ogcapi-records-1/1.0/req/record-core");

        Metadata metadataRecord = metadataRepository
                .findOneByUuid(elasticIndexJsonRecord.getMetadataIdentifier())
                .get();

        if (ogcApiRecordsOutputConfig.includeRawXML) {
            result.setMetadataRecordText(metadataRecord.getData());
        }
        if (ogcApiRecordsOutputConfig.includeElasticJson) {
            result.setGeoNetworkElasticIndexRecord(elasticIndexJsonRecord);
        }

        result.setType("Feature");
        result.getProperties().setType("unknown");
        if (elasticIndexJsonRecord.getResourceType() != null
                && !elasticIndexJsonRecord.getResourceType().isEmpty()) {
            result.getProperties()
                    .setType(elasticIndexJsonRecord.getResourceType().getFirst());
        }

        extraElasticPropertiesService.inject(elasticIndexJsonRecord, iso3lang, result);

        return result;
    }

    /**
     * Converts an elastic index record to a geojson feature.
     *
     * @param elasticIndexJsonRecord elastic index record
     * @param iso3lang language (From request) - 3 letter iso lang value (i.e. 'eng')
     * @return geojson feature.
     */
    private OgcApiRecordsRecordGeoJSONDto convertElasticIndexToGeoJson(
            IndexRecord elasticIndexJsonRecord, String iso3lang) {

        var result = new OgcApiRecordsRecordGeoJSONDto();
        var properties = new OgcApiRecordsRecordGeoJSONPropertiesDto();

        var collectionInfo = ElasticIndex2Catalog.convertIndexRecordToOgcApiCatalog(elasticIndexJsonRecord, iso3lang);

        result.setId(elasticIndexJsonRecord.getUuid());
        properties
                .title(collectionInfo.getTitle())
                .description(collectionInfo.getDescription())
                .language(collectionInfo.getLanguage())
                .languages(collectionInfo.getLanguages())
                .created(collectionInfo.getCreated())
                .updated(collectionInfo.getUpdated())
                .type(collectionInfo.getType().toString())
                .keywords(collectionInfo.getKeywords())
                .themes(collectionInfo.getThemes())
                .resourceLanguages(collectionInfo.getResourceLanguages())
                .contacts(collectionInfo.getContacts())
                .license(collectionInfo.getLicense())
                .rights(collectionInfo.getRights());

        if (collectionInfo.getExtent() != null
                && collectionInfo.getExtent().getSpatial() != null
                && collectionInfo.getExtent().getSpatial().getBbox() != null
                && !collectionInfo.getExtent().getSpatial().getBbox().isEmpty()) {

            var bbox = fromBBox(collectionInfo.getExtent().getSpatial().getBbox());
            result.setGeometry(JsonNullable.of(bbox));
        } else {
            result.setGeometry(null); // explicitly set this or there will be problems during jackson serialization
        }

        if (collectionInfo.getExtent() != null
                && collectionInfo.getExtent().getTemporal() != null
                && collectionInfo.getExtent().getTemporal().getInterval() != null
                && !collectionInfo.getExtent().getTemporal().getInterval().isEmpty()) {
            var time = new OgcApiRecordsTimeDto();
            List<OffsetDateTime> temporalInterval =
                    collectionInfo.getExtent().getTemporal().getInterval().getFirst();
            if (!temporalInterval.isEmpty()) {
                var interval = temporalInterval.stream()
                        .filter(Objects::nonNull)
                        .map(OffsetDateTime::toString)
                        .toList();
                time.setInterval(interval);
                result.setTime(JsonNullable.of(time));
            }
        }

        if (elasticIndexJsonRecord.getOverview() != null
                && !elasticIndexJsonRecord.getOverview().isEmpty()) {
            var thumbnails = elasticIndexJsonRecord.getOverview().stream()
                    .map(overview -> new OgcApiRecordsThumbnailsDto()
                            .name(getLangString(overview.getName(), iso3lang))
                            .url(overview.getUrl()))
                    .toList();
            properties.setThumbnails(thumbnails);
        }

        result.setProperties(properties);
        return result;
    }
}
