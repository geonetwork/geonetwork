/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.schemas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entry model.
 *
 * <pre>{@code
 * <codelist name="cit:CI_TelephoneTypeCode">
 *   <entry>
 *     <code>voice</code>
 *     <label>Voice</label>
 *     <description>Telephone provides voice service</description>
 *   </entry>
 * }</pre>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entry {
    private String code;
    private String label;
    private String description;
}
