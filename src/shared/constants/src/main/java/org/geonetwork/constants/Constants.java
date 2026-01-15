/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.constants;

import java.nio.charset.Charset;

public interface Constants {
    String ENCODING = System.getProperty("geonetwork.file.encoding", "UTF-8");
    Charset CHARSET = Charset.forName(ENCODING);
    String ERROR = "error";
    String XML_CATALOG_FILES = "jeeves.xml.catalog.files";
    String XML_CATALOG_VERBOSITY = "jeeves.xml.catalog.verbosity";
    String XML_CATALOG_BLANKXSLFILE = "jeeves.xml.catalog.blankxslfile";
}
