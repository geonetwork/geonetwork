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
 * <p>This contains some beans and some overrides for setting up spring.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                // .favorPathExtension(false)
                .favorParameter(true)
                .parameterName("f")
                .ignoreAcceptHeader(true)

                //      defaultContentType(MediaType.APPLICATION_JSON).
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("html", MediaType.TEXT_HTML)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("geojson", MediaType.valueOf("application/geo+json"))
                .defaultContentType(MediaType.TEXT_HTML);
        ;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        messageConverters.add(new TrivialHtmlMessageWriter(MediaType.TEXT_HTML));
    }

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
