/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * <pre>
 *     "processSteps": [...
 *       "source": [
 *         {
 *           "descriptionObject": {
 *             "default": "desc",
 *             "langfre": "desc"
 *           }
 *         }
 *       ]
 *     }
 *   ],
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessStepSource {
    @JsonProperty(IndexRecordFieldNames.ProcessStepField.DESCRIPTION)
    Map<String, String> description;
}
