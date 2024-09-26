/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.utility.nio;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import org.geonetwork.schemas.constant.Constants;
import org.xml.sax.InputSource;

/**
 * An input source backed by a java nio path.
 *
 * @author Jesse on 1/20/2015.
 */
public class PathInputSource extends InputSource {
  private final PathSourceMixin pathSourceMixin;

  public PathInputSource(Path resource) {
    this.pathSourceMixin = new PathSourceMixin(resource);
  }

  @Override
  public InputStream getByteStream() {
    return this.pathSourceMixin.getInputStream();
  }

  @Override
  public void setByteStream(InputStream byteStream) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Reader getCharacterStream() {
    final Charset cs;
    if (getEncoding() != null) {
      cs = Charset.forName(getEncoding());
    } else {
      cs = Constants.CHARSET;
    }
    return this.pathSourceMixin.getReader(cs);
  }

  @Override
  public void setCharacterStream(Reader characterStream) {
    throw new UnsupportedOperationException();
  }
}
