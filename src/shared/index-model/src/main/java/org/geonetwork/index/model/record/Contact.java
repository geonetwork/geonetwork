/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import static org.geonetwork.index.model.record.IndexRecordFieldNames.CONTACT_IDENTIFIERS;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.ORGANISATION_NAME;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Contact. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Contact {

    private String role;
    private String individual;

    @JsonProperty(ORGANISATION_NAME)
    @Builder.Default
    private Map<String, String> organisation = new HashMap<>();

    private String email;
    private String logo;
    private String phone;
    private String address;
    private String website;
    private String position;
    private String nilReason;

    @JsonProperty(CONTACT_IDENTIFIERS)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<PartyIdentifier> identifier;
}
