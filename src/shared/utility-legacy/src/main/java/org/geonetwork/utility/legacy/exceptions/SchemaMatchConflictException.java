/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
// =============================================================================
// ===	Copyright (C) 2001-2012 Food and Agriculture Organization of the
// ===	United Nations (FAO-UN), United Nations World Food Programme (WFP)
// ===	and United Nations Environment Programme (UNEP)
// ===
// ===	This library is free software; you can redistribute it and/or
// ===	modify it under the terms of the GNU Lesser General Public
// ===	License as published by the Free Software Foundation; either
// ===	version 2.1 of the License, or (at your option) any later version.
// ===
// ===	This library is distributed in the hope that it will be useful,
// ===	but WITHOUT ANY WARRANTY; without even the implied warranty of
// ===	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// ===	Lesser General Public License for more details.
// ===
// ===	You should have received a copy of the GNU Lesser General Public
// ===	License along with this library; if not, write to the Free Software
// ===	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
// ===
// ===	Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
// ===	Rome - Italy. email: geonetwork@osgeo.org
// ==============================================================================

package org.geonetwork.utility.legacy.exceptions;

public class SchemaMatchConflictException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SchemaMatchConflictException(String name) {
        this(name, null);
    }

    public SchemaMatchConflictException(String name, Object value) {

        //        id = "schema-match-conflict";
    }
}
