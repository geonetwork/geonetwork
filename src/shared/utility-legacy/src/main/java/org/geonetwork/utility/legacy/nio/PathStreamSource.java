/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.utility.legacy.nio;

import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;
import javax.xml.transform.stream.StreamSource;
import org.geonetwork.constants.Constants;

/**
 * A StreamSource that reads from a Path.
 *
 * @author Jesse on 1/20/2015.
 */
public class PathStreamSource extends StreamSource {
    private final PathSourceMixin pathSourceMixin;

    public PathStreamSource(Path path) {
        this.pathSourceMixin = new PathSourceMixin(path);
    }

    @Override
    public InputStream getInputStream() {
        return pathSourceMixin.getInputStream();
    }

    @Override
    public void setInputStream(InputStream inputStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Reader getReader() {
        return this.pathSourceMixin.getReader(Constants.CHARSET);
    }

    @Override
    public void setReader(Reader reader) {
        throw new UnsupportedOperationException();
    }
}
