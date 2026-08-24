/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.ogcapi;

import java.util.List;
import org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfile;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsCollectionsResponse;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetCollections200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLandingPageDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsSortOrderDto;
import org.geonetwork.ogcapi.service.dataaccess.CatalogApi;
import org.geonetwork.ogcapi.service.dataaccess.simpleobjects.CatalogInfo;
import org.geonetwork.ogcapi.service.indexConvert.ElasticIndex2Catalog;
import org.geonetwork.ogcapi.service.links.CollectionPageLinks;
import org.geonetwork.ogcapi.service.links.CollectionsPageLinks;
import org.geonetwork.ogcapi.service.links.LandingPageLinks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * High level implementation for the OgcApiCollectionsApi endpoints. The controller is responsible for the web-details,
 * this will handle the actual work.
 */
@Component
public class OgcApiCollectionsApi {

    @Autowired
    CatalogApi catalogApi;

    @Autowired
    LandingPageLinks landingPageLinks;

    @Autowired
    CollectionsPageLinks collectionsPageLinks;

    @Autowired
    CollectionPageLinks collectionPageLinks;

    public OgcApiCollectionsApi() {}

    /**
     * gets the landing page data (DB table source for the 'portal').
     *
     * @return OgcApiRecordsLandingPageDto
     * @throws Exception bad config
     */
    public OgcApiRecordsLandingPageDto getLandingPage(RequestMediaTypeAndProfile requestMediaTypeAndProfile)
            throws Exception {
        var uuid = catalogApi.getMainPortalUUID();
        if (uuid == null) {
            throw new Exception("no main portal found in DB table source");
        }
        var collectionInfo = describeCollection(uuid, requestMediaTypeAndProfile);
        var result = new OgcApiRecordsLandingPageDto();
        result.description(collectionInfo.getDescription()).title(collectionInfo.getTitle());

        result.setCatalogInfo(collectionInfo);

        landingPageLinks.addLinks(requestMediaTypeAndProfile, uuid, result);

        return result;
    }

    /**
     * given a collectionId, get the DB/elastic catalogInfo and convert it to the final ogcapi-records output.
     *
     * @param catalogId collectionId (From user)
     * @param requestMediaTypeAndProfile info about the request
     * @throws Exception catalogId invalid, cannot find catalog.
     */
    public OgcApiRecordsCatalogDto describeCollection(
            String catalogId, RequestMediaTypeAndProfile requestMediaTypeAndProfile) throws Exception {
        var info = catalogApi.getPortalInfo(catalogId);

        var result = catalogInfoToOgcApiRecordsCatalogDto(info);

        collectionPageLinks.addAllLinks(requestMediaTypeAndProfile, result);
        return result;
    }

    /**
     * converts the CatalogInfo (DB/elastic) into a OgcApiRecordsCatalogDto
     *
     * @param info CatalogInfo (DB `source` table + elastic json index record)
     * @return OgcApiRecordsCatalogDto
     */
    protected OgcApiRecordsCatalogDto catalogInfoToOgcApiRecordsCatalogDto(CatalogInfo info) {
        OgcApiRecordsCatalogDto result = new OgcApiRecordsCatalogDto();
        result.setId(info.getSource().getUuid());
        result.setTitle(info.getSource().getName());
        // Most of the times itemType is RECORD, but it can also be CATALOG
        // Since this is not implemented yet, we can assume RECORD for now.
        result.setItemType(OgcApiRecordsCatalogDto.ItemTypeEnum.RECORD);

        if (info.getLinkedIndexRecord() != null) {
            ElasticIndex2Catalog.injectLinkedServiceRecordInfo(result, info.getLinkedIndexRecord(), "eng");
            result.setGeoNetworkElasticIndexRecord(info.getLinkedIndexRecord());
        }

        // TODO: don't hard code this - perhaps put in application.yaml?
        result.defaultSortOrder(
                List.of(new OgcApiRecordsSortOrderDto("id", OgcApiRecordsSortOrderDto.DirectionEnum.ASC)));

        return result;
    }

    /**
     * get info about ALL collections.
     *
     * @return OgcApiRecordsGetCollections200ResponseDto
     */
    public OgcApiRecordsGetCollections200ResponseDto getCollections(
            OgcApiRecordsCollectionsResponse ogcApiRecordsCollectionsResponse) throws Exception {
        var collectionInfos = catalogApi.getAllPortalInfos();
        var collections = collectionInfos.stream()
                .map(x -> catalogInfoToOgcApiRecordsCatalogDto(x))
                .toList();

        var result = new OgcApiRecordsGetCollections200ResponseDto();
        result.setCollections(collections);

        RequestMediaTypeAndProfile requestMediaTypeAndProfile =
                ogcApiRecordsCollectionsResponse.getRequestMediaTypeAndProfile();
        collectionsPageLinks.addAllLinks(requestMediaTypeAndProfile, result);

        collections.stream().forEach(collection -> {
            try {
                collectionPageLinks.addAllLinks(requestMediaTypeAndProfile, collection);
            } catch (Exception e) {
                // do nothing
            }
        });
        return result;
    }
}
