/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.constant;

import java.nio.charset.Charset;

public interface Constants {
  String ENCODING = System.getProperty("geonetwork.file.encoding", "UTF-8");
  Charset CHARSET = Charset.forName(ENCODING);
  String ERROR = "error";
  String XML_CATALOG_FILES = "jeeves.xml.catalog.files";
  String XML_CATALOG_VERBOSITY = "jeeves.xml.catalog.verbosity";
  String XML_CATALOG_BLANKXSLFILE = "jeeves.xml.catalog.blankxslfile";
}
