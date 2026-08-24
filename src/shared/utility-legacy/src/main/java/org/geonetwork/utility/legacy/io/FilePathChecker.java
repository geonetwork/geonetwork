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

package org.geonetwork.utility.legacy.io;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class to validate a file path.
 *
 * @author josegar
 */
public class FilePathChecker {

    /**
     * Checks that a file path is not absolute path and doesn't have .. characters, throwing an exception in these
     * cases.
     */
    public static void verify(String filePath) throws IllegalArgumentException {
        if (StringUtils.isEmpty(filePath)) return;

        if (filePath.contains("..")) {
            throw new IllegalArgumentException("Invalid character found in path." + filePath);
        }

        Path path = Paths.get(filePath);
        if (path.isAbsolute() || filePath.startsWith("/") || filePath.startsWith("://", 1)) {
            throw new IllegalArgumentException("Invalid character found in path." + filePath);
        }
    }
}
