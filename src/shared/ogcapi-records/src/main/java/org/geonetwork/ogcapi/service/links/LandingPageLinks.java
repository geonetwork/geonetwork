/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.links;

import io.micrometer.common.util.StringUtils;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLandingPageDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.geonetwork.ogcapi.service.configuration.ConformancePageLinksConfiguration;
import org.geonetwork.ogcapi.service.configuration.LandingPageLinksConfiguration;
import org.geonetwork.ogcapi.service.configuration.OgcApiLinkConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.util.ArrayList;

/**
 * Injects links into the Landing Page response
 */
@Component
public class LandingPageLinks extends BasicLinks {

  @Autowired
  OgcApiLinkConfiguration linkConfiguration;

  @Autowired
  LandingPageLinksConfiguration landingPageLinksConfiguration;

  @Autowired
  ConformancePageLinksConfiguration conformancePageLinksConfiguration;

  /**
   * Adds the links to the landing page document.
   *
   * @param nativeWebRequest request from user
   * @param catalogUuid      uuid of the linked catalog
   * @param landingPage      where to add the link
   */
  public void addLinks(
    NativeWebRequest nativeWebRequest, String catalogUuid, OgcApiRecordsLandingPageDto landingPage) {
    addStandardLinks(
      nativeWebRequest,
      linkConfiguration.getOgcApiRecordsBaseUrl(),
      "",
      landingPage,
      new ArrayList<String>(
        landingPageLinksConfiguration.getMimeFormats().keySet()),
      "self",
      "alternative");

    addOpenApiLink(landingPage);
    addIconLink(landingPage, catalogUuid);
    addConformanceLinks(nativeWebRequest, landingPage);
  }

  /**
   * add the conformance link to the landing page
   *
   * @param nativeWebRequest from user
   * @param landingPage      which page to attach link to
   */
  private void addConformanceLinks(NativeWebRequest nativeWebRequest, OgcApiRecordsLandingPageDto landingPage) {
    addStandardLinks(
      nativeWebRequest,
      linkConfiguration.getOgcApiRecordsBaseUrl(),
      "conformance",
      landingPage,
      new ArrayList<String>(
        conformancePageLinksConfiguration.getMimeFormats().keySet()),
      "conformance",
      "conformance");
  }

  /**
   * add the openapi (swagger) link to the landing page.
   *
   * @param landingPage where to add lnk
   */
  private void addOpenApiLink(OgcApiRecordsLandingPageDto landingPage) {
    var uri = URI.create(linkConfiguration.getOgcApiRecordsBaseUrl()).resolve("../v3/api-docs?f=json");
    var link = new OgcApiRecordsLinkDto()
      .href(uri)
      .rel("service-doc")
      .type("application/json")
      .title("The OpenAPI Documentation as JSON");

    landingPage.addLinksItem(link);
  }

  /**
   * Adds an icon link (to the GN icon for a DB `source`).
   *
   * @param landingPage where to add
   * @param catalogUuid catalog that the main portal is logged to (can be null)
   */
  private void addIconLink(OgcApiRecordsLandingPageDto landingPage, String catalogUuid) {
    if (StringUtils.isBlank(catalogUuid)) {
      return;
    }
    // assume its a png
    var uri = URI.create(linkConfiguration.getGnBaseUrl()).resolve("images/logos/" + catalogUuid + ".png");
    var link = new OgcApiRecordsLinkDto().href(uri).rel("icon").type("image/png");

    landingPage.addLinksItem(link);
  }
}
