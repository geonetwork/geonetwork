/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
// =============================================================================
// ===	Copyright (C) 2001-2005 Food and Agriculture Organization of the
// ===	United Nations (FAO-UN), United Nations World Food Programme (WFP)
// ===	and United Nations Environment Programme (UNEP)
// ===
// ===	This program is free software; you can redistribute it and/or modify
// ===	it under the terms of the GNU General Public License as published by
// ===	the Free Software Foundation; either version 2 of the License, or (at
// ===	your option) any later version.
// ===
// ===	This program is distributed in the hope that it will be useful, but
// ===	WITHOUT ANY WARRANTY; without even the implied warranty of
// ===	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// ===	General Public License for more details.
// ===
// ===	You should have received a copy of the GNU General Public License
// ===	along with this program; if not, write to the Free Software
// ===	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
// ===
// ===	Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
// ===	Rome - Italy. email: geonetwork@osgeo.org
// ==============================================================================

package org.geonetwork.schemas.model.xsd;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.geonetwork.schemas.SchemaPluginConfiguration;

/** Schema substitutions. */
public class SchemaSubstitutions {
    private Map<String, List<String>> htFields = new LinkedHashMap<>();

    public SchemaSubstitutions(List<SchemaPluginConfiguration.Substitute> substitutes) {
        substitutes.forEach((substitute) -> {
            htFields.put(substitute.getField(), substitute.getSubstitutes());
        });
    }

    public List<String> getSubstitutes(String child) {
        List<String> fieldSubstitutes = htFields.get(child);

        return fieldSubstitutes;
    }
}
