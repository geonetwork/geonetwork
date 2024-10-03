/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * "indexingErrorMsg": [ { "string": "indexingErrorMsg-keywordNotFoundInThesaurus", "type":
 * "warning", "values": { "keyword": "Open Data", "thesaurus":
 * "geonetwork.thesaurus.external.theme.infraSIG" } } ],
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexingErrorMsg {
  String string;
  String type;
  @JsonAnySetter Map<String, String> values;
}
