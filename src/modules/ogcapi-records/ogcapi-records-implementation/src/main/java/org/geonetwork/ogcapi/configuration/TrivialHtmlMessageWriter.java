/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;

@Configuration
public class TrivialHtmlMessageWriter extends AbstractGenericHttpMessageConverter {

    TrivialHtmlMessageWriter() {
        this(MediaType.TEXT_HTML);
    }

    TrivialHtmlMessageWriter(MediaType supportedMediaType) {
        super(supportedMediaType);
    }

    @Override
    protected boolean canRead(@Nullable MediaType mediaType) {
        return false;
    }

    @Override
    protected void writeInternal(Object o, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        var objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, true);
        objectMapper.findAndRegisterModules();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        var json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);

        var result = "<html><head>";
        result += "<style>";
        result += ".button {\n" + "  background-color: #0070ff;\n"
                + "  border: none;\n"
                + "  color: white;\n"
                + "  padding: 15px 30px;\n"
                + "  text-align: center;\n"
                + "  display: inline-block;\n"
                + "  font-size: 25px;\n"
                + "  text-decoration: none;\n"
                + "  cursor: pointer;\n"
                + "}";

        result += "</style>";

        result += "</head><body>";
        result += "<h1>" + o.getClass().getSimpleName() + "</h1>\n<br>\n";

        result += insertJsonButton(o);

        result += "<pre style='    background-color: eee;border: 1px solid black;padding: 10px'> \n";
        result += json;
        result += "</pre>\n";

        result += "</body></html>";

        var bytes = result.getBytes(StandardCharsets.UTF_8);
        outputMessage.getHeaders().setContentType(MediaType.TEXT_HTML);
        outputMessage.getHeaders().setContentLength(bytes.length);
        outputMessage.getBody().write(result.getBytes(StandardCharsets.UTF_8));
    }

    private String insertJsonButton(Object o) {
        if (o == null) {
            return "";
        }
        try {
            var method = o.getClass().getMethod("getJsonLink");
            var uriLink = method.invoke(o);
            if (uriLink != null) {
                return makeButton(uriLink.toString());
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public String makeButton(String url) {
        return "<a href='" + url + "' class=button>Show JSON</a>";
    }

    @Override
    protected Object readInternal(Class clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return new Object();
    }

    @Override
    public Object read(Type type, Class contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return new Object();
    }
}
