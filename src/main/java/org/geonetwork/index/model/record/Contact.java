/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import static org.geonetwork.index.model.record.IndexRecordFieldNames.ORGANISATION_NAME;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/** Contact. */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Contact {

  private String role;
  private String individual;

  @JsonProperty(ORGANISATION_NAME)
  private Map<String, String> organisation = new HashMap<>();

  private String email;
  private String logo;
  private String phone;
  private String address;
  private String website;
  private String position;
}
