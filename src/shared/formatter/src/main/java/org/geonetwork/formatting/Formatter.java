/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

import java.io.IOException;
import java.io.OutputStream;
import org.geonetwork.domain.Metadata;

public interface Formatter {
    void format(Metadata metadata, String formatterId, OutputStream outputStream) throws IOException;

    boolean isFormatterAvailable(Metadata metadata, String formatterId);
}
