/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.dataaccess;

import org.geonetwork.domain.Source;
import org.geonetwork.domain.repository.SettingRepository;
import org.geonetwork.domain.repository.SourceRepository;
import org.geonetwork.ogcapi.controllerexceptions.NotFoundException;
import org.geonetwork.ogcapi.service.dataaccess.simpleobjects.CatalogInfo;
import org.geonetwork.ogcapi.service.validation.SimpleValidators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * help with catalog objects - portal/subportals in GN.
 */
@Component
public class CatalogApi {

  private final SourceRepository sourceRepository;

  private final SimpleElastic simpleElastic;

  private final SettingRepository settingRepository;

  @Autowired
  public CatalogApi(
    SourceRepository sourceRepository, SimpleElastic simpleElastic, SettingRepository settingRepository) {
    this.settingRepository = settingRepository;
    this.simpleElastic = simpleElastic;
    this.sourceRepository = sourceRepository;
  }

  /**
   * Gets the main portal UUID (`source` table with type=portal).
   *
   * @return main portal UUID
   */
  public String getMainPortalUUID() {
    var main = sourceRepository.findByType("portal");
    return main.map(Source::getUuid).orElse(null);
  }

  /**
   * Finds all the defined portals (in DB `source`) and converts them to CatalogInfo (DB `source` and connected
   * elastic record)
   *
   * @return all the defined portals
   */
  public List<CatalogInfo> getAllPortalInfos() {
    var portals = sourceRepository.findAll().stream()
      .map(x -> {
        try {
          return getPortalInfo(x.getUuid());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      })
      .toList();
    return portals;
  }

  /**
   * given a catalogId, returns the DB `source` info.
   *
   * @param catalogId name (column `id`) of the source
   * @return the DB `source` info
   * @throws Exception not found or invalid catalogId
   */
  public Source getSource(String catalogId) throws Exception {
    SimpleValidators.validateCatalogId(catalogId);

    var sourceOptional = sourceRepository.findById(catalogId);
    if (sourceOptional.isEmpty()) {
      throw new NotFoundException(
        MessageFormat.format("Collection (GeoNetwork Source) with ID=''{0}'' was not found.", catalogId));
    }
    return sourceOptional.get();
  }

  /**
   * Retrieves the ElasticSearch filter related to a collection.
   *
   * @param source from the DB `source table`
   * @return the filter for that portal (might be "" for the main portal)
   */
  public String retrieveCollectionFilter(Source source) {
    String collectionFilter = "";

    if (source.getType().equals("subportal")) {
      collectionFilter = source.getFilter();
    } else if (source.getType().equals("harvester")) {
      collectionFilter = String.format("+harvesterUuid:\"%s\"", source.getUuid());
    }

    return collectionFilter;
  }

  /**
   * given a catalogId, get the corresponding DB `source` column + attached elastic record.
   *
   * @param catalogId collection/catalog/source ID
   * @return DB `source` column + attached elastic record.
   * @throws Exception invalid catalogId or not found
   */
  public CatalogInfo getPortalInfo(String catalogId) throws Exception {
    SimpleValidators.validateCatalogId(catalogId);

    var sourceOptional = sourceRepository.findById(catalogId);
    if (sourceOptional.isEmpty()) {
      throw new NotFoundException(
        MessageFormat.format("Collection (GeoNetwork Source) with ID=''{0}'' was not found.", catalogId));
    }
    var source = sourceOptional.get();

    var serviceRecordUUID = getServiceRecord(source);

    if (isBlank(serviceRecordUUID)) {
      return new CatalogInfo(source, null);
    }

    var indexRecord = simpleElastic.getOne(serviceRecordUUID);
    var result = new CatalogInfo(source, indexRecord);

    return result;
  }

  /**
   * find the attached metadata record for the source. If its the main portal, use setting
   * `system/csw/capabilityRecordUuid`
   *
   * @param source from DB `source` table
   * @return actual metdata record (usually XML)
   */
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
