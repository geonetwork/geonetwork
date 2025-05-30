/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting.processor;

import java.io.IOException;
import java.io.OutputStream;
import org.geonetwork.index.model.record.IndexRecord;

public interface IndexFormatterProcessor {
    String getId();

    void process(IndexRecord indexRecord, OutputStream out) throws IOException;
}
