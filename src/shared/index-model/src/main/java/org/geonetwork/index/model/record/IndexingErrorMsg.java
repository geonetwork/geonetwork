/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.index.model.record;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * "indexingErrorMsg": [ { "string": "indexingErrorMsg-keywordNotFoundInThesaurus", "type": "warning", "values": {
 * "keyword": "Open Data", "thesaurus": "geonetwork.thesaurus.external.theme.infraSIG" } } ],
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexingErrorMsg {
    String string;
    String type;

    @JsonAnySetter
    Map<String, String> values;
}
