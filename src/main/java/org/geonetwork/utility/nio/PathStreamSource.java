/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.utility.nio;

import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;
import javax.xml.transform.stream.StreamSource;
import org.geonetwork.schemas.constant.Constants;

/**
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
