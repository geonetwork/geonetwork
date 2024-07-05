/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Process step.
 *
 * <pre>
 *     "processSteps": [
 *     {
 *       "descriptionObject": {
 *         "default": "desc",
 *         "langfre": "desc"
 *       },
 *       "date": "2024-06-06",
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
public class ProcessStep {
  @JsonProperty(IndexRecordFieldNames.ProcessStepField.DESCRIPTION)
  Map<String, String> description;

  @JsonProperty(IndexRecordFieldNames.ProcessStepField.DATE)
  String date;

  @JsonProperty(IndexRecordFieldNames.ProcessStepField.SOURCE)
  List<ProcessStepSource> source;

  @JsonProperty(IndexRecordFieldNames.ProcessStepField.PROCESSOR)
  List<Contact> processor;
}
