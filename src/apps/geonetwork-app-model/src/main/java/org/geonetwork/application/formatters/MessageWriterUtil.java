/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.application.formatters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geonetwork.application.ctrlreturntypes.IControllerResultFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;

/** This will help find and initialize classes for formatting (MessageWriter). */
@Component
public class MessageWriterUtil {

    @Autowired(required = false)
    private List<IContentNegotiationInitializable> contentNegotiationInitializables;

    boolean initialized = false;

    private List<IControllerResultFormatter> formatters = new ArrayList<>();

    /**
     * Call this early.
     *
     * <p>This will find classes that implement IContentNegotiationInitializable and initialize those class. They will
     * return a set of beans to be added to the context.
     */
    public void initialize() throws Exception {
        if (!initialized) {
            if (contentNegotiationInitializables != null) {
                for (var initializable : contentNegotiationInitializables) {
                    var items = initializable.initialize();
                    formatters.addAll(items);
                }
            }
            initialized = true;
        }
    }

    public Map<String, MediaType> getMediaTypes() throws Exception {
        if (!initialized) {
            initialize();
        }
        var result = new HashMap<String, MediaType>();
        for (var item : formatters) {
            result.put(item.getMimeTypeQuickName(), item.getMimeType());
        }
        return result;
    }

    public List<? extends HttpMessageConverter<?>> getFormatters() throws Exception {
        if (!initialized) {
            initialize();
        }
        return formatters.stream().map(x -> (HttpMessageConverter<?>) x).toList();
    }
}
