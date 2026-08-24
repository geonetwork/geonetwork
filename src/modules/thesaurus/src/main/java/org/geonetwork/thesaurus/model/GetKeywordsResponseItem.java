/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.thesaurus.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetKeywordsResponseItem {

    /** Multilingual values (lang -> value) Example: fre -> "Adresses", eng -> "Addresses" */

    // ?? enum for language?

    private Map<String, String> values;

    /** Multilingual definitions (lang -> definition) Example: fre -> "Degré", eng -> "Degree" */
    private Map<String, String> definitions;

    private String coordEast;
    private String coordWest;
    private String coordSouth;
    private String coordNorth;

    /** Thesaurus identifier Example: local.theme.codelist_unit_distance */
    private String thesaurusKey;

    /** Default value (usually in current UI language) */
    private String value;

    /** Default definition (usually in current UI language) */
    private String definition;

    /** External reference URI */
    private String uri;
}
