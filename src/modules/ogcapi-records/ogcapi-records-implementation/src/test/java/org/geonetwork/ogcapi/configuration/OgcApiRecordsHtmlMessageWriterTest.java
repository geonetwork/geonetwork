/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiCollectionResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiLandingPageResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsCollectionsResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsSingleRecordResponse;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsExceptionDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetCollections200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLandingPageDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONPropertiesDto;
import org.geonetwork.ogcapi.service.ogcapi.OgcApiCollectionsApi;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;

class OgcApiRecordsHtmlMessageWriterTest {

    @Test
    void selectTemplateShouldReturnLandingPageForLandingPageResponse() {
        var writer = writer();
        assertEquals("landing-page", writer.selectTemplate(new OgcApiLandingPageResponse(null, null)));
    }

    @Test
    void selectTemplateShouldReturnCollectionsForCollectionsResponse() {
        var writer = writer();
        assertEquals("collections", writer.selectTemplate(new OgcApiRecordsCollectionsResponse(null, null)));
    }

    @Test
    void selectTemplateShouldReturnCollectionForCollectionResponse() {
        var writer = writer();
        assertEquals("collection", writer.selectTemplate(new OgcApiCollectionResponse()));
    }

    @Test
    void selectTemplateShouldReturnItemsForMultiRecordResponse() {
        var writer = writer();
        assertEquals("items", writer.selectTemplate(new OgcApiRecordsMultiRecordResponse()));
    }

    @Test
    void selectTemplateShouldReturnItemForSingleRecordResponse() {
        var writer = writer();
        assertEquals("item", writer.selectTemplate(new OgcApiRecordsSingleRecordResponse()));
    }

    @Test
    void selectTemplateShouldReturnErrorForExceptionDto() {
        var writer = writer();
        assertEquals("error", writer.selectTemplate(new OgcApiRecordsExceptionDto("500")));
    }

    @Test
    void shouldRenderLandingPageTitleAndLinks() throws Exception {
        var collectionsApi = landingPageApi(
                "My GeoNetwork",
                "A catalog of spatial data",
                List.of(
                        link("http://localhost/api/", "self", "Landing Page", "application/json"),
                        link("http://localhost/api/", "alternative", "Landing Page HTML", "text/html"),
                        link("http://localhost/api/collections", "data", "Collections", "application/json")));

        var html = renderLandingPage(collectionsApi);

        assertTrue(html.contains("My GeoNetwork"));
        assertTrue(html.contains("A catalog of spatial data"));
        // alternative link becomes a format-switch button
        assertTrue(html.contains("<a class=\"button\""));
        assertTrue(html.contains("text/html"));
        // non-alternative links appear in Links section
        assertTrue(html.contains("Collections"));
        // alternative links do NOT appear as plain list items (they're buttons)
        assertFalse(html.contains("<li") && html.contains("Landing Page HTML"));
    }

    @Test
    void shouldRenderCollectionsGrid() throws Exception {
        var collectionsApi = collectionsApi(
                List.of(catalog("c1", "Catalog One", "First catalog"), catalog("c2", "Catalog Two", null)));

        var html = renderCollections(collectionsApi);

        assertTrue(html.contains("Collections"));
        assertTrue(html.contains("Catalog One"));
        assertTrue(html.contains("Catalog Two"));
        assertTrue(html.contains("First catalog"));
        assertTrue(html.contains("2 collections"));
    }

    @Test
    void shouldShowEmptyMessageWhenNoCollections() throws Exception {
        var collectionsApi = collectionsApi(List.of());
        var html = renderCollections(collectionsApi);
        assertTrue(html.contains("No collections found."));
    }

    @Test
    void shouldRenderCollectionDetails() throws Exception {
        var collectionsApi = collectionApi("my-id", "My Collection", "Detailed description", List.of("geo", "maps"));
        var html = renderCollection(collectionsApi);

        assertTrue(html.contains("My Collection"));
        assertTrue(html.contains("Detailed description"));
        assertTrue(html.contains("geo"));
        assertTrue(html.contains("maps"));
    }

    @Test
    void shouldRenderItemsGrid() throws Exception {
        var page = new OgcApiRecordsGetRecords200ResponseDto();
        page.setFeatures(List.of(feature("f1", "Feature One", "Description one"), feature("f2", "Feature Two", null)));
        page.numberMatched(2);
        page.numberReturned(2);

        var html = renderPage(new OgcApiRecordsMultiRecordResponse(), page);

        assertTrue(html.contains("Feature One"));
        assertTrue(html.contains("Feature Two"));
        assertTrue(html.contains("Description one"));
        assertTrue(html.contains("2 items"));
    }

    @Test
    void shouldShowPaginationButtons() throws Exception {
        var page = new OgcApiRecordsGetRecords200ResponseDto();
        page.setFeatures(List.of(feature("f1", "Feature One", null)));
        page.numberMatched(100);
        page.numberReturned(10);
        page.setLinks(List.of(link("http://localhost/api/items?offset=10", "next", "Next", "application/json")));

        var html = renderPage(new OgcApiRecordsMultiRecordResponse(), page);

        assertTrue(html.contains("next"));
    }

    @Test
    void shouldRenderItemDetails() throws Exception {
        var feature = feature("item-123", "My Record", "Full description of the record");
        feature.setLinks(List.of(link("http://localhost/api/items/item-123", "self", "Self", "application/json")));

        var html = renderPage(new OgcApiRecordsSingleRecordResponse(), feature);

        assertTrue(html.contains("My Record"));
        assertTrue(html.contains("Full description of the record"));
    }

    private OgcApiRecordsHtmlMessageWriter writer() {
        return new OgcApiRecordsHtmlMessageWriter(null, null, null, null);
    }

    private String renderLandingPage(OgcApiCollectionsApi collectionsApi) throws Exception {
        var writer = new OgcApiRecordsHtmlMessageWriter(collectionsApi, null, null, null);
        var source = new OgcApiLandingPageResponse(null, null);
        return render(writer, source);
    }

    private String renderCollections(OgcApiCollectionsApi collectionsApi) throws Exception {
        var writer = new OgcApiRecordsHtmlMessageWriter(collectionsApi, null, null, null);
        var source = new OgcApiRecordsCollectionsResponse(null, null);
        return render(writer, source);
    }

    private String renderCollection(OgcApiCollectionsApi collectionsApi) throws Exception {
        var writer = new OgcApiRecordsHtmlMessageWriter(collectionsApi, null, null, null);
        var source = new OgcApiCollectionResponse();
        return render(writer, source);
    }

    private String renderPage(Object source, Object resolvedDto) throws Exception {
        var sw = new java.io.StringWriter();
        var tmpWriter = new OgcApiRecordsHtmlMessageWriter(null, null, null, null);
        var ctx = new org.thymeleaf.context.Context();
        ctx.setVariable("page", resolvedDto);
        tmpWriter.templateEngine.process(tmpWriter.selectTemplate(source), ctx, sw);
        return sw.toString();
    }

    private String render(OgcApiRecordsHtmlMessageWriter writer, Object source) throws Exception {
        var output = new TestOutputMessage();
        // Use renderToOutput to bypass canWrite (which needs a servlet request context)
        writer.renderToOutput(source, output);
        return output.body.toString(StandardCharsets.UTF_8);
    }

    private OgcApiCollectionsApi landingPageApi(String title, String desc, List<OgcApiRecordsLinkDto> links)
            throws URISyntaxException {
        return new OgcApiCollectionsApi() {
            @Override
            public OgcApiRecordsLandingPageDto getLandingPage(
                    org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfile r) {
                var page = new OgcApiRecordsLandingPageDto();
                page.setTitle(title);
                page.setDescription(desc);
                page.setLinks(links);
                return page;
            }
        };
    }

    private OgcApiCollectionsApi collectionsApi(List<OgcApiRecordsCatalogDto> collections) {
        return new OgcApiCollectionsApi() {
            @Override
            public org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetCollections200ResponseDto
                    getCollections(OgcApiRecordsCollectionsResponse r) {
                var dto = new OgcApiRecordsGetCollections200ResponseDto();
                dto.setCollections(collections);
                return dto;
            }
        };
    }

    private OgcApiCollectionsApi collectionApi(String id, String title, String desc, List<String> keywords) {
        return new OgcApiCollectionsApi() {
            @Override
            public OgcApiRecordsCatalogDto describeCollection(
                    String collectionId, org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfile r) {
                var dto = new OgcApiRecordsCatalogDto();
                dto.setId(id);
                dto.setTitle(title);
                dto.setDescription(desc);
                dto.setKeywords(keywords);
                return dto;
            }
        };
    }

    private OgcApiRecordsLinkDto link(String href, String rel, String title, String type) throws URISyntaxException {
        return OgcApiRecordsLinkDto.builder()
                .href(new URI(href))
                .rel(rel)
                .title(title)
                .type(type)
                .build();
    }

    private OgcApiRecordsCatalogDto catalog(String id, String title, String desc) {
        var dto = new OgcApiRecordsCatalogDto();
        dto.setId(id);
        dto.setTitle(title);
        dto.setDescription(desc);
        return dto;
    }

    private OgcApiRecordsRecordGeoJSONDto feature(String id, String title, String desc) {
        var props = new OgcApiRecordsRecordGeoJSONPropertiesDto();
        props.setTitle(title);
        props.setDescription(desc);
        var dto = new OgcApiRecordsRecordGeoJSONDto();
        dto.setId(id);
        dto.setProperties(props);
        return dto;
    }

    static class TestOutputMessage implements HttpOutputMessage {
        private final HttpHeaders headers = new HttpHeaders();
        private final ByteArrayOutputStream body = new ByteArrayOutputStream();

        @Override
        public OutputStream getBody() {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }
}
