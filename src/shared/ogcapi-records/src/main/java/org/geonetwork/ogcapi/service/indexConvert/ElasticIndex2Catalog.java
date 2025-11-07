/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.indexConvert;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsExtentDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLanguageDto;

/** This converts an ElasticIndex (Elastic JSON Index Record) to the OGCAPI-Records Catalog. */
public class ElasticIndex2Catalog {

    /**
     * given an elastic index multilingual property like;
     *
     * <p>"pointOfContactOrgForResourceObject": { "default": "GeoCat Canada Ltd", "langeng": "GeoCat Canada Ltd" }
     *
     * <p>Return the correct language string.
     *
     * @param multiLingual elastic index multilingual property
     * @param lang3iso language (From request) - 3 letter iso lang value (i.e. 'eng')
     * @return the correct language string, or default.
     */
    public static String getLangString(Map<String, String> multiLingual, String lang3iso) {
        if (multiLingual == null || multiLingual.isEmpty()) {
            return null;
        }

        // no lang
        if (isBlank(lang3iso)) {
            var val = multiLingual.get("default");
            if (val != null) {
                return val;
            }
            return multiLingual.get(multiLingual.keySet().iterator().next()); // first one is the best guess...
        }

        var val = multiLingual.get("lang" + lang3iso);
        if (val != null) {
            return val;
        }
        val = multiLingual.get("default");
        if (val != null) {
            return val;
        }
        return multiLingual.get(multiLingual.keySet().iterator().next()); // first one is the best guess...
    }

    /**
     * convert an elastic json index record to an OGCAPI-records OgcApiCatalog object.
     *
     * @param indexRecord from elastic
     * @param lang3iso language (From request) - 3 letter iso lang value (i.e. 'eng')
     * @return converted IndexRecord To OgcApiCatalog object
     */
    public static OgcApiRecordsCatalogDto convertIndexRecordToOgcApiCatalog(IndexRecord indexRecord, String lang3iso) {
        var result = new OgcApiRecordsCatalogDto();

        injectLinkedServiceRecordInfo(result, indexRecord, lang3iso);
        return result;
    }

    /**
     * inject the "extra" info from the LinkedServiceRecord into the CollectionInfo.
     *
     * @param indexRecord Parsed JSON of the linked Service record (GN's DB "source" "serviceRecord")
     * @param catalog collection metadata we've gathered so far (usually not much)
     * @param lang3iso language (From request) - 3 letter iso lang value (i.e. 'eng')
     */
    public static void injectLinkedServiceRecordInfo(
            OgcApiRecordsCatalogDto catalog, IndexRecord indexRecord, String lang3iso) {
        if (indexRecord == null) {
            return; // nothing to do
        }

        if (catalog.getId() == null) {
            catalog.setId(catalog.getId());
        }

        var title = getLangString(indexRecord.getResourceTitle(), lang3iso);
        if (title != null) {
            catalog.setTitle(title);
        }

        var description = getLangString(indexRecord.getResourceAbstract(), lang3iso);
        if (description != null) {
            catalog.setDescription(description);
        }

        var contacts = indexRecord.getContactByRole();
        if (contacts != null && !contacts.isEmpty()) {
            catalog.setContacts(ContactConvert.convert(contacts, lang3iso));
        }

        var spatialExtent = ExtentConvert.convertSpatialExtent(indexRecord);
        var temporalExtent = ExtentConvert.convertTemporalExtent(indexRecord);
        if (spatialExtent != null || temporalExtent != null) {
            var extent = new OgcApiRecordsExtentDto();
            extent.temporal(temporalExtent);
            extent.spatial(spatialExtent);
            catalog.setExtent(extent);
        }

        var crss = indexRecord.getCoordinateSystem();
        if (crss != null && !crss.isEmpty()) {
            catalog.setCrs(crss);
        }

        var createDate = indexRecord.getCreateDate();
        if (!isBlank(createDate)) {
            var date = OffsetDateTime.parse(createDate);
            catalog.setCreated(date);
        }

        var changeDate = indexRecord.getChangeDate();
        if (!isBlank(changeDate)) {
            var date = OffsetDateTime.parse(changeDate);
            catalog.setUpdated(date);
        }

        var tags = indexRecord.getTag();
        if (tags != null && !tags.isEmpty()) {
            tags.stream().forEach(x -> catalog.addKeywordsItem(getLangString(x, lang3iso)));
        }

        var mainLang = indexRecord.getMainLanguage();
        if (!isBlank(mainLang)) {
            catalog.setLanguage(new OgcApiRecordsLanguageDto(mainLang));
        }

        var langs = indexRecord.getOtherLanguage();
        if (langs != null && !langs.isEmpty()) {
            langs.stream().forEach(x -> catalog.addLanguagesItem(new OgcApiRecordsLanguageDto(x)));
        }

        var resourceLangs = indexRecord.getResourceLanguage();
        if (resourceLangs != null && !resourceLangs.isEmpty()) {
            resourceLangs.stream().forEach(x -> catalog.addResourceLanguagesItem(new OgcApiRecordsLanguageDto(x)));
        }

        var allKeywords = indexRecord.getAllKeywords();
        if (allKeywords != null && !allKeywords.isEmpty()) {
            try {
                var themes = ThemeConvert.convert(allKeywords, lang3iso);
                catalog.setThemes(themes);
            } catch (URISyntaxException e) {
                // do nothing
            }
        }

        var licenseList = indexRecord.getLicenses();
        if (licenseList != null && !licenseList.isEmpty()) {
            catalog.setLicense(licenseList.get(0).get("default"));
        }

        var rights = indexRecord.getOtherProperties().get("MD_LegalConstraintsUseLimitationObject");
        if (rights != null) {
            if (rights instanceof List) {
                @SuppressWarnings("unchecked")
                var rightsList = (List<Map>) rights;
                if (!rightsList.isEmpty()
                        && rightsList.get(0) != null
                        && rightsList.get(0).containsKey("default")) {
                    catalog.setRights(rightsList.get(0).get("default").toString());
                }
            }
        }

        catalog.setItemType(OgcApiRecordsCatalogDto.ItemTypeEnum.RECORD);
        catalog.setType(OgcApiRecordsCatalogDto.TypeEnum.CATALOG);

        catalog.addConformsToItem("http://www.opengis.net/spec/ogcapi-records-1/1.0/req/record-core");
    }
}
