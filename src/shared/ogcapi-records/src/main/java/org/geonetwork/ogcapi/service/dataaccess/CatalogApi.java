/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.dataaccess;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.text.MessageFormat;
import org.geonetwork.domain.Source;
import org.geonetwork.domain.repository.SettingRepository;
import org.geonetwork.domain.repository.SourceRepository;
import org.geonetwork.ogcapi.controllerexceptions.NotFoundException;
import org.geonetwork.ogcapi.service.dataaccess.simpleobjects.CatalogInfo;
import org.geonetwork.ogcapi.service.validation.SimpleValidators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** help with catalog objects - portal/subportals in GN. */
@Component
public class CatalogApi {

    private SourceRepository sourceRepository;

    private SimpleElastic simpleElastic;

    private SettingRepository settingRepository;

    @Autowired
    public CatalogApi(
            SourceRepository sourceRepository, SimpleElastic simpleElastic, SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
        this.simpleElastic = simpleElastic;
        this.sourceRepository = sourceRepository;
    }

    public CatalogInfo getPortalInfo(String catalogId) throws Exception {
        SimpleValidators.validateCatalogId(catalogId);

        var sourceOptional = sourceRepository.findById(catalogId);
        if (!sourceOptional.isPresent()) {
            throw new NotFoundException(
                    MessageFormat.format("Collection (GeoNetwork Source) with ID=''{0}'' was not found.", catalogId));
        }
        var source = sourceOptional.get();

        var result = new CatalogInfo();
        result.setSource(source);

        var serviceRecordUUID = getServiceRecord(source);

        if (isBlank(serviceRecordUUID)) {
            return result;
        }

        var indexRecord = simpleElastic.getOne(serviceRecordUUID);
        result.setLinkedIndexRecord(indexRecord);

        return result;
    }

    private String getServiceRecord(Source source) {
        var serviceRecordUUID = source.getServicerecord();

        if (source.getType().equals("portal")) {
            // this is the main portal - we have a 2nd way to get the link (admin->settings->CSW)
            var settingOptional = settingRepository.findById("system/csw/capabilityRecordUuid");
            if (settingOptional.isPresent()) {
                serviceRecordUUID = settingOptional.get().getValue();
            }
        }
        if (isBlank(serviceRecordUUID) || serviceRecordUUID.trim().equals("-1")) {
            return null;
        }
        return serviceRecordUUID;
    }
}
