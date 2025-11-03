/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.config;

import static com.fasterxml.jackson.databind.DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.text.SimpleDateFormat;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.application.formatters.MessageWriterUtil;
import org.geonetwork.formatting.FormatterApi;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private BeanFactory beanFactory;

    private MessageWriterUtil messageWriterUtil;
    private FormatterApi formatterApi;

    /**
     * TODO: There is a circular dependency because WebConfig -> FormatterApiMessageWriter -> FormatterApi -> ... ->
     * IndexClient -> WebConfig. I've just explicitly gotten the FormatterApi at runtime instead of at instantiation.
     * This is a bit ugly, but ...
     *
     * <p>There are other circular dependencies - mostly because of the formatter -> index -> webconfig cycle.
     *
     * @throws Exception config?
     */
    void initDependencies() throws Exception {
        if (messageWriterUtil == null) {
            messageWriterUtil = beanFactory.getBean(MessageWriterUtil.class);
            messageWriterUtil.initialize();
        }
        if (formatterApi == null) {
            formatterApi = beanFactory.getBean(FormatterApi.class);
        }
    }

    @SneakyThrows
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

        initDependencies(); // this needs to happen EARLY

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
        initDependencies();
        messageConverters.addAll(0, messageWriterUtil.getFormatters());
    }

    /** Generic object mapper to use in the system */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {

        var result = JsonMapper.builder()
                .enable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
                .enable(UNWRAP_SINGLE_VALUE_ARRAYS)
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
