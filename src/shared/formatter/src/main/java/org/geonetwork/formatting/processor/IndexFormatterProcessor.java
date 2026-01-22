/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.formatting.processor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.geonetwork.index.model.record.IndexRecord;

/** See schema-ident.xml and schema-ident.xsd. */
public interface IndexFormatterProcessor {
    String getName();

    String getContentType();

    String getTitle();

    String getOfficialProfileName();

    String getProfileName();

    void process(IndexRecord indexRecord, OutputStream out, Map<String, Object> config) throws IOException;
}
