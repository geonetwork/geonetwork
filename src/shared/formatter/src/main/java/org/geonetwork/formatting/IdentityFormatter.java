/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.geonetwork.domain.Metadata;
import org.springframework.stereotype.Component;

/** Identity formatter that writes the metadata XML document as is to the output stream. */
@Component
public class IdentityFormatter implements Formatter {
    @Override
    public void format(Metadata metadata, String formatterId, OutputStream outputStream) throws IOException {
        outputStream.write(metadata.getData().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean isFormatterAvailable(Metadata metadata, String formatterId) {
        return true;
    }
}
