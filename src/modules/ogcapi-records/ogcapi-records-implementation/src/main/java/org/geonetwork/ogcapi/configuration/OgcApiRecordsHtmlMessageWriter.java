/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.configuration;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiCollectionResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiLandingPageResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsCollectionsResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsMultiRecordResponse;
import org.geonetwork.ogcapi.ctrlreturntypes.OgcApiRecordsSingleRecordResponse;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsExceptionDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.geonetwork.ogcapi.service.indexConvert.OgcApiGeoJsonConverter;
import org.geonetwork.ogcapi.service.links.ItemPageLinks;
import org.geonetwork.ogcapi.service.links.ItemsPageLinks;
import org.geonetwork.ogcapi.service.ogcapi.OgcApiCollectionsApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Component
public class OgcApiRecordsHtmlMessageWriter extends AbstractGenericHttpMessageConverter<Object> {

    @Value("${geonetwork.openapi-records.links.base-path:/ogcapi-records}")
    private String ogcApiRecordsBasePath;

    private final OgcApiCollectionsApi collectionsApi;
    private final OgcApiGeoJsonConverter geoJsonConverter;
    private final ItemsPageLinks itemsPageLinks;
    private final ItemPageLinks itemPageLinks;
    final SpringTemplateEngine templateEngine;

    public OgcApiRecordsHtmlMessageWriter(
            OgcApiCollectionsApi collectionsApi,
            OgcApiGeoJsonConverter geoJsonConverter,
            ItemsPageLinks itemsPageLinks,
            ItemPageLinks itemPageLinks) {
        super(MediaType.TEXT_HTML);
        this.collectionsApi = collectionsApi;
        this.geoJsonConverter = geoJsonConverter;
        this.itemsPageLinks = itemsPageLinks;
        this.itemPageLinks = itemPageLinks;
        this.templateEngine = buildTemplateEngine();
    }

    private static SpringTemplateEngine buildTemplateEngine() {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/ogcapi/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(true);
        var engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        return engine;
    }

    @Override
    public boolean canWrite(@Nullable Type type, Class<?> clazz, @Nullable MediaType mediaType) {
        if (!super.canWrite(type, clazz, mediaType)) {
            return false;
        }
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return false;
        }
        HttpServletRequest request = attributes.getRequest();
        return request.getRequestURI().contains(ogcApiRecordsBasePath);
    }

    @Override
    protected boolean canRead(@Nullable MediaType mediaType) {
        return false;
    }

    @Override
    protected void writeInternal(Object source, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        Object resolved;
        try {
            resolved = resolveObject(source);
        } catch (Exception e) {
            throw new IOException("Cannot build HTML response payload", e);
        }

        var ctx = new Context();
        ctx.setVariable("page", resolved);

        var writer = new StringWriter();
        templateEngine.process(selectTemplate(source), ctx, writer);

        var bytes = writer.toString().getBytes(StandardCharsets.UTF_8);
        outputMessage.getHeaders().setContentType(MediaType.TEXT_HTML);
        outputMessage.getHeaders().setContentLength(bytes.length);
        outputMessage.getBody().write(bytes);
    }

    /** Package-visible entry-point for unit tests (bypasses canWrite servlet-context check). */
    void renderToOutput(Object source, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        writeInternal(source, null, outputMessage);
    }

    String selectTemplate(Object source) {
        if (source instanceof OgcApiLandingPageResponse) return "landing-page";
        if (source instanceof OgcApiRecordsCollectionsResponse) return "collections";
        if (source instanceof OgcApiCollectionResponse) return "collection";
        if (source instanceof OgcApiRecordsMultiRecordResponse) return "items";
        if (source instanceof OgcApiRecordsSingleRecordResponse) return "item";
        if (source instanceof OgcApiRecordsExceptionDto) return "error";
        return "landing-page";
    }

    private Object resolveObject(Object source) throws Exception {
        if (source instanceof OgcApiRecordsCollectionsResponse collectionsResponse) {
            return collectionsApi.getCollections(collectionsResponse);
        }
        if (source instanceof OgcApiCollectionResponse collectionResponse) {
            return collectionsApi.describeCollection(
                    collectionResponse.getCollectionId(), collectionResponse.getRequestMediaTypeAndProfile());
        }
        if (source instanceof OgcApiLandingPageResponse landingPageResponse) {
            return collectionsApi.getLandingPage(landingPageResponse.getRequestMediaTypeAndProfile());
        }
        if (source instanceof OgcApiRecordsMultiRecordResponse itemsResponse) {
            var sourceRecords = itemsResponse.getRecords() == null
                    ? List.<OgcApiRecordsSingleRecordResponse>of()
                    : itemsResponse.getRecords();
            var records = sourceRecords.stream()
                    .map(OgcApiRecordsSingleRecordResponse::getIndexRecord)
                    .map(x -> geoJsonConverter.convert(x, null))
                    .toList();
            var result = new OgcApiRecordsGetRecords200ResponseDto();
            result.setType(OgcApiRecordsGetRecords200ResponseDto.TypeEnum.FEATURE_COLLECTION);
            result.setFeatures(records);
            result.numberMatched((int) itemsResponse.getTotalHits());
            result.numberReturned(records.size());
            result.setTimeStamp(OffsetDateTime.now(ZoneId.of("UTC")));
            result.setFacets(itemsResponse.getFacetInfo());
            if (itemsResponse.getRequestMediaTypeAndProfile() != null && itemsResponse.getUserQuery() != null) {
                itemsPageLinks.addLinks(
                        itemsResponse.getRequestMediaTypeAndProfile(),
                        itemsResponse.getUserQuery().getCollectionId(),
                        result,
                        itemsResponse.getUserQuery());
                for (var feature : records) {
                    itemPageLinks.addAllLinks(
                            itemsResponse.getRequestMediaTypeAndProfile(),
                            itemsResponse.getUserQuery().getCollectionId(),
                            feature);
                }
            }
            return result;
        }
        if (source instanceof OgcApiRecordsSingleRecordResponse singleRecordResponse) {
            var result = geoJsonConverter.convert(singleRecordResponse.getIndexRecord(), null);
            itemPageLinks.addAllLinks(
                    singleRecordResponse.getRequestMediaTypeAndProfile(), singleRecordResponse.getCatalogId(), result);
            return result;
        }
        return source;
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return new Object();
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return new Object();
    }
}
