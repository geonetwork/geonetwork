/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.utility.legacy.resolver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.util.XMLCatalogResolver;
import org.geonetwork.utility.legacy.io.IO;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.springframework.web.client.RestClient;
import org.w3c.dom.ls.LSInput;

/* Resolves system and public ids as well as URIs using oasis catalog
 as per XMLCatalogResolver, but goes further and retrieves any
external references since we need to use config'd proxy details on
any http connection we make and Xerces doesn't do this (why?)
hence this extension.  */
@Slf4j(topic = "org.geonetwork.xml.resolver")
public class XmlResolver extends XMLCatalogResolver {

  @SuppressWarnings("unused")
  private ProxyParams proxyParams;

  /**
   * Constructs a catalog resolver with the given list of entry files.
   *
   * @param catalogs an ordered array list of absolute URIs
   * @param proxyParams proxy parameters when connecting to external sites
   */
  public XmlResolver(String[] catalogs, ProxyParams proxyParams) {
    super(catalogs, true);
    this.proxyParams = proxyParams;
  }

  // --------------------------------------------------------------------------

  /**
   * Resolves any public and system ids as well as URIs from the catalog - also retrieves any
   * external references using Jeeves XmlRequest so that config'd proxy details can be used on the
   * http connection.
   *
   * @param type the type of the resource being resolved (usually XML schema)
   * @param namespaceURI the namespace of the resource being resolved, or <code>null</code> if none
   *     was supplied
   * @param publicId the public identifier of the resource being resolved, or <code>null</code> if
   *     none was supplied
   * @param systemId the system identifier of the resource being resolved, or <code>null</code> if
   *     none was supplied
   * @param baseURI the absolute base URI of the resource being parsed, or <code>null</code> if
   *     there is no base URI
   */
  @Override
  public LSInput resolveResource(
      String type, String namespaceURI, String publicId, String systemId, String baseURI) {

    if (log.isDebugEnabled())
      log.debug(
          "XmlResolver: Before resolution: Type: "
              + type
              + " NamespaceURI :"
              + namespaceURI
              + " "
              + "PublicId :"
              + publicId
              + " SystemId :"
              + systemId
              + " BaseURI:"
              + baseURI);
    LSInput result = null;

    try {
      result = tryToResolveOnFs(publicId, systemId, baseURI);
      if (result != null) {
        return result;
      }
    } catch (FileSystemNotFoundException e) {
      // Do nothing, just continue
    }

    result = super.resolveResource(type, namespaceURI, publicId, systemId, baseURI);

    if (result != null) { // some changes made so update
      publicId = result.getPublicId();
      systemId = result.getSystemId();
      baseURI = result.getBaseURI();
    }

    if (log.isDebugEnabled())
      log.debug(
          "Jeeves XmlResolver: After resolution: PublicId :"
              + publicId
              + " SystemId :"
              + systemId
              + " "
              + "BaseURI:"
              + baseURI);

    URL externalRef = null;
    try {

      if (publicId != null && publicId.startsWith("http://")) {
        externalRef = new URI(publicId).toURL();
      } else if (systemId != null && systemId.startsWith("http://")) {
        externalRef = new URI(systemId).toURL();
      } else if (systemId != null && baseURI != null) {
        if (baseURI.startsWith("http://")) {
          URL ref = new URI(baseURI).toURL();
          String thePath = new File(ref.getPath()).getParent().replace('\\', '/');
          externalRef =
              new URI(
                      ref.getProtocol(),
                      null,
                      ref.getHost(),
                      ref.getPort(),
                      thePath + "/" + systemId,
                      null,
                      null)
                  .toURL();
        }
      }
    } catch (MalformedURLException e) { // leave this to someone else?
      log.error(e.getMessage(), e);
      return result;
    } catch (URISyntaxException e) { // leave this to someone else?
      log.error(e.getMessage(), e);
      return result;
    }

    if (externalRef != null) {

      Element elResult = null;

      //            try {
      //                elResult = isXmlInCache(externalRef.toString());
      //            } catch (CacheException e) {
      //                log.error("Request to cache for " + externalRef + " failed.", e);
      //            }

      try {
        elResult =
            Xml.loadString(
                RestClient.create().get().uri(externalRef.toString()).retrieve().body(String.class),
                false);
      } catch (IOException e) {
        throw new RuntimeException(e);
      } catch (JDOMException e) {
        throw new RuntimeException(e);
      }

      //            if (elResult == null) { // use XMLRequest to get the XML
      //                XmlRequest xml = new
      // GeonetHttpRequestFactory().createXmlRequest(externalRef);
      //                if (proxyParams.useProxy) {
      //                    xml.setUseProxy(true);
      //                    xml.setProxyHost(proxyParams.proxyHost);
      //                    xml.setProxyPort(proxyParams.proxyPort);
      //                    if (proxyParams.useProxyAuth) {
      //                        xml.setProxyCredentials(proxyParams.username, proxyParams.password);
      //                    }
      //                }
      //
      //                elResult = null;
      //                try {
      //                    elResult = xml.execute();
      //                    addXmlToCache(externalRef.toString(), elResult);
      //                    if (log.isDebugEnabled()) {
      //                        log.debug("Retrieved: \n" + Xml.getString(elResult));
      //                    }
      //                } catch (Exception e) {
      //                    log.error("Request on " + externalRef + " failed." + e.getMessage());
      //                }
      //            }

      if (result == null) {
        result = new DOMInputImpl(publicId, systemId, baseURI);
      }
      if (elResult != null) {
        result.setStringData(Xml.getString(elResult));
      }
    }
    return result;
  }

  private LSInput tryToResolveOnFs(String publicId, String systemId, String baseURI) {
    if (baseURI != null && systemId != null) {
      try {
        Path basePath = IO.toPath(new URI(baseURI));
        Path parent = basePath.getParent();
        if (parent == null) {
          throw new RuntimeException(basePath.toUri() + " does not have parent");
        }
        final Path resolved = parent.resolve(systemId);
        if (Files.isRegularFile(resolved)) {
          try {
            final String uri = resolved.normalize().toUri().toASCIIString();
            return new DOMInputImpl(
                publicId, uri, uri, new ByteArrayInputStream(Files.readAllBytes(resolved)), null);
          } catch (IOException e) {
            log.error("Error opening resource: " + resolved + " for reading. " + e.getMessage());
          }
        }
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }
}
