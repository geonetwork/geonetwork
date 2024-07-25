/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.schemas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Codelist model.
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
public class Codelist {

  @JacksonXmlProperty(isAttribute = true)
  String name;

  @JacksonXmlProperty(isAttribute = true)
  String alias;

  @JacksonXmlProperty(localName = "entry")
  @JacksonXmlElementWrapper(useWrapping = false)
  List<Entry> entry;
}
