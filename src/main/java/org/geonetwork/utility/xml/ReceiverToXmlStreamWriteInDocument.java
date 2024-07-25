/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.utility.xml;

import javax.xml.stream.XMLStreamWriter;
import net.sf.saxon.stax.ReceiverToXMLStreamWriter;
import net.sf.saxon.trans.XPathException;

/**
 * A Receiver that writes to a StAX XMLStreamWriter, but does not attempt to close the writer when
 * it finishes. This is because the writer is owned by the caller, who is responsible for closing
 * it.
 */
public class ReceiverToXmlStreamWriteInDocument extends ReceiverToXMLStreamWriter {
  public ReceiverToXmlStreamWriteInDocument(XMLStreamWriter writer) {
    super(writer);
  }

  @Override
  public void startDocument(int properties) throws XPathException {
    // Document exists.
  }

  @Override
  public void endDocument() throws XPathException {
    // Document exists.
  }

  @Override
  public void close() throws XPathException {
    //
  }
}
