/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.config;

import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.application.formatters.MessageWriterUtil;
import org.geonetwork.formatting.FormatterApi;
import org.geonetwork.ogcapi.service.configuration.ItemPageLinksConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    MessageWriterUtil messageWriterUtil;

    @Autowired
    private FormatterApi formatterApi;

    @Autowired
    private ItemPageLinksConfiguration itemPageLinksConfiguration;

    @SneakyThrows
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

        messageWriterUtil.initialize(); // this needs to happen EARLY

        configurer
                // .favorPathExtension(false)
                .favorParameter(true)
                .parameterName("f")
                .ignoreAcceptHeader(false);

        configurer
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("html", MediaType.TEXT_HTML)
                .mediaType("json", MediaType.APPLICATION_JSON);

        configurer.mediaTypes(messageWriterUtil.getMediaTypes());

        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @SneakyThrows
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        // put at start since the first matching converter is used.
        // spring will automatically put other general converters (like jackson json/xml) in this list.
        messageConverters.addAll(0, messageWriterUtil.getFormatters());
    }
}
