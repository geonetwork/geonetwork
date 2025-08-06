/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
// =============================================================================
// ===	Copyright (C) 2001-2007 Food and Agriculture Organization of the
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

package org.geonetwork.schemas;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SchemaSuggestions {
    private Map<String, List<String>> htFields = new LinkedHashMap<>();

    public SchemaSuggestions(List<SchemaPluginConfiguration.Suggestion> suggestions) {
        suggestions.forEach(suggestion -> {
            htFields.put(suggestion.getField(), suggestion.getSuggestions());
        });
    }

    private boolean isX(String parent, String child, @SuppressWarnings("unused") String what) {
        final List<String> fieldEl = htFields.get(parent);

        if (fieldEl == null) return false;

        return fieldEl.stream().filter(f -> f.equals(child)).findFirst().isPresent();
    }

    public boolean isSuggested(String parent, String child) {
        return isX(parent, child, "suggest");
    }

    // TODO: Review if required, seems not used
    public boolean isFiltered(String parent, String child) {
        return false; // isX(parent, child, "filter");
    }

    /**
     * Return true if parent element is defined in suggestion file and check that suggested elements are valid children
     * of current element. <br>
     * For example, gmd:extent could have suggestions as a child of gmd:identificationInfo or as a child of
     * gmd:EX_TemporalExtent.
     *
     * @return true if having suggestion for at least one of its child elements.
     */
    public boolean hasSuggestion(String parent, List<String> childElements) {
        List<String> suggestions = htFields.get(parent);

        if (suggestions == null) {
            return false; // No suggestion available for element
        } else {
            return childElements.stream()
                    .filter(child -> isSuggested(parent, child))
                    .findFirst()
                    .isPresent();
        }
    }

    /**
     * Return the list of suggestion for an element.
     *
     * @param elementName The name of the element
     * @return The list of element names
     */
    public List<String> getSuggestedElements(String elementName) {
        return htFields.get(elementName) == null ? List.of() : htFields.get(elementName);
    }
}
