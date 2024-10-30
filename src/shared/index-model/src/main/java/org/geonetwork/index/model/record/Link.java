/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 *
 *
 * <pre>
 *     {
 *       "protocol": "WWW:LINK",
 *       "function": "legend",
 *       "urlObject": {
 *         "default": "https://metawal.wallonie.be/geonetwork/srv/api/records/47b348f1-6e7a-4baa-963c-0232a43c0cff/attachments/WAL_OCS_IA__2020.qml",
 *         "langfre": "https://metawal.wallonie.be/geonetwork/srv/api/records/47b348f1-6e7a-4baa-963c-0232a43c0cff/attachments/WAL_OCS_IA__2020.qml"
 *       },
 *       "nameObject": {
 *         "default": "Légende pour QGIS",
 *         "langfre": "Légende pour QGIS"
 *       },
 *       "descriptionObject": {
 *         "default": "Fichier de style qml pour QGIS",
 *         "langfre": "Fichier de style qml pour QGIS"
 *       },
 *       "applicationProfile": ""
 *     },
 * </pre>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Link {

    @JsonInclude
    private String protocol;

    @JsonProperty(IndexRecordFieldNames.LinkField.URL)
    private Map<String, String> url = new HashMap<>();

    @JsonProperty(IndexRecordFieldNames.LinkField.NAME)
    private Map<String, String> name = new HashMap<>();

    @JsonProperty(IndexRecordFieldNames.LinkField.DESCRIPTION)
    private Map<String, String> description = new HashMap<>();

    private String function;
    private String applicationProfile;
    private String group;
    private String mimeType;
    private String nilReason;
    private String hash;
    private String idx;
}
