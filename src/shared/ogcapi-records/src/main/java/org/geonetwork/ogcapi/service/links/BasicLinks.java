/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.links;

import lombok.SneakyThrows;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * basic ops for links.
 *
 * <p>1. This document links Links should point to the various representations of this document ("alternative") and the
 * exact link to this document ("self").
 */
@Component
public class BasicLinks {

  @Autowired
  ContentNegotiationManager contentNegotiationManager;

  /**
   * this adds the "standard" links - which is to "self" and "alternative" versions of the landing page.
   *
   * @param nativeWebRequest from user
   * @param baseUrl          baseURL for the system
   * @param endpointLoc      which endpoint is this for
   * @param page             where to attach the links?
   * @param formatNames      what formats should we use
   */
  public void addStandardLinks(
    NativeWebRequest nativeWebRequest,
    String baseUrl,
    String endpointLoc,
    Object page,
    List<String> formatNames,
    String selfName,
    String altName) {
    var links =
      this.createSelfRelativeLinks(nativeWebRequest, baseUrl, endpointLoc, formatNames, selfName, altName);

    // the objects are auto-generated from yaml, so we have to use reflection
    try {
      var method = page.getClass().getMethod("addLinksItem", OgcApiRecordsLinkDto.class);
      links.forEach(x -> {
        try {
          method.invoke(page, x);
        } catch (Throwable e) {
          throw new RuntimeException(e);
        }
      });
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public List<OgcApiRecordsLinkDto> createSelfRelativeLinks(
    NativeWebRequest nativeWebRequest,
    String baseUrl,
    String endpointLocation,
    List<String> mediaNames,
    String selfName,
    String altName) {
    var url = baseUrl + endpointLocation;
    if (!url.contains("?")) {
      url += "?";
    } else {
      url += "&";
    }
    var requestMediaInfo = this.getMediaTypeAndName(nativeWebRequest);
    var mediaMappings = contentNegotiationManager.getMediaTypeMappings();

    String finalUrl = url;

    var links = mediaNames.stream()
      .map(x -> {
        if (x.equals(requestMediaInfo.getName())) {
          // self
          var link = new OgcApiRecordsLinkDto();
          try {
            link.rel(selfName)
              .type(mediaMappings.get(x).toString())
              .hreflang("eng")
              .href(new URI(finalUrl + "f=" + x));
            return link;
          } catch (URISyntaxException e) {
            throw new RuntimeException(e);
          }
        } else {
          // alternative
          var link = new OgcApiRecordsLinkDto();
          try {
            link.rel(altName)
              .type(mediaMappings.get(x).toString())
              .hreflang("eng")
              .href(new URI(finalUrl + "f=" + x));
            return link;
          } catch (URISyntaxException e) {
            throw new RuntimeException(e);
          }
        }
      })
      .toList();

    return links;
  }

  /**
   * given a mediatype, what is its "name" (i.e. "html", "json") based on the contentNegotiationManager?
   *
   * @param mediaType media type to search for
   * @return name of the media type
   */
  public String getMediaName(MediaType mediaType) {
    var matching = contentNegotiationManager.getMediaTypeMappings().entrySet().stream()
      .filter(e -> e.getValue().equals(mediaType))
      .findFirst();

    if (matching.isEmpty()) {
      return null;
    }
    return matching.get().getKey();
  }

  /**
   * given a request, find (using the contentNegotiationManager) the resulting mediatype this request will create.
   *
   * @param nativeWebRequest current request
   * @return which media type it will be producing
   */
  @SneakyThrows
  public MediaType getRequestMediaType(NativeWebRequest nativeWebRequest) {
    var types = contentNegotiationManager.resolveMediaTypes(nativeWebRequest);
    if (types.isEmpty()) {
      return MediaType.TEXT_HTML;
    }
    return types.getFirst();
  }

  /**
   * Given a request, find the name ("json") and mediatype ("application/json") that this request will result in. use
   * the contentNegotiationManager to do this work.
   *
   * @param nativeWebRequest from user
   * @return the name and mediatype
   */
  public MediaTypeAndName getMediaTypeAndName(NativeWebRequest nativeWebRequest) {
    var mediaType = getRequestMediaType(nativeWebRequest);
    if (mediaType == null) {
      return null;
    }
    var name = getMediaName(mediaType);

    return new MediaTypeAndName(mediaType, name);
  }
}
