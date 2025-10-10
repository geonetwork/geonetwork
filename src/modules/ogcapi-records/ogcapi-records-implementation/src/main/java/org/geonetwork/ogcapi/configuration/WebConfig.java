/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.configuration;

import static com.fasterxml.jackson.databind.DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.geonetwork.formatting.FormatterInfo;
import org.geonetwork.ogcapi.service.formatter.CswCollectionMessageWriter;
import org.geonetwork.ogcapi.service.formatter.FormatterApiMessageWriter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * configuration for the Spring Boot app.
 *
 * <p>This sets up the ContentNegotiation. 1. We have some simple/generic mimetype. 2. We register the very specific
 * mimetype (and formatterid) for the FormatterApi 3. We register some message writers (specifically the
 * FormatterApiMessageWriter)
 *
 * <p>NOTE/TODO: There is a circular dependency because WebConfig -> FormatterApiMessageWriter -> FormatterApi -> ... ->
 * IndexClient -> WebConfig. I've just explicitly gotten the FormatterApi at runtime instead of at instantiation. This
 * is a bit ugly, but ...
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private FormatterApiMessageWriter formatterApiMessageWriter;
    private CswCollectionMessageWriter cswCollectionMessageWriter;

    @Autowired
    private BeanFactory beanFactory;
    // todo - remove this.
    // There is a circular dependency because Formatter depends on WebConfig
    void setupFormatterApiMessageWriter() {
        if (formatterApiMessageWriter == null) {
            formatterApiMessageWriter = beanFactory.getBean(FormatterApiMessageWriter.class);
        }
        if (cswCollectionMessageWriter == null) {
            cswCollectionMessageWriter = beanFactory.getBean(CswCollectionMessageWriter.class);
        }
    }

    /**
     * configures the `?f=&lt;...&gt;` format requests.
     *
     * @param configurer - from Spring Boot
     */
    @SneakyThrows
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                // .favorPathExtension(false)
                .favorParameter(true)
                .parameterName("f")
                .ignoreAcceptHeader(false)

                //      defaultContentType(MediaType.APPLICATION_JSON).
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("html", MediaType.TEXT_HTML)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("geojson", MediaType.valueOf("application/geo+json"))
                .mediaType(MediaType.APPLICATION_XML.toString(), MediaType.APPLICATION_XML)
                .mediaType(MediaType.TEXT_HTML.toString(), MediaType.TEXT_HTML)
                .mediaType(MediaType.APPLICATION_JSON.toString(), MediaType.APPLICATION_JSON)
                .mediaType("application/geo+json", MediaType.valueOf("application/geo+json"))
                .defaultContentType(MediaType.APPLICATION_JSON);

        // add the FormatterApi media types.  We allows   f=<formatterId> or f=<formatter mime type>
        setupFormatterApiMessageWriter();
        Map<String, Map<String, FormatterInfo>> formats = this.formatterApiMessageWriter.getFormatNamesAndMimeTypes();
        for (var format : formats.keySet()) {
            configurer.mediaType(format, MediaType.valueOf(format));
            if (format.contains("+")) {
                var f = format.replace("+", " ");
                configurer.mediaType(f, MediaType.valueOf(format));
            }
        }
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        messageConverters.add(new TrivialHtmlMessageWriter(MediaType.TEXT_HTML));
        messageConverters.add(formatterApiMessageWriter);
        messageConverters.add(cswCollectionMessageWriter);
    }

    /** Generic object mapper to use in the system */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {

        var result = JsonMapper.builder()
                .enable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
                .enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
                .build();

        result.configure(UNWRAP_SINGLE_VALUE_ARRAYS, true);

        result.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        result.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, true);
        result.findAndRegisterModules();
        result.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        result.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return result;
    }
}
