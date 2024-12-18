/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.ogcapi;

import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.service.dataaccess.CatalogApi;
import org.geonetwork.ogcapi.service.indexConvert.ElasticIndex2Catalog;
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

    public OgcApiCollectionsApi() {}

    public OgcApiRecordsCatalogDto describeCollection(String catalogId) throws Exception {
        var info = catalogApi.getPortalInfo(catalogId);

        if (info.getLinkedIndexRecord() == null) {
            var result = new OgcApiRecordsCatalogDto();
            result.setId(catalogId);
            result.setTitle(info.getSource().getName());
            return result;
        }

        return ElasticIndex2Catalog.injectLinkedServiceRecordInfo(info.getLinkedIndexRecord(), "eng");
    }
}
