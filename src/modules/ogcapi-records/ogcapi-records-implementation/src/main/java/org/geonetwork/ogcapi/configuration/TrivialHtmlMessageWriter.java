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
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

public class TrivialHtmlMessageWriter extends AbstractGenericHttpMessageConverter {

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

    var result = "<h1>" + o.getClass().getSimpleName() + "</h1>\n<br>\n";
    result += "<pre style='    background-color: eee;border: 1px solid black;padding: 10px'> \n";
    result += json;
    result += "</pre>\n";

    var bytes = result.getBytes(StandardCharsets.UTF_8);
    outputMessage.getHeaders().setContentType(MediaType.TEXT_HTML);
    outputMessage.getHeaders().setContentLength(bytes.length);
    outputMessage.getBody().write(result.getBytes(StandardCharsets.UTF_8));
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
