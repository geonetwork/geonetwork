/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.index.model.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

/** Index records. */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@JacksonXmlRootElement(localName = "indexRecords")
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexRecords {

    @JsonProperty(value = "indexRecord")
    @JacksonXmlElementWrapper(useWrapping = false)
    @Singular("indexRecord")
    private List<IndexRecord> indexRecord;
}
