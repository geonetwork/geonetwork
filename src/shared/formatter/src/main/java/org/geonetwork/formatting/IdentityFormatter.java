/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.formatting;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.geonetwork.domain.Metadata;
import org.springframework.stereotype.Component;

/** Identity formatter that writes the metadata XML document as is to the output stream. */
@Component
public class IdentityFormatter implements Formatter {
    @Override
    public void format(Metadata metadata, String formatterId, OutputStream outputStream, Map<String, Object> config)
            throws IOException {
        outputStream.write(metadata.getData().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean isFormatterAvailable(Metadata metadata, String formatterId) {
        return true;
    }
}
