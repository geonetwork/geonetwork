/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProcessStep {
    @JsonProperty(IndexRecordFieldNames.ProcessStepField.DESCRIPTION)
    Map<String, String> description;

    @JsonProperty(IndexRecordFieldNames.ProcessStepField.DATE)
    String date;

    @JsonProperty(IndexRecordFieldNames.ProcessStepField.SOURCE)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JacksonXmlElementWrapper(useWrapping = false)
    List<ProcessStepSource> source;

    @JsonProperty(IndexRecordFieldNames.ProcessStepField.PROCESSOR)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JacksonXmlElementWrapper(useWrapping = false)
    List<Contact> processor;
}
