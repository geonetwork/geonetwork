/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.links;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.application.ctrlreturntypes.MimeAndProfilesForResponseType;
import org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfile;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiCollectionResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiLandingPageResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsCollectionsResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponse;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.geonetwork.ogcapi.service.configuration.OgcApiLinkConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * basic ops for links.
 *
 * <p>1. This document links Links should point to the various representations of this document ("alternate") and the
 * exact link to this document ("self").
 */
@Component
@Slf4j
public class BasicLinks {

    @Autowired
    MimeAndProfilesForResponseType mimeAndProfilesForResponseType;

    @Autowired
    OgcApiLinkConfiguration linkConfiguration;

    public void addLinks(
            RequestMediaTypeAndProfile requestMediaTypeAndProfile,
            Object page,
            String endpointFragment,
            Class<?> linkType)
            throws Exception {

        var allResponseInfo = mimeAndProfilesForResponseType.getResponseTypeInfos(linkType);
        var links = allResponseInfo.stream()
                .map(x -> {
                    var link = new OgcApiRecordsLinkDto();
                    var href = linkConfiguration.getOgcApiRecordsBaseUrl() + endpointFragment + "?f=" + x.getMimeType();
                    try {
                        link.setHref(new URI(href));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    setupLinkName(requestMediaTypeAndProfile, x, linkType, link);
                    link.setType(x.getMimeType().toString());
                    link.hreflang("eng");
                    link.setProfile(x.getProfiles());
                    return link;
                })
                .toList();

        addLinksReflect(links, page);
    }

    /**
     * Annotate the link's name (and maybe title)
     *
     * @param requestMediaTypeAndProfile information about the request (esp what type of document it is requesting)
     * @param responseInfo information about the formatter
     * @param linkType what type of document is this link for
     * @param link actual link to annotate
     */
    public void setupLinkName(
            RequestMediaTypeAndProfile requestMediaTypeAndProfile,
            MimeAndProfilesForResponseType.ResponseTypeInfo responseInfo,
            Class<?> linkType,
            OgcApiRecordsLinkDto link) {
        if (!requestMediaTypeAndProfile.getResponseClass().equals(linkType)) {
            // request is for another type - this should be a LABELLED name, not "alternative"/"self"
            if (linkType.equals(OgcApiRecordsCollectionsResponse.class)) {
                link.setRel("collections");
                link.setTitle("Information about the collections");
                return;
            }
            if (linkType.equals(OgcApiCollectionResponse.class)) {
                link.setRel("http://www.opengis.net/def/rel/ogc/1.0/ogc-catalog");
                link.setTitle("Information about the collection");
                return;
            }
            if (linkType.equals(OgcApiRecordsMultiRecordResponse.class)) {
                link.setRel("items");
                link.setTitle("Records in the collection");
                return;
            }
            if (linkType.equals(OgcApiLandingPageResponse.class)) {
                link.setRel("root");
                link.setTitle("Landing Page");
                return;
            }
        }
        // should be "alternative"/"self"
        link.setRel("alternative");

        // see if it should be "self"
        // if:
        //    requested mime type is the same as what the link will produce
        if (requestMediaTypeAndProfile.getRequestMediaType().equals(responseInfo.getMimeType())) {

            var requestResolvedProfile = requestMediaTypeAndProfile.getRequestResolvedProfile();
            var supportedProfiles = responseInfo.getProfiles();
            var noSupportedProfiles = supportedProfiles == null || supportedProfiles.isEmpty();
            if (requestResolvedProfile == null && noSupportedProfiles) {
                link.setRel("self");
            } else if (requestResolvedProfile != null
                    && !noSupportedProfiles
                    && supportedProfiles.contains(requestResolvedProfile)) {
                link.setRel("self");
            }
        }
    }

    /**
     * add the openapi (swagger) link to the landing page.
     *
     * @param page where to add lnk
     */
    protected void addOpenApiLink(Object page) {
        var uri = URI.create(linkConfiguration.getOgcApiRecordsBaseUrl()).resolve("../v3/api-docs?f=json");
        var link = new OgcApiRecordsLinkDto()
                .href(uri)
                .rel("service-doc")
                .type("application/json")
                .title("The OpenAPI Documentation as JSON");

        addLinksReflect(List.of(link), page);
    }

    /**
     * add queryables links
     *
     * @param page page to add to
     */
    protected void addQueryables(OgcApiRecordsCatalogDto page) {
        var uri = URI.create(linkConfiguration.getOgcApiRecordsBaseUrl())
                .resolve("collections/" + page.getId() + "/queryables?f=json");
        var link = new OgcApiRecordsLinkDto()
                .href(uri)
                .rel("http://www.opengis.net/def/rel/ogc/1.0/queryables")
                .type("application/schema+json")
                .title("Queryables for this collection");
        page.addLinksItem(link);
    }

    public void addLinksReflect(List<OgcApiRecordsLinkDto> links, Object page) {
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

    /**
     * add the conformance link to the landing page
     *
     * @param page which page to attach link to
     */
    @SuppressWarnings("UnusedVariable")
    protected void addConformanceLinks(Object page) {
        try {
            var link = new OgcApiRecordsLinkDto()
                    .href(new URI(linkConfiguration.getOgcApiRecordsBaseUrl() + "conformance"))
                    .rel("conformance")
                    .type("application/json")
                    .title("ogcapi-records conformance document");
            addLinksReflect(List.of(link), page);
        } catch (URISyntaxException e) {
            log.error("Invalid conformance link URI", e);
        }
    }

    protected void addRootLinks(RequestMediaTypeAndProfile requestMediaTypeAndProfile, Object page) throws Exception {
        addLinks(requestMediaTypeAndProfile, page, "", OgcApiLandingPageResponse.class);
    }
}
